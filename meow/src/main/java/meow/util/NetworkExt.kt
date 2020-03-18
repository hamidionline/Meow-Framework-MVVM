/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package meow.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.Fragment

/**
 * Extensions of Network.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-04
 */

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
fun Context?.hasNetwork() = avoidException(tryBlock = {
    if (this == null)
        return@avoidException false
    val cm = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    val netInfo = cm.activeNetworkInfo
    netInfo != null && netInfo.isConnected
}, exceptionBlock = { false })!!

fun Context?.hasNotNetwork() = !hasNetwork()

fun Fragment?.hasNetwork() = this?.context?.hasNetwork() ?: false
fun Fragment?.hasNotNetwork() = !hasNetwork()