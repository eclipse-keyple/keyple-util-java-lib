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
import javax.inject.Inject
import org.eclipse.keyple.famoco.validator.di.scopes.AppScoped
import org.eclipse.keyple.famoco.validator.ticketing.TicketingSession
import org.eclipse.keyple.famoco.validator.ticketing.TicketingSessionManager
import org.eclipse.keyple.core.seproxy.SeProxyService
import org.eclipse.keyple.core.seproxy.SeReader
import org.eclipse.keyple.core.seproxy.event.ObservableReader
import org.eclipse.keyple.core.seproxy.exception.KeyplePluginInstantiationException
import org.eclipse.keyple.core.seproxy.exception.KeyplePluginNotFoundException
import org.eclipse.keyple.core.seproxy.protocol.SeCommonProtocols
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoPluginFactory
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoReader
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcPluginFactory
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcProtocolSettings.getSetting
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcReader
import timber.log.Timber

@AppScoped
class CardReaderApi @Inject constructor() {

    private lateinit var poReader: SeReader
    private lateinit var samReader: SeReader
    private lateinit var ticketingSessionManager: TicketingSessionManager
    private lateinit var ticketingSession: TicketingSession

    @Throws(KeyplePluginInstantiationException::class)
    fun init(observer: ObservableReader.ReaderObserver?) {
        Timber.d("Initialize SEProxy with Android Plugin")
        val nfcPlugin = SeProxyService.getInstance().registerPlugin(AndroidNfcPluginFactory())
        val samPlugin = SeProxyService.getInstance().registerPlugin(AndroidFamocoPluginFactory())
        // define task as an observer for ReaderEvents
        poReader = nfcPlugin.getReader(AndroidNfcReader.READER_NAME)
        Timber.d("PO (NFC) reader name: ${poReader.name}")

        poReader.setParameter("FLAG_READER_RESET_STATE", "0")
        poReader.setParameter("FLAG_READER_PRESENCE_CHECK_DELAY", "100")
        poReader.setParameter("FLAG_READER_NO_PLATFORM_SOUNDS", "0")
        poReader.setParameter("FLAG_READER_SKIP_NDEF_CHECK", "0")

        // with this protocol settings we activate the nfc for ISO1443_4 protocol
        poReader.addSeProtocolSetting(SeCommonProtocols.PROTOCOL_ISO14443_4, getSetting(SeCommonProtocols.PROTOCOL_ISO14443_4))
        poReader.addSeProtocolSetting(SeCommonProtocols.PROTOCOL_MIFARE_CLASSIC, getSetting(SeCommonProtocols.PROTOCOL_MIFARE_CLASSIC))

        /* remove the observer if it already exist */
        (poReader as ObservableReader).addObserver(observer)

        samReader = samPlugin.getReader(AndroidFamocoReader.READER_NAME)

        ticketingSessionManager = TicketingSessionManager()

        ticketingSession = ticketingSessionManager.createTicketingSession(poReader, samReader) as TicketingSession
    }

    fun startNfcDetection(activity: Activity) {
        (poReader as AndroidNfcReader).enableNFCReaderMode(activity)

        /*
        * Provide the SeReader with the selection operation to be processed when a PO is
        * inserted.
        */
        ticketingSession.prepareAndSetPoDefaultSelection()

        (poReader as ObservableReader).startSeDetection(ObservableReader.PollingMode.REPEATING)
    }

    fun stopNfcDetection(activity: Activity) {
        try {
            // notify reader that se detection has been switched off
            (poReader as AndroidNfcReader).stopSeDetection()
            // Disable Reader Mode for NFC Adapter
            (poReader as AndroidNfcReader).disableNFCReaderMode(activity)
        } catch (e: KeyplePluginNotFoundException) {
            Timber.e(e, "NFC Plugin not found")
        }
    }

    fun getTicketingSession(): TicketingSession {
        return ticketingSession
    }
}
