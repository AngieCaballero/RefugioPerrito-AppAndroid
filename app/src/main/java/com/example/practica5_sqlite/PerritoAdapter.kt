package com.example.practica5_sqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class PerritoAdapter constructor (private val context: Context, private var data: MutableList<Perrito>) : BaseAdapter()
{
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItem(position: Int): Perrito = data[position]

    override fun getCount(): Int = data.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view : View?
        val viewRowHolder: ListRowHolder

        if(convertView == null) {
            view = this.inflater.inflate(R.layout.item_perrito, parent, false)
            viewRowHolder = ListRowHolder(view)
            view.tag = viewRowHolder
        } else {
            view = convertView
            viewRowHolder = view.tag as ListRowHolder
        }
        viewRowHolder.tvNombre.text = data[position].Nombre
        viewRowHolder.tvRaza.text = data[position].Raza
        viewRowHolder.tvEdad.text = data[position].Edad
        //viewRowHolder.vImagen.setImageResource(data[position].Imagen)

        val imageUri = ImageController.getImageUri(context, data[position].Id.toLong())
        viewRowHolder.vImagen.setImageURI(imageUri)

        return view
    }
    private class ListRowHolder(row: View?) {
        val tvNombre: TextView
        val tvRaza: TextView
        val tvEdad: TextView
        val vImagen: ImageView

        init {
            this.tvNombre = row?.findViewById(R.id.tvNombre) as TextView
            this.tvRaza = row.findViewById(R.id.tvRaza)
            this.tvEdad = row.findViewById(R.id.tvEdad)
            this.vImagen = row.findViewById(R.id.imageView)
        }
    }

}