package mort.ar.searxme

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import mort.ar.searxme.data.repositories.SearchParameterRepositoryImplTemp
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


private const val testStringCategories = "general,it,pictures"


@RunWith(MockitoJUnitRunner::class)
class SearchParameterRepositoryImplTempUnitTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    lateinit var sharedPreferences: SharedPreferences

    lateinit var searchParameterTemp: SearchParameterRepositoryImplTemp

    @Test
    fun testCategoriesProperty() {
        before()

        searchParameterTemp.categories = testStringCategories

        assertEquals(testStringCategories, searchParameterTemp.categories)
    }

    private fun before() {
        sharedPreferences = context.getSharedPreferences("SearchParameterTest", Context.MODE_PRIVATE)
//        Mockito
//            .`when`(context.getSharedPreferences("SearchParameterTest", Context.MODE_PRIVATE))
//            .thenReturn(sharedPreferences)

        searchParameterTemp =
            SearchParameterRepositoryImplTemp(sharedPreferences)
    }

}