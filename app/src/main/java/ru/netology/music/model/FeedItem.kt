package ru.netology.music.model

sealed interface FeedItem {
    val id: Int
}

data class AlbumItem(
    override val id: Int,
    val artist: String,
    val genre: String,
    val published: String,
    val subtitle: String,
    val title: String,
) : FeedItem

data class TrackItem(
    override val id: Int,
    val `file`: String,
) : FeedItem