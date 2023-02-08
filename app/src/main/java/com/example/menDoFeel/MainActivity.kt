package com.example.menDoFeel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.menDoFeel.adapters.VideoAdapter
import com.example.menDoFeel.databinding.ActivityMainBinding
import com.example.menDoFeel.model.ItemPlayer
import com.example.menDoFeel.model.Video

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: VideoAdapter
    private val videos = ArrayList<Video>()
    private val itemPlayers = ArrayList<ItemPlayer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = VideoAdapter(this, videos, object : VideoAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(itemPlayer: ItemPlayer) {
                itemPlayers.add(itemPlayer)
            }
        })

        binding.viewPager2.adapter = adapter

        videos.add(
            Video(
                "For Bigger Blazes",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"
            )

        )

        videos.add(
            Video(
                "Elephant Dream",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
            )
        )

        videos.add(
            Video(
                "Big Buck Bunny",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            )
        )

        videos.add(
            Video(
                "In Dream",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
            )
        )

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = itemPlayers.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1) {
                    val player = itemPlayers[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = itemPlayers.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = itemPlayers[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val index = itemPlayers.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = itemPlayers[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = itemPlayers.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = itemPlayers[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (itemPlayers.isNotEmpty()) {
            for (item in itemPlayers) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
}