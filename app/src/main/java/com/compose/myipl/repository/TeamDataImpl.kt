package com.compose.myipl.repository

import androidx.lifecycle.MutableLiveData
import com.compose.myipl.app.MyApp
import com.compose.myipl.data.IplReadData
import com.compose.myipl.data.IplTeam
import com.compose.myipl.utils.LogUtils
import com.google.gson.Gson
import kotlinx.coroutines.*
import javax.inject.Inject


class TeamDataImpl @Inject constructor(): TeamDataRepository{

    private var mTag ="TeamDataImpl"

    private val mMutableIplList= MutableLiveData<ArrayList<IplTeam>>()

    override suspend fun getTeamList(): MutableLiveData<ArrayList<IplTeam>> {
        var iplReadData : IplReadData ? = null
        withContext(Dispatchers.IO){
            val gson = Gson()
            val jsonResponse = MyApp.getAppObject()?.assets?.open("teamlist.json")?.bufferedReader().use { it!!.readText() }
            LogUtils.i(mTag,"JSON:: 1 Team is $jsonResponse Thread is ${Thread.currentThread()}")
            iplReadData = gson.fromJson(jsonResponse, IplReadData::class.java)
            mMutableIplList.postValue(iplReadData!!.mArrayList)
        }
        LogUtils.i(mTag,"JSON:: 2 Thread is ${Thread.currentThread()}")
        return  mMutableIplList
    }
}