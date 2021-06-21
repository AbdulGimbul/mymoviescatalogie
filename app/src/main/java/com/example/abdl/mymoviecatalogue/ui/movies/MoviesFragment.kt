package com.example.abdl.mymoviecatalogue.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abdl.mymoviecatalogue.databinding.FragmentMoviesBinding
import com.example.abdl.mymoviecatalogue.viewmodel.ViewModelFactory
import com.example.abdl.mymoviecatalogue.vo.Status

class MoviesFragment : Fragment(){
    private lateinit var fragmentMoviesBinding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMoviesBinding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return fragmentMoviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){

            val factory = ViewModelFactory.getInstance(requireActivity())
            val viewModel = ViewModelProvider(this, factory)[MoviesViewModel::class.java]

            val moviesAdapter = MoviesAdapter()

            viewModel.getMovies().observe(viewLifecycleOwner, { movies ->
                if (movies != null){
                    when (movies.status){
                        Status.LOADING -> fragmentMoviesBinding?.progressBar?.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            fragmentMoviesBinding?.progressBar?.visibility = View.GONE
                            moviesAdapter.submitList(movies.data)
                        }
                        Status.ERROR -> {
                            fragmentMoviesBinding?.progressBar?.visibility = View.GONE
                            Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            with(fragmentMoviesBinding?.rvMovies){
                this?.layoutManager = LinearLayoutManager(context)
                this?.setHasFixedSize(true)
                this?.adapter = moviesAdapter
            }
        }
    }
}