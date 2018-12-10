package mort.ar.searxme.access

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Observable
import mort.ar.searxme.model.SearxInstance

@Dao
abstract class SearxInstanceDao {

    @Query("SELECT * from searx_instances")
    abstract fun getAll(): Observable<List<SearxInstance>>

    @Query("SELECT name from searx_instances")
    abstract fun getSearxInstance(name: String): Observable<SearxInstance>

    @Query("SELECT name from searx_instances where name IN (:names)")
    abstract fun getSearxInstances(names: List<String>): Observable<List<SearxInstance>>

    @Insert(onConflict = REPLACE)
    abstract fun insert(searxInstance: SearxInstance)

    @Query("DELETE from searx_instances")
    abstract fun deleteAll()

}
