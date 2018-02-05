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

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.BottomNavigationView
import android.util.AttributeSet
import com.ivianuu.aesthetic.mode.BottomNavBgMode
import com.ivianuu.aesthetic.mode.BottomNavIconTextMode
import com.ivianuu.aesthetic.tinter.AbstractTinter
import com.ivianuu.aesthetic.util.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo

internal class BottomNavigationViewTinter(view: BottomNavigationView, attrs: AttributeSet) :
    AbstractTinter<BottomNavigationView>(view, attrs) {

    override fun attach() {
        super.attach()

        Observables
            .combineLatest(
                aesthetic.bottomNavBgMode(),
                aesthetic.bottomNavIconTextMode()
            )
            .switchMap { (bgMode, iconTextMode) ->
                val iconTextModeObservable = when (iconTextMode) {
                    BottomNavIconTextMode.SELECTED_PRIMARY -> aesthetic.primaryColor()
                    BottomNavIconTextMode.SELECTED_ACCENT -> aesthetic.accentColor()
                    BottomNavIconTextMode.BLACK_WHITE_AUTO -> Observable.just(Color.TRANSPARENT)
                    else -> throw IllegalStateException("Unknown bottom nav icon/text mode $iconTextMode")
                }

                val bgModeObservable = when (bgMode) {
                    BottomNavBgMode.PRIMARY -> aesthetic.primaryColor()
                    BottomNavBgMode.PRIMARY_DARK -> aesthetic.primaryColor().map { it.darken() }
                    BottomNavBgMode.ACCENT -> aesthetic.accentColor()
                    BottomNavBgMode.BLACK_WHITE_AUTO -> {
                        val color = MaterialColorHelper.getBottomNavBgColor(context)
                        Observable.just(color)
                    }
                    else -> throw IllegalStateException("Unknown bottom nav bg mode $bgMode")
                }

                Observables.combineLatest(iconTextModeObservable, bgModeObservable)
            }
            .subscribe { (iconText, bg) ->
                val iconText = if (iconText == Color.TRANSPARENT) {
                    if (bg.isLight()) Color.BLACK else Color.WHITE
                } else {
                    iconText
                }
                invalidateColors(bg)
            }
            .addTo(compositeDisposable)
    }

    private fun invalidateColors(bgColor: Int) {
        val selectedColor = if (bgColor.isLight()) Color.BLACK else Color.WHITE
        view.setBackgroundColor(bgColor)

        val baseColor = MaterialColorHelper.getSecondaryTextColor(context, bgColor.isDark())
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-R.attr.state_checked),
                intArrayOf(R.attr.state_checked)
            ),
            intArrayOf(baseColor.adjustAlpha(0.87f), selectedColor)
        )

        view.itemIconTintList = colorStateList
        view.itemTextColor = colorStateList

        try {
            val menuViewField = BottomNavigationView::class.getField("mMenuView")
            val menuView = menuViewField.get(this)
            val buttonsField = menuView::class.getField("mButtons")
            val buttons = buttonsField.get(menuView) as Array<BottomNavigationItemView>
            buttons.forEach {
                it.background =
                        RippleDrawableHelper.getBorderlessRippleDrawable(context, bgColor.isDark())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}