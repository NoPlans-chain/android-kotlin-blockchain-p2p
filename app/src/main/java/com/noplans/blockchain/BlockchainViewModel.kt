package com.noplans.blockchain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

data class BlockchainUiState(
    val blockchain: List<Block> = emptyList(),
    val pendingTransactions: List<Transaction> = emptyList(),
    val chainValid: Boolean = true,
    val isMining: Boolean = false,
    val blockCount: Int = 1
)

class BlockchainViewModel : ViewModel() {
    private val blockchain = Blockchain()

    private val _uiState = MutableStateFlow(BlockchainUiState())
    val uiState: StateFlow<BlockchainUiState> = _uiState

    init {
        updateUI()
    }

    fun addTransaction(sender: String, receiver: String, amount: Double) {
        if (sender.isBlank() || receiver.isBlank() || amount <= 0) {
            Timber.w("Invalid transaction input")
            return
        }
        blockchain.addTransaction(sender, receiver, amount)
        updateUI()
        Timber.d("Transaction added: $sender -> $receiver: $amount")
    }

    fun minePendingTransactions(minerAddress: String) {
        if (minerAddress.isBlank()) {
            Timber.w("Invalid miner address")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isMining = true)
            try {
                blockchain.minePendingTransactions(minerAddress)
                updateUI()
                Timber.i("Block mined successfully")
            } catch (e: Exception) {
                Timber.e(e, "Mining failed")
            } finally {
                _uiState.value = _uiState.value.copy(isMining = false)
            }
        }
    }

    private fun updateUI() {
        val chain = blockchain.getChain()
        val isValid = blockchain.isChainValid()
        _uiState.value = BlockchainUiState(
            blockchain = chain,
            pendingTransactions = blockchain.pendingTransactions,
            chainValid = isValid,
            blockCount = chain.size
        )
    }
}
