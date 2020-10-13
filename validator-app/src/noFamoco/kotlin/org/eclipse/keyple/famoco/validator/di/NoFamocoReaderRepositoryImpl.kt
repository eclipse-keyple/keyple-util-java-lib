package org.eclipse.keyple.famoco.validator.di

import android.app.Activity
import org.eclipse.keyple.core.seproxy.SeProxyService
import org.eclipse.keyple.core.seproxy.SeReader
import org.eclipse.keyple.core.seproxy.exception.KeypleException
import org.eclipse.keyple.core.seproxy.protocol.SeProtocol
import org.eclipse.keyple.core.seproxy.protocol.TransmissionMode
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoPlugin
import org.eclipse.keyple.famoco.se.plugin.AndroidFamocoReader
import org.eclipse.keyple.famoco.validator.reader.IReaderRepository
import org.eclipse.keyple.plugin.android.nfc.AndroidNfcReader
import javax.inject.Inject

/**
 *
 *  created on 21/09/2020
 *
 *  @author youssefamrani
 */

class NoFamocoReaderRepositoryImpl @Inject constructor() :
    IReaderRepository {

    override var poReader: SeReader? = MockSeReader()
    override var samReaders: MutableMap<String, SeReader> = mutableMapOf()

    @Throws(KeypleException::class)
    override fun registerPlugin() {
        //Do nothing
    }

    @Throws(KeypleException::class)
    override suspend fun initPoReader(): SeReader? {
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

    class MockSeReader() : SeReader {
        override fun addSeProtocolSetting(seProtocol: SeProtocol?, protocolRule: String?) {
        }

        override fun setParameter(key: String?, value: String?) {
        }

        override fun getName(): String {
            return "No Famoco"
        }

        override fun getParameters(): MutableMap<String, String> {
            return mutableMapOf()
        }

        override fun setParameters(parameters: MutableMap<String, String>?) {
        }

        override fun setSeProtocolSetting(protocolSetting: MutableMap<SeProtocol, String>?) {
        }

        override fun isSePresent(): Boolean {
            return true
        }

        override fun getTransmissionMode(): TransmissionMode {
            return TransmissionMode.CONTACTS
        }
    }
}