package com.example.mytesting

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytesting.databinding.RecyclerAdaptorBinding

class RecycleViewAdaptorFragment() : Fragment() {
    private var _binding: RecyclerAdaptorBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = RecyclerAdaptorBinding.inflate(inflater,container,false)
        val view = binding.root
        val recycler = binding.rvBluetooths
        recycler.layoutManager = LinearLayoutManager(activity)
        val deviceArray: Array<BluetoothDevice> = arguments?.getParcelableArray("bluetoothlist") as Array<BluetoothDevice>
        val deviceList  = deviceArray.toList()
        recycler.adapter = BlueToothAdapter(deviceList)

//        return inflater.inflate(R.layout.recycler_adaptor,container,false)
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object{
        fun newInstance(deviceList: List<BluetoothDevice>): RecycleViewAdaptorFragment {
            return RecycleViewAdaptorFragment().apply{
                arguments=Bundle().apply {
                    putParcelableArray("bluetoothlist",deviceList.toTypedArray())
                }
            }
        }
    }
}