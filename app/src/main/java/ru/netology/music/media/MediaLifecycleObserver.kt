package ru.netology.music.media

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import ru.netology.music.model.TrackItem

class MediaLifecycleObserver : LifecycleEventObserver {
    var player: MediaPlayer? = MediaPlayer()

    var listTrack = listOf<TrackItem>()
    var currentTrack: TrackItem? = null

//    fun play() {
//        player?.setOnPreparedListener {
//            it.start()
//        }
//        player?.prepareAsync()
//    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                player?.pause()
            }

            Lifecycle.Event.ON_STOP -> {
                player?.release()
                player = null
            }

            Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
            else -> Unit
        }
    }
}