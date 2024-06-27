package com.test.test.domain.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.test.model.User

@Dao
interface UserDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertUser(user : User) :  Long

    @Query("""
SELECT * 
From User 
WHERE userId = :id
    """)
    fun fetchUserById(id:Long): LiveData<User>

    @Query("""
SELECT * 
From User 
    """)
    fun fetchUsers(): LiveData<List<User>>

    @Update()
    suspend fun updateUser(user:User): Int

    @Delete
    suspend fun deleteUser(user:User): Int

}