package com.example.bigdipper

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import com.example.bigdipper.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*


class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private val TAG = "MainActivity"
    private val RC_SIGN_IN = 9001

    // 구글api클라이언트
    private var mGoogleSignInClient: GoogleSignInClient? = null

    // 구글 계정
    private var gsa: GoogleSignInAccount? = null

    // 파이어베이스 인증 객체 생성
    private var mAuth: FirebaseAuth? = null

    // 구글  로그인 버튼
    private var btnGoogleLogin: SignInButton? = null
    private var btnLogoutGoogle: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initLayout()

        // 파이어베이스 인증 객체 선언
        // 파이어베이스 인증 객체 선언
        mAuth = FirebaseAuth.getInstance()

        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출

        // Google 로그인을 앱에 통합
        // GoogleSignInOptions 개체를 구성할 때 requestIdToken을 호출
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        btnGoogleLogin = findViewById<SignInButton>(R.id.btn_google_sign_in)
        btnGoogleLogin.setOnClickListener(View.OnClickListener { view: View? ->
            // 기존에 로그인 했던 계정을 확인한다.
            gsa = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
            if (gsa != null) // 로그인 되있는 경우
                Toast.makeText(this@MainActivity, R.string.status_login, Toast.LENGTH_SHORT)
                    .show() else signIn()
        })

        //btnLogoutGoogle = findViewById<Button>(R.id.btn_logout_google)
        /*btnLogoutGoogle.setOnClickListener(View.OnClickListener { view: View? ->
            signOut() //로그아웃
        })*/
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

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    /* 사용자 정보 가져오기 */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acct: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            if (acct != null) {
                firebaseAuthWithGoogle(acct.idToken)
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri? = acct.photoUrl
                Log.d(TAG, "handleSignInResult:personName $personName")
                Log.d(TAG, "handleSignInResult:personGivenName $personGivenName")
                Log.d(TAG, "handleSignInResult:personEmail $personEmail")
                Log.d(TAG, "handleSignInResult:personId $personId")
                Log.d(TAG, "handleSignInResult:personFamilyName $personFamilyName")
                Log.d(TAG, "handleSignInResult:personPhoto $personPhoto")
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }


    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    //Toast.makeText(this@MainActivity, R.string., Toast.LENGTH_SHORT)
                        //.show()
                    val user = mAuth!!.currentUser
                    //                            updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //Toast.makeText(this@MainActivity, R.string.failed_login, Toast.LENGTH_SHORT)
                        //.show()
                    //                            updateUI(null);
                }
            }
    }

    private fun updateUI(user: FirebaseUser) {}

    /* 로그아웃 */
    private fun signOut() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) { task: Task<Void?>? ->
                mAuth!!.signOut()
                //Toast.makeText(this@MainActivity, R.string.success_logout, Toast.LENGTH_SHORT)
                    //.show()
            }
        gsa = null
    }

    /* 회원 삭제요청 */
    private fun revokeAccess() {
        mGoogleSignInClient!!.revokeAccess()
            .addOnCompleteListener(
                this
            ) { task: Task<Void?>? -> }
    }

}