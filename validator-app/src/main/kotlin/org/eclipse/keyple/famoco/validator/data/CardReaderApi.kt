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
package org.eclipse.keyple.famoco.validator.data

import android.app.Activity
import org.eclipse.keyple.core.seproxy.SeProxyService
import org.eclipse.keyple.core.seproxy.SeReader
import org.eclipse.keyple.core.seproxy.event.ObservableReader
import org.eclipse.keyple.core.seproxy.exception.KeypleException
import org.eclipse.keyple.core.seproxy.exception.KeyplePluginInstantiationException
import org.eclipse.keyple.core.seproxy.exception.KeyplePluginNotFoundException
import org.eclipse.keyple.core.seproxy.exception.KeypleReaderIOException
import org.eclipse.keyple.famoco.validator.BuildConfig
import org.eclipse.keyple.famoco.validator.di.scopes.AppScoped
import org.eclipse.keyple.famoco.validator.reader.IReaderRepository
import org.eclipse.keyple.famoco.validator.ticketing.TicketingSession
import org.eclipse.keyple.famoco.validator.ticketing.TicketingSessionManager
import timber.log.Timber
import javax.inject.Inject

@AppScoped
class CardReaderApi @Inject constructor(private var readerRepository: IReaderRepository) {

    private lateinit var ticketingSessionManager: TicketingSessionManager
    private var ticketingSession: TicketingSession? = null

    @Throws(
        KeyplePluginInstantiationException::class,
        IllegalStateException::class,
        KeyplePluginNotFoundException::class
    )
    suspend fun init(observer: ObservableReader.ReaderObserver?) {

        /*
         * Register plugin
         */
        try {
            readerRepository.registerPlugin()
        } catch (e: KeypleException) {
            Timber.e(e)
            throw IllegalStateException(e.message)
        }

        /*
         * Init PO reader
         */
        val poReader: SeReader?
        try {
            poReader = readerRepository.initPoReader()
        } catch (e: KeyplePluginNotFoundException) {
            Timber.e(e)
            throw IllegalStateException("PoReader with name AndroidCoppernicAskPlugin was not found")
        } catch (e: KeypleReaderIOException) {
            Timber.e(e)
            throw IllegalStateException(e.message)
        } catch (e: KeypleException) {
            Timber.e(e)
            throw IllegalStateException(e.message)
        }
        if (poReader == null) {
            throw IllegalStateException("No proxy reader available - ${BuildConfig.FLAVOR}")
        }

        /*
         * Init SAM reader
         */
        var samReaders: Map<String, SeReader>? = null
        try {
            samReaders = readerRepository.initSamReaders()
        } catch (e: KeyplePluginNotFoundException) {
            Timber.e(e)
        }
        if (samReaders.isNullOrEmpty()) {
            Timber.w("No SAM reader available")
        }

        poReader.let { reader ->
            /* remove the observer if it already exist */
            (reader as ObservableReader).addObserver(observer)

            ticketingSessionManager = TicketingSessionManager()

            ticketingSession =
                ticketingSessionManager.createTicketingSession(readerRepository) as TicketingSession
        }
    }

    fun startNfcDetection(activity: Activity) {
        readerRepository.enableNfcReaderMode(activity)

        /*
        * Provide the SeReader with the selection operation to be processed when a PO is
        * inserted.
        */
        ticketingSession?.prepareAndSetPoDefaultSelection()

        (readerRepository.poReader as ObservableReader).startSeDetection(ObservableReader.PollingMode.REPEATING)
    }

    fun stopNfcDetection(activity: Activity) {
        try {
            // notify reader that se detection has been switched off
            (readerRepository.poReader as ObservableReader).stopSeDetection()
            // Disable Reader Mode for NFC Adapter
            readerRepository.disableNfcReaderMode(activity)
        } catch (e: KeyplePluginNotFoundException) {
            Timber.e(e, "NFC Plugin not found")
        }
    }

    fun getTicketingSession(): TicketingSession? {
        return ticketingSession
    }

    fun onDestroy(observer: ObservableReader.ReaderObserver?) {
        if (observer != null && readerRepository.poReader != null) {
            (readerRepository.poReader as ObservableReader).removeObserver(observer)
        }
        SeProxyService.getInstance().plugins.forEach {
            SeProxyService.getInstance().unregisterPlugin(it.key)
        }
        SeProxyService.getInstance().plugins.clear()
        readerRepository.onDestroy()
        ticketingSession = null
    }
}
