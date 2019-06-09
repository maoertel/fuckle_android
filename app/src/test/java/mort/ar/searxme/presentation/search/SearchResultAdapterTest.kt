package mort.ar.searxme.presentation.search

import com.nhaarman.mockitokotlin2.mock
import mort.ar.searxme.data.model.SearchResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.jvm.isAccessible

class SearchResultAdapterTest {

    private lateinit var adapter: SearchResultAdapter

    private lateinit var searchResultPresenter: SearchPresenter

    @Before
    fun setUp() {
        searchResultPresenter = mock()

        adapter = SearchResultAdapter(searchResultPresenter)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> SearchResultAdapter.setValueOfPrivateMutableProperty(propertyName: String, value: T) =
        this::class.members.findLast { it.name == propertyName }?.let { field ->
            field.isAccessible = true
            (field as? KMutableProperty1<SearchResultAdapter, T>)?.set(this, value)
        }

    @Test
    fun `GIVEN search results not empty WHEN getItemCount() THEN actual size returned`() {
        val itemCount = adapter.itemCount

        assertEquals(0, itemCount)
    }

    @Test(expected = NullPointerException::class)
    @Ignore("solve the problems with mocking notifyDataSetChanged, or pull out the logic to a presenter too and test there")
    fun `GIVEN search results empty WHEN getItemCount() THEN size zero returned`() {
        val newSearchResults = listOf<SearchResult>(mock(), mock())
        adapter.setValueOfPrivateMutableProperty("searchResults", newSearchResults)

        val itemCount = adapter.itemCount

        assertEquals(newSearchResults.size, itemCount)
    }

}