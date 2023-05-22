package com.example.app_comp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_comp.data.FileData
import com.example.app_comp.databinding.ItemFileBinding

class FileListAdapter(private var files: List<FileData>): RecyclerView.Adapter<FileListAdapter.FileViewHolder>() {

    inner class FileViewHolder(val binding: ItemFileBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFileBinding.inflate(layoutInflater, parent, false)
        return FileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return files.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.binding.apply {
            tvFileName.text = files[position].fileName
            tvFileSize.text = files[position].fileSize
        }
    }

    fun setData(files: List<FileData>) {
        this.files = files
        notifyDataSetChanged()
    }
}
