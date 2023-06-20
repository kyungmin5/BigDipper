package com.example.bigdipper

import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bigdipper.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ProfileFragment : Fragment() {
    lateinit var binding :FragmentProfileBinding
    val userManager = UserDataManager.getInstance()
    val CurUserData = userManager.getUserData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.writingBtn.setOnClickListener {
            val exploreFragment = ExploreFragment()
            (activity as MainActivity).replaceFragment(HomeFragment())
            (activity as MainActivity).binding.mainBottomNavigation.menu.findItem(R.id.home).isChecked = true
        }
        Log.i("asdasdadsasd",CurUserData.toString())
        init()
        return binding.root
    }

    private fun init(){
        binding.apply {
            Log.i("asdasd",CurUserData.toString())
            handle.text=CurUserData!!.NickName
            intro.text=CurUserData!!.explain
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}