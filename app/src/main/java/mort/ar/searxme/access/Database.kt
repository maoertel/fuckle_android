package mort.ar.searxme.access

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import mort.ar.searxme.model.SearxInstance


@Database(
    entities = [SearxInstance::class],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun searxInstanceDao(): SearxInstanceDao

}
