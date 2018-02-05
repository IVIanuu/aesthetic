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

import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.util.AttributeSet
import com.ivianuu.aesthetic.util.MaterialColorHelper
import com.ivianuu.aesthetic.tinter.AbstractTinter
import com.ivianuu.aesthetic.util.getField
import io.reactivex.rxkotlin.addTo

internal class DrawerLayoutTinter(view: DrawerLayout, attrs: AttributeSet) :
    AbstractTinter<DrawerLayout>(view, attrs) {

    override fun attach() {
        super.attach()

        aesthetic
            .primaryColor()
            .map { MaterialColorHelper.getPrimaryTextColor(context, it) }
            .subscribe { invalidateColors(it) }
            .addTo(compositeDisposable)
    }

    private fun invalidateColors(color: Int) {
        try {
            val listenersField = DrawerLayout::class.getField("mListeners")
            val listeners = listenersField.get(view) as List<DrawerLayout.DrawerListener>
            listeners
                .filter { it is ActionBarDrawerToggle }
                .map { it as ActionBarDrawerToggle }
                .forEach { it.drawerArrowDrawable.color = color }

            val listenerField = DrawerLayout::class.getField("mListener")
            val listener = listenerField.get(view) as DrawerLayout.DrawerListener?
            if (listener is ActionBarDrawerToggle?) {
                listener?.drawerArrowDrawable?.color = color
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}