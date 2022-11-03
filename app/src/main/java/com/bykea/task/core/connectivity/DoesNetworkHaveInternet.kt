package com.bykea.task.core.connectivity

import com.bykea.task.utils.LoggerUtil
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Send a ping to google's primary DNS.
 * If successful, that means we have internet.
 */
object DoesNetworkHaveInternet {

    // Make sure to execute this on a background thread.
    fun execute(): Boolean {
        return try {
            LoggerUtil.debug(TAG, "PINGING google.")
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            LoggerUtil.debug(TAG, "PING success.")
            true
        } catch (e: IOException) {
            LoggerUtil.debug(TAG, "No internet connection. $e")
            false
        }
    }
}