package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button Event (Button Start)
        viewBinding.playMusic.setOnClickListener {
            Intent(this, MainMusicService::class.java).also {
                // Use the name of your music resource.
                it.putExtra("MUSIC_NAME", "sleep_away")
                startService(it)
            }
        }

        // Button Event (Button Stop)
        viewBinding.stopMusic.setOnClickListener {
            Intent(this, MainMusicService::class.java).also {
                stopService(it)
            }
        }
    }
}