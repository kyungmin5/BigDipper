package com.example.bigdipper

import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bigdipper.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    var binding :FragmentProfileBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.writingBtn.setOnClickListener {
            val exploreFragment = ExploreFragment()
            (activity as MainActivity).replaceFragment(HomeFragment())
            (activity as MainActivity).binding.mainBottomNavigation.menu.findItem(R.id.home).isChecked = true
        }

        return binding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}