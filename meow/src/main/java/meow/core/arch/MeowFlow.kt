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

package meow.core.arch

import android.app.Dialog
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.etebarian.meowframework.R
import meow.core.api.*
import meow.core.arch.MeowFlow.GetDataApi
import meow.core.ui.FragmentActivityInterface
import meow.util.safeObserve
import meow.util.snackL
import meow.util.toastL
import meow.widget.impl.MeowEmptyStateInterface
import meow.widget.impl.ProgressBarInterface

/**
 * Meow Flow class containing [GetDataApi].
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-10
 */

sealed class MeowFlow(open val fragmentActivity: FragmentActivityInterface<*>) {

    class GetDataApi(fragmentActivity: FragmentActivityInterface<*>, action: () -> Unit) :
        Api(fragmentActivity, action) {
        init {
            onBeforeAction = {
                containerViews.forEach { it.visibility = visibilityWhenLoading }
                onShowLoading(null)
            }
            onAfterAction = {
                onHideLoading()
                if (!lastStateHasBeenError)
                    containerViews.forEach { it.visibility = View.VISIBLE }
            }
        }
    }

    open class Api(
        fragmentActivity: FragmentActivityInterface<*>,
        private var action: () -> Unit
    ) : MeowFlow(fragmentActivity) {

        var isShowingErrorMassageEnabled: Boolean = true

        var errorHandlerType: ErrorHandlerType = ErrorHandlerType.TOAST

        var lastStateHasBeenError: Boolean = false

        var containerViews: Array<View> = arrayOf()

        var visibilityWhenLoading: Int = View.GONE

        var progressBarInterface: ProgressBarInterface? = null

        var emptyStateInterface: MeowEmptyStateInterface? = null

        var emptyErrorModel: UIErrorModel = UIErrorModel(
            icon = R.drawable.ic_sentiment_dissatisfied,
            title = fragmentActivity.resources().getString(R.string.error_empty_title),
            actionText = null
        )

        var dialog: Dialog? = null

        var onBeforeAction: () -> Unit = { onShowLoading(null) }

        var onAfterAction: () -> Unit = { onHideLoading() }

        var onSuccessAction: (it: MeowResponse<*>) -> Unit = {}

        var onCancellationAction: () -> Unit = {
            val title = MeowEvent.Api.Cancellation().title(fragmentActivity.resources())
            val message = MeowEvent.Api.Cancellation().message(fragmentActivity.resources())
            onShowErrorMessage(UIErrorModel(title = title, message = message))
        }

        var onErrorAction: (it: MeowEvent.Api.Error) -> Unit = {
            onShowErrorMessage(it.data.createErrorModel(fragmentActivity.resources()))
        }

        var onShowErrorMessage: (error: UIErrorModel) -> Unit = {
            if (isShowingErrorMassageEnabled) {
                if (errorHandlerType == ErrorHandlerType.TOAST)
                    fragmentActivity.toastL(it.titlePlusMessage)
                if (errorHandlerType == ErrorHandlerType.SNACK_BAR)
                    fragmentActivity.snackL(it.titlePlusMessage)
                if (errorHandlerType == ErrorHandlerType.EMPTY_STATE)
                    emptyStateInterface?.show(it)
            }
        }

        var onShowLoading: (text: String?) -> Unit = {
            progressBarInterface?.show()
            dialog?.show()
            emptyStateInterface?.hide()
        }

        var onHideLoading: () -> Unit = {
            progressBarInterface?.hide()
            dialog?.hide()
        }

        var onClickedActionEmptyState: () -> Unit = {
            action()
        }

        fun <T : Any> observeForIndex(
            owner: LifecycleOwner?,
            eventLiveData: LiveData<*>,
            listLiveData: LiveData<List<T>>
        ) = observe(owner, eventLiveData, listLiveData)

        fun observeForDetail(
            owner: LifecycleOwner?,
            eventLiveData: LiveData<*>
        ) = observe<Nothing>(owner, eventLiveData)

        private fun <T : Any> observe(
            owner: LifecycleOwner?,
            eventLiveData: LiveData<*>,
            listLiveData: LiveData<List<T>>? = null
        ) {
            emptyStateInterface?.setOnActionClickListener {
                onClickedActionEmptyState()
            }

            action()

            eventLiveData.safeObserve(owner) {
                if (it is MeowEvent<*>) {
                    when {
                        it.isApiCancellation() -> {
                            lastStateHasBeenError = true
                            onAfterAction()
                            onCancellationAction()
                        }
                        it.isApiLoading() -> {
                            lastStateHasBeenError = false
                            onBeforeAction()
                        }
                        it.isApiSuccess() -> {
                            onAfterAction()
                            onSuccessAction((it as MeowEvent.Api.Success).data)
                        }
                        it.isApiError() -> {
                            lastStateHasBeenError = true
                            onAfterAction()
                            onErrorAction(it as MeowEvent.Api.Error)
                        }
                    }
                }
            }
            listLiveData.safeObserve(owner) {
                if (it.isEmpty())
                    emptyStateInterface?.show(emptyErrorModel)
                else
                    emptyStateInterface?.hide()
            }
        }
    }

    enum class ErrorHandlerType {
        TOAST,
        SNACK_BAR,
        EMPTY_STATE,
    }

}