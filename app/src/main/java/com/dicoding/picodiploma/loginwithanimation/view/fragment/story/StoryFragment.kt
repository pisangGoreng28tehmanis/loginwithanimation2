package com.dicoding.picodiploma.loginwithanimation.view.fragment.story

import StoryViewModel
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import kotlinx.coroutines.launch

class StoryFragment : Fragment() {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val sharedPref = requireContext().getSharedPreferences("USER_PREF", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        // Log token to ensure it's passed correctly
        Log.d("StoryFragment", "Token: $token")

        // Initialize ViewModel without launching a coroutine
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getStoryViewModelFactory(requireContext()) // Synchronously get ViewModelFactory
        ).get(StoryViewModel::class.java)

        // Set token to ViewModel
        viewModel.setToken(token)

        // Fetch stories after setting token
        viewModel.fetchStories()

        // Set up RecyclerView and adapter with an empty list initially
        binding.rvStories.layoutManager = LinearLayoutManager(requireContext())
        val adapter = StoryAdapter() // Create adapter instance with an empty list
        binding.rvStories.adapter = adapter // Set the adapter to RecyclerView

        // Observe stories
        observeStories(adapter) // Pass the adapter to the observer
    }

    private fun observeStories(adapter: StoryAdapter) {
        lifecycleScope.launch {
            viewModel.stories.collect { stories ->
                if (stories.isEmpty()) {
                    Log.d("StoryFragment", "No stories found.")
                    binding.tvNoStories.visibility = View.VISIBLE // Show no stories text
                } else {
                    binding.tvNoStories.visibility = View.GONE // Hide no stories text
                    // Update the adapter's data
                    adapter.updateList(stories) // Use updateList to update the list
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
