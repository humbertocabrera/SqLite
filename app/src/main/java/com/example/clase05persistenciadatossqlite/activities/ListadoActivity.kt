package com.example.clase05persistenciadatossqlite.activities

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clase05persistenciadatossqlite.R
import com.example.clase05persistenciadatossqlite.adapters.AnimesAdapter
import com.example.clase05persistenciadatossqlite.db.ManejadorBaseDatos
import com.example.clase05persistenciadatossqlite.interfaces.animesInterface
import com.example.clase05persistenciadatossqlite.modelos.Anime
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListadoActivity : AppCompatActivity(), animesInterface {

    private lateinit var recyclerView: RecyclerView
    private var listaDeAnimes = ArrayList<Anime>()
    private lateinit var fab: FloatingActionButton
    private val ORDENAR_POR_NOMBRE : String  = "nombre"
    val columnas = arrayOf("id", "nombre","demo", "rating" )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)
        inicializarVistas()
        asignarEventos()
    }
    override fun onResume() {
        super.onResume()
        traerMisAnimes()
    }
    private fun inicializarVistas(){
        recyclerView = findViewById(R.id.recycler)
        fab = findViewById(R.id.fab)
    }

    private fun asignarEventos(){
        fab.setOnClickListener{
            val intent = Intent(this, AgregarActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_listado, menu)
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val manejador = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(manejador.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                buscarAnime("%" + p0 + "%")
                Toast.makeText(applicationContext, p0, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(TextUtils.isEmpty(p0)){
                    this.onQueryTextSubmit("");
                }
                return false
            }


        })
        return super.onCreateOptionsMenu(menu)
    }


    private fun traerMisAnimes() {
        val baseDatos = ManejadorBaseDatos(this)
        val cursor = baseDatos.traerTodos(columnas, ORDENAR_POR_NOMBRE)
        recorrerResultados( cursor)
        baseDatos.cerrarConexion()
    }

    @SuppressLint("Range")
    private fun buscarAnime(nombre: String) {
        val baseDatos = ManejadorBaseDatos(this)
        val camposATraer = arrayOf(nombre)
        val cursor = baseDatos.seleccionar(columnas,"nombre like ?", camposATraer, ORDENAR_POR_NOMBRE)
        recorrerResultados( cursor)
        baseDatos.cerrarConexion()
    }

    @SuppressLint("Range")
    fun recorrerResultados(cursor : Cursor){
        if(listaDeAnimes.size > 0)
            listaDeAnimes.clear()

        if(cursor.moveToFirst()){
            do{
                val anime_id = cursor.getInt(cursor.getColumnIndex("id"))
                val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                val demo = cursor.getString(cursor.getColumnIndex("demo"))
                val rating = cursor.getString(cursor.getColumnIndex("rating"))
                val anime: Anime
                anime = Anime(anime_id, nombre, demo, rating)
                listaDeAnimes.add(anime)
            }while(cursor.moveToNext())
        }
        val linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        recyclerView.layoutManager = linearLayoutManager

        val adapter = AnimesAdapter(this, listaDeAnimes,this)
        recyclerView.adapter = adapter

    }

    override fun animeEliminado() {
        Log.d("PRUEBAS", "animeEliminado")
        traerMisAnimes()
    }

    override fun editarAnime(anime: Anime) {
        Log.d("PRUEBAS", "editar Anime "+anime.id)
        val intent = Intent(this, EditarActivity::class.java)
        intent.putExtra("id",anime.id)
        intent.putExtra("nombre",anime.nombre)
        intent.putExtra("rating",anime.rating)
        startActivity(intent)
    }


}