/********************************************************************************
 * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package org.cna.keyple.famoco.validator.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieDrawable
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import org.cna.keyple.famoco.validator.R
import org.cna.keyple.famoco.validator.data.model.CardReaderResponse
import org.cna.keyple.famoco.validator.data.model.Status
import org.cna.keyple.famoco.validator.databinding.FragmentCardReaderBinding
import org.cna.keyple.famoco.validator.di.scopes.ActivityScoped
import org.cna.keyple.famoco.validator.ui.BaseView
import org.cna.keyple.famoco.validator.util.ActivityUtils
import org.cna.keyple.famoco.validator.viewModels.CardReaderViewModel
import org.eclipse.keyple.core.seproxy.exception.KeyplePluginInstantiationException
import timber.log.Timber

@ActivityScoped
class CardReaderFragment @Inject constructor() : DaggerFragment(), BaseView {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var cardReaderViewModel: CardReaderViewModel
    lateinit var binding: FragmentCardReaderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cardReaderViewModel = ViewModelProvider(this, viewModelFactory).get(CardReaderViewModel::class.java)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_reader, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = cardReaderViewModel
        binding.animation.setAnimation("card_scan.json")
        binding.animation.playAnimation()

        try {
            cardReaderViewModel.initCardReader()
        } catch (e: KeyplePluginInstantiationException) {
            Timber.e(e)
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        bindViewModel()
        binding.animation.playAnimation()
        cardReaderViewModel.startNfcDetection(activity)
    }

    override fun onPause() {
        super.onPause()
        unbindViewModel()
        binding.animation.cancelAnimation()
        cardReaderViewModel.stopNfcDetection(activity)
    }

    override fun bindViewModel() {
        cardReaderViewModel.response.observe(this, Observer { cardReaderResponse: CardReaderResponse? -> changeDisplay(cardReaderResponse) })
    }

    override fun unbindViewModel() {
        cardReaderViewModel.response.removeObservers(this)
    }

    private fun changeDisplay(cardReaderResponse: CardReaderResponse?) {
        if (cardReaderResponse != null) {
            if (cardReaderResponse.status === Status.LOADING) {
                val mTitle = activity?.findViewById<TextView>(R.id.toolbar_title)
                mTitle?.setText(R.string.card_reading_title)

                binding.presentCardTv.visibility = View.GONE
                binding.mainView.setBackgroundColor(resources.getColor(R.color.turquoise))
                (activity as AppCompatActivity?)?.supportActionBar?.show()
                binding.animation.playAnimation()
                binding.animation.repeatCount = LottieDrawable.INFINITE
            } else {
                binding.animation.cancelAnimation()
                val bundle = Bundle()
                bundle.putString(CardSummaryFragment.STATUS_KEY, cardReaderResponse.status.toString())
                bundle.putInt(CardSummaryFragment.TICKETS_KEY, cardReaderResponse.ticketsNumber)
                bundle.putString(CardSummaryFragment.CONTRACT, cardReaderResponse.contract)
                bundle.putString(CardSummaryFragment.CARD_TYPE, cardReaderResponse.cardType)
                val fragment = CardSummaryFragment()
                fragment.arguments = bundle
                ActivityUtils.addFragmentToActivity(parentFragmentManager, fragment, R.id.contentFrame)
            }
        } else {
            binding.presentCardTv.visibility = View.VISIBLE
        }
    }
}
