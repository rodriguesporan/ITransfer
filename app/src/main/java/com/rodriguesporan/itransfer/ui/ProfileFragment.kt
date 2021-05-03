package com.rodriguesporan.itransfer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rodriguesporan.itransfer.R

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val signOutButton = container?.findViewById<MaterialButton>(R.id.sign_out_button)
        signOutButton?.setOnClickListener {
            signOut()
        }

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun signOut() {
        Firebase.auth.signOut()
    }
}