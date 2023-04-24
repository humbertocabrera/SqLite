package com.example.clase05persistenciadatossqlite.interfaces

import com.example.clase05persistenciadatossqlite.modelos.Anime

public interface animesInterface {
    fun animeEliminado()
    fun editarAnime(anime: Anime)

}