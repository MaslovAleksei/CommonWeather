package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.weather.WeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class RefreshDataUseCaseTest {

    @Test
    fun `should return true if refresh was successful`() = runTest {

        val weatherRepository = Mockito.mock<com.margarin.commonweather.weather.WeatherRepository>()
        val useCase =
            com.margarin.commonweather.weather.usecases.RefreshDataUseCase(weatherRepository = weatherRepository)
        val expected = true
        val testParams = "test name"
        Mockito.`when`(weatherRepository.refreshData(query = testParams)).thenReturn(expected)
        val actual = useCase.invoke(location = testParams)

        Assert.assertEquals(expected, actual)
        Mockito.verify(weatherRepository, Mockito.times(1))
            .refreshData(query = testParams)
    }
}