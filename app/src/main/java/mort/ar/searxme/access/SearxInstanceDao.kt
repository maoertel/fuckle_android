package mort.ar.searxme.access

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.model.SearxInstance


@Dao
abstract class SearxInstanceDao {

    @Query("SELECT * FROM searx_instances")
    abstract fun getAllSearxInstances(): Observable<List<SearxInstance>>

    @Query("SELECT * FROM searx_instances")
    abstract fun getAllSearxInstancesSingle(): Single<List<SearxInstance>>

    @Query("SELECT * FROM searx_instances WHERE name IS :name")
    abstract fun getSearxInstance(name: String): Single<SearxInstance>

    @Query("SELECT * FROM searx_instances WHERE favorite")
    abstract fun getFavoriteInstance(): Observable<SearxInstance>

    @Query("SELECT * FROM searx_instances WHERE favorite")
    abstract fun getFavoriteInstanceSingle(): Single<SearxInstance>

    @Query("SELECT * FROM searx_instances WHERE name IN (:names)")
    abstract fun getSearxInstances(names: List<String>): Single<List<SearxInstance>>

    @Insert(onConflict = REPLACE)
    abstract fun insert(searxInstance: SearxInstance): Completable

    @Update
    abstract fun updateInstance(instance: SearxInstance): Completable

    @Query("DELETE FROM searx_instances")
    abstract fun deleteAll()

}
