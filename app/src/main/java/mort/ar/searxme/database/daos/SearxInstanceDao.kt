package mort.ar.searxme.database.daos

import androidx.room.*
import androidx.room.OnConflictStrategy.FAIL
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
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

    @Insert(onConflict = FAIL)
    abstract fun insert(searxInstanceEntity: SearxInstanceEntity): Completable

    @Update
    abstract fun updateInstance(instanceEntity: SearxInstanceEntity): Completable

    @Query("DELETE FROM searx_instances")
    abstract fun deleteAll()

    @Transaction
    open fun changeFavoriteInstance(newFavoriteInstance: String): Completable =
        Completable.concat(
            listOf(
                setInstanceToFavorite(false) { getFavoriteInstance() },
                setInstanceToFavorite(true) { getSearxInstance(newFavoriteInstance) }
            )
        )

    private fun setInstanceToFavorite(
        markAsFavorite: Boolean,
        instanceEntity: () -> Single<SearxInstanceEntity>
    ): Completable =
        instanceEntity()
            .subscribeOn(Schedulers.io())
            .flatMap { searxInstance ->
                searxInstance.favorite = markAsFavorite
                updateInstance(searxInstance)
                    .subscribeOn(Schedulers.io())
                    .toSingle { true }
            }
            .subscribeOn(Schedulers.io())
            .ignoreElement()

}