package com.example.clase05persistenciadatossqlite.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clase05persistenciadatossqlite.R
import com.example.clase05persistenciadatossqlite.db.ManejadorBaseDatos
import com.example.clase05persistenciadatossqlite.interfaces.animesInterface
import com.example.clase05persistenciadatossqlite.modelos.Anime


class AnimesAdapter(contexto: Context,  animes: ArrayList<Anime>, animeInterface: animesInterface) :
    RecyclerView.Adapter<AnimesAdapter.ContenedorDeVista> () {

    var contexto: Context? = contexto
    var animesInterface: animesInterface = animeInterface
    var innerAnimes: ArrayList<Anime> = animes

    inner class ContenedorDeVista(view: View):
        RecyclerView.ViewHolder(view){

        val etTitleAnime : TextView
        val tvDemoDis : TextView
        val tvRatingDis : TextView
        val btnEdit : ImageView
        val btnDelete : ImageView

        init {
            etTitleAnime = view.findViewById(R.id.etTitleAnime)
            tvDemoDis = view.findViewById(R.id.tvDemoDis)
            tvRatingDis = view.findViewById(R.id.tvRatingDis)
            btnEdit = view.findViewById(R.id.img01)
            btnDelete = view.findViewById(R.id.img02)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContenedorDeVista {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.row_juego, parent, false)

        return ContenedorDeVista(view)
    }

    override fun getItemCount(): Int {
        return innerAnimes.size
    }

    override fun onBindViewHolder(holder: ContenedorDeVista, position: Int) {
        val anime: Anime = innerAnimes.get(position)
        holder.btnDelete.setOnClickListener{
            val baseDatos = ManejadorBaseDatos(contexto!!)
            val argumentosWhere = arrayOf(anime.id.toString())
            baseDatos.eliminar("id = ?", argumentosWhere)
            animesInterface.animeEliminado()
        }
        holder.btnEdit.setOnClickListener{
            animesInterface.editarAnime(anime)
        }
        holder.etTitleAnime.text = anime.nombre
        holder.tvDemoDis.text = anime.demo
        holder.tvRatingDis.text = anime.rating
    }
}
