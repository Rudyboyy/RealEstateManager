package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.utils.DummyRealEstateProvider
import com.openclassrooms.realestatemanager.utils.TestCoroutineRule
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import com.openclassrooms.realestatemanager.utils.observeForTesting
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import java.util.concurrent.Executor

class RealEstateViewModelTest {

    companion object {
        private val DUMMY_PROPERTY = DummyRealEstateProvider.samplePropertyList

    }

    private val propertyRepository: RealEstateRepository = mockk()
    private val executor: Executor = mockk()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp() {
        every { propertyRepository.properties } returns flowOf(
            DUMMY_PROPERTY
        )
        every { executor.execute(any()) } answers {
            val runnable = arg<Runnable>(0)
            runnable.run()
        }
    }

    @Test
    fun `filteredProperties should return DUMMY_PROPERTY`() = runBlocking {
        val viewModel = getViewModel()

        val observedProperties = mutableListOf<List<Property>>()
        val observer = Observer<List<Property>> { properties ->
            observedProperties.add(properties)
        }
        viewModel.filteredProperties.observeForever(observer)

        assertThat(observedProperties.size).isEqualTo(1)
        assertThat(observedProperties[0]).isEqualTo(DUMMY_PROPERTY)

        viewModel.filteredProperties.removeObserver(observer)
    }

    @Test
    fun `updateCoordinatesFromAddress should update latitude and longitude`() = runBlocking {
        val address = "123 Test Street"
        val latitude = 12.345
        val longitude = 67.890

        coEvery { propertyRepository.getCoordinatesFromAddress(address) } returns Pair(
            latitude,
            longitude
        )

        val viewModel = getViewModel()
        viewModel.updateCoordinatesFromAddress(address)

        assertThat(viewModel.latitude.value).isEqualTo(latitude)
        assertThat(viewModel.longitude.value).isEqualTo(longitude)
    }

    @Test
    fun `updateSelectedProperty should update selectedProperty LiveData`() {
        val viewModel = getViewModel()
        val mockProperty = DUMMY_PROPERTY[0]

        viewModel.updateSelectedProperty(mockProperty)

        assertThat(viewModel.selectedProperty.value).isEqualTo(mockProperty)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `update should update the property`() = runBlocking {
        val viewModel = getViewModel()
        val mockProperty = DUMMY_PROPERTY[0].copy(agent = "new agent")

        viewModel.update(mockProperty)

        viewModel.filteredProperties.observeForTesting { properties ->
            assertThat(properties[0].agent).isEqualTo("new agent")
        }
    }

    @Test
    fun `addProperty should invoke repository and invoke callback with true`() {
        val viewModel = getViewModel()
        val mockProperty = DUMMY_PROPERTY[0]

        coEvery { propertyRepository.invoke(mockProperty) } just Runs

        var callbackResult: Boolean? = null
        val callback: (Boolean) -> Unit = { result ->
            callbackResult = result
        }

        viewModel.addProperty(mockProperty, callback)

        assertThat(callbackResult).isEqualTo(true)
    }

    private fun getViewModel() = RealEstateViewModel(
        propertyRepository,
        executor
    )
}