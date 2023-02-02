package com.example.app_comp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_comp.databinding.FragmentViewComplaintsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ViewComplaintsFragment : Fragment() {

    private lateinit var binding: FragmentViewComplaintsBinding
    private val viewModel: UserViewModel by viewModels()
    private val adapter = ComplaintAdapter(emptyList())

    class ComplaintDiffCallback : DiffUtil.ItemCallback<Complaint>() {
        override fun areItemsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewComplaintsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.complaintsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ViewComplaintsFragment.adapter
        }

        viewModel.getComplaints(config.userId)
            .flowOn(Dispatchers.Main)
            .onEach { result ->
                when (result) {
                    is Result.Success<List<Complaint>> -> {
                        val diffResult = DiffUtil.calculateDiff(ComplaintDiffCallback(adapter.complaints, result.data))
                        //val diffResult = DiffUtil.calculateDiff(ComplaintDiffCallback(), adapter.complaints, result.data)
                        //val diffResult = DiffUtil.calculateDiff(ComplaintDiffCallback(), result.data)
                        adapter.complaints = result.data
                        diffResult.dispatchUpdatesTo(adapter)
                    }
                    is Result.Loading -> {
                        // Show loading indicator
                    }
                    is Result.Failed -> {
                        showToast("failed pri view complaints")
                    }
                    is Result.Error -> {
                        showToast("error pri view complaints")
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

    }
}
