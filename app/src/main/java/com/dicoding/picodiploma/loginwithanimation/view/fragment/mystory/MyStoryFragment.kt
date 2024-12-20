package com.dicoding.picodiploma.loginwithanimation.view.fragment.mystory

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        // Inflate the layout for this fragment
        val binding = inflater.inflate(R.layout.fragment_my_story, container, false)

        // Set up the button to navigate to UploadStoryActivity
        val uploadStoryButton: View = binding.findViewById(R.id.uploadStoryButton)
        uploadStoryButton.setOnClickListener {
            val intent = Intent(activity, UploadStoryActivity::class.java)
            startActivity(intent)
        }

        return binding
    }
}
