package mort.ar.searxme.database

import androidx.room.RoomDatabase
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.database.daos.SearxInstanceDao

@androidx.room.Database(
    entities = [SearxInstanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun searxInstanceDao(): SearxInstanceDao

}