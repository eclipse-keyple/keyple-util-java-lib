//package org.eclipse.keyple.famoco.validator.ui.activities
//
///**
// *
// *  created on 23/10/2020
// *
// *  @author youssefamrani
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
//package org.cna.keyple.famoco.validator.viewModels
//
//import android.app.Activity
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import io.reactivex.Observable
//import io.reactivex.disposables.CompositeDisposable
//import org.cna.keyple.famoco.validator.data.CardReaderApi
//import org.cna.keyple.famoco.validator.data.model.CardReaderResponse
//import org.cna.keyple.famoco.validator.data.model.Status
//import org.cna.keyple.famoco.validator.di.scopes.AppScoped
//import org.cna.keyple.famoco.validator.rx.SchedulerProvider
//import org.cna.keyple.famoco.validator.ticketing.ITicketingSession
//import org.cna.keyple.famoco.validator.ticketing.TicketingSession
//import org.cna.keyple.famoco.validator.util.LiveEvent
//import org.eclipse.keyple.core.seproxy.event.ObservableReader
//import org.eclipse.keyple.core.seproxy.event.ReaderEvent
//import org.eclipse.keyple.core.seproxy.exception.KeyplePluginInstantiationException
//import timber.log.Timber
//import javax.inject.Inject
//
//@AppScoped
//class CardReaderViewModel @Inject constructor(
//    private val cardReaderApi: CardReaderApi,
//    private val schedulerProvider: SchedulerProvider
//) : ViewModel() {
//    /* application states */
//    enum class AppState {
//        UNSPECIFIED, WAIT_SYSTEM_READY, WAIT_CARD, CARD_STATUS
//    }
//
//    private var poReaderObserver: PoObserver? = null
//    var currentAppState = AppState.WAIT_SYSTEM_READY
//    private val disposables = CompositeDisposable()
//    val response = LiveEvent<CardReaderResponse>()
//    lateinit var ticketingSession: TicketingSession
//
//    val isNfcDetecting: MutableLiveData<Boolean> by lazy {
//        MutableLiveData<Boolean>()
//    }
//
//    var readersInitialized = false
//
//    override fun onCleared() {
//        disposables.clear()
//    }
//
//    /**
//     * Generate observable to communicate with UI thread
//     *
//     * @param status
//     * @param tickets
//     */
//    private fun setUiResponse(status: Status, tickets: Int, contract: String, cardType: String) {
//        Timber.d("setUiResponse $status $tickets $contract $cardType")
//        disposables.add(Observable.just(
//            CardReaderResponse(
//                status,
//                tickets,
//                contract,
//                cardType
//            )
//        )
//            .subscribeOn(schedulerProvider.io())
//            .observeOn(schedulerProvider.ui())
//            .subscribe { t: CardReaderResponse? ->
//                response.setValue(t)
//            }
//        )
//    }
//
//    @Throws(KeyplePluginInstantiationException::class)
//    suspend fun initCardReader() {
//        Timber.d("initCardReader")
//        if (!readersInitialized) {
//            poReaderObserver = PoObserver()
//            cardReaderApi.init(poReaderObserver)
//            ticketingSession = cardReaderApi.getTicketingSession()!!
//            handleAppEvents(AppState.WAIT_CARD, null)
//            readersInitialized = true
//            Timber.d("readersInitialized")
//        }
//    }
//
//    fun samReaderAvailable(): String {
//        val samReaderAvailable = ticketingSession.samReaderAvailable()
//        return if (samReaderAvailable) {
//            "OK"
//        } else {
//            "not available"
//        }
//    }
//
//    fun startNfcDetection(activity: Activity?) {
//        if (readersInitialized) {
//            cardReaderApi.startNfcDetection(activity!!)
//            isNfcDetecting.postValue(true)
//            Timber.d("startNfcDetection")
//        }
//    }
//
//    fun stopNfcDetection(activity: Activity?) {
//        if (readersInitialized) {
//            cardReaderApi.stopNfcDetection(activity!!)
//            isNfcDetecting.postValue(false)
//            Timber.d("stopNfcDetection")
//        }
//    }
//
//    /**
//     * main app state machine handle
//     *
//     * @param appState
//     * @param readerEvent
//     */
//    private fun handleAppEvents(appState: AppState, readerEvent: ReaderEvent?) {
//
//        var newAppState = appState
//        Timber.i("Current state = $currentAppState, wanted new state = $newAppState, event = ${readerEvent?.eventType}")
//        when (readerEvent?.eventType) {
//            ReaderEvent.EventType.SE_INSERTED, ReaderEvent.EventType.SE_MATCHED -> {
//                if (newAppState == AppState.WAIT_SYSTEM_READY) {
//                    return
//                }
//                Timber.i("Process default selection...")
//
//                val seSelectionResult =
//                    ticketingSession.processDefaultSelection(readerEvent.defaultSelectionsResponse)
//
//                if (!seSelectionResult.hasActiveSelection()) {
//                    Timber.e("PO Not selected")
//                    setUiResponse(Status.INVALID_CARD, 0, "", "")
//                    return
//                }
//
//                Timber.i("PO Type = ${ticketingSession.poTypeName}")
//                if ("CALYPSO" != ticketingSession.poTypeName) {
//                    setUiResponse(Status.INVALID_CARD, 0, "", ticketingSession.poTypeName ?: "")
//                    return
//                } else {
//                    Timber.i("A Calypso PO selection succeeded.")
//                    newAppState = AppState.CARD_STATUS
//                }
//            }
//            ReaderEvent.EventType.SE_REMOVED -> {
//                currentAppState = AppState.WAIT_SYSTEM_READY
//            }
//            else -> {
//                Timber.w("Event type not handled.")
//            }
//        }
//
//        when (newAppState) {
//            AppState.WAIT_SYSTEM_READY, AppState.WAIT_CARD -> {
//                currentAppState = newAppState
//            }
//            AppState.CARD_STATUS -> {
//                currentAppState = newAppState
//                when (readerEvent?.eventType) {
//                    ReaderEvent.EventType.SE_INSERTED, ReaderEvent.EventType.SE_MATCHED -> {
//                        try {
//                            if (ticketingSession.analyzePoProfile()) {
//                                val cardContent = ticketingSession.cardContent
//                                val contract = String(cardContent.contracts[1] ?: byteArrayOf(0))
//                                Timber.i("Contract =  $contract")
//                                if (contract.isEmpty() ||
//                                    contract.contains("NO CONTRACT") ||
//                                    !contract.contains("SEASON")
//                                ) {
//                                    // index des counters commence à un
//                                    cardContent.counters[1]?.let {
//                                        if (it > 0) {
//                                            val result = ticketingSession.debitTickets(1)
//                                            if (result == ITicketingSession.STATUS_OK) {
//                                                Timber.i("Debit TICKETS_FOUND page.")
//                                                setUiResponse(
//                                                    Status.TICKETS_FOUND,
//                                                    it - 1,
//                                                    "",
//                                                    ticketingSession.poTypeName ?: ""
//                                                )
//                                            } else {
//                                                Timber.i("Debit ERROR page.")
//                                                setUiResponse(
//                                                    Status.ERROR,
//                                                    0,
//                                                    "",
//                                                    ticketingSession.poTypeName ?: ""
//                                                )
//                                            }
//                                        } else {
//                                            Timber.i("Load EMPTY_CARD page.")
//                                            setUiResponse(
//                                                Status.EMPTY_CARD,
//                                                0,
//                                                "",
//                                                ticketingSession.poTypeName ?: ""
//                                            )
//                                        }
//                                    }
//                                } else {
//                                    if (ticketingSession.loadTickets(0) == ITicketingSession.STATUS_OK) {
//                                        Timber.i("Season TICKETS_FOUND page.")
//                                        setUiResponse(
//                                            Status.TICKETS_FOUND,
//                                            0,
//                                            contract,
//                                            ticketingSession.poTypeName ?: ""
//                                        )
//                                    } else {
//                                        Timber.i("Season ticket ERROR page.")
//                                        setUiResponse(
//                                            Status.ERROR,
//                                            0,
//                                            "",
//                                            ticketingSession.poTypeName ?: ""
//                                        )
//                                    }
//                                }
//                            }
//                        } catch (e: IllegalStateException) {
//                            Timber.e(e)
//                            Timber.e("Load ERROR page after exception = ${e.message}")
//                            setUiResponse(Status.ERROR, 0, "", ticketingSession.poTypeName ?: "")
//                        }
//                    }
//                }
//            }
//            else -> {
//            }
//        }
//        Timber.i("New state = $currentAppState")
//    }
//
//    inner class PoObserver : ObservableReader.ReaderObserver {
//        override fun update(event: ReaderEvent) {
//            Timber.i("New ReaderEvent received :${event.eventType.name}")
//            handleAppEvents(currentAppState, event)
//        }
//    }
//
//    fun onDestroy() {
//        readersInitialized = false
//        cardReaderApi.onDestroy(poReaderObserver)
//        poReaderObserver = null
//    }
//
//    companion object {
//        private val TAG = CardReaderViewModel::class.java.simpleName
//        private const val CALYPSO_PO_TYPE = "CALYPSO"
//    }
//}
