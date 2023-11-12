package com.example.auracle.com.example.auracle

interface PlayerInterface {
    companion object {
        const val START_PLAYING = "start_playing"
        const val RESUME_PLAYING = "resume_playing"
        const val STOP_PLAYING = "stop_playing"
        const val PLAY_NEXT = "play_next"
        const val PLAY_PREVIOUS = "play_previous"
        const val TOGGLE_PLAYING = "toggle_playing"
        const val TOGGLE_LOOPING = "toggle_looping"
        const val SEEK = "seek"
        const val NOTIFICATION_START_PLAYING = "notification_start_playing"
        const val NOTIFICATION_STOP_PLAYING = "notification_stop_playing"
        const val NOTIFICATION_PLAY_NEXT = "notification_play_next"
        const val NOTIFICATION_PLAY_PREVIOUS = "notification_play_previous"
        const val NOTIFICATION_TOGGLE_PLAYING = "notification_toggle_playing"
    }

    fun updateTimeUI(position: Int)
    fun newEpisodePlayUI(progress: Int, duration: Int)
    fun togglePlayingUI()
    fun playNextEpisode()
    fun playPreviousEpisode()
}