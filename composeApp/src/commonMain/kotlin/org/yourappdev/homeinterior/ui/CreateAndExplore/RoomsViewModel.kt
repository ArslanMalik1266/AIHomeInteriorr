package org.yourappdev.homeinterior.ui.CreateAndExplore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.yourappdev.homeinterior.data.mapper.toUi
import org.yourappdev.homeinterior.domain.repo.RoomsRepository
import org.yourappdev.homeinterior.ui.Generate.UiScreens.ColorPalette
import org.yourappdev.homeinterior.ui.Generate.UiScreens.InteriorStyle
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent
import org.yourappdev.homeinterior.ui.common.base.CommonUiEvent.ShowError
import org.yourappdev.homeinterior.utils.executeApiCall

class RoomsViewModel(val roomsRepository: RoomsRepository) : ViewModel() {

    private val _state = MutableStateFlow(RoomUiState())
    val state: StateFlow<RoomUiState> = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CommonUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getRooms()
    }

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
                _state.update { it.copy(
                    isGenerating = true,
                    selectedImageBytes = event.imageBytes,
                    selectedFileName = event.fileName,
                    errorMessage = null
                )}

                viewModelScope.launch {
                    try {
                        val prompt = buildPromptFromState(_state.value)
                        val response = roomsRepository.generateRoom(
                            imageBytes = event.imageBytes,
                            fileName = event.fileName,
                            prompt = prompt,
                            strength = 0.7f
                        )
                        if (response.success) {
                            _state.update { it.copy(
                                isGenerating = false,
                                generatedImages = response.static_urls,
                                recentGeneratedImages = _state.value.recentGeneratedImages.plus(element = response.static_urls),                                errorMessage = null
                            )}
                        } else {
                            _state.update { it.copy(isGenerating = false, errorMessage = response.message) }
                            println("DEBUG_API_FLOW: API Error Message: ${response.message}")
                        }
                    } catch (e: Exception) {
                        _state.update { it.copy(isGenerating = false, errorMessage = e.message) }
                    }
                }
            }
            is RoomEvent.OnGenerationComplete -> {
                _state.update { it.copy(
                    selectedImageBytes = null,
                    selectedFileName = null,
                    selectedImage = null,
                    generatedImages = emptyList(), // Isse ResultScreen band ho jayegi
                    isGenerating = false,
                    selectedRoomType = null,
                    selectedStyleName = null,
                    selectedPaletteId = null
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
        viewModelScope.launch {
            executeApiCall(
                updateState = { result -> _state.value = _state.value.copy(getRoomsResponse = result) },
                apiCall = { roomsRepository.getRoomsList() },
                onSuccess = { response ->
                    if (response.success) {
                        val finalList = response.rooms.map { it.toUi() }
                        val trending = finalList.filter { it.isTrending == 1 }
                        _state.value = _state.value.copy(
                            trendingRooms = trending,
                            allRooms = finalList,
                            filteredRooms = finalList,
                        )
                        extractDynamicFilters(finalList)
                    } else {
                        _uiEvent.emit(ShowError("Something went wrong"))
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch { _uiEvent.emit(ShowError(errorMessage)) }
                }
            )
        }
    }

    private fun buildPromptFromState(state: RoomUiState): String {
        val roomType = state.selectedRoomType?.ifBlank { "living room" }
        val style = state.selectedStyleName?.ifBlank { "modern" }
        val colors = state.availableColors
            .firstOrNull { it.id == state.selectedPaletteId }
            ?.colors?.joinToString(", ") ?: "neutral tones"

        return """
            Design a $roomType in a $style with the color palette $colors, specifying primary, secondary, and accent colors. 
            Provide a detailed furniture layout, including essential pieces, spatial arrangement, and functional zones. 
            Recommend materials, textures, and finishes for walls, flooring, furniture, and textiles to enhance the style. 
            Suggest lighting solutions, including natural light utilization, fixture types, and placement for ambient, task, and accent lighting. 
            Include complementary decorative elements such as artwork, plants, rugs, curtains, and accessories that reinforce the mood and atmosphere. 
            Ensure the design is cohesive, functional, visually balanced, and creates the intended ambiance while reflecting the chosen style and palette.
        """.trimIndent()
    }
}