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

package sample.ui.user.index

import android.view.View
import androidx.lifecycle.MutableLiveData
import meow.controller
import meow.core.api.MeowEvent
import meow.core.arch.MeowViewModel
import meow.util.logD
import sample.App
import sample.data.User

/**
 * [User]/Index View Model class.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-08
 */

class UserIndexViewModel(
    override val app: App,
    private val repository: User.Repository
) : MeowViewModel(app) {

    var eventLiveData = MutableLiveData<MeowEvent<*>>()
    val listLiveData = MutableLiveData<List<User>>()

    fun callApi() {
        eventLiveData.safeCallApi(
            isNetworkRequired = true,
            apiAction = { repository.getUsersApi() }
        ) { _, it ->
            logD(m = "zzzz : ${it?.size}")
            listLiveData.postValue(it)
        }
    }

    fun onClickedChangeMode(view: View) {
        controller.swapTheme()
    }

    fun getTextByDayNightMode() = if (controller.isNightMode) "Day Mode" else "Night Mode"

    fun getPrefixText() = "AAA"
}