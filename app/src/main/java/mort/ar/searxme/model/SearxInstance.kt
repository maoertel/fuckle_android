package mort.ar.searxme.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "searx_instances")
data class SearxInstance(
    @PrimaryKey
    @ColumnInfo
    val name: String,

    @ColumnInfo
    val url: String,

    @ColumnInfo
    val priority: Int = 0,

    @ColumnInfo
    val favorite: Boolean = false,

    @ColumnInfo(name = "times_failed")
    val timesFailed: Long = 0
)