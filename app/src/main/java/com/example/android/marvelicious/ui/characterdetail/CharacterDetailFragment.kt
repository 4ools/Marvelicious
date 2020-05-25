package com.example.android.marvelicious.ui.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.data.Status
import com.example.android.marvelicious.databinding.FragmentCharacterDetailBinding
import timber.log.Timber

class CharacterDetailFragment : Fragment() {
    private val args: CharacterDetailFragmentArgs by navArgs()

    private val charactersViewModel: CharacterDetailViewModel by lazy {
        ViewModelProvider(
            this,
            CharacterDetailViewModel.Factory(
                requireActivity().application,
                args.characterId
            )
        ).get(CharacterDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCharacterDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        charactersViewModel.character.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                Timber.d("So here i am with ${it.name}")
            }
            binding.character = it
        })

        charactersViewModel.networkState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.RUNNING -> {
                    binding.textViewNetwork.text = it.status.name
                }
                Status.SUCCESS -> {
                    binding.textViewNetwork.text = it.status.name
                }
                Status.FAILED -> {
                    binding.textViewNetwork.text = it.message
                }
            }
        })

        return binding.root
    }
}