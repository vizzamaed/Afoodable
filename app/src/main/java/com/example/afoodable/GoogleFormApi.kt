package com.example.afoodable

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleFormApi {
    @FormUrlEncoded
    @POST("/forms/d/e/1FAIpQLSdb2yRVnkFYXjOtTKsPN1oUIhepBFZmPD1hIqv8ax7gBbVOdA/formResponse")
    fun sendFormData(
        @Field("entry.1027048632") sEmail: String,
        @Field("entry.339678722") sFirstName: String,
        @Field("entry.72792599") sLastName: String,
        @Field("entry.1101414231") sBusinessName: String,
        @Field("entry.587924615") sBusinessLocation: String,
        @Field("entry.2007829283") sTinNumber: String,
        @Field("entry.1657170388") sDtiNumber: String,
        @Field("entry.439072922") sBfarNumber: String,
        // if you have more questions you can add more here
    ): Call<Void>
}