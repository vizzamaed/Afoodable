package com.example.afoodable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field

class SellerRegistration : AppCompatActivity() {
    private lateinit var maile: EditText
    private lateinit var nameFirst: EditText
    private lateinit var nameLast: EditText
    private lateinit var nameBusiness: EditText
    private lateinit var locationBusiness: EditText
    private lateinit var numberTIN: EditText
    private lateinit var numberDTI: EditText
    private lateinit var numberBFAR: EditText
    private lateinit var sellerRegistrationBtn: Button



    private val retrofit = Retrofit.Builder()
        .baseUrl("https://docs.google.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(GoogleFormApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_registration)
        init()

        sellerRegistrationBtn.setOnClickListener {
            val sFirstName = nameFirst.text.toString()
            val sLastName = nameLast.text.toString()
            val sEmail = maile.text.toString()
            val sBusinessName = nameBusiness.text.toString()
            val sBusinessLocation = locationBusiness.text.toString()
            val sTinNumber = numberTIN.text.toString()
            val sDtiNumber = numberDTI.text.toString()
            val sBfarNumber = numberBFAR.text.toString()

            val call = api.sendFormData(sEmail, sFirstName, sLastName, sBusinessName, sBusinessLocation, sTinNumber, sDtiNumber,sBfarNumber)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    try {
                        if (response.isSuccessful) {
                            Toast.makeText(this@SellerRegistration, "Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SellerRegistration,AccountFragment::class.java))
                        } else {
                            Toast.makeText(this@SellerRegistration, "Unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@SellerRegistration, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    try {
                        Toast.makeText(this@SellerRegistration, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@SellerRegistration, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    private fun init() {
        maile = findViewById(R.id.maile)
        nameFirst = findViewById(R.id.nameFirst)
        nameLast = findViewById(R.id.nameLast)
        nameBusiness = findViewById(R.id.nameBusiness)
        locationBusiness = findViewById(R.id.locationBusiness)
        numberTIN = findViewById(R.id.numberTIN)
        numberDTI = findViewById(R.id.numberDTI)
        numberBFAR = findViewById(R.id.numberBFAR)
        sellerRegistrationBtn = findViewById(R.id.sellerRegistrationBtn)
    }

}