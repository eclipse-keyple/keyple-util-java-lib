package org.eclipse.keyple.famoco.validator.di

import android.app.Activity
import org.eclipse.keyple.core.plugin.reader.AbstractLocalReader
import org.eclipse.keyple.core.service.Reader
import org.eclipse.keyple.core.service.SmartCardService
import org.eclipse.keyple.core.service.event.ObservableReader
import org.eclipse.keyple.core.service.exception.KeypleException
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoPlugin
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoPluginFactory
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoReader
import org.eclipse.keyple.famoco.validator.reader.IReaderRepository
import org.eclipse.keyple.famoco.validator.reader.PoReaderProtocol
import org.eclipse.keyple.plugin.android.nfc.*
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

    override var poReader: Reader? = null
    override var samReaders: MutableMap<String, Reader> = mutableMapOf()

    @Throws(KeypleException::class)
    override fun registerPlugin() {
        SmartCardService.getInstance().registerPlugin(AndroidNfcPluginFactory())
        try {
            SmartCardService.getInstance().registerPlugin(AndroidFamocoPluginFactory())
        } catch (e: UnsatisfiedLinkError) {
            Timber.w(e)
        }
    }

    @Throws(KeypleException::class)
    override suspend fun initPoReader(): Reader? {
        val readerPlugin = SmartCardService.getInstance().getPlugin(AndroidNfcPlugin.PLUGIN_NAME)
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
        if (samReaders.isNullOrEmpty()) {
            val samPlugin = SmartCardService.getInstance().getPlugin(AndroidFamocoPlugin.PLUGIN_NAME)

            if (samPlugin != null) {
                val samReader = samPlugin.getReader(AndroidFamocoReader.READER_NAME)
                samReader?.let {
                    (it as AbstractLocalReader).activateProtocol(
                        getSamReaderProtocol(),
                        getSamReaderProtocol()
                    )

                    samReaders[AndroidFamocoReader.READER_NAME] = it
                }
            }
        }

        return samReaders
    }

    override fun enableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).enableNFCReaderMode(activity)
    }

    override fun disableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).disableNFCReaderMode(activity)
    }

    override fun getSamReader(): Reader? {
        return samReaders[AndroidFamocoReader.READER_NAME]
    }

    override fun getContactlessIsoProtocol(): PoReaderProtocol? {
        return PoReaderProtocol(
            AndroidNfcSupportedProtocols.ISO_14443_4.name,
            AndroidNfcProtocolSettings.getSetting(AndroidNfcSupportedProtocols.ISO_14443_4.name)
        )
    }

    override fun getContactlessMifareProtocol(): PoReaderProtocol? {
        return PoReaderProtocol(AndroidNfcSupportedProtocols.MIFARE_CLASSIC.name,
            AndroidNfcProtocolSettings.getSetting(AndroidNfcSupportedProtocols.MIFARE_CLASSIC.name)
        )
    }

    override fun getSamReaderProtocol(): String {
        return ContactsCardCommonProtocols.ISO_7816_3.name
    }

    override fun onDestroy() {
        //Do nothing
    }
}