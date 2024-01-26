package ru.netology.music.dto

data class Album(
    val artist: String,
    val genre: String,
    val id: Int,
    val published: String,
    val subtitle: String,
    val title: String,
    val tracks: List<Track>
)

data class Track(
    val `file`: String,
    val id: Int
)