package com.movie.app.modules

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "genre")
data class Genre(@PrimaryKey() var id: Long = 0, @ColumnInfo(name = "name") var name: String? = null)
