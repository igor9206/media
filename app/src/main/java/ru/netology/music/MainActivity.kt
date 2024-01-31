package ru.netology.music

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.music.adapter.AlbumAdapter
import ru.netology.music.adapter.OnClickListener
import ru.netology.music.databinding.ActivityMainBinding
import ru.netology.music.media.MediaLifecycleObserver
import ru.netology.music.model.TrackItem
import ru.netology.music.viewmodel.AlbumViewModel

private const val trackUrl =
    "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AlbumViewModel by viewModels()
    private val mediaObserver = MediaLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = AlbumAdapter(object : OnClickListener {
            override fun play(trackItem: TrackItem?) {
                mediaPlayer(trackItem)
            }

            override fun next() {
                nextTrack()
            }

            override fun previous() {
                previousTrack()
            }

        })

        binding.recyclerView.adapter = adapter
        viewModel.data.observe(this) { feedItems ->
            adapter.submitList(feedItems)

            if (mediaObserver.listTrack.isEmpty()) {
                mediaObserver.listTrack = feedItems.filterIsInstance<TrackItem>()
            }
        }

        mediaObserver.player?.setOnCompletionListener {
            nextTrack()
        }

    }

    private fun nextTrack() {
        var currentPosition =
            mediaObserver.listTrack.firstOrNull { it.id == mediaObserver.currentTrack?.id }
                .let { track ->
                    mediaObserver.listTrack.indexOf(track) + 1
                }
        if (currentPosition >= mediaObserver.listTrack.size) {
            currentPosition = 0
        }
        mediaPlayer(mediaObserver.listTrack[currentPosition])
    }

    private fun previousTrack() {
        var currentPosition =
            mediaObserver.listTrack.firstOrNull { it.id == mediaObserver.currentTrack?.id }
                .let { track ->
                    mediaObserver.listTrack.indexOf(track) - 1
                }
        if (currentPosition < 0) {
            currentPosition = mediaObserver.listTrack.size - 1
        }
        mediaPlayer(mediaObserver.listTrack[currentPosition])
    }

    private fun mediaPlayer(trackItem: TrackItem?) {
        when {
            trackItem != null -> {
                when (mediaObserver.currentTrack) {
                    null -> {
                        mediaObserver.apply {
                            currentTrack = trackItem
                            player?.setDataSource(
                                trackUrl + mediaObserver.listTrack.first().file
                            )
                            player?.prepareAsync()
                        }
                    }

                    else -> {
                        if (trackItem.id != mediaObserver.currentTrack!!.id) {
                            if (mediaObserver.player?.isPlaying == true) {
                                mediaObserver.player?.stop()
                            }
                            mediaObserver.apply {
                                player?.reset()
                                player?.setDataSource(
                                    trackUrl + trackItem.file
                                )
                                player?.prepareAsync()
                            }
                            mediaObserver.currentTrack = trackItem
                        }
                    }
                }
            }

            mediaObserver.currentTrack == null -> {
                mediaObserver.apply {
                    currentTrack = listTrack.first()
                    player?.setDataSource(
                        trackUrl + mediaObserver.listTrack.first().file
                    )
                    player?.prepareAsync()
                }
            }
        }
        playPause(mediaObserver.currentTrack)
    }

    private fun playPause(trackItem: TrackItem?) {
        if (trackItem != null) {
            when (mediaObserver.player?.isPlaying) {
                false -> {
                    if (mediaObserver.player?.currentPosition == 0) {
                        mediaObserver.player?.setOnPreparedListener {
                            it.start()
                        }
                    } else {
                        mediaObserver.player?.start()
                    }
                    mediaObserver.currentTrack = trackItem.copy(play = true)
                }

                else -> {
                    mediaObserver.player?.pause()
                    mediaObserver.currentTrack = trackItem.copy(play = false)
                }
            }
        }
        viewModel.setPlayPauseTrack(mediaObserver.currentTrack)
    }


}