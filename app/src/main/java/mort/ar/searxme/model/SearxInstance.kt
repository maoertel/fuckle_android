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
    val priority: Int,

    @ColumnInfo
    val primary: Boolean,

    @ColumnInfo(name = "times_failed")
    val timesFailed: Long
)