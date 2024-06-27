package com.test.test.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.test.domain.repository.dao.StateDAO
import com.test.test.domain.repository.dao.TownDAO
import com.test.test.domain.repository.dao.UserDAO
import com.test.test.model.State
import com.test.test.model.Town
import com.test.test.model.User

const val DB_NAME = "TestDB.db"

@Database(entities = [
    User::class, State::class,Town::class
                     ], version = 1, exportSchema = false)
abstract class DB() : RoomDatabase()
{
    abstract fun userDAO() : UserDAO
    abstract fun stateDAO() : StateDAO
    abstract fun townDAO() : TownDAO

    companion object
    {
        @Volatile
        private var instance : DB? = null

        /**
         * Carga la base de datos con el catalogo de estados
         */
        fun getInstance(ctx: Context) : DB = instance ?: synchronized(this){
            instance = Room.databaseBuilder(ctx, DB::class.java, DB_NAME).createFromAsset("TestDB.db").build()
            return@synchronized instance
        }!!
    }
}