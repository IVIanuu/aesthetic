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

import android.graphics.drawable.Drawable
import android.support.v7.view.menu.ActionMenuItemView
import com.ivianuu.aesthetic.theming.util.getEnabledColorStateList
import com.ivianuu.aesthetic.theming.util.getField
import com.ivianuu.aesthetic.theming.util.tintedNullable

fun ActionMenuItemView.tint(activeColor: Int,
                            inactiveColor: Int) {
    val iconField = ActionMenuItemView::class.getField("mIcon")
    val icon = iconField.get(this) as Drawable?
    iconField.set(this, icon.tintedNullable(getEnabledColorStateList(activeColor, inactiveColor)))
    setTextColor(getEnabledColorStateList(activeColor, inactiveColor))
}