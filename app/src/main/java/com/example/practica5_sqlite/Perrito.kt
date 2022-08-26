package com.example.practica5_sqlite

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "perritos")
class Perrito (
    val Nombre:String,
    val Raza:String,
    val Edad:String,
    val Descripcion:String,
    val Imagen : Int,
    @PrimaryKey(autoGenerate = true)
    var Id : Int = 0
) : Serializable