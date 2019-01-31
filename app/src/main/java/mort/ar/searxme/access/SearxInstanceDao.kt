package mort.ar.searxme.access

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.model.SearxInstance


@Dao
abstract class SearxInstanceDao {

    @Query("SELECT * FROM searx_instances")
    abstract fun observeAllSearxInstances(): Observable<List<SearxInstance>>

    @Query("SELECT * FROM searx_instances")
    abstract fun getAllSearxInstancesSingle(): Single<List<SearxInstance>>

    @Query("SELECT * FROM searx_instances WHERE name IS :name")
    abstract fun getSearxInstance(name: String): Single<SearxInstance>

    @Query("SELECT * FROM searx_instances WHERE favorite")
    abstract fun observeFavoriteInstance(): Observable<SearxInstance>

    @Query("SELECT * FROM searx_instances WHERE favorite")
    abstract fun getFavoriteInstance(): Single<SearxInstance>

    @Query("SELECT * FROM searx_instances WHERE name IN (:names)")
    abstract fun getSearxInstances(names: List<String>): Single<List<SearxInstance>>

    @Insert(onConflict = REPLACE)
    abstract fun insert(searxInstance: SearxInstance): Completable

    @Update
    abstract fun updateInstance(instance: SearxInstance): Completable

    @Query("DELETE FROM searx_instances")
    abstract fun deleteAll()

    @Transaction
    open fun changeFavoriteInstance(newFavoriteInstance: String): Completable =
        Completable
            .concat(
                listOf(
                    setInstanceToFavorite(false) { getFavoriteInstance() },
                    setInstanceToFavorite(true) { getSearxInstance(newFavoriteInstance) }
                )
            )

    private fun setInstanceToFavorite(
        markAsFavorite: Boolean,
        instance: () -> Single<SearxInstance>
    ): Completable =
        instance()
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
