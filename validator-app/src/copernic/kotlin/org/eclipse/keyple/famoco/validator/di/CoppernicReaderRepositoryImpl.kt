package org.eclipse.keyple.famoco.validator.di

import android.app.Activity
import android.content.Context
import fr.coppernic.sdk.power.PowerManager
import fr.coppernic.sdk.power.api.PowerListener
import fr.coppernic.sdk.power.api.peripheral.Peripheral
import fr.coppernic.sdk.power.impl.cone.ConePeripheral
import fr.coppernic.sdk.utils.core.CpcResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.eclipse.keyple.coppernic.ask.plugin.AndroidCoppernicAskContactReader
import org.eclipse.keyple.coppernic.ask.plugin.AndroidCoppernicAskContactlessReader
import org.eclipse.keyple.coppernic.ask.plugin.AndroidCoppernicAskContactlessReaderImpl
import org.eclipse.keyple.coppernic.ask.plugin.AndroidCoppernicAskPluginFactory
import org.eclipse.keyple.core.seproxy.PluginFactory
import org.eclipse.keyple.core.seproxy.SeProxyService
import org.eclipse.keyple.core.seproxy.SeReader
import org.eclipse.keyple.core.seproxy.exception.KeypleException
import org.eclipse.keyple.core.seproxy.exception.KeyplePluginInstantiationException
import org.eclipse.keyple.core.seproxy.plugin.reader.AbstractLocalReader
import org.eclipse.keyple.core.seproxy.plugin.reader.util.ContactlessCardCommonProtocols
import org.eclipse.keyple.core.seproxy.plugin.reader.util.ContactsCardCommonProtocols
import org.eclipse.keyple.famoco.validator.reader.IReaderRepository
import org.eclipse.keyple.famoco.validator.reader.PoReaderProtocol
import org.eclipse.keyple.famoco.validator.util.suspendCoroutineWithTimeout
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

/**
 *
 *  created on 21/09/2020
 *
 *  @author youssefamrani
 */

class CoppernicReaderRepositoryImpl @Inject constructor(private val applicationContext: Context) :
    IReaderRepository, PowerListener {

    override var poReader: SeReader? = null
    override var samReaders: MutableMap<String, SeReader> = mutableMapOf()
    var peripheral: ConePeripheral? = null

    var powerListenerContinuation: Continuation<Boolean>? = null

    override fun onPowerUp(res: CpcResult.RESULT?, peripheral: Peripheral?) {
        this.peripheral = peripheral as ConePeripheral
        powerListenerContinuation?.resume(true)
    }

    override fun onPowerDown(res: CpcResult.RESULT?, peripheral: Peripheral?) {
    }

    @Throws(KeypleException::class)
    override fun registerPlugin() {
        runBlocking {
            val result: Boolean? =
                suspendCoroutineWithTimeout(
                    POWER_UP_TIMEOUT
                ) { continuation ->
                    powerListenerContinuation = continuation

                    PowerManager.get().registerListener(this@CoppernicReaderRepositoryImpl)
                    ConePeripheral.RFID_ASK_UCM108_GPIO.on(applicationContext)
                }

            if (result != null && result) {
                val pluginFactory: PluginFactory?
                pluginFactory = withContext(Dispatchers.IO) {
                    AndroidCoppernicAskPluginFactory.init(applicationContext)
                }
                SeProxyService.getInstance().registerPlugin(pluginFactory)
            } else {
                throw KeyplePluginInstantiationException("An error occured during Copernic AskReader power up.")
            }
        }
    }

    @Throws(KeypleException::class)
    override suspend fun initPoReader(): SeReader? {
        val askPlugin =
            SeProxyService.getInstance().getPlugin(AndroidCoppernicAskPluginFactory.pluginName)
        val poReader = askPlugin?.getReader(AndroidCoppernicAskContactlessReader.READER_NAME)
        poReader?.let {

            (it as AbstractLocalReader).activateProtocol(
                getContactlessIsoProtocol()!!.readerProtocolName,
                getContactlessIsoProtocol()!!.applicationProtocolName
            )

            this.poReader = poReader
        }

        return poReader
    }

    @Throws(KeypleException::class)
    override suspend fun initSamReaders(): Map<String, SeReader> {
        val askPlugin =
            SeProxyService.getInstance().getPlugin(AndroidCoppernicAskPluginFactory.pluginName)
        samReaders = askPlugin?.readers?.filter {
            !it.value.isContactless
        }?.toMutableMap() ?: mutableMapOf()

        samReaders.forEach {
            (it.value as AbstractLocalReader).activateProtocol(
                getSamReaderProtocol(),
                getSamReaderProtocol()
            )
        }
        return samReaders
    }

    override fun getSamReader(): SeReader? {
        return if (samReaders.isNotEmpty()) {
            val filteredByName = samReaders.filter {
                it.value.name == SAM_READER_1_NAME
            }

            return if (filteredByName.isNullOrEmpty()) {
                samReaders.values.first()
            } else {
                filteredByName.values.first()
            }
        } else {
            null
        }
    }

    override fun enableNfcReaderMode(activity: Activity) {
        //Do nothing
    }

    override fun disableNfcReaderMode(activity: Activity) {
        //Do nothing
    }

    override fun getContactlessIsoProtocol(): PoReaderProtocol? {
        return PoReaderProtocol(
            ContactlessCardCommonProtocols.ISO_14443_4.name,
            ContactlessCardCommonProtocols.ISO_14443_4.name
        )
    }

    override fun getContactlessMifareProtocol(): PoReaderProtocol? {
        return null
    }

    override fun getSamReaderProtocol(): String = ContactsCardCommonProtocols.ISO_7816_3.name

    override fun onDestroy() {
        ConePeripheral.RFID_ASK_UCM108_GPIO.off(applicationContext)
        poReader?.let {
            (poReader as AndroidCoppernicAskContactlessReaderImpl).clearInstance()
        }

        // Releases PowerManager
        PowerManager.get().unregisterAll()
        PowerManager.get().releaseResources()
    }

    companion object {
        private const val POWER_UP_TIMEOUT: Long = 3000
        const val SAM_READER_SLOT_1 = "1"
        const val SAM_READER_1_NAME =
            "${AndroidCoppernicAskContactReader.READER_NAME}_${SAM_READER_SLOT_1}"
    }
}