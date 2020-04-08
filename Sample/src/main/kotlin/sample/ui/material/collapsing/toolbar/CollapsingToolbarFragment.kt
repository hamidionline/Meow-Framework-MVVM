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

package sample.ui.material.collapsing.toolbar

import android.os.Bundle
import meow.util.instanceViewModel
import sample.R
import sample.databinding.FragmentCollapsingToolbarBinding
import sample.ui.base.BaseFragment

/**
 * Material Collapsing Toolbar Fragment.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-30
 */

class CollapsingToolbarFragment : BaseFragment<FragmentCollapsingToolbarBinding>() {

    private val viewModel: CollapsingToolbarViewModel by instanceViewModel()
    override fun layoutId() = R.layout.fragment_collapsing_toolbar

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity().setSupportActionBar(binding.toolbarCollapsing)
    }

    override fun initViewModel() {
        binding.viewModel = viewModel
    }

}