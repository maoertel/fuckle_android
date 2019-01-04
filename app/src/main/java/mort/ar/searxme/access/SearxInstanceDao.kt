package mort.ar.searxme.access

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import mort.ar.searxme.model.SearxInstance


@Dao
abstract class SearxInstanceDao {

    @Query("SELECT * FROM searx_instances")
    abstract fun getAllSearxInstances(): Flowable<List<SearxInstance>>

    @Query("SELECT * FROM searx_instances WHERE name IS :name")
    abstract fun getSearxInstance(name: String): Flowable<SearxInstance>

    @Query("SELECT * FROM searx_instances WHERE favorite")
    abstract fun getFavoriteInstance(): Flowable<SearxInstance>

    @Query("SELECT * FROM searx_instances WHERE name IN (:names)")
    abstract fun getSearxInstances(names: List<String>): Flowable<List<SearxInstance>>

    @Insert(onConflict = REPLACE)
    abstract fun insert(searxInstance: SearxInstance)

    @Query("DELETE FROM searx_instances")
    abstract fun deleteAll()

}
