/*
 * Copyright 2024 Erfan Sn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalTextApi::class, ExperimentalTextApi::class)

package ir.erfansn.composablescreens.travel.qclay_trip.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.erfansn.composablescreens.travel.qclay_trip.R

internal val PoppinsFontFamily =
  FontFamily(
    Font(
      resId = R.font.poppins_regular,
      weight = FontWeight.Normal,
    ),
    Font(
      resId = R.font.poppins_medium,
      weight = FontWeight.Medium,
    ),
    Font(
      resId = R.font.poppins_bold,
      weight = FontWeight.Bold,
    ),
    Font(
      resId = R.font.poppins_semi_bold,
      weight = FontWeight.SemiBold,
    ),
  )

internal val AbrilFatfaceFontFamily =
  FontFamily(
    Font(
      resId = R.font.abril_fatface_regular,
      weight = FontWeight.Normal,
    ),
  )

internal val Typography =
  Typography(
    h5 =
      TextStyle(
        fontFamily = AbrilFatfaceFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      ),
    subtitle1 =
      TextStyle(
        fontFamily = AbrilFatfaceFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      ),
    subtitle2 =
      TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      ),
    body1 =
      TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      ),
    body2 =
      TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      ),
    button =
      TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      ),
    caption =
      TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.0.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      ),
    overline =
      TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      ),
  )
