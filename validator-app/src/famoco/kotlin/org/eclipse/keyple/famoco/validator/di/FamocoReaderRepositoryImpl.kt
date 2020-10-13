package org.eclipse.keyple.famoco.validator.di

import android.app.Activity
import org.eclipse.keyple.core.seproxy.SeProxyService
import org.eclipse.keyple.core.seproxy.SeReader
import org.eclipse.keyple.core.seproxy.exception.KeypleException
import org.eclipse.keyple.core.seproxy.protocol.SeCommonProtocols
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoPlugin
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoPluginFactory
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoReader
import org.eclipse.keyple.famoco.validator.reader.IReaderRepository
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcPlugin
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcPluginFactory
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcProtocolSettings
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcReader
import timber.log.Timber
import javax.inject.Inject

/**
 *
 *  created on 21/09/2020
 *
 *  @author youssefamrani
 */

class FamocoReaderRepositoryImpl @Inject constructor() :
    IReaderRepository {

    override var poReader: SeReader? = null
    override var samReaders: MutableMap<String, SeReader> = mutableMapOf()

    @Throws(KeypleException::class)
    override fun registerPlugin() {
        SeProxyService.getInstance().registerPlugin(AndroidNfcPluginFactory())
        try {
            SeProxyService.getInstance().registerPlugin(AndroidFamocoPluginFactory())
        } catch (e: UnsatisfiedLinkError) {
            Timber.w(e)
        }
    }

    @Throws(KeypleException::class)
    override suspend fun initPoReader(): SeReader? {
        val readerPlugin = SeProxyService.getInstance().getPlugin(AndroidNfcPlugin.PLUGIN_NAME)
        poReader = readerPlugin.readers.values.first()

        poReader?.let {
            Timber.d("Initialize SEProxy with Android Plugin")

            // define task as an observer for ReaderEvents
            Timber.d("PO (NFC) reader name: ${it.name}")

            it.setParameter("FLAG_READER_RESET_STATE", "0")
            it.setParameter("FLAG_READER_PRESENCE_CHECK_DELAY", "100")
            it.setParameter("FLAG_READER_NO_PLATFORM_SOUNDS", "0")
            it.setParameter("FLAG_READER_SKIP_NDEF_CHECK", "0")

            // with this protocol settings we activate the nfc for ISO1443_4 protocol
            it.addSeProtocolSetting(
                SeCommonProtocols.PROTOCOL_ISO14443_4,
                AndroidNfcProtocolSettings.getSetting(SeCommonProtocols.PROTOCOL_ISO14443_4)
            )
            it.addSeProtocolSetting(
                SeCommonProtocols.PROTOCOL_MIFARE_CLASSIC,
                AndroidNfcProtocolSettings.getSetting(SeCommonProtocols.PROTOCOL_MIFARE_CLASSIC)
            )
        }

        return poReader
    }

    @Throws(KeypleException::class)
    override suspend fun initSamReaders(): Map<String, SeReader> {
        if (samReaders.isNullOrEmpty()) {
            val samPlugin = SeProxyService.getInstance().getPlugin(AndroidFamocoPlugin.PLUGIN_NAME)

            if (samPlugin != null) {
                val samReader = samPlugin.getReader(AndroidFamocoReader.READER_NAME)

                if (samReader != null) {
                    samReaders[AndroidFamocoReader.READER_NAME] = samReader
                }
            }
        }

        return samReaders
    }

    override fun setSamParameters(samReader: SeReader) {
        samReader.setParameter(AndroidFamocoReader.FLAG_READER_RESET_STATE, "")
    }

    override fun enableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).enableNFCReaderMode(activity)
    }

    override fun disableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).disableNFCReaderMode(activity)
    }

    override fun getSamReader(): SeReader? {
        return samReaders[AndroidFamocoReader.READER_NAME]
    }

    override fun onDestroy() {
        //Do nothing
    }
}