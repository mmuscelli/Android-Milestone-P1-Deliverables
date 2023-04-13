package com.example.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MainMusicService : Service() {
    // MediaPlayer variable.
    private lateinit var musicPlayer: MediaPlayer

    // onStartCommand function
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Retrieve music name from Intent.
        val musicName = intent?.getStringExtra("MUSIC_NAME")
        val musicId = this.resources.getIdentifier(musicName, "raw", this.packageName)

        // Initialize MediaPlayer using musicId value.
        musicPlayer = MediaPlayer.create(this, musicId)
        musicPlayer.isLooping = true

        // Start Music
        musicPlayer.start()
        return Service.START_NOT_STICKY
    }

    // onDestroy function
    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.stop()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}