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

package com.ivianuu.aesthetic.tinter

import android.support.design.R.style.Widget_AppCompat_Button_Borderless
import android.support.design.R.style.Widget_AppCompat_Button_Borderless_Colored
import android.util.AttributeSet
import android.widget.Button
import com.ivianuu.aesthetic.theming.tint
import com.ivianuu.aesthetic.theming.util.adjustAlpha
import com.ivianuu.aesthetic.theming.util.getEnabledColorStateList
import com.ivianuu.aesthetic.tinter.base.AbstractTinter
import com.ivianuu.aesthetic.util.getObservableForResId
import com.ivianuu.aesthetic.util.resolveResId
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo


internal class ButtonTinter(
    view: Button,
    attrs: AttributeSet
) : AbstractTinter<Button>(view, attrs) {

    private val backgroundResId = context.resolveResId(attrs, android.R.attr.background)
    private val styleResId = attrs.styleAttribute
    private val borderless =
        styleResId == Widget_AppCompat_Button_Borderless_Colored
                || styleResId == Widget_AppCompat_Button_Borderless

    override fun attach() {
        super.attach()

        Observables
            .combineLatest(
                context.getObservableForResId(backgroundResId, aesthetic.accentColor()),
                aesthetic.isDark()
            )
            .subscribe { view.tint(it.first, it.second, borderless) }
            .addTo(compositeDisposable)

        if (styleResId == Widget_AppCompat_Button_Borderless_Colored) {
            aesthetic
                .accentColor()
                .subscribe {
                    val disabledColor = it.adjustAlpha(0.70f)
                    val textColorSl = getEnabledColorStateList(it, disabledColor)
                    view.setTextColor(textColorSl)
                }
                .addTo(compositeDisposable)
        }
    }
}