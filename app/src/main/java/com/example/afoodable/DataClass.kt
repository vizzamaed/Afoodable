package com.example.afoodable

class DataClass {
    var productID: String? = null
    var dataItemName: String?=null
    var dataItemDescription: String?=null
    var dataItemPrice: String?=null
    var dataImage: String?=null
    var businessName: String? = null
    var businessLocation: String? = null
    var sellerID: String? = null



    constructor(dataItemName:String?, dataItemDescription:String?,dataItemPrice:String?,dataImage:String?) {
        this.dataItemName = dataItemName
        this.dataItemDescription = dataItemDescription
        this.dataItemPrice = dataItemPrice
        this.dataImage = dataImage

    }

    constructor(){

    }



}