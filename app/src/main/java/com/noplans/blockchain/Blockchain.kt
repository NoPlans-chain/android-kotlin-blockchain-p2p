package com.noplans.blockchain

class Blockchain {
    private val chain = mutableListOf<Block>()
    val pendingTransactions = mutableListOf<Transaction>()

    init {
        chain.add(createGenesisBlock())
    }

    private fun createGenesisBlock(): Block = Block(0, System.currentTimeMillis(), emptyList(), "0")

    fun addTransaction(sender: String, receiver: String, amount: Double) {
        pendingTransactions.add(Transaction(sender, receiver, amount))
    }

    fun minePendingTransactions(minerRewardAddress: String) {
        val rewardTx = Transaction("System", minerRewardAddress, 100.0)
        val transactions = pendingTransactions + rewardTx

        val previousBlock = chain.last()
        var newBlock = Block(
            index = chain.size,
            timestamp = System.currentTimeMillis(),
            transactions = transactions,
            previousHash = previousBlock.hash
        )

        // Simple Proof of Work
        println("Mining block...")
        while (newBlock.hash.substring(0, 4) != "0000") {
            newBlock = newBlock.copy(nonce = newBlock.nonce + 1)
        }

        chain.add(newBlock)
        pendingTransactions.clear()
        println("Block mined: ${newBlock.hash}")
    }

    fun isChainValid(): Boolean {
        for (i in 1 until chain.size) {
            val current = chain[i]
            val previous = chain[i-1]

            if (current.hash != current.calculateHash() || current.previousHash != previous.hash) {
                return false
            }
        }
        return true
    }

    fun getChain(): List<Block> = chain.toList()
}
