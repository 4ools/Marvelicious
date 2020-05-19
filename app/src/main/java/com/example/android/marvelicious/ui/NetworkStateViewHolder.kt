package com.example.android.marvelicious.ui

import android.view.LayoutInflater
import android.view.View
import com.example.android.marvelicious.data.Result
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marvelicious.R
import com.example.android.marvelicious.databinding.NetworkStateItemBinding

class NetworkStateViewHolder(private val binding: NetworkStateItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): NetworkStateViewHolder {
            val dataBinding: NetworkStateItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.network_state_item,
                parent,
                false
            )
            return NetworkStateViewHolder(dataBinding)
        }
    }

    fun bind(
        result: Result<Any?>,
        retry: OnClick
    ) {
        binding.errorMsg.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        when (result) {
            is Result.Success -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is Result.Error -> {
                binding.retryButton.visibility = View.VISIBLE
                binding.errorMsg.text = result.exception.toString()
                binding.errorMsg.visibility = View.VISIBLE
            }

            is Result.Loading -> {

            }
        }
        binding.retryButton.setOnClickListener { retry.onClick() }
    }

    class OnClick(val retry: () -> Unit) {
        fun onClick() = retry()
    }

}