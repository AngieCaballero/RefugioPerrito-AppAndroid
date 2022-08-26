package com.example.practica5_sqlite

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PerritosDao {
    @Query("SELECT * FROM perritos")
    fun getAll(): LiveData<List<Perrito>>

    @Query("SELECT * FROM perritos WHERE Id = :id")
    fun get(id: Int): LiveData<Perrito>

    @Insert
    fun insertAll(vararg perritos: Perrito): List<Long>

    @Update
    fun update(perrito: Perrito)

    @Delete
    fun delete(perrito: Perrito)
}