package com.compose.myipl.data

import com.google.gson.annotations.SerializedName

class IplReadData {

    @SerializedName("list")
    var mArrayList : ArrayList<IplTeam> ?= null
}