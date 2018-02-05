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

package com.ivianuu.aesthetic.widget

import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.View
import com.ivianuu.aesthetic.mode.TabLayoutIndicatorMode
import com.ivianuu.aesthetic.tinter.AbstractTinter

import com.ivianuu.aesthetic.util.*
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo

internal class TabLayoutTinter(view: TabLayout, attrs: AttributeSet) :
    AbstractTinter<TabLayout>(view, attrs) {

    override fun attach() {
        super.attach()

        Observables
            .combineLatest(
                aesthetic.tabLayoutBgMode(),
                aesthetic.tabLayoutIndicatorMode()
            )
            .switchMap { (bgMode, indicatorMode) ->
                val bgModeObservable = when (bgMode) {
                    TabLayoutIndicatorMode.PRIMARY -> aesthetic.primaryColor()
                    TabLayoutIndicatorMode.ACCENT -> aesthetic.accentColor()
                    else -> throw IllegalStateException("Unknown bg mode $bgMode")
                }
                val indicatorModeObservable = when (indicatorMode) {
                    TabLayoutIndicatorMode.PRIMARY -> aesthetic.primaryColor()
                    TabLayoutIndicatorMode.ACCENT -> aesthetic.accentColor()
                    else -> throw IllegalStateException("Unknown indicator mode $indicatorMode")
                }

                Observables
                    .combineLatest(
                        bgModeObservable,
                        indicatorModeObservable
                    )
            }
            .subscribe { invalidateColors(it.first, it.second) }
            .addTo(compositeDisposable)
    }

    private fun invalidateColors(bgColor: Int,
                                 indicatorColor: Int) {
        val activeColor = MaterialColorHelper.getPrimaryTextColor(context, bgColor)
        val inactiveColor = MaterialColorHelper.getSecondaryTextColor(context, bgColor)
        with(view) {
            setBackgroundColor(bgColor)

            val colorStateList = getDisabledColorStateList(activeColor, inactiveColor)

            (0 until tabCount).mapNotNull { getTabAt(it) }
                .forEach {
                    it.icon?.tint(colorStateList)
                    try {
                        val viewField = it::class.getField("mView")
                        val tabView = viewField.get(it) as View
                        tabView.background =
                                RippleDrawableHelper.getRippleDrawable(context, bgColor.isDark())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            setTabTextColors(inactiveColor, activeColor)
            setSelectedTabIndicatorColor(indicatorColor)
        }
    }
}