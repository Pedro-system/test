package com.test.test.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class State(
    @PrimaryKey val stateId: Long, val name: String, val abbr: String
)
{
    override fun toString(): String = name
}


data class StateWithTowns(
    @Embedded val state: State,
    @Relation(
        parentColumn = "stateId",
        entityColumn = "stateId"
    ) val towns: List<Town>
)
{
    override fun toString(): String = state.toString()
}
