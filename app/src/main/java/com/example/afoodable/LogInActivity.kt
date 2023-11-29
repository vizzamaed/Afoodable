package com.example.afoodable

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.afoodable.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    //NA ADD
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password= binding.loginPassword.text.toString()

            //NA ADD
            firebaseDatabase=FirebaseDatabase.getInstance()
            databaseReference=firebaseDatabase.reference.child("Users")

            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        //NA ADD
                        loginUser(email,password)

                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Fields cannot be empty", Toast.LENGTH_SHORT).show()
            }

        }

        binding.forgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view=layoutInflater.inflate(R.layout.dialog_forgot,null)
            val userEmail=view.findViewById<EditText>(R.id.editBox)

            builder.setView(view)
            val dialog=builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }

        binding.signupRedirectText.setOnClickListener {
            val signupIntent= Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)
        }
    }

    //Outside onCreate
    private fun compareEmail(email: EditText){
        if(email.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Check your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loginUser(email: String,password: String){
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData != null && userData.password == password) {
                                Toast.makeText(
                                    this@LogInActivity,
                                    "Login Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                                finish()
                                return
                        }
                    }
                }
                Toast.makeText(this@LogInActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LogInActivity, "Database Error:${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}