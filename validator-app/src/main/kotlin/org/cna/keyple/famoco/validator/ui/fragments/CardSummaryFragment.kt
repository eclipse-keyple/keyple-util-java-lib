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

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import org.cna.keyple.famoco.validator.R
import org.cna.keyple.famoco.validator.data.model.Status
import org.cna.keyple.famoco.validator.data.model.Status.Companion.getStatus
import org.cna.keyple.famoco.validator.databinding.FragmentCardSummaryBinding

class CardSummaryFragment : Fragment() {

    lateinit var binding: FragmentCardSummaryBinding
    private val timer = Timer()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_summary, container, false)
        binding.lifecycleOwner = this
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        val mTitle =
            activity!!.findViewById<TextView>(R.id.toolbar_title)
        val toolbar: Toolbar =
            activity!!.findViewById(R.id.my_toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_icon_back)
        toolbar.setNavigationOnClickListener { v: View? -> activity!!.onBackPressed() }
        val status =
            getStatus(
                arguments!!.getString(STATUS_KEY)!!
            )
        when (status) {
            Status.TICKETS_FOUND -> {
                val df: DateFormat =
                    SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.ENGLISH)
                val date = df.format(Calendar.getInstance().time)
                binding?.mainView?.setBackgroundColor(resources.getColor(R.color.green))
                binding?.animation?.setAnimation("tick_white.json")
                mTitle.setText(R.string.valid_title)
                binding?.bigText?.setText(R.string.valid_main_desc)
                val nbTickets =
                    arguments!!.getInt(TICKETS_KEY)
                if (nbTickets != 0) {
                    binding?.smallDesc?.text = String.format(
                        getString(R.string.valid_small_desc),
                        date,
                        nbTickets
                    )
                } else {
                    binding?.smallDesc?.text = String.format(
                        getString(R.string.valid_season_ticket_small_desc),
                        date,
                        arguments!!.getString(CONTRACT)
                            ?.trim { it <= ' ' }
                    )
                }
                binding?.mediumText?.setText(R.string.valid_last_desc)
                binding?.mediumText?.visibility = View.VISIBLE
            }
            Status.INVALID_CARD -> {
                binding?.mainView?.setBackgroundColor(resources.getColor(R.color.orange))
                binding?.animation?.setAnimation("error_white.json")
                mTitle.setText(R.string.card_invalid_title)
                binding?.bigText?.setText(R.string.card_invalid_main_desc)
                binding?.smallDesc?.text = String.format(
                    getString(R.string.card_invalid_small_desc),
                    arguments!!.getString(CARD_TYPE)
                        ?.trim { it <= ' ' }
                )
                binding?.mediumText?.visibility = View.GONE
            }
            Status.EMPTY_CARD -> {
                binding?.mainView?.setBackgroundColor(resources.getColor(R.color.red))
                binding?.animation?.setAnimation("error_white.json")
                mTitle.setText(R.string.no_tickets_title)
                binding?.bigText?.setText(R.string.no_tickets_main_desc)
                binding?.smallDesc?.setText(R.string.no_tickets_small_desc)
                binding?.mediumText?.visibility = View.GONE
            }
            else -> {
                binding?.mainView?.setBackgroundColor(resources.getColor(R.color.red))
                binding?.animation?.setAnimation("error_white.json")
                mTitle.setText(R.string.error_title)
                binding?.bigText?.setText(R.string.error_main_desc)
                binding?.smallDesc?.setText(R.string.error_small_desc)
                binding?.mediumText?.visibility = View.GONE
            }
        }
        binding?.animation?.playAnimation()

        // Play sound
        val mp =
            MediaPlayer.create(activity, R.raw.reading_sound)
        mp.start()
        timer.schedule(object : TimerTask() {
            override fun run() {
                activity!!.runOnUiThread { activity!!.onBackPressed() }
            }
        }, RETURN_DELAY_MS.toLong())
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        timer.cancel()
    }

    companion object {
        const val STATUS_KEY = "status"
        const val TICKETS_KEY = "tickets"
        const val CONTRACT = "contract"
        const val CARD_TYPE = "cardtype"
        private const val RETURN_DELAY_MS = 6000
    }
}
