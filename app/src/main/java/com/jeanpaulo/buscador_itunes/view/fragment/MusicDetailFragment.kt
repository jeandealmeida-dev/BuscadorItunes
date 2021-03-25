package com.jeanpaulo.buscador_itunes.view.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeanpaulo.buscador_itunes.R

/**
 * A placeholder fragment containing a simple view.
 */
class CollectionActivityFragment : Fragment() {

    lateinit var fragmentView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentView = inflater.inflate(R.layout.fragment_collection, container, false)

        return fragmentView
    }
}
