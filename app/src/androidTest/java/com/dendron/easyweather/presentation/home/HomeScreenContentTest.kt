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
    fun emptyState_showsGetStartedCopy() {
        var permissionRequests = 0

        setContent(
            state = WeatherScreenState.Empty,
            onRequestPermission = { permissionRequests += 1 },
        )

        composeTestRule.onNodeWithText("Ready to check the weather").assertIsDisplayed()
        composeTestRule.onNodeWithText(
            "Use your location to fetch the latest local forecast.",
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText("Use my location").performClick()

        assertEquals(1, permissionRequests)
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
                    descriptionText = "Clear sky",
                    windText = "10 km/h N",
                    temperatureText = "12° / 18° / 22°",
                    weatherIcon = com.dendron.easyweather.R.drawable.day_sunny,
                ),
                lastUpdatedAtMillis = 1_718_000_000_000,
            ),
        )

        composeTestRule.onNodeWithText("Clear sky").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 km/h N").assertIsDisplayed()
        composeTestRule.onNodeWithText("12° / 18° / 22°").assertIsDisplayed()
        composeTestRule.onNodeWithText("Last updated:", substring = true).assertIsDisplayed()
    }

    private fun setContent(
        state: WeatherScreenState,
        onRefresh: () -> Unit = {},
        onRequestPermission: () -> Unit = {},
        onOpenAppSettings: () -> Unit = {},
        onOpenLocationSettings: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            EasyWeatherTheme {
                HomeScreenContent(
                    state = state,
                    onRefresh = onRefresh,
                    onRequestPermission = onRequestPermission,
                    onOpenAppSettings = onOpenAppSettings,
                    onOpenLocationSettings = onOpenLocationSettings,
                )
            }
        }
    }
}
