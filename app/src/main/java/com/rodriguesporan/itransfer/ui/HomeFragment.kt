package com.rodriguesporan.itransfer.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.rodriguesporan.itransfer.R
import com.rodriguesporan.itransfer.data.AppViewModel
import com.rodriguesporan.itransfer.databinding.FragmentHomeBinding
import com.rodriguesporan.itransfer.sendNotification

class HomeFragment : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()
    private var notificationManager: NotificationManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            lifecycleOwner = this@HomeFragment
            homeFragment = this@HomeFragment
            appViewModel = viewModel
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        createChannel(
                requireContext().getString(R.string.itransfer_notification_channel_id),
                requireContext().getString(R.string.itransfer_notification_channel_name)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationManager?.sendNotification(
                requireContext().getString(R.string.notification_text_payment_received),
                requireContext()
        )
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "ITransfer notification description"

            notificationManager = requireContext().getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    fun goToScanner() = startActivity(Intent(requireContext(), ScanActivity::class.java))
}