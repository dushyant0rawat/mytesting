package com.example.mytesting

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytesting.databinding.RecyclerViewItemBinding

class BlueToothAdapter(
    val bluetooths: List<BluetoothDevice>) :
    RecyclerView.Adapter<BlueToothAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder( var itemBinding: RecyclerViewItemBinding ):
        RecyclerView.ViewHolder(itemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemsViewHolder(view);
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val bluetooth = bluetooths[position]
        holder.itemBinding.tvDevice.text = bluetooth?.name
        holder.itemBinding.tvHardware.text = bluetooth?.address

    }

    override fun getItemCount() = bluetooths.size
}