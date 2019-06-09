package mort.ar.searxme.domain.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import mort.ar.searxme.TestSchedulerManager
import mort.ar.searxme.data.SearchParameterRepository
import mort.ar.searxme.data.SearxInstanceRepository
import mort.ar.searxme.presentation.model.Languages
import mort.ar.searxme.data.model.SettingsParameter
import mort.ar.searxme.presentation.model.TimeRanges
import mort.ar.searxme.presentation.settings.Categories.FILES
import mort.ar.searxme.presentation.settings.Categories.IMAGES
import mort.ar.searxme.presentation.settings.Engines.BING
import mort.ar.searxme.presentation.settings.Engines.DUCKDUCKGO
import org.junit.Before
import org.junit.Test

class SaveSettingsParameterUseCaseImplTest {

    private lateinit var useCase: SaveSettingsParameterUseCaseImpl

    private lateinit var searxInstanceRepository: SearxInstanceRepository
    private lateinit var searchParameterRepository: SearchParameterRepository

    @Before
    fun setUp() {
        TestSchedulerManager.throwEverythingOnTrampoline()

        searxInstanceRepository = mock()
        searchParameterRepository = mock()

        useCase = SaveSettingsParameterUseCaseImpl(searxInstanceRepository, searchParameterRepository)
    }

    @Test
    fun `GIVEN saving to db succeeds WHEN saveSettingsParameter() called THEN set`() {
        whenever(searchParameterRepository.setEngines("duckduckgo,bing")).thenReturn(Completable.complete())
        whenever(searchParameterRepository.setCategories("files,images")).thenReturn(Completable.complete())
        whenever(searchParameterRepository.setLanguage(settingsParameter.language)).thenReturn(Completable.complete())
        whenever(searchParameterRepository.setTimeRange(settingsParameter.timeRange)).thenReturn(Completable.complete())
        whenever(searxInstanceRepository.setPrimaryInstance(primaryInstance))
            .thenReturn(Completable.complete())

        val testCompletable = useCase.saveSettingsParameter(settingsParameter).test()

        testCompletable
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `GIVEN saving to db fails WHEN saveSettingsParameter() called THEN set`() {
        val throwable = Throwable("error")
        whenever(searxInstanceRepository.setPrimaryInstance(primaryInstance)).thenReturn(Completable.error(throwable))

        val testCompletable = useCase.saveSettingsParameter(settingsParameter).test()

        testCompletable
            .assertError(throwable)
            .assertNotComplete()
            .dispose()
    }

    companion object {
        private const val primaryInstance = "https://searx.0x1b.de"
        private const val secondaryInstance = "https://anonyk.com"
        val settingsParameter = SettingsParameter(
            searxInstances = listOf(primaryInstance, secondaryInstance),
            engines = listOf(DUCKDUCKGO, BING),
            categories = listOf(FILES, IMAGES),
            language = Languages.GERMAN,
            timeRange = TimeRanges.DAY
        )
    }

}