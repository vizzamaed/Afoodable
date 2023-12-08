package com.example.afoodable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.afoodable.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    //NA ADD BEH
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        //NA ADD BEH
        firebaseDatabase=FirebaseDatabase.getInstance()
        databaseReference=firebaseDatabase.reference.child("Users")
        //
        binding.signupButton.setOnClickListener{
            val fullname = binding.signupFullName.text.toString()
            val username = binding.signupUserName.text.toString()
            val phone = binding.signupPhone.text.toString()
            val address = binding.signupAddress.text.toString()
            val email = binding.signupEmail.text.toString()
            val password =binding.signupPassword.text.toString()
            val confirmPassword =binding.signupConfirm.text.toString()

            if(fullname.isNotEmpty()&&username.isNotEmpty()&&phone.isNotEmpty()&&address.isNotEmpty()&&email.isNotEmpty()&& password.isNotEmpty()&&confirmPassword.isNotEmpty()){
                if(password==confirmPassword){

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){

                            //NA ADD BEH
                            signupUser(fullname,username,phone,address,email,password)
                        }else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show()
                }


            }else{
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, LogInActivity::class.java)
            startActivity(loginIntent)
        }
    }
    //NA ADD BEHH
    private fun signupUser(fullname: String, username: String, phone: String, address: String, email: String, password: String) {
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userData = UserData(userId, fullname, username, phone, address, email, password)
            databaseReference.child(userId).setValue(userData)

            Toast.makeText(this@SignUpActivity, "Signup Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@SignUpActivity, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }


}

