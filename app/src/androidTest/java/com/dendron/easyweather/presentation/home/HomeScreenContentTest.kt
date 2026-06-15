package com.dendron.easyweather.presentation.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dendron.easyweather.presentation.ui.theme.EasyWeatherTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyState_showsFirstRunGuidance() {
        var permissionRequests = 0

        setContent(
            state = WeatherScreenState.Empty,
            onRequestPermission = { permissionRequests += 1 },
        )

        composeTestRule.onNodeWithText("Get a local forecast in seconds").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "EasyWeather uses your location once to show the weather where you are right now.",
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Current conditions for your area").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quick refresh whenever the weather changes").assertIsDisplayed()
        composeTestRule.onNodeWithText("No account setup required").assertIsDisplayed()
        composeTestRule.onNodeWithText("Use my location").performClick()
        composeTestRule.onNodeWithText("Choose a city instead").assertIsDisplayed()

        assertEquals(1, permissionRequests)
    }

    @Test
    fun manualLocationState_showsSearchUi() {
        var searched = 0
        var back = 0

        setContent(
            state = WeatherScreenState.ManualLocation(
                query = "New York",
                results = emptyList(),
            ),
            onManualLocationSearch = { searched += 1 },
            onManualLocationBack = { back += 1 },
        )

        composeTestRule.onNodeWithText("Search for a city").assertIsDisplayed()
        composeTestRule.onNodeWithText("Skip location access and check the weather for any city.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search").performClick()
        composeTestRule.onNodeWithText("Back").performClick()

        assertEquals(1, searched)
        assertEquals(1, back)
    }

    @Test
    fun loadingState_showsLoadingCopy() {
        setContent(WeatherScreenState.Loading)

        composeTestRule.onNodeWithText("Loading weather").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Fetching the latest forecast for your current location.",
        ).assertIsDisplayed()
    }

    @Test
    fun permissionRequiredState_showsActions_andHandlesClicks() {
        var permissionRequests = 0
        var openSettingsRequests = 0

        setContent(
            state = WeatherScreenState.PermissionRequired,
            onRequestPermission = { permissionRequests += 1 },
            onOpenAppSettings = { openSettingsRequests += 1 },
        )

        composeTestRule.onNodeWithText("Location access needed").assertIsDisplayed()
        composeTestRule.onNodeWithText("Allow location").performClick()
        composeTestRule.onNodeWithText("Open app settings").performClick()

        assertEquals(1, permissionRequests)
        assertEquals(1, openSettingsRequests)
    }

    @Test
    fun locationDisabledState_showsActions_andHandlesClicks() {
        var locationSettingsRequests = 0
        var refreshRequests = 0

        setContent(
            state = WeatherScreenState.LocationDisabled,
            onRefresh = { refreshRequests += 1 },
            onOpenLocationSettings = { locationSettingsRequests += 1 },
        )

        composeTestRule.onNodeWithText("Turn on location services").assertIsDisplayed()
        composeTestRule.onNodeWithText("Open location settings").performClick()
        composeTestRule.onNodeWithText("Retry").performClick()

        assertEquals(1, locationSettingsRequests)
        assertEquals(1, refreshRequests)
    }

    @Test
    fun errorState_showsRetry_andHandlesClick() {
        var refreshRequests = 0

        setContent(
            state = WeatherScreenState.Error(ErrorReason.Network),
            onRefresh = { refreshRequests += 1 },
        )

        composeTestRule.onNodeWithText("Couldn’t load the weather").assertIsDisplayed()
        composeTestRule.onNodeWithText("Please check your connection and try again.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()

        assertEquals(1, refreshRequests)
    }

    @Test
    fun contentState_showsWeatherDetails() {
        setContent(
            state = WeatherScreenState.Content(
                model = WeatherUiModel(
                    locationName = "New York",
                    descriptionText = "Clear sky",
                    currentTemperatureText = "18°",
                    highLowText = "H:22°  L:12°",
                    windText = "10 km/h N",
                    temperatureText = "12° / 18° / 22°",
                    humidityText = "65%",
                    precipitationText = "40%",
                    sunriseText = "6:30 am",
                    sunsetText = "8:15 pm",
                    hourlyForecast = listOf(
                        HourlyForecastUiModel(timeText = "1pm", temperatureText = "18°"),
                        HourlyForecastUiModel(timeText = "2pm", temperatureText = "19°"),
                    ),
                    dailyForecast = listOf(
                        DailyForecastUiModel(dayLabel = "Sun", rangeText = "12° / 22°"),
                        DailyForecastUiModel(dayLabel = "Mon", rangeText = "14° / 24°"),
                    ),
                    weatherIcon = com.dendron.easyweather.R.drawable.day_sunny,
                ),
                lastUpdatedAtMillis = 1_718_000_000_000,
            ),
        )

        composeTestRule.onNodeWithText("New York").assertIsDisplayed()
        composeTestRule.onNodeWithText("Today").assertIsDisplayed()
        composeTestRule.onNodeWithText("18°").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clear sky").assertIsDisplayed()
        composeTestRule.onNodeWithText("H:22°  L:12°").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 km/h N").assertIsDisplayed()
        composeTestRule.onNodeWithText("12° / 18° / 22°").assertIsDisplayed()
        composeTestRule.onNodeWithText("65%").assertIsDisplayed()
        composeTestRule.onNodeWithText("40%").assertIsDisplayed()
        composeTestRule.onNodeWithText("6:30 am").assertIsDisplayed()
        composeTestRule.onNodeWithText("8:15 pm").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hourly forecast").assertIsDisplayed()
        composeTestRule.onNodeWithText("1pm").assertIsDisplayed()
        composeTestRule.onNodeWithText("19°").assertIsDisplayed()
        composeTestRule.onNodeWithText("Next days").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sun").assertIsDisplayed()
        composeTestRule.onNodeWithText("14° / 24°").assertIsDisplayed()
        composeTestRule.onNodeWithText("Last updated:", substring = true).assertIsDisplayed()
    }

    private fun setContent(
        state: WeatherScreenState,
        onRefresh: () -> Unit = {},
        onRequestPermission: () -> Unit = {},
        onShowManualLocation: () -> Unit = {},
        onManualLocationQueryChange: (String) -> Unit = {},
        onManualLocationSearch: () -> Unit = {},
        onManualLocationSelect: (com.dendron.easyweather.domain.location.SearchedLocation) -> Unit = {},
        onManualLocationBack: () -> Unit = {},
        onOpenAppSettings: () -> Unit = {},
        onOpenLocationSettings: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            EasyWeatherTheme {
                HomeScreenContent(
                    state = state,
                    onRefresh = onRefresh,
                    onRequestPermission = onRequestPermission,
                    onShowManualLocation = onShowManualLocation,
                    onManualLocationQueryChange = onManualLocationQueryChange,
                    onManualLocationSearch = onManualLocationSearch,
                    onManualLocationSelect = onManualLocationSelect,
                    onManualLocationBack = onManualLocationBack,
                    onOpenAppSettings = onOpenAppSettings,
                    onOpenLocationSettings = onOpenLocationSettings,
                )
            }
        }
    }
}
