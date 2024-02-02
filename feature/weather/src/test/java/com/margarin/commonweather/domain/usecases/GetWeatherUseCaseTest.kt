package com.margarin.commonweather.domain.usecases

import com.margarin.commonweather.weather.models.CurrentWeatherModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock


class GetWeatherUseCaseTest {

    val currentWeatherModel = CurrentWeatherModel(
        name = "test name",
        condition = "test condition",
        icon_url = 505,
        last_updated = "test last_updated",
        wind_kph = "test wind_kph",
        wind_dir = "test wind_dir",
        wind_dir_img = 506,
        temp_c = "test temp_c",
        pressure_mb = "test pressure_mb",
        humidity = "test humidity",
        uv = 508,
        feels_like = "feels_like",
        latitude = 50.9F,
        longitude = 51.0F
    )

    val byDaysWeatherModel = listOf(
        com.margarin.commonweather.weather.models.ByDaysWeatherModel(
            name = "test name",
            id = 10,
            date = "test date",
            maxtemp_c = "test maxtemp_c",
            mintemp_c = "test mintemp_c",
            condition = "test condition",
            icon_url = 11,
            maxwind_kph = "test maxwind_kph",
            chance_of_rain = "test chance_of_rain",
            day_of_week = "test day_of_week"
        )
    )

    val byHoursWeatherModel = listOf(
        com.margarin.commonweather.weather.models.ByHoursWeatherModel(
            name = "test name",
            id = 12,
            time = "test time",
            temp_c = "test temp_c",
            icon_url = 13,
            wind_kph = "test wind_kph"
        )
    )

    val weatherModel = com.margarin.commonweather.weather.models.WeatherModel(
        currentWeatherModel,
        byDaysWeatherModel,
        byHoursWeatherModel
    )

    val weatherRepository = mock<com.margarin.commonweather.weather.WeatherRepository>()

    @Test
    fun `should return the same data as in repository`() = runTest {

        Mockito.`when`(weatherRepository.getWeather("test name")).thenReturn(weatherModel)

        val useCase =
            com.margarin.commonweather.weather.usecases.GetWeatherUseCase(weatherRepository = weatherRepository)
        val actual = useCase.invoke(name = "test name")
        val expected = weatherModel

        assertEquals(expected, actual)

    }
}