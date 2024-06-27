package com.test.test.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val  DEFAULT_VALUE = Integer.MIN_VALUE.toLong()
@Entity
data class User(
    @PrimaryKey(autoGenerate = true)var userId: Long = 0,
    val nombre: String,
    val edad: String,
    val domicilio_calle: String,
    val domicilio_no_exterior: String,
    val domicilio_no_interior: String,
    val domicilio_colonia: String,
    val stateId : Long,
    val townId : Long,
    var photoName:String
)

