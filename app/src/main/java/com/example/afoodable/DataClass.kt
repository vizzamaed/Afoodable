package com.example.afoodable

class DataClass {
    var dataItemName: String?=null
    var dataItemDescription: String?=null
    var dataItemPrice: String?=null
    var dataImage: String?=null


    constructor(dataItemName:String?, dataItemDescription:String?,dataItemPrice:String?,dataImage:String?) {
        this.dataItemName = dataItemName
        this.dataItemDescription = dataItemDescription
        this.dataItemPrice = dataItemPrice
        this.dataImage = dataImage

    }

    constructor(){

    }



}