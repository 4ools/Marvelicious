package com.example.android.marvelicious.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.marvelicious.R
import com.example.android.marvelicious.databinding.FragmentCharactersBinding

class CharactersFragment : Fragment() {

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
}