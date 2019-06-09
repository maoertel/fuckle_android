package mort.ar.searxme.domain.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.data.localdata.model.SearxInstanceEntity
import mort.ar.searxme.data.model.SearchInstance
import mort.ar.searxme.data.mapper.SettingsParameterMapper
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.presentation.model.TimeRanges
import mort.ar.searxme.presentation.model.Categories.FILES
import mort.ar.searxme.presentation.model.Categories.IMAGES
import mort.ar.searxme.presentation.model.Engines.BING
import mort.ar.searxme.presentation.model.Engines.DUCKDUCKGO
import org.junit.Before
import org.junit.Test

class GetSettingsParameterUseCaseImplTest {

    private lateinit var useCase: GetSettingsParameterUseCaseImpl

    private lateinit var searxInstanceRepository: SearxInstanceRepository
    private lateinit var searchParameterRepository: SearchParameterRepository
    private lateinit var mapper: SettingsParameterMapper

    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        searxInstanceRepository = mock()
        searchParameterRepository = mock()
        mapper = SettingsParameterMapper()

        useCase = GetSettingsParameterUseCaseImpl(searxInstanceRepository, searchParameterRepository, mapper)
    }

    @Test
    fun `GIVEN all repo queries succeed WHEN getSettingsParameter() THEN SettingsParameter returned`() {
        whenever(searxInstanceRepository.getAllInstances()).thenReturn(Single.just(searchInstances))
        whenever(searchParameterRepository.getEngines()).thenReturn(Single.just(engines))
        whenever(searchParameterRepository.getCategories()).thenReturn(Single.just(categories))
        whenever(searchParameterRepository.getLanguage()).thenReturn(Single.just(language))
        whenever(searchParameterRepository.getTimeRange()).thenReturn(Single.just(timeRange))

        val testSingle = useCase.getSettingsParameter().test()

        testSingle
            .assertValue {
                it.searxInstances == listOf(url) &&
                        it.engines == listOf(DUCKDUCKGO, BING) &&
                        it.categories == listOf(FILES, IMAGES) &&
                        it.language == language &&
                        it.timeRange == timeRange
            }
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN one of the queries fails WHEN getSettingsParameter() THEN SettingsParameter throws`() {
        val throwable = Throwable("error")
        whenever(searxInstanceRepository.getAllInstances()).thenReturn(Single.error(throwable))
        whenever(searchParameterRepository.getEngines()).thenReturn(Single.just(engines))
        whenever(searchParameterRepository.getCategories()).thenReturn(Single.just(categories))
        whenever(searchParameterRepository.getLanguage()).thenReturn(Single.just(language))
        whenever(searchParameterRepository.getTimeRange()).thenReturn(Single.just(timeRange))

        val testSingle = useCase.getSettingsParameter().test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    @Test
    fun `GIVEN all of the queries fail WHEN getSettingsParameter() THEN SettingsParameter throws`() {
        val throwable = Throwable("error")
        whenever(searxInstanceRepository.getAllInstances()).thenReturn(Single.error(throwable))
        whenever(searchParameterRepository.getEngines()).thenReturn(Single.error(throwable))
        whenever(searchParameterRepository.getCategories()).thenReturn(Single.error(throwable))
        whenever(searchParameterRepository.getLanguage()).thenReturn(Single.error(throwable))
        whenever(searchParameterRepository.getTimeRange()).thenReturn(Single.error(throwable))

        val testSingle = useCase.getSettingsParameter().test()

        testSingle
            .assertError(throwable)
            .assertNoValues()
            .dispose()
    }

    companion object {
        private const val url = "https://searx.0x1b.de"
        private val favoriteSearxInstanceEntity = SearxInstanceEntity(
            name = "searx",
            url = url,
            favorite = true
        )

        private val favoriteSearchInstance = SearchInstance(
            name = "searx",
            url = url,
            favorite = true
        )

        private val searchInstances = listOf(favoriteSearchInstance)
        private const val engines = "duckduckgo,bing"
        private const val categories = "files,images"
        private val language = Languages.GERMAN
        private val timeRange = TimeRanges.MONTH

    }

}