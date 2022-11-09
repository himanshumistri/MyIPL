package com.compose.myipl.data

import com.google.gson.annotations.SerializedName

data class IplTeam(@SerializedName("name") var teamName : String){

    var teamFlag :String = ""

    @SerializedName("win")
    var matchWin = 0

    @SerializedName("loss")
    var matchLoss = 0

    @SerializedName("url")
    var url : String =""
}
