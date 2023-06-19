package com.openclassrooms.realestatemanager.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
fun <T> LiveData<T>.observeForTesting(block: (T) -> Unit) {
    runTest {
        val observer = Observer<T> { value ->
            value?.let(block)
        }

        withContext(Dispatchers.Main) {
            observeForever(observer)
        }

        removeObserver(observer)
    }
}

@ExperimentalCoroutinesApi
suspend fun <T> Flow<T>.collectForTesting(block: (T) -> Unit) {
    runTest {
        val collectJob = launch {
            collect { value ->
                block(value)
            }
        }

        advanceUntilIdle()
        collectJob.cancel()
    }
}
