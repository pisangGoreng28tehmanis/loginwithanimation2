package com.dicoding.picodiploma.loginwithanimation.view.fragment.mystory

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.picodiploma.loginwithanimation.R

class MyStoryFragment : Fragment() {

    companion object {
        fun newInstance() = MyStoryFragment()
    }

    private val viewModel: MyStoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_story, container, false)
    }
}