package com.example.kotlinpokedex.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes

@Preview(
    name = "Light Phone",
    showSystemUi = true,
    device = Devices.PIXEL_7,
    apiLevel = 33,
)
@Preview(
    name = "Dark Phone",
    showSystemUi = true,
    device = Devices.PIXEL_7,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    apiLevel = 33
)
@Preview(
    name = "Light Phone Landscape",
    showSystemUi = true,
    device = "spec:width= 411dp, height= 891dp, orientation= landscape",
    apiLevel = 33
)
@Preview(
    name = "Light Tablet",
    showSystemUi = true,
    device = "spec:width= 800dp, height= 1280dp",
    apiLevel = 33,
)
@Preview(
    name = "Dark Tablet",
    showSystemUi = true,
    device = "spec:width= 800dp, height= 1280dp",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    apiLevel = 33,
)
@Preview(
    name = "Light Tablet",
    showSystemUi = true,
    device = "spec:width= 800dp, height= 1280dp, orientation= landscape",
    apiLevel = 33,
)
annotation class DevicesPreview()