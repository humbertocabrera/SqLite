package com.example.clase05persistenciadatossqlite.activities

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.clase05persistenciadatossqlite.R
import com.example.clase05persistenciadatossqlite.db.ManejadorBaseDatos
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AgregarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private  lateinit var fabAgregar: FloatingActionButton
    private  lateinit var etAnime: EditText
    private  lateinit var etDemo: EditText
    private  lateinit var spRating: Spinner
    private val ratings = arrayOf("10", "9", "8", "7", "6", "5", "4", "3", "2", "1")
    private var ratingSeleccionado: String = ""
    private  lateinit var tvJuego: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        inicializarVistas()

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, ratings)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spRating.adapter = adapter
        spRating.onItemSelectedListener = this
        fabAgregar.setOnClickListener{
            insertarAnime( etAnime.text.toString(),  etDemo.text.toString(), ratingSeleccionado)
        }
    }

    val columnaID = "id"
    val columnaNombreAnime = "nombre"
    val columnaDemografia = "demo"
    val columnaRating = "rating"
    var id: Int = 0
    private fun insertarAnime(nombreAnime: String, demografia: String, rating: String){
       if(!TextUtils.isEmpty(rating)) {
           val baseDatos = ManejadorBaseDatos(this)
           //  val columnas = arrayOf(columnaID, columnaNombreJuego, columnaPrecio, columnaConsola)
           val contenido = ContentValues()
           contenido.put(columnaNombreAnime, nombreAnime)
           contenido.put(columnaDemografia, demografia)
           contenido.put(columnaRating, rating)
           //guardar imagen
            id = baseDatos.insertar(contenido).toInt()
           if (id > 0) {
               Toast.makeText(this,  nombreAnime + " agregado a la lista", Toast.LENGTH_LONG).show()
               finish()
           } else
               Toast.makeText(this, "Ups no se pudo guardar el anime", Toast.LENGTH_LONG).show()
           baseDatos.cerrarConexion()
       }else{
           Snackbar.make(tvJuego,"Favor seleccionar una calificación", 0).show()
       }
    }

    private fun inicializarVistas(){
        etAnime = findViewById(R.id.etAnime)
        fabAgregar = findViewById(R.id.fabAgregar)
        etDemo = findViewById(R.id.etDemo)
        spRating = findViewById(R.id.spRating)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        ratingSeleccionado = ratings[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }



}