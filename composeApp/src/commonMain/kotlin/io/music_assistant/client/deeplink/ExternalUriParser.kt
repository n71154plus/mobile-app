package io.music_assistant.client.deeplink

import io.music_assistant.client.data.model.server.MediaType

data class DeepLinkItemTarget(
    val itemId: String,
    val mediaType: MediaType,
    val providerId: String
)

object ExternalUriParser {
    private val genericProviderUriRegex = Regex(
        pattern = "^([a-zA-Z0-9_.-]+)://(track|album|artist|playlist|podcast|audiobook)/([^/?#]+)",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    private val spotifyWebRegex = Regex(
        pattern = "^https?://open\\.spotify\\.com/(track|album|artist|playlist)/([^/?#]+)",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    private val spotifySchemeRegex = Regex(
        pattern = "^spotify:(track|album|artist|playlist):([^:?/#]+)",
        options = setOf(RegexOption.IGNORE_CASE)
    )
    private val spotifySlashedSchemeRegex = Regex(
        pattern = "^spotify://(track|album|artist|playlist)/([^/?#]+)",
        options = setOf(RegexOption.IGNORE_CASE)
    )

    fun parseItemTarget(rawUri: String): DeepLinkItemTarget? {
        val uri = rawUri.trim()
        if (uri.isBlank()) return null

        genericProviderUriRegex.matchEntire(uri)?.let { match ->
            val provider = match.groupValues[1]
            val mediaType = mediaTypeFromPath(match.groupValues[2]) ?: return null
            val itemId = match.groupValues[3]
            return DeepLinkItemTarget(itemId = itemId, mediaType = mediaType, providerId = provider)
        }

        spotifyWebRegex.matchEntire(uri)?.let { match ->
            val mediaType = mediaTypeFromPath(match.groupValues[1]) ?: return null
            val itemId = match.groupValues[2]
            return DeepLinkItemTarget(itemId = itemId, mediaType = mediaType, providerId = "spotify")
        }

        spotifySchemeRegex.matchEntire(uri)?.let { match ->
            val mediaType = mediaTypeFromPath(match.groupValues[1]) ?: return null
            val itemId = match.groupValues[2]
            return DeepLinkItemTarget(itemId = itemId, mediaType = mediaType, providerId = "spotify")
        }

        spotifySlashedSchemeRegex.matchEntire(uri)?.let { match ->
            val mediaType = mediaTypeFromPath(match.groupValues[1]) ?: return null
            val itemId = match.groupValues[2]
            return DeepLinkItemTarget(itemId = itemId, mediaType = mediaType, providerId = "spotify")
        }

        return null
    }

    private fun mediaTypeFromPath(path: String): MediaType? = when (path.lowercase()) {
        "artist" -> MediaType.ARTIST
        "album" -> MediaType.ALBUM
        "track" -> MediaType.TRACK
        "playlist" -> MediaType.PLAYLIST
        "podcast" -> MediaType.PODCAST
        "audiobook" -> MediaType.AUDIOBOOK
        else -> null
    }
}
