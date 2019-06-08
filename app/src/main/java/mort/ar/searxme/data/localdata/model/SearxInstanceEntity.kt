package mort.ar.searxme.data.localdata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searx_instances")
data class SearxInstanceEntity(

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