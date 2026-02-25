package org.yourappdev.homeinterior.ui.CreateAndExplore

import androidx.compose.ui.graphics.Color
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import org.yourappdev.homeinterior.data.remote.util.ResultState
import org.yourappdev.homeinterior.domain.model.Room
import org.yourappdev.homeinterior.domain.model.RoomUi
import org.yourappdev.homeinterior.domain.model.Rooms
import org.yourappdev.homeinterior.ui.Generate.UiScreens.ColorPalette
import org.yourappdev.homeinterior.ui.Generate.UiScreens.InteriorStyle

data class RoomUiState(
    val getRoomsResponse: ResultState<Rooms> = ResultState.Stable,
    val trendingRooms: List<RoomUi> = emptyList(),
    val allRooms: List<RoomUi> = emptyList(),
    val filteredRooms: List<RoomUi> = emptyList(),
    val searchQuery: String = "",
    val filterState: FilterState = FilterState(),
    val filterCount: Int = 0,
    val tempFilterCount: Int = 0,
    val isLoading: Boolean = true,
    val showFilterSheet: Boolean = false,
    val tempFilterState: FilterState = FilterState(),
    val expandedRoomType: Boolean = false,
    val expandedStyle: Boolean = false,
    val expandedColor: Boolean = false,
    val expandedFormat: Boolean = false,
    val expandedPrice: Boolean = false,
    val availableRoomTypes: List<String> = emptyList(),
    val availableStyles: List<InteriorStyle> = emptyList(),
    val availableStylesString: List<String> = emptyList(),
    val availableColors: List<ColorPalette> = emptyList(),

    val selectedImage: String? = null,
    val currentPage: Int = 0,
    val pageCount: Int = 4,
    val selectedRoomType: String? = availableRoomTypes.firstOrNull(),
    val roomSearchQuery: String = "",
    val isRoomSearchExpanded: Boolean = false,
    val selectedStyleName: String? = availableStyles.firstOrNull()?.name,
    val styleSearchQuery: String = "",
    val isStyleSearchExpanded: Boolean = false,
    val selectedPaletteId: Int? = availableColors.firstOrNull()?.id,
    val isGenerating: Boolean = false
)

data class FilterState(
    val selectedRoomTypes: Set<String> = emptySet(),
    val selectedStyles: Set<String> = emptySet(),
    val selectedColors: Set<Int> = emptySet(),
    val selectedFormats: Set<String> = emptySet(),
    val selectedPrices: Set<String> = emptySet()
)
enum class FilterSection {
    ROOM_TYPE, STYLE, COLOR, FORMAT, PRICE
}
