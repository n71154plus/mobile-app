package io.music_assistant.client.deeplink

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DeepLinkCoordinator {
    private val _pendingUri = MutableStateFlow<String?>(null)
    val pendingUri: StateFlow<String?> = _pendingUri.asStateFlow()

    fun submit(uri: String) {
        _pendingUri.value = uri
    }

    fun clearPendingUri() {
        _pendingUri.value = null
    }
}
