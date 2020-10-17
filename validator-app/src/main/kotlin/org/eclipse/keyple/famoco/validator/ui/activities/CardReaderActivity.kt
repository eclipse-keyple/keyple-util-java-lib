/*
 * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

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
package org.eclipse.keyple.famoco.validator.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieDrawable
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_card_reader.*
import org.eclipse.keyple.core.seproxy.exception.KeyplePluginInstantiationException
import org.eclipse.keyple.famoco.validator.R
import org.eclipse.keyple.famoco.validator.data.model.CardReaderResponse
import org.eclipse.keyple.famoco.validator.data.model.Status
import org.eclipse.keyple.famoco.validator.di.scopes.ActivityScoped
import org.eclipse.keyple.famoco.validator.ui.BaseView
import org.eclipse.keyple.famoco.validator.util.KeypleSettings
import org.eclipse.keyple.famoco.validator.viewModels.CardReaderViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ActivityScoped
class CardReaderActivity @Inject constructor() : DaggerAppCompatActivity(), BaseView {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var cardReaderViewModel: CardReaderViewModel

    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_reader)
        cardReaderViewModel = ViewModelProvider(this, viewModelFactory).get(CardReaderViewModel::class.java)
        // Inflate the layout for this fragment
        animation.setAnimation("card_scan.json")
        animation.playAnimation()

        try {
            cardReaderViewModel.initCardReader()
        } catch (e: KeyplePluginInstantiationException) {
            Timber.e(e)
        }
    }

    override fun onResume() {
        super.onResume()
        bindViewModel()
        animation.playAnimation()
        cardReaderViewModel.startNfcDetection(this)

        if(KeypleSettings.batteryPowered) {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread { onBackPressed() }
                }
            }, RETURN_DELAY_MS.toLong())
        }
    }

    override fun onPause() {
        super.onPause()
        unbindViewModel()
        animation.cancelAnimation()
        cardReaderViewModel.stopNfcDetection(this)
        timer.cancel()
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
                presentCardTv.visibility = View.GONE
                mainView.setBackgroundColor(resources.getColor(R.color.turquoise))
                supportActionBar?.show()
                animation.playAnimation()
                animation.repeatCount = LottieDrawable.INFINITE
            } else {
                animation.cancelAnimation()
                val intent = Intent(this, CardSummaryActivity::class.java)
                intent.putExtra(CardSummaryActivity.STATUS_KEY, cardReaderResponse.status.toString())
                intent.putExtra(CardSummaryActivity.TICKETS_KEY, cardReaderResponse.ticketsNumber)
                intent.putExtra(CardSummaryActivity.CONTRACT, cardReaderResponse.contract)
                intent.putExtra(CardSummaryActivity.CARD_TYPE, cardReaderResponse.cardType)
                startActivity(intent)
            }
        } else {
            presentCardTv.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val RETURN_DELAY_MS = 30000
    }
}
