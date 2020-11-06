package org.eclipse.keyple.famoco.validator.di

import android.app.Activity
import org.eclipse.keyple.core.plugin.reader.AbstractLocalReader
import org.eclipse.keyple.core.service.Reader
import org.eclipse.keyple.core.service.SmartCardService
import org.eclipse.keyple.core.service.event.ObservableReader
import org.eclipse.keyple.core.service.exception.KeypleException
import org.eclipse.keyple.core.service.util.ContactsCardCommonProtocols
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

class MockSamReaderRepositoryImpl @Inject constructor() :
    IReaderRepository {

    override var poReader: Reader? = null
    override var samReaders: MutableMap<String, Reader> = mutableMapOf()

    @Throws(KeypleException::class)
    override fun registerPlugin() {
        SmartCardService.getInstance().registerPlugin(AndroidNfcPluginFactory())
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
        samReaders =
            mutableMapOf(Pair(AndroidMockReaderImpl.READER_NAME, AndroidMockReaderImpl()))

        return samReaders
    }

    override fun enableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).enableNFCReaderMode(activity)
    }

    override fun disableNfcReaderMode(activity: Activity) {
        (poReader as AndroidNfcReader).disableNFCReaderMode(activity)
    }

    override fun getSamReader(): Reader? {
        return samReaders[AndroidMockReaderImpl.READER_NAME]
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

    override fun isMockedResponse(): Boolean {
        return true
    }

    @Suppress("INVISIBLE_ABSTRACT_MEMBER_FROM_SUPER_WARNING")
    class AndroidMockReaderImpl : AbstractLocalReader(
        "",
        ""
    ) {

        override fun transmitApdu(apduIn: ByteArray?): ByteArray {
            return apduIn ?: throw IllegalStateException("Mock no apdu in")
        }

        override fun getATR(): ByteArray? {
            return null
        }

        override fun openPhysicalChannel() {
        }

        override fun isPhysicalChannelOpen(): Boolean {
            return true
        }

        override fun isCardPresent(): Boolean {
            return true
        }

        override fun checkCardPresence(): Boolean {
            return true
        }

        override fun closePhysicalChannel() {
        }

        override fun isContactless(): Boolean {
            return false
        }

        override fun isCurrentProtocol(readerProtocolName: String?): Boolean {
            return true
        }

        override fun deactivateReaderProtocol(readerProtocolName: String?) {
            //Do nothing
        }

        override fun activateReaderProtocol(readerProtocolName: String?) {
            //Do nothing
        }

        companion object {
            const val READER_NAME = "Mock_Sam"
        }
    }
}