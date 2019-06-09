package mort.ar.searxme.data.localdata

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.localdata.localdatasources.SearxInstanceDataSourceImpl
import mort.ar.searxme.data.localdata.mapper.SearchInstanceMapper
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.database.daos.SearxInstanceDao
import org.junit.Before
import org.junit.Test

class SearxInstanceDataSourceImplTest {

    private lateinit var dataSource: SearxInstanceDataSourceImpl

    private lateinit var instanceDao: SearxInstanceDao
    private val mapper = SearchInstanceMapper()

    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        instanceDao = mock()

        dataSource = SearxInstanceDataSourceImpl(instanceDao, mapper)
    }

    @Test
    fun `GIVEN query successful, fetched unsorted list WHEN getAllInstances() THEN sortedList with favorite on head position`() {
        whenever(instanceDao.getAllSearxInstances())
            .thenReturn(Single.just(listOf(notFavoriteSearxInstance, favoriteSearxInstanceEntity)))

        val testSingle = dataSource.getAllInstances().test()

        testSingle
            .assertValue { it.first().favorite }
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN query fails WHEN getAllInstances() THEN sortedList with favorite on head`() {
        val throwable = Throwable("error")
        whenever(instanceDao.getAllSearxInstances()).thenReturn(Single.error(throwable))

        val testSingle = dataSource.getAllInstances().test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }


    @Test
    fun `GIVEN query successful WHEN setPrimaryInstance() called THEN complete successfully`() {
        val instance = "https://searx.0x1b.de"

        val testCompletable = dataSource.setPrimaryInstance(instance).test()

        testCompletable
            .assertComplete()
            .assertNoErrors()
            .dispose()

        verify(instanceDao).changeFavoriteInstanceSync(instance)
    }

    companion object {
        val favoriteSearxInstanceEntity = SearxInstanceEntity(
            name = "https://searx.0x1b.de",
            url = "https://searx.0x1b.de",
            favorite = true
        )

        val notFavoriteSearxInstance = SearxInstanceEntity(
            name = "https://anonyk.com",
            url = "https://anonyk.com",
            favorite = false
        )
    }

}