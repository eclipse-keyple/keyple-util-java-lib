package org.eclipse.keyple.famoco.validator.reader

import android.app.Activity
import org.eclipse.keyple.core.service.Reader
import org.eclipse.keyple.core.service.exception.KeypleException

/**
 *
 *  created on 21/09/2020
 *
 *  @author youssefamrani
 */

interface IReaderRepository {

    var poReader: Reader?
    var samReaders: MutableMap<String, Reader>

    @Throws(KeypleException::class)
    fun registerPlugin()

    @Throws(KeypleException::class)
    suspend fun initPoReader(): Reader?

    @Throws(KeypleException::class)
    suspend fun initSamReaders(): Map<String, Reader>

    fun enableNfcReaderMode(activity: Activity)
    fun disableNfcReaderMode(activity: Activity)
    fun getSamReader(): Reader?
    fun getContactlessIsoProtocol(): PoReaderProtocol?
    fun getContactlessMifareProtocol(): PoReaderProtocol?
    fun getSamReaderProtocol(): String
    fun onDestroy()

    fun isMockedResponse(): Boolean = false
}

data class PoReaderProtocol(val readerProtocolName: String, val applicationProtocolName: String)
