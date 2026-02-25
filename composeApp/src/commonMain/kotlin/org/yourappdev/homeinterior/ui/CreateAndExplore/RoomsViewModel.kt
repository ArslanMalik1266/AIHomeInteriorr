package org.yourappdev.homeinterior.ui.CreateAndExplore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
            is RoomEvent.OnSearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                applyFiltersAndSearch()
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

            RoomEvent.OnDismissFilterSheet -> {
                _state.value = _state.value.copy(
                    showFilterSheet = false,
                    tempFilterCount = _state.value.filterCount
                )
            }

            RoomEvent.OnClearFilters -> {
                _state.value = _state.value.copy(tempFilterState = FilterState(), tempFilterCount = 0)
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
            // generate screen events from here
            is RoomEvent.SetImage -> {
                _state.value = _state.value.copy(
                    selectedImage = event.imageDetails.uri
                )
                println("DEBUG_VM: SelectedImage URI = ${event.imageDetails.uri}")
                println("DEBUG_VM: Full GalleryPhotoResult = $event.imageDetails")            }

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

            RoomEvent.OnGenerateClick -> {
                _state.value = _state.value.copy(isGenerating = true)
            }

            RoomEvent.OnGenerationComplete -> {
                _state.value = _state.value.copy(isGenerating = false)
            }

        }
    }

    private fun applyFiltersAndSearch() {
        val state = _state.value
        var filtered = state.allRooms

        // Apply search filter
        if (state.searchQuery.isNotBlank()) {
            filtered = filtered.filter { room ->
                room.roomType.contains(state.searchQuery, ignoreCase = true)
            }
        }

        // Apply room type filter
        if (state.filterState.selectedRoomTypes.isNotEmpty() &&
            !state.filterState.selectedRoomTypes.contains("All")
        ) {
            filtered = filtered.filter { room ->
                state.filterState.selectedRoomTypes.contains(room.roomType)
            }
        }

        // Apply style filter
        if (state.filterState.selectedStyles.isNotEmpty() &&
            !state.filterState.selectedStyles.contains("All")
        ) {
            filtered = filtered.filter { room ->
                state.filterState.selectedStyles.any { style ->
                    room.roomStyle.contains(style, ignoreCase = true)
                }
            }
        }

        _state.value = _state.value.copy(filteredRooms = filtered)
    }

    private fun calculateFilterCount(filterState: FilterState): Int {
        var count = 0
        if (filterState.selectedRoomTypes.isNotEmpty() &&
            !filterState.selectedRoomTypes.contains("All")
        ) count++
        if (filterState.selectedStyles.isNotEmpty() &&
            !filterState.selectedStyles.contains("All")
        ) count++
        if (filterState.selectedColors.isNotEmpty()) count++
        if (filterState.selectedFormats.isNotEmpty() &&
            !filterState.selectedFormats.contains("All")
        ) count++
        if (filterState.selectedPrices.isNotEmpty()) count++
        return count
    }

    private fun extractDynamicFilters(rooms: List<org.yourappdev.homeinterior.domain.model.RoomUi>) {
        val roomTypes = rooms
            .map { it.roomType }
            .filter { it.isNotBlank() }
            .distinct()

        val styles = rooms
            .map { data -> InteriorStyle(name = data.roomStyle, imageUrl = data.imageUrl, id = data.id) }
            .distinct()


        val colorPalettes = rooms
            .map { room ->
                ColorPalette(colors = room.colors, id = room.id)
            }
            .distinct()

        val stylesString = styles.map { style -> style.name }.distinct()
        _state.value = _state.value.copy(
            availableRoomTypes = roomTypes,
            availableStyles = styles,
            availableStylesString = stylesString,
            availableColors = colorPalettes
        )
    }


    fun getRooms() {
        viewModelScope.launch {
            executeApiCall(
                updateState = { result ->
                    _state.value = _state.value.copy(getRoomsResponse = result)
                },
                apiCall = {
                    roomsRepository.getRoomsList()
                }, onSuccess = { response ->
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


}