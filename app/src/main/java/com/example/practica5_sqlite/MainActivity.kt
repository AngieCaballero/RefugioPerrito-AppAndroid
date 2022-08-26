package com.example.practica5_sqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var lvLista : ListView
    lateinit var btnNuevo : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var listPerritos = emptyList<Perrito>()

        val database = AppDatabase.getDatabase(this)

        lvLista = findViewById<ListView>(R.id.lvLista)
        btnNuevo = findViewById(R.id.btnNuevo)

        database.perritos().getAll().observe(this, Observer {
            listPerritos = it

            val adapter = PerritoAdapter(this, listPerritos.toMutableList())

            lvLista.adapter = adapter
        })

        lvLista.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, PerritoActivity::class.java)
            intent.putExtra("id", listPerritos[position].Id)

            startActivity(intent)
        }

        btnNuevo.setOnClickListener {
            val intent = Intent(this, NewPerritoActivity::class.java)
            startActivity(intent)
        }
    }
}