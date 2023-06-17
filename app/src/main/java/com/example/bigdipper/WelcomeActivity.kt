package com.example.bigdipper


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.bigdipper.databinding.ActivityWelcomeBinding
import com.google.android.gms.auth.api.Auth

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.math.log

@Suppress("DEPRECATION")
class WelcomeActivity : AppCompatActivity() {
    private var auth : FirebaseAuth? = null
    private var googleSignInClient : GoogleSignInClient? = null
    private var GOOGLE_LOGIN_CODE = 9001
    lateinit var binding: ActivityWelcomeBinding

    private var welcomeTitleList = mutableListOf<String>("내가 읽고 싶었던 책을\n" +
            "다른 사람들과 함께","북클럽에 가장\n" +
            "최적화된 플랫폼", "북두칠성은 당신의\n" +
            "완독메이트")
    private var welcomeDescriptionList = mutableListOf<String>("나와 비슷한 관심사를 가진 사람들과 함께,\n" +
            "그동안 읽고 싶었던 책을 함께 읽어요.",  "공지사항, 인증, 글 작성을 한 곳에서!\n이젠 책 읽고 생각을 나누는데만 집중하세요.",
        "완독하고 북클럽에 참여할 때마다 성장하는 별을 보며 자신이 얼마나 발전했는지 알 수 있어요 :)")
    private var welcomeAnimationList = mutableListOf<Int>(R.raw.welcome_guide_img1, R.raw.welcome_guide_img2, R.raw.welcome_guide_img3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id2))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
    }

    fun moveMainPage(user: FirebaseUser?){
        if( user!= null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun firebaseAuthWithGoogle(account : GoogleSignInAccount?){
        var credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    // 아이디, 비밀번호 맞을 때
                    moveMainPage(task.result?.user)
                }else{
                    // 틀렸을 때
                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            // 구글API가 넘겨주는 값 받아옴
            Log.i("dd",task.toString());
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
                Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
            } catch (e: ApiException) {
                Toast.makeText(this, "실패ㅋ: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    fun googleLogin(){
        var signInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(signInIntent,GOOGLE_LOGIN_CODE)
    }




    fun initLayout(){
        binding.apply{
            welcomeGuideView.adapter = ViewPagerAdapter(welcomeTitleList, welcomeDescriptionList, welcomeAnimationList)
            welcomeGuideView.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            welcomePageIndicator.setViewPager(welcomeGuideView)
            welcomeContinueBtn.setOnClickListener {
                welcomeGuideView.apply{
                    beginFakeDrag()
                    for(i in 1..3){
                        fakeDragBy(-10f)
                        fakeDragBy(-10f)
                        fakeDragBy(-10f)
                    }
                    endFakeDrag()
                }
            }
            loginBtn.setOnClickListener {
                googleLogin()
            }
        }

    }
}