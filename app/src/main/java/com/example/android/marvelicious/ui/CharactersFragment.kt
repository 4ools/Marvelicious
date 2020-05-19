package com.example.android.marvelicious.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.marvelicious.R
import com.example.android.marvelicious.data.Result
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

    private lateinit var adapter: CharactersDataAdapter
    private lateinit var binding: FragmentCharactersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_characters,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = charactersViewModel

        adapter = CharactersDataAdapter(CharacterClick {
            Toast.makeText(activity, "character called ${it.name}", Toast.LENGTH_SHORT).show()
        }, NetworkStateViewHolder.OnClick {
            charactersViewModel.refresh()
        })

        binding.charactersList.adapter = adapter
        binding.charactersList.layoutManager = LinearLayoutManager(context)

        binding.swipeRefresh.setOnRefreshListener {
            charactersViewModel.refresh()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        charactersViewModel.characters.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            adapter.setResultState(Result.Success(it))
        })

        charactersViewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                adapter.setResultState(Result.Loading)
            }
        })

        charactersViewModel.result.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Result.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        Timber.d("result is success ${it.data}")
                        adapter.setResultState(it)
                    }
                    is Result.Loading -> {
                        Timber.d("result is loading $it")
                        binding.swipeRefresh.isRefreshing = true
                        adapter.setResultState(it)
                    }
                    is Result.Error -> {
                        Timber.d("result is error $it")
                        adapter.setResultState(it)
                    }
                }
            })
    }
}