package com.noplans.blockchain

import java.security.MessageDigest

data class Transaction(
    val sender: String,
    val receiver: String,
    val amount: Double
)

data class Block(
    val index: Int,
    val timestamp: Long,
    val transactions: List<Transaction>,
    val previousHash: String,
    var nonce: Int = 0
) {
    val hash: String
        get() = calculateHash()

    private fun calculateHash(): String {
        val input = "$index$timestamp$transactions$previousHash$nonce"
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
