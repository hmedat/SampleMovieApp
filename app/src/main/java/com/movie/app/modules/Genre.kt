package com.movie.app.modules

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genre")
data class Genre (
    @PrimaryKey() var id: Long,
    @ColumnInfo(name = "name") var name: String? = null
)
