package org.yourappdev.homeinterior.ui.Generate.UiScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.yourappdev.homeinterior.domain.model.AboutToGenerateUiState
import org.yourappdev.homeinterior.domain.repo.RoomsRepository

class AboutToGenerateViewModel (  ): ViewModel() {

    private val _uiState = MutableStateFlow(AboutToGenerateUiState())
    val uiState: StateFlow<AboutToGenerateUiState> = _uiState

    // Ye method use kar ke pichli screen se data set karoge
    fun setInitialData(type: String, style: String, imageUrl: String) {
        _uiState.update {
            it.copy(
                selectedType = type,
                selectedStyle = style,
                selectedImageRes = imageUrl
            )
        }
    }
    fun setSelectedImage(imageRes: String) {
        _uiState.update {
            it.copy(selectedImageRes = imageRes)
        }
    }

    fun setType(type: String) {
        _uiState.update {
            it.copy(selectedType = type)

        }
        println("selectedroomtype: ${type}")

    }
    fun setStyle(style: String) {
        _uiState.update {
            it.copy(selectedStyle = style)
        }
    }

    fun onGenerateClicked(onResult: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            delay(5000) // simulate generation

            _uiState.update { it.copy(isLoading = false) }

            onResult()
        }
    }
}