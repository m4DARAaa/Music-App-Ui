package com.example.musicappui

import androidx.annotation.DrawableRes

data class Lib(@DrawableRes val icon:Int, val name:String)
val libraries= listOf<Lib>(
    Lib(R.drawable.ic_music_player_green,"PlayerLIst"),
    Lib(R.drawable.baseline_mic_24,"Artists"),
    Lib(R.drawable.baseline_album_24,"Album"),
    Lib(R.drawable.baseline_music_note_24,"Song"),
    Lib(R.drawable.baseline_view_agenda_24,"Genre")

)

