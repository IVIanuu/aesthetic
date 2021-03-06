/*
 * Copyright 2018 Manuel Wrage
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

package com.ivianuu.aesthetic.theming

import android.graphics.Color
import android.support.design.widget.FloatingActionButton
import com.ivianuu.aesthetic.theming.util.isLight
import com.ivianuu.aesthetic.theming.util.isWindowBackgroundDark
import com.ivianuu.aesthetic.theming.util.tintedNullable

fun FloatingActionButton.tint(color: Int, isDark: Boolean = context.isWindowBackgroundDark()) {
    tintBackground(color, isDark)
    val iconColor = if (color.isLight()) Color.BLACK else Color.WHITE
    setImageDrawable(drawable?.tintedNullable(iconColor))
}