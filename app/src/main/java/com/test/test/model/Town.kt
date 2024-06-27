package com.test.test.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Town(
@PrimaryKey val townId: Long
,val key:Long
,val stateId: Long
,val name: String
)
{
    override fun toString(): String = name
}
