package mort.ar.searxme

import android.content.Context
import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import mort.ar.searxme.data.repositories.SearchParameterRepositoryImpl
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


private const val testStringCategories = "general,it,pictures"


@RunWith(MockitoJUnitRunner::class)
class SearchParameterRepositoryImplUnitTest {

    private val context: Context = InstrumentationRegistry.getInstrumentation().context

    lateinit var sharedPreferences: SharedPreferences

    lateinit var searchParameter: SearchParameterRepositoryImpl

    @Test
    fun testCategoriesProperty() {
        before()

        searchParameter.categories = testStringCategories

        assertEquals(testStringCategories, searchParameter.categories)
    }

    private fun before() {
        sharedPreferences = context.getSharedPreferences("SearchParameterTest", Context.MODE_PRIVATE)
//        Mockito
//            .`when`(context.getSharedPreferences("SearchParameterTest", Context.MODE_PRIVATE))
//            .thenReturn(sharedPreferences)

        searchParameter =
            SearchParameterRepositoryImpl(sharedPreferences)
    }

}