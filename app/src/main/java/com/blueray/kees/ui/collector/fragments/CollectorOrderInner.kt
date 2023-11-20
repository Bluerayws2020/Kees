package com.blueray.kees.ui.collector.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueray.kees.R
import com.blueray.kees.databinding.FragmentCollectorOrderInnerBinding


class CollectorOrderInner : Fragment() {

    private lateinit var  binding : FragmentCollectorOrderInnerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCollectorOrderInnerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the video source from the resources
        val videoPath = "android.resource://" + "com.blueray.kees" + "/" + R.raw.box_animation

        // Convert the path to a Uri
        val videoUri = Uri.parse(videoPath)

        // Set the Uri as the video source
        binding.videoView.setVideoURI(videoUri)

        // Start playing the video
        binding.videoView.start()
    }
}