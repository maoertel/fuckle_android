package mort.ar.searxme.data.localdata

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.localdata.localdatasources.SearxInstanceDataSourceImpl
import mort.ar.searxme.data.localdata.mapper.SearchInstanceMapper
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.data.model.SearchInstance
import mort.ar.searxme.database.daos.SearxInstanceDao
import org.junit.Assert.assertEquals
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

    @Test
    fun `GIVEN query successful WHEN insert() called THEN complete successfully`() {
        val captor = argumentCaptor<SearxInstanceEntity>()
        whenever(instanceDao.insert(captor.capture())).thenReturn(Completable.complete())

        val testCompletable = dataSource.insert(searchInstance).test()

        assertEquals(favoriteSearxInstanceEntity, captor.firstValue)
        testCompletable
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN query fails WHEN insert() called THEN throws`() {
        val throwable = Throwable("error")
        val captor = argumentCaptor<SearxInstanceEntity>()
        whenever(instanceDao.insert(captor.capture())).thenReturn(Completable.error(throwable))

        val testCompletable = dataSource.insert(searchInstance).test()

        assertEquals(favoriteSearxInstanceEntity, captor.firstValue)
        testCompletable
            .assertError(throwable)
            .assertNotComplete()
            .dispose()
    }

    companion object {
        val searchInstance = SearchInstance(
            name = "https://searx.0x1b.de",
            url = "https://searx.0x1b.de",
            favorite = true
        )

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