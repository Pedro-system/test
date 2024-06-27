package com.test.test.domain.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.test.test.model.Town

@Dao
interface TownDAO
{
    @Query(
        """
           SELECT 
           *
            FROM Town
            WHERE
            townId = :id
        """
    )
    fun fetchTownNameById(id: Long): LiveData<Town>
}