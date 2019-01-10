package mort.ar.searxme.access

import androidx.room.RoomDatabase
import mort.ar.searxme.model.SearxInstance

@androidx.room.Database(
    entities = [SearxInstance::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun searxInstanceDao(): SearxInstanceDao

}
