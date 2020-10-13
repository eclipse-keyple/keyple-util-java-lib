package org.eclipse.keyple.famoco.validator.reader

import android.app.Activity
import org.eclipse.keyple.core.seproxy.SeReader
import org.eclipse.keyple.core.seproxy.exception.KeypleException

/**
 *
 *  created on 21/09/2020
 *
 *  @author youssefamrani
 */

interface IReaderRepository {

    var poReader: SeReader?
    var samReaders: MutableMap<String, SeReader>

    @Throws(KeypleException::class)
    fun registerPlugin()

    @Throws(KeypleException::class)
    suspend fun initPoReader(): SeReader?

    @Throws(KeypleException::class)
    suspend fun initSamReaders(): Map<String, SeReader>

    fun setSamParameters(samReader: SeReader)

    fun enableNfcReaderMode(activity: Activity)
    fun disableNfcReaderMode(activity: Activity)
    fun getSamReader(): SeReader?
    fun onDestroy()
}