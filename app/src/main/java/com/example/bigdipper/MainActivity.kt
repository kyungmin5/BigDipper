package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.widget.ViewPager2
import com.example.bigdipper.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    val userManager = UserDataManager.getInstance() // 싱글톤 객체 가져오기

    // 유저 데이터 설정
    val userData = UserData("uid123", "강대훈", "자고싶다", 1, arrayListOf(), arrayListOf(), arrayListOf())
    val uid = userData.Uid
    val databaseReference = FirebaseDatabase.getInstance().reference.child("users")
    val userQuery = databaseReference.orderByChild("Uid").equalTo(uid)




    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initLayout()
        init()
    }

    private fun init(){
        userManager.setUserData(userData)
        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                } else {
                    // 동일한 UID가 없는 경우 새로운 사용자를 생성하고 데이터를 추가합니다.
                    databaseReference.child(uid).setValue(userData)
                        .addOnSuccessListener {
                            // 데이터 추가 성공
                        }
                        .addOnFailureListener { error ->
                            // 데이터 추가 실패
                        }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 쿼리 취소 또는 오류 처리
            }
        })
    }

    private fun initLayout(){
        replaceFragment(HomeFragment())
        binding.navigationProfileIcon.setOnClickListener {
            replaceFragment(ProfileFragment())
            binding.mainBottomNavigation.menu.findItem(R.id.profile).isChecked = true
        }
        binding.mainBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.explore -> replaceFragment(ExploreFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainView, fragment)
        fragmentTransaction.commit()
    }

}