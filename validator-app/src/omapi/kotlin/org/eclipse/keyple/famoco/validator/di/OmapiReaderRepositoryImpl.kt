package org.eclipse.keyple.famoco.validator.di

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.eclipse.keyple.core.seproxy.SeProxyService
import org.eclipse.keyple.core.seproxy.event.ObservableReader
import org.eclipse.keyple.core.seproxy.exception.KeypleException
import org.eclipse.keyple.core.seproxy.plugin.reader.AbstractLocalReader
import org.eclipse.keyple.core.seproxy.plugin.reader.util.ContactsCardCommonProtocols
import org.eclipse.keyple.famoco.validator.reader.IReaderRepository
import org.eclipse.keyple.famoco.validator.reader.PoReaderProtocol
import org.eclipse.keyple.plugin.android.nfc.*
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

    override var poReader: Reader? = null
    override var samReaders: MutableMap<String, Reader> = mutableMapOf()

    @Throws(KeypleException::class)
    override fun registerPlugin() {
        SeProxyService.getInstance().registerPlugin(AndroidNfcPluginFactory())
        SeProxyService.getInstance().registerPlugin(AndroidOmapiPluginFactory(applicationContext))
    }

    @Throws(KeypleException::class)
    override suspend fun initPoReader(): Reader? {
        val readerPlugin = SeProxyService.getInstance().getPlugin(AndroidNfcPlugin.PLUGIN_NAME)
        poReader = readerPlugin.readers.values.first()

        poReader?.let {
            val androidNfcReader = it as AndroidNfcReader
            Timber.d("Initialize SEProxy with Android Plugin")

            // define task as an observer for ReaderEvents
            Timber.d("PO (NFC) reader name: ${it.name}")

            androidNfcReader.setParameter("FLAG_READER_RESET_STATE", "0")
            androidNfcReader.setParameter("FLAG_READER_PRESENCE_CHECK_DELAY", "100")
            androidNfcReader.setParameter("FLAG_READER_NO_PLATFORM_SOUNDS", "0")
            androidNfcReader.setParameter("FLAG_READER_SKIP_NDEF_CHECK", "0")

            // with this protocol settings we activate the nfc for ISO1443_4 protocol
            (poReader as ObservableReader).activateProtocol(
                getContactlessIsoProtocol()!!.readerProtocolName,
                getContactlessIsoProtocol()!!.applicationProtocolName
            )

            (poReader as ObservableReader).activateProtocol(
                getContactlessMifareProtocol()!!.readerProtocolName,
                getContactlessMifareProtocol()!!.applicationProtocolName
            )
        }

        return poReader
    }

    @Throws(KeypleException::class)
    override suspend fun initSamReaders(): Map<String, Reader> {
        return withContext(Dispatchers.IO) {
            for (x in 1..MAX_TRIES) {
                samReaders = SeProxyService.getInstance().getPlugin(PLUGIN_NAME).readers
                if (samReaders.isEmpty()) {
                    Timber.d("No readers found in OMAPI Keyple Plugin")
                    Timber.d("Retrying in 1 second")
                    delay(1000)
                } else {
                    Timber.d("Readers Found")
                    break
                }
            }
            samReaders.forEach {
                (it.value as AbstractLocalReader).activateProtocol(
                    getSamReaderProtocol(),
                    getSamReaderProtocol()
                )
            }

            samReaders
        }
    }

    override fun enableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).enableNFCReaderMode(activity)
    }

    override fun disableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).disableNFCReaderMode(activity)
    }

    override fun getSamReader(): Reader? {
        return if (samReaders.isNotEmpty()) {
            samReaders.values.first()
        } else {
            null
        }
    }

    override fun getContactlessIsoProtocol(): PoReaderProtocol? {
        return PoReaderProtocol(
            AndroidNfcSupportedProtocols.ISO_14443_4.name,
            AndroidNfcProtocolSettings.getSetting(AndroidNfcSupportedProtocols.ISO_14443_4.name)
        )
    }

    override fun getContactlessMifareProtocol(): PoReaderProtocol? {
        return PoReaderProtocol(
            AndroidNfcSupportedProtocols.MIFARE_CLASSIC.name,
            AndroidNfcProtocolSettings.getSetting(AndroidNfcSupportedProtocols.MIFARE_CLASSIC.name)
        )
    }

    override fun getSamReaderProtocol(): String {
        return ContactsCardCommonProtocols.ISO_7816_3.name
    }

    override fun onDestroy() {
        //Do nothing
    }

    companion object {
        private const val MAX_TRIES = 10
    }
}