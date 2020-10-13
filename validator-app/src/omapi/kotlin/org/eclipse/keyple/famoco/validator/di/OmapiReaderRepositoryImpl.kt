package org.eclipse.keyple.famoco.validator.di

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.eclipse.keyple.core.seproxy.SeProxyService
import org.eclipse.keyple.core.seproxy.SeReader
import org.eclipse.keyple.core.seproxy.exception.KeypleException
import org.eclipse.keyple.core.seproxy.exception.KeyplePluginInstantiationException
import org.eclipse.keyple.core.seproxy.protocol.SeCommonProtocols
import org.eclipse.keyple.famoco.validator.reader.IReaderRepository
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcPlugin
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcPluginFactory
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcProtocolSettings
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcReader
import org.eclipse.keyple.plugin.android.omapi.AndroidOmapiPluginFactory
import org.eclipse.keyple.plugin.android.omapi.PLUGIN_NAME
import timber.log.Timber
import javax.inject.Inject

/**
 *
 *  created on 21/09/2020
 *
 *  @author youssefamrani
 */

class OmapiReaderRepositoryImpl @Inject constructor(private val applicationContext: Context) :
    IReaderRepository {

    override var poReader: SeReader? = null
    override var samReaders: MutableMap<String, SeReader> = mutableMapOf()

    @Throws(KeypleException::class)
    override fun registerPlugin() {
        SeProxyService.getInstance().registerPlugin(AndroidNfcPluginFactory())
        SeProxyService.getInstance().registerPlugin(AndroidOmapiPluginFactory(applicationContext))
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
        return withContext(Dispatchers.IO) {
            var readers: Map<String, SeReader>? = null
            for (x in 1..MAX_TRIES) {
                readers = SeProxyService.getInstance().getPlugin(PLUGIN_NAME).readers
                if (readers == null || readers.size < 1) {
                    Timber.d("No readers found in OMAPI Keyple Plugin")
                    Timber.d("Retrying in 1 second")
                    delay(1000)
                } else {
                    Timber.d("Readers Found")
                    break
                }
            }

            readers ?: throw KeyplePluginInstantiationException("No reader found")
        }
    }

    override fun setSamParameters(samReader: SeReader) {
        //Do nothing
    }

    override fun enableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).enableNFCReaderMode(activity)
    }

    override fun disableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).disableNFCReaderMode(activity)
    }

    override fun getSamReader(): SeReader? {
        return if (samReaders.isNotEmpty()) {
            samReaders.values.first()
        } else {
            null
        }
    }

    override fun onDestroy() {
        //Do nothing
    }

    companion object {
        private const val MAX_TRIES = 10
    }
}