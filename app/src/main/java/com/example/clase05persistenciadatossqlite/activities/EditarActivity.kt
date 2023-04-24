package com.example.clase05persistenciadatossqlite.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.clase05persistenciadatossqlite.R
import com.example.clase05persistenciadatossqlite.db.ManejadorBaseDatos
import com.example.clase05persistenciadatossqlite.modelos.Anime
import com.google.android.material.snackbar.Snackbar

class EditarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var bnGuardar: Button
    private lateinit var etAnimeEdit: EditText
    private lateinit var etDemoEdit: EditText
    private lateinit var spRatingEdit: Spinner
    private val ratings = arrayOf("10", "9", "8", "7", "6", "5", "4", "3", "2", "1")
    private var ratingSeleccionado: String = ""
    private lateinit var tvAnimeEdit: TextView
    var anime: Anime? = null
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)
        //  setSupportActionBar(toolbar)
        supportActionBar?.title = "Edición"
        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        inicializarVistas()
        id = intent.getIntExtra("id", 0)
        buscarAnime(id)
        poblarCampos()
    }

    private fun poblarCampos() {
        etAnimeEdit.setText(anime?.nombre)
        etDemoEdit.setText(anime?.demo.toString())
        val position = ratings.indexOf(anime?.rating)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ratings)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spRatingEdit.adapter = adapter
        spRatingEdit.onItemSelectedListener = this
        if (position >= 0) {
            spRatingEdit.setSelection(position)
            ratingSeleccionado = ratings[position]
        }
    }

    private fun inicializarVistas() {
        etAnimeEdit = findViewById(R.id.etAnimeEdit)
        bnGuardar = findViewById(R.id.bnGuardar)
        etDemoEdit = findViewById(R.id.etDemoEdit)
        spRatingEdit = findViewById(R.id.spRatingEdit)
        tvAnimeEdit = findViewById(R.id.tvAnimeEdit)
        bnGuardar.setOnClickListener {
            actualizarAnime(etAnimeEdit.text.toString(), etDemoEdit.text.toString(), ratingSeleccionado)
        }
    }

    val columnaNombreAnime = "nombre"
    val columnaPrecio = "demo"
    val columnaConsola = "rating"

    private fun actualizarAnime(nombreAnime: String, demo: String, rating: String) {
        if (!TextUtils.isEmpty(rating)) {
            val baseDatos = ManejadorBaseDatos(this)
            val contenido = ContentValues()
            contenido.put(columnaNombreAnime, nombreAnime)
            contenido.put(columnaPrecio, demo)
            contenido.put(columnaConsola, rating)
            if ( id > 0) {
                val argumentosWhere = arrayOf(id.toString())
                val id_actualizado = baseDatos.actualizar(contenido, "id = ?", argumentosWhere)
                if (id_actualizado > 0) {
                    Snackbar.make(etAnimeEdit, "Anime actualizado", Snackbar.LENGTH_LONG).show()
                } else {
                    val alerta = AlertDialog.Builder(this)
                    alerta.setTitle("Atención")
                        .setMessage("No fue posible actualizarlo")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar") { dialog, which ->

                        }
                        .show()
                }
            } else {
                Toast.makeText(this, "no hiciste id", Toast.LENGTH_LONG).show()
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun buscarAnime(idAnime: Int) {

        if (idAnime > 0) {
            val baseDatos = ManejadorBaseDatos(this)
            val columnasATraer = arrayOf("id", "nombre", "demo", "rating")
            val condicion = " id = ?"
            val argumentos = arrayOf(idAnime.toString())
            val ordenarPor = "id"
            val cursor = baseDatos.seleccionar(columnasATraer, condicion, argumentos, ordenarPor)

            if (cursor.moveToFirst()) {
                do {
                    val anime_id = cursor.getInt(cursor.getColumnIndex("id"))
                    val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                    val demo = cursor.getString(cursor.getColumnIndex("demo"))
                    val rating = cursor.getString(cursor.getColumnIndex("rating"))
                    anime = Anime(anime_id, nombre, demo, rating)
                } while (cursor.moveToNext())
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        ratingSeleccionado = ratings[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}