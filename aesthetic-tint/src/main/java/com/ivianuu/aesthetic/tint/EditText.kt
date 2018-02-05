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

package com.ivianuu.aesthetic.tint

import android.content.res.ColorStateList
import android.os.Build
import android.support.v4.view.TintableBackgroundView
import android.support.v4.view.ViewCompat
import android.widget.EditText
import com.ivianuu.aesthetic.tint.util.MaterialColorHelper

fun EditText.tint(color: Int) {
    val colorStateList = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(
                android.R.attr.state_enabled,
                -android.R.attr.state_pressed,
                -android.R.attr.state_focused
            ),
            intArrayOf()
        ),
        intArrayOf(
            MaterialColorHelper.getPrimaryDisabledTextColor(context),
            MaterialColorHelper.getPrimaryTextColor(context),
            color
        )
    )
    if (this is TintableBackgroundView) {
        ViewCompat.setBackgroundTintList(this, colorStateList)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        backgroundTintList = colorStateList
    }

    setCursorTint(color)
    setTextHandleTint(color)
}