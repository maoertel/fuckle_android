package mort.ar.searxme.access

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import mort.ar.searxme.model.SearxInstance


@Database(entities = [SearxInstance::class], version = 1, exportSchema = false)
abstract class SearxDatabase : RoomDatabase() {

    abstract fun searxInstanceDao(): SearxInstanceDao

    /*companion object {

        private var INSTANCE: SearxDatabase? = null

        fun getInstance(context: Context): SearxDatabase? {
            if (INSTANCE == null) {
                synchronized(SearxDatabase::class) {
                    INSTANCE = Room
                        .databaseBuilder(
                            context.applicationContext,
                            SearxDatabase::class.java, "searx.db"
                        )
                        .build()
                }
            }

            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }*/

}