package com.noplans.blockchain.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.noplans.blockchain.Block
import com.noplans.blockchain.databinding.ItemBlockBinding

class BlockAdapter : ListAdapter<Block, BlockAdapter.BlockViewHolder>(BlockDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val binding = ItemBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BlockViewHolder(private val binding: ItemBlockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(block: Block) {
            binding.blockIndexText.text = "Block #${block.index}"
            binding.blockHashText.text = "Hash: ${block.hash.take(16)}..."
            binding.blockNonceText.text = "Nonce: ${block.nonce}"
            binding.blockTransactionsCount.text = "Transactions: ${block.transactions.size}"
        }
    }

    private class BlockDiffCallback : DiffUtil.ItemCallback<Block>() {
        override fun areItemsTheSame(oldItem: Block, newItem: Block) = oldItem.index == newItem.index
        override fun areContentsTheSame(oldItem: Block, newItem: Block) = oldItem == newItem
    }
}
