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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import meow.core.ui.MeowAdapter
import meow.core.ui.MeowViewHolder
import meow.util.getColorCompat
import sample.BR
import sample.R
import sample.data.user.User
import sample.databinding.ItemUserBinding

/**
 * [User] Adapter.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-08
 */

typealias Model = User

typealias ViewHolder = MeowViewHolder<Model, ViewModel>
typealias ViewModel = UserIndexViewModel
typealias DiffCallback = User.DiffCallback

class UserAdapter(
    override var viewModel: ViewModel
) : MeowAdapter<Model, ViewHolder, ViewModel>(
    viewModel,
    DiffCallback()
) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (Types.values()[viewType]) {
            Types.A -> {
                val binding =
                    ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MeowViewHolder(binding.root) { _, model, viewModel ->
                    binding.let {
                        it.tvX.setTextColor(parent.context.getColorCompat(R.color.material_on_surface_emphasis_high_type))// bug fix for realtime day/night mode
                        it.viewModel = viewModel
                        it.setVariable(BR.model, model)
                        it.executePendingBindings()
                    }
                }
            }
            else -> MeowViewHolder(View(parent.context))
        }
    }

    enum class Types {
        A, B
    }

}