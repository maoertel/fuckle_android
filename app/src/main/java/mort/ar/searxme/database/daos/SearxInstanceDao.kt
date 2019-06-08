package mort.ar.searxme.database.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity

@Dao
abstract class SearxInstanceDao {

    @Query("SELECT * FROM searx_instances")
    abstract fun observeAllSearxInstances(): Observable<List<SearxInstanceEntity>>

    @Query("SELECT * FROM searx_instances")
    abstract fun getAllSearxInstances(): Single<List<SearxInstanceEntity>>

    @Query("SELECT * FROM searx_instances WHERE name IS :name")
    abstract fun getSearxInstance(name: String): Single<SearxInstanceEntity>

    @Query("SELECT * FROM searx_instances WHERE favorite")
    abstract fun observeFavoriteInstance(): Observable<SearxInstanceEntity>

    @Query("SELECT * FROM searx_instances WHERE favorite")
    abstract fun getFavoriteInstance(): Single<SearxInstanceEntity>

    @Query("SELECT * FROM searx_instances WHERE name IN (:names)")
    abstract fun getSearxInstances(names: List<String>): Single<List<SearxInstanceEntity>>

    @Insert(onConflict = REPLACE)
    abstract fun insert(searxInstanceEntity: SearxInstanceEntity): Completable

    @Update
    abstract fun updateInstance(instanceEntity: SearxInstanceEntity): Completable

    @Query("DELETE FROM searx_instances")
    abstract fun deleteAll()

    @Query("SELECT * FROM searx_instances WHERE favorite")
    abstract fun getFavoriteInstanceSync(): SearxInstanceEntity

    @Query("SELECT * FROM searx_instances WHERE name IS :name")
    abstract fun getSearxInstanceSync(name: String): SearxInstanceEntity

    @Update
    abstract fun updateInstanceSync(instanceEntity: SearxInstanceEntity)

    @Transaction
    open fun changeFavoriteInstanceSync(newFavoriteInstance: String) {
        val fav = getFavoriteInstanceSync()
        val newFav = getSearxInstanceSync(newFavoriteInstance)

        updateInstanceSync(fav.copy(favorite = false))
        updateInstanceSync(newFav.copy(favorite = true))
    }

}