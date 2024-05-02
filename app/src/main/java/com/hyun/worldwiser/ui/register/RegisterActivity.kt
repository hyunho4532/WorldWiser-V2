package com.hyun.worldwiser.ui.register

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hyun.worldwiser.R
import com.hyun.worldwiser.databinding.ActivityRegisterBinding
import com.hyun.worldwiser.ui.login.LoginActivity
import com.hyun.worldwiser.ui.register.verification.VerificationActivity
import com.hyun.worldwiser.util.IntentFilter
import com.hyun.worldwiser.util.SnackBarFilter
import com.hyun.worldwiser.viewmodel.AuthRegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding

    private val intentFilter: IntentFilter = IntentFilter()
    private val snackBarFilter: SnackBarFilter = SnackBarFilter()

    private lateinit var context: Context

    private val verificationActivity: VerificationActivity = VerificationActivity()
    private val loginActivity: LoginActivity = LoginActivity()

    private lateinit var authRegisterViewModel: AuthRegisterViewModel

    private var onTapClient: SignInClient? = null
    private lateinit var signInRequest: BeginSignInRequest

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        authRegisterViewModel = ViewModelProvider(this)[AuthRegisterViewModel::class.java]

        context = applicationContext

        registerBinding.btnRegisterInsert.setOnClickListener {
            val email = registerBinding.etEmailFormField.text.toString()
            val password = registerBinding.etPasswordFormField.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일 또는 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                authRegisterViewModel.registerUsers(email, password)
            }
        }

        registerBinding.loginToMove.setOnClickListener {
            intentFilter.getIntent(context, loginActivity)
        }

        authRegisterViewModel.registerResult.observe(this) { success ->
            if (success) {
                snackBarFilter.getEmailInsertSnackBar(registerBinding.root)
                intentFilter.getIntent(context, verificationActivity)
            } else {
                snackBarFilter.getEmailNotInsertSnackBar(registerBinding.root)
            }
        }

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions (
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()


        val activityResultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    try {
                        val credential = onTapClient!!.getSignInCredentialFromIntent(result.data)
                        val idToken = credential.googleIdToken

                        if (idToken != null) {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                            auth.signInWithCredential(firebaseCredential).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this, "성공!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "실패!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } catch (e: ApiException) {
                        e.printStackTrace()
                    }
                }
            }
    }
}