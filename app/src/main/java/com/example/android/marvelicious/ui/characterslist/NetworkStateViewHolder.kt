package com.example.android.marvelicious.ui.characterslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.marvelicious.R
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.data.Status
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
            return NetworkStateViewHolder(
                dataBinding
            )
        }

        fun toVisibility(constrain: Boolean): Int {
            return if (constrain) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    fun bind(
        networkState: NetworkState?,
        retry: OnClick
    ) {
        binding.progressBar.visibility =
            toVisibility(
                networkState?.status == Status.RUNNING
            )
        binding.retryButton.visibility =
            toVisibility(
                networkState?.status == Status.FAILED
            )
        binding.errorMsg.visibility =
            toVisibility(
                networkState?.message != null
            )
        binding.errorMsg.text = networkState?.message
        binding.retryButton.setOnClickListener { retry.onClick() }
    }

    class OnClick(val retry: () -> Unit) {
        fun onClick() = retry()
    }

}