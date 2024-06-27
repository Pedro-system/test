package com.test.test.domain.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.test.test.model.State
import com.test.test.model.StateWithTowns

@Dao
interface StateDAO
{
    @Query(
        """
           SELECT * 
            FROM State
            WHERE
            stateId = :id
        """
    )
    fun fetchStateById(id: Long): LiveData<State>

    @Query(
        """
           SELECT * 
            FROM State
        """
    )
    fun fetchStates(): LiveData<List<StateWithTowns>>
}
