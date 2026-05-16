package com.noplans.blockchain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.noplans.blockchain.databinding.ActivityMainBinding
import com.noplans.blockchain.ui.BlockAdapter
import com.noplans.blockchain.ui.TransactionAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: BlockchainViewModel
    private lateinit var blockAdapter: BlockAdapter
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(BlockchainViewModel::class.java)

        setupRecyclerViews()
        setupClickListeners()
        observeViewModel()

        Timber.d("MainActivity created")
    }

    private fun setupRecyclerViews() {
        blockAdapter = BlockAdapter()
        transactionAdapter = TransactionAdapter()

        binding.blockRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = blockAdapter
        }

        binding.transactionRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = transactionAdapter
        }
    }

    private fun setupClickListeners() {
        binding.addTransactionButton.setOnClickListener {
            val sender = binding.senderInput.text.toString()
            val receiver = binding.receiverInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull() ?: 0.0

            viewModel.addTransaction(sender, receiver, amount)
            clearTransactionInputs()
        }

        binding.mineButton.setOnClickListener {
            val minerAddress = binding.minerInput.text.toString()
            viewModel.minePendingTransactions(minerAddress)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                blockAdapter.submitList(state.blockchain)
                transactionAdapter.submitList(state.pendingTransactions)
                binding.chainStatusText.text = "Chain Valid: ${state.chainValid}"
                binding.blockCountText.text = "Total Blocks: ${state.blockCount}"
                binding.miningStatusText.text = if (state.isMining) "Mining..." else "Ready"
            }
        }
    }

    private fun clearTransactionInputs() {
        binding.senderInput.text?.clear()
        binding.receiverInput.text?.clear()
        binding.amountInput.text?.clear()
    }
}
