package com.noplans.blockchain.network

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import com.google.gson.Gson
import com.noplans.blockchain.Block
import com.noplans.blockchain.Transaction
import timber.log.Timber

class P2PNode(port: Int = 8080) {
    private val server = object : WebSocketServer(InetSocketAddress(port)) {
        override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
            Timber.d("Client connected: ${conn.remoteSocketAddress}")
        }

        override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
            Timber.d("Client disconnected: ${conn.remoteSocketAddress}")
        }

        override fun onMessage(conn: WebSocket, message: String) {
            Timber.d("Message received: $message")
        }

        override fun onError(conn: WebSocket?, ex: Exception) {
            Timber.e(ex, "WebSocket error")
        }

        override fun onStart() {
            Timber.d("P2P Server started on port: $port")
        }
    }

    private val gson = Gson()

    fun start() {
        try {
            server.start()
            Timber.d("P2P Node started successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to start P2P Node")
        }
    }

    fun stop() {
        try {
            server.stop()
            Timber.d("P2P Node stopped")
        } catch (e: Exception) {
            Timber.e(e, "Failed to stop P2P Node")
        }
    }

    fun broadcastBlock(block: Block) {
        val message = gson.toJson(mapOf("type" to "block", "data" to block))
        broadcast(message)
        Timber.d("Block broadcasted")
    }

    fun broadcastTransaction(transaction: Transaction) {
        val message = gson.toJson(mapOf("type" to "transaction", "data" to transaction))
        broadcast(message)
        Timber.d("Transaction broadcasted")
    }

    private fun broadcast(message: String) {
        server.broadcast(message)
    }
}
