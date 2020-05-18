package com.example.android.marvelicious.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.android.marvelicious.R
import com.example.android.marvelicious.databinding.FragmentCharactersBinding
import timber.log.Timber

class CharactersFragment : Fragment() {

    private val charactersViewModel: CharactersViewModel by lazy {
        ViewModelProvider(
            this, CharactersViewModel.Factory(
                requireActivity().application
            )
        ).get(CharactersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCharactersBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_characters,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        charactersViewModel.characters.observe(viewLifecycleOwner, Observer {
            Timber.i("returned the character ${it.size}")
        })
    }
}