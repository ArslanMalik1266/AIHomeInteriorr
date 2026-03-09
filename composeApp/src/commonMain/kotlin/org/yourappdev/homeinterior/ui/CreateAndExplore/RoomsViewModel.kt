package org.yourappdev.homeinterior.ui.CreateAndExplore

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.yourappdev.homeinterior.data.local.entities.DraftEntity
import org.yourappdev.homeinterior.data.local.entities.RecentGeneratedEntity
import org.yourappdev.homeinterior.data.mapper.toUi
import org.yourappdev.homeinterior.domain.repo.DraftsRepository
import org.yourappdev.homeinterior.domain.repo.RecentGeneratedRepository
import org.yourappdev.homeinterior.domain.repo.RoomsRepository
import org.yourappdev.homeinterior.domain.usecase.AddCreditsUseCase
import org.yourappdev.homeinterior.ui.Generate.UiScreens.ColorPalette
import org.yourappdev.homeinterior.ui.Generate.UiScreens.InteriorStyle
import org.yourappdev.homeinterior.ui.authentication.AuthViewModel
import org.yourappdev.homeinterior.ui.authentication.register.RegisterEvent
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent.ShowError
import org.yourappdev.homeinterior.utils.executeApiCall
import kotlin.time.ExperimentalTime

class RoomsViewModel(val roomsRepository: RoomsRepository,
                     private val addCreditsUseCase: AddCreditsUseCase,
                     private val authViewModel: AuthViewModel,
                     private val draftsRepository: DraftsRepository,
                     private val recentGeneratedRepository: RecentGeneratedRepository,
//                     private val consumeCreditsUseCase: ConsumeCreditsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RoomUiState())
    val state: StateFlow<RoomUiState> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CommonUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    private val _selectedGeneratedImage = MutableStateFlow<String?>(null)
    val selectedGeneratedImage: StateFlow<String?> = _selectedGeneratedImage.asStateFlow()
    val draftImages: StateFlow<List<DraftEntity>> = draftsRepository.getAllDrafts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private var currentDraftId: Long? = null


    fun selectDraftImage(draft: DraftEntity) {
        currentDraftId = draft.id

        onRoomEvent(
            RoomEvent.SetImageBytes(
                bytes = draft.userImageBytes ?: byteArrayOf(),
                fileName = "draft_${draft.id}.jpg"
            )
        )
        _state.update { it.copy(
            selectedRoomType = draft.roomType,
            selectedStyleName = draft.style,
            selectedPaletteId = draft.paletteId,
            currentPage = draft.currentPage,
            selectedImage = "draft_picked"
        )}
    }

    val dbGeneratedImages: StateFlow<List<RecentGeneratedEntity>> =
        recentGeneratedRepository.getRecentGenerated()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    @OptIn(ExperimentalTime::class)
    fun saveOrUpdateDraft() {
        val currentState = _state.value
        val currentImage = currentState.selectedImageBytes ?: return

        // Room Entity object banayein
        val newDraft = DraftEntity(
            id = currentDraftId ?: 0L, // Agar ID hai to update hoga, 0 hai to naya banega
            userImageBytes = currentImage,
            roomType = currentState.selectedRoomType ?: "Living Room",
            style = currentState.selectedStyleName ?: "Modern",
            paletteId = currentState.selectedPaletteId ?: 0,
            currentPage = currentState.currentPage,
            createdAt = kotlin.time.Clock.System.now().toEpochMilliseconds()
        )

        viewModelScope.launch {
            draftsRepository.saveDraft(newDraft)
            currentDraftId = null
            resetGenerationState()
        }
    }

    fun selectDraftForEditing(draft: DraftEntity) {
        currentDraftId = draft.id

        _state.update { it.copy(
            selectedImageBytes = draft.userImageBytes,
            selectedRoomType = draft.roomType,
            selectedStyleName = draft.style,
            selectedPaletteId = draft.paletteId,
            currentPage = draft.currentPage,
            selectedImage = "draft_picked"
        )}
    }


    fun onGeneratedImageClick(imageUrl: String) {
        _selectedGeneratedImage.value = imageUrl
    }
    fun resetSelectedGeneratedImage() {
        _selectedGeneratedImage.value = null
    }

    init {
        getRooms()
    }

    fun resetGenerationState() {
        _state.update { it.copy(
            selectedRoomType = null,
            selectedStyleName = null,
            selectedPaletteId = null,
            currentPage = 0, // <--- YE LINE ADD KAREIN
            errorMessage = null,
            isGenerating = false,
            generatedImages = emptyList()
        ) }
        currentDraftId = null
    }

    @OptIn(ExperimentalTime::class)
    fun onRoomEvent(event: RoomEvent) {
        when (event) {

            // Filtering and search events (keep as-is)
            is RoomEvent.OnSearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                applyFiltersAndSearch()
            }

            is RoomEvent.SetImageBytes -> {
                _state.update { it.copy(
                    selectedImageBytes = event.bytes,
                    selectedFileName = event.fileName,
                    selectedImage = "image_picked"
                )}
            }


            is RoomEvent.OnApplyFilters -> {
                val tempFilter = _state.value.tempFilterState
                val count = calculateFilterCount(tempFilter)
                _state.value = _state.value.copy(
                    filterState = tempFilter,
                    filterCount = count,
                    tempFilterCount = count,
                    showFilterSheet = false
                )
                applyFiltersAndSearch()
            }
            RoomEvent.OnFilterClick -> {
                _state.value = _state.value.copy(
                    showFilterSheet = true,
                    tempFilterState = _state.value.filterState,
                    tempFilterCount = _state.value.filterCount
                )
            }
            RoomEvent.OnResetLoading -> {
                _state.update { it.copy(isLoading = false) }
            }
            RoomEvent.OnDismissFilterSheet -> {
                _state.value = _state.value.copy(
                    showFilterSheet = false,
                    tempFilterCount = _state.value.filterCount
                )
            }
            RoomEvent.OnClearFilters -> {
                _state.value = _state.value.copy(
                    tempFilterState = FilterState(),
                    tempFilterCount = 0
                )
            }
            is RoomEvent.OnTempFilterChange -> {
                val newCount = calculateFilterCount(event.filterState)
                _state.value = _state.value.copy(
                    tempFilterState = event.filterState,
                    tempFilterCount = newCount
                )
            }
            is RoomEvent.OnToggleFilterSection -> {
                _state.value = when (event.section) {
                    FilterSection.ROOM_TYPE -> _state.value.copy(
                        expandedRoomType = !_state.value.expandedRoomType
                    )
                    FilterSection.STYLE -> _state.value.copy(
                        expandedStyle = !_state.value.expandedStyle
                    )
                    FilterSection.COLOR -> _state.value.copy(
                        expandedColor = !_state.value.expandedColor
                    )
                    FilterSection.FORMAT -> _state.value.copy(
                        expandedFormat = !_state.value.expandedFormat
                    )
                    FilterSection.PRICE -> _state.value.copy(
                        expandedPrice = !_state.value.expandedPrice
                    )
                }
            }

            // Image selection event
            is RoomEvent.SetImage -> {
                _state.value = _state.value.copy(
                    selectedImage = event.imageDetails.uri
                )
                println("DEBUG_VM: SelectedImage URI = ${event.imageDetails.uri}")
            }

            // Pagination events
            is RoomEvent.OnPageChange -> {
                _state.value = _state.value.copy(currentPage = event.page)
            }
            RoomEvent.OnNextPage -> {
                val currentPage = _state.value.currentPage
                if (currentPage < _state.value.pageCount - 1) {
                    _state.value = _state.value.copy(currentPage = currentPage + 1)
                }
            }
            RoomEvent.OnPreviousPage -> {
                val currentPage = _state.value.currentPage
                if (currentPage > 0) {
                    _state.value = _state.value.copy(currentPage = currentPage - 1)
                }
            }

            // Room type / style / palette selection events
            is RoomEvent.OnRoomTypeSelected -> {
                _state.value = _state.value.copy(selectedRoomType = event.roomType)
            }
            is RoomEvent.OnRoomSearchQueryChange -> {
                _state.value = _state.value.copy(roomSearchQuery = event.query)
            }
            is RoomEvent.OnRoomSearchExpandedChange -> {
                _state.value = _state.value.copy(isRoomSearchExpanded = event.isExpanded)
            }
            is RoomEvent.OnStyleSelected -> {
                val styleName = _state.value.availableStyles
                    .firstOrNull { it.id == event.styleId }
                    ?.name ?: "Unknown"
                _state.value = _state.value.copy(selectedStyleName = styleName)
            }
            is RoomEvent.OnStyleSearchQueryChange -> {
                _state.value = _state.value.copy(styleSearchQuery = event.query)
            }
            is RoomEvent.OnStyleSearchExpandedChange -> {
                _state.value = _state.value.copy(isStyleSearchExpanded = event.isExpanded)
            }
            is RoomEvent.OnPaletteSelected -> {
                _state.value = _state.value.copy(selectedPaletteId = event.paletteId)
            }

            // **Generate room using ByteArray image**
// RoomsViewModel.kt mein replace karein:
            is RoomEvent.OnGenerateClick -> {
                println("DEBUG_VM: 1. OnGenerateClick Triggered")
                _state.update { it.copy(
                    isGenerating = true,
                    selectedImageBytes = event.imageBytes,
                    selectedFileName = event.fileName,
                    errorMessage = null
                )}

                viewModelScope.launch {

                    try {
                        val prompt = buildPromptFromState(_state.value)
                        println("DEBUG_VM: 2. Prompt Built: $prompt")
                        println("DEBUG_VM: 3. Calling API with image size: ${event.imageBytes.size}")
                        val response = roomsRepository.generateRoom(
                            imageBytes = event.imageBytes,
                            fileName = event.fileName,
                            prompt = prompt,
                            strength = 0.7f
                        )
                        println("DEBUG_VM: 4. API Response Received. Success = ${response.success}")
                        if (response.success) {
                            println("DEBUG_VM: 5. Success! Images found: ${response.images.size}")
                            response.images.forEach { url ->
                                recentGeneratedRepository.saveGenerated(
                                    RecentGeneratedEntity(
                                        imageBytes = byteArrayOf(),
                                        imageUrl = url,
                                        createdAt = kotlin.time.Clock.System.now().toEpochMilliseconds()
                                    )
                                )
                            }

                            _state.update { it.copy(
                                isGenerating = false,
                                generatedImages = response.images,
                                generatedCount = response.count,
                                jobId = response.job_id,
                                generatedRoom = response,
                            )}


                        } else {
                            println("DEBUG_VM: 6. API Failed! Response: $response")
                            _state.update { it.copy(isGenerating = false, errorMessage = "") }
                        }
                    } catch (e: Exception) {
                        println("DEBUG_VM: CRASH! Error: ${e.message}")
                        _state.update { it.copy(isGenerating = false, errorMessage = e.message) }
                    }
                }
            }
            is RoomEvent.OnGenerationComplete -> {
                _state.update { it.copy(
                    selectedImageBytes = null,
                    selectedFileName = null,
                    selectedImage = null,
                    generatedImages = emptyList(),
                    isGenerating = false,
                    selectedRoomType = null,
                    selectedStyleName = null,
                    selectedPaletteId = null,
                    currentPage = 0 // <--- Reset to Step 1
                )}
            }
            is RoomEvent.ShowSelectedBundle -> {
                _state.update { it.copy(
                    generatedImages = event.bundle, // Taake ResultScreen ye images dikhaye
                    isGenerating = false // Loading band ho jaye agar khuli ho
                )}
            }

            else -> {}
        }
    }

    // --- Helper functions ---

    private fun applyFiltersAndSearch() {
        val state = _state.value
        var filtered = state.allRooms

        if (state.searchQuery.isNotBlank()) {
            filtered = filtered.filter { room -> room.roomType.contains(state.searchQuery, ignoreCase = true) }
        }

        if (state.filterState.selectedRoomTypes.isNotEmpty() && !state.filterState.selectedRoomTypes.contains("All")) {
            filtered = filtered.filter { room -> state.filterState.selectedRoomTypes.contains(room.roomType) }
        }

        if (state.filterState.selectedStyles.isNotEmpty() && !state.filterState.selectedStyles.contains("All")) {
            filtered = filtered.filter { room ->
                state.filterState.selectedStyles.any { style -> room.roomStyle.contains(style, ignoreCase = true) }
            }
        }

        if (state.filterState.selectedColors.isNotEmpty()) {
            filtered = filtered.filter { room ->
                state.filterState.selectedColors.contains(room.id)
            }
        }

        _state.value = _state.value.copy(filteredRooms = filtered)
    }

    private fun calculateFilterCount(filterState: FilterState): Int {
        var count = 0
        if (filterState.selectedRoomTypes.isNotEmpty() && !filterState.selectedRoomTypes.contains("All")) count++
        if (filterState.selectedStyles.isNotEmpty() && !filterState.selectedStyles.contains("All")) count++
        if (filterState.selectedColors.isNotEmpty()) count++
        if (filterState.selectedFormats.isNotEmpty() && !filterState.selectedFormats.contains("All")) count++
        if (filterState.selectedPrices.isNotEmpty()) count++
        return count
    }

    private fun extractDynamicFilters(rooms: List<org.yourappdev.homeinterior.domain.model.RoomUi>) {
        val roomTypes = rooms.map { it.roomType }.filter { it.isNotBlank() }.distinct()

        val styles = rooms.map { data ->
            InteriorStyle(name = data.roomStyle, imageUrl = data.imageUrl, id = data.id)
        }.distinct()

        val colorPalettes = rooms.map { room -> ColorPalette(colors = room.colors, id = room.id) }.distinct()
        val stylesString = styles.map { it.name }.distinct()

        _state.value = _state.value.copy(
            availableRoomTypes = roomTypes,
            availableStyles = styles,
            availableStylesString = stylesString,
            availableColors = colorPalettes,
            selectedPaletteId = _state.value.selectedPaletteId ?: colorPalettes.firstOrNull()?.id
        )
    }

    fun getRooms() {
        println("DEBUG_VM: 1. getRooms() called") // Check if called
        viewModelScope.launch {
            executeApiCall(
                updateState = { result -> _state.value = _state.value.copy(getRoomsResponse = result) },
                apiCall = {
                    println("DEBUG_VM: 2. Launching API Call...")
                    roomsRepository.getRoomsList() },
                onSuccess = { response ->
                    println("DEBUG_VM: 3. Success! Rooms Count: ${response.rooms.size}")
                    if (response.success) {
                        val finalList = response.rooms.map { it.toUi() }
                        val trending = finalList.filter { it.isTrending == 1 }
                        _state.value = _state.value.copy(
                            trendingRooms = trending,
                            allRooms = finalList,
                            filteredRooms = finalList,
                            isLoading = false
                        )
                        extractDynamicFilters(finalList)
                    } else {
                        println("DEBUG_VM: 4. API Success was False.")
                        _uiEvent.emit(ShowError("Something went wrong"))
                    }
                },
                onError = { errorMessage ->
                    println("DEBUG_VM: 5. API Error: $errorMessage")
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }

    private fun buildPromptFromState(state: RoomUiState): String {
        val roomType = state.selectedRoomType?.ifBlank { "living room" } ?: "living room"
        val style = state.selectedStyleName?.ifBlank { "modern" } ?: "modern"

        // 1. Pehle selected palette dhoondein
        val selectedPalette = state.availableColors.firstOrNull { it.id == state.selectedPaletteId }

        // 2. Colors ko transform karein (Color object -> "FFFFFF")
        val cleanHexColors = selectedPalette?.colors?.map { colorValue ->
            when (colorValue) {
                is Color -> colorValue.toRawHex() // Agar Compose Color hai
                is String -> cleanColorString(colorValue) // Agar String hai
                else -> "FFFFFF"
            }
        } ?: listOf("neutral tones")

        val colorPaletteString = cleanHexColors.joinToString(", ")

        return """
        Design a $roomType in a $style with the color palette $colorPaletteString, specifying primary, secondary, and accent colors. 
        Provide a detailed furniture layout, including essential pieces, spatial arrangement, and functional zones. 
        Recommend materials, textures, and finishes for walls, flooring, furniture, and textiles to enhance the style. 
        Suggest lighting solutions, including natural light utilization, fixture types, and placement for ambient, task, and accent lighting. 
        Include complementary decorative elements such as artwork, plants, rugs, curtains, and accessories that reinforce the mood and atmosphere. 
        Ensure the design is cohesive, functional, visually balanced, and creates the intended ambiance while reflecting the chosen style and palette.
    """.trimIndent()
    }

    // Helper to clean existing strings
    private fun cleanColorString(rawColor: String): String {
        return if (rawColor.contains("Color")) {
            // Agar galti se "Color(1.0...)" string ban chuka hai, toh usse handle karein
            "FFFFFF"
        } else {
            rawColor.replace("#", "").trim()
        }
    }

    // Compose Color to "FFFFFF"
    fun Color.toRawHex(): String {
        val r = (this.red * 255).toInt().coerceIn(0, 255)
        val g = (this.green * 255).toInt().coerceIn(0, 255)
        val b = (this.blue * 255).toInt().coerceIn(0, 255)

        // Har component ko 2-digit hex string mein badlein aur join karein
        return listOf(r, g, b).joinToString("") {
            it.toString(16).padStart(2, '0').uppercase()
        }
    }

    fun onSubscriptionEvent(event: RoomEvent) {
        when (event) {
            is RoomEvent.OnPurchasePlan -> {
                // Price to Credits Mapping
                val amount = when (event.price) {
                    "$9.99" -> 500
                    "$18.99" -> 1100
                    "$28.99" -> 2300
                    else -> 0
                }

                val email = authViewModel.state.value.email ?: ""

                println("DEBUG_PURCHASE: Event triggered for price: ${event.price}")
                println("DEBUG_PURCHASE: Mapped amount: $amount")
                println("DEBUG_PURCHASE: User email: '$email'")

                if (email.isBlank()) {
                    _state.update { it.copy(purchaseError = "User not logged in") }
                    return
                }

                _state.update { it.copy(isPurchasing = true, purchaseError = null) }

                viewModelScope.launch {
                    val result = addCreditsUseCase(email, amount)

                    result.onSuccess { response ->
                        _state.update { it.copy(
                            isPurchasing = false,
                            purchaseSuccess = "Credits added: ${response.purchasedCredits}"
                        )}
                        authViewModel.onAuthEvent(RegisterEvent.FetchUserDetails)
                    }.onFailure { error ->
                        println("DEBUG_PURCHASE: Failure! Error: ${error.message}")
                        _state.update { it.copy(
                            isPurchasing = false,
                            purchaseError = error.message ?: "Transaction failed"
                        )}
                    }
                }
            }
            RoomEvent.ClearPurchaseState -> {
                _state.update { it.copy(purchaseSuccess = null, purchaseError = null) }
            }
            else -> onRoomEvent(event) // Purane events ko bhej dein
        }
    }
}