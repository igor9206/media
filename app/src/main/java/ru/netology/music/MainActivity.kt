package ru.netology.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.music.adapter.AlbumAdapter
import ru.netology.music.databinding.ActivityMainBinding
import ru.netology.music.viewmodel.AlbumViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = AlbumAdapter()
        binding.recyclerView.adapter = adapter

        viewModel.data.observe(this) {
            adapter.submitList(it)
        }
    }
}