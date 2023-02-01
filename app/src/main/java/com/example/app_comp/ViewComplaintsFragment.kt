package com.example.app_comp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_comp.databinding.FragmentViewComplaintsBinding
import androidx.lifecycle.Observer

class ViewComplaintsFragment : Fragment() {

    private lateinit var binding: FragmentViewComplaintsBinding
    private val viewModel: UserViewModel by viewModels()
    private val adapter = ComplaintAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = FragmentViewComplaintsBinding.inflate(layoutInflater)
        binding = FragmentViewComplaintsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.complaintsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ViewComplaintsFragment.adapter
        }

        viewModel.getComplaints(config.userId).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success<List<Complaint>> -> {
                    adapter.complaints = result.data
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}
