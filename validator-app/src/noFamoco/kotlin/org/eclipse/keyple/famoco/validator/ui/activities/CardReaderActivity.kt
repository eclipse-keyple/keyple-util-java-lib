///*
// * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
// *
// * See the NOTICE file(s) distributed with this work for additional information
// * regarding copyright ownership.
// *
// * This program and the accompanying materials are made available under the terms of the
// * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
// *
// * SPDX-License-Identifier: EPL-2.0
// */
//
///********************************************************************************
// * Copyright (c) 2020 Calypso Networks Association https://www.calypsonet-asso.org/
// *
// * See the NOTICE file(s) distributed with this work for additional information regarding copyright
// * ownership.
// *
// * This program and the accompanying materials are made available under the terms of the Eclipse
// * Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
// *
// * SPDX-License-Identifier: EPL-2.0
// ********************************************************************************/
//package org.eclipse.keyple.famoco.validator.ui.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import com.airbnb.lottie.LottieDrawable
//import dagger.android.support.DaggerAppCompatActivity
//import kotlinx.android.synthetic.main.activity_card_reader.*
//import org.eclipse.keyple.famoco.validator.R
//import org.eclipse.keyple.famoco.validator.data.model.CardReaderResponse
//import org.eclipse.keyple.famoco.validator.data.model.Status
//import org.eclipse.keyple.famoco.validator.di.scopes.ActivityScoped
//import org.eclipse.keyple.famoco.validator.util.KeypleSettings
//import java.util.*
//
//@ActivityScoped
//class CardReaderActivity: DaggerAppCompatActivity() {
//
//    private val timer = Timer()
//
//    override fun onCreate(savedInstanceState: Bundle?){
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_card_reader)
//        // Inflate the layout for this fragment
//        animation.setAnimation("card_scan.json")
//        animation.playAnimation()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        animation.playAnimation()
//        if(KeypleSettings.batteryPowered) {
//            timer.schedule(object : TimerTask() {
//                override fun run() {
//                    runOnUiThread { onBackPressed() }
//                }
//            }, RETURN_DELAY_MS.toLong())
//        }
//
//        // STUB Card event
//        val timer = Timer()
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//                /** Change this value to see other status screens **/
//                val status: Status = Status.INVALID_CARD
//                when (status) {
//                    Status.TICKETS_FOUND -> changeDisplay(
//                        CardReaderResponse(
//                        Status.TICKETS_FOUND,
//                        7,
//                        "Season Pass",
//                        ""
//                    ))
//                    Status.LOADING, Status.ERROR, Status.SUCCESS, Status.INVALID_CARD, Status.EMPTY_CARD -> changeDisplay(
//                        CardReaderResponse(
//                        status,
//                        0,
//                        "",
//                        ""
//                    ))
//                }
//            }
//        }, EVENT_DELAY_MS.toLong())
//    }
//
//    override fun onPause() {
//        super.onPause()
//        animation.cancelAnimation()
//        timer.cancel()
//    }
//
//    private fun changeDisplay(cardReaderResponse: CardReaderResponse?) {
//        if (cardReaderResponse != null) {
//            if (cardReaderResponse.status === Status.LOADING) {
//                presentCardTv.visibility = View.GONE
//                mainView.setBackgroundColor(resources.getColor(R.color.turquoise))
//                supportActionBar?.show()
//                animation.playAnimation()
//                animation.repeatCount = LottieDrawable.INFINITE
//            } else {
//                runOnUiThread{
//                    animation.cancelAnimation()
//                }
//                val intent = Intent(this, CardSummaryActivity::class.java)
//                intent.putExtra(CardSummaryActivity.STATUS_KEY, cardReaderResponse.status.toString())
//                intent.putExtra(CardSummaryActivity.TICKETS_KEY, cardReaderResponse.ticketsNumber)
//                intent.putExtra(CardSummaryActivity.CONTRACT, cardReaderResponse.contract)
//                intent.putExtra(CardSummaryActivity.CARD_TYPE, cardReaderResponse.cardType)
//                startActivity(intent)
//            }
//        } else {
//            presentCardTv.visibility = View.VISIBLE
//        }
//    }
//
//    companion object {
//        private const val RETURN_DELAY_MS = 30000
//        private const val EVENT_DELAY_MS = 3000
//    }
//}
