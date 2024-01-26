package ru.netology.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.netology.music.dto.Album
import ru.netology.music.model.AlbumItem
import ru.netology.music.model.FeedItem
import ru.netology.music.model.TrackItem
import java.io.IOException

private const val list = "{\n" +
        "  \"id\": 1,\n" +
        "  \"title\": \"SoundHelix Songs\",\n" +
        "  \"subtitle\": \"www.soundhelix.com\",\n" +
        "  \"artist\": \"T. Schürger\",\n" +
        "  \"published\": \"2009, 2010, 2011, 2013\",\n" +
        "  \"genre\": \"electronic\",\n" +
        "  \"tracks\": [\n" +
        "    {\n" +
        "      \"id\": 1,\n" +
        "      \"file\": \"1.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 2,\n" +
        "      \"file\": \"2.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 3,\n" +
        "      \"file\": \"3.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 4,\n" +
        "      \"file\": \"4.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 5,\n" +
        "      \"file\": \"5.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 6,\n" +
        "      \"file\": \"6.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 7,\n" +
        "      \"file\": \"7.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 8,\n" +
        "      \"file\": \"8.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 9,\n" +
        "      \"file\": \"9.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 10,\n" +
        "      \"file\": \"10.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 11,\n" +
        "      \"file\": \"11.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 12,\n" +
        "      \"file\": \"12.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 13,\n" +
        "      \"file\": \"13.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 14,\n" +
        "      \"file\": \"14.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 15,\n" +
        "      \"file\": \"15.mp3\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 16,\n" +
        "      \"file\": \"16.mp3\"\n" +
        "    }\n" +
        "  ]\n" +
        "}"

private const val albumUrl =
    "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"

class AlbumViewModel : ViewModel() {
    private val gson = Gson()

    private val _data: MutableLiveData<List<FeedItem>> = MutableLiveData()
    val data: LiveData<List<FeedItem>> = _data


    init {
//        load()
        testLoad()
    }

    fun testLoad() {
        val album = gson.fromJson(list, Album::class.java)
        val pars = parseAlbum(album)
        _data.value = pars
    }

    fun load() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(albumUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val album = gson.fromJson(response.body!!.string(), Album::class.java)
                    _data.value = parseAlbum(album)
                }
            }
        })
    }

    private fun parseAlbum(album: Album): List<FeedItem> {
        return listOf<FeedItem>(
            AlbumItem(
                album.id, album.artist, album.genre, album.published, album.subtitle, album.title
            )
        ) + album.tracks.map {
            TrackItem(it.id, it.file)
        }
    }


}