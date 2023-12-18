package com.example.afoodable

data class ProductsData (
    var productID: String? = null,
    var dataItemName: String?=null,
    var dataItemDescription: String?=null,
    var dataItemPrice: String?=null,
    var dataImage: String?=null,
    var businessName:String?= null,
    var businessLocation:String?= null,
    var sellerID: String? = null,
    //
    var orderID: String? = null
)