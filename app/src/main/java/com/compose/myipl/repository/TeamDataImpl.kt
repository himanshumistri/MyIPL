package com.compose.myipl.repository

import com.compose.myipl.app.MyApp
import com.compose.myipl.data.IplReadData
import com.compose.myipl.data.IplTeam
import com.compose.myipl.utils.LogUtils
import com.google.gson.Gson
import kotlinx.coroutines.*
import javax.inject.Inject


class TeamDataImpl @Inject constructor(): TeamDataRepository{

    private var mTag ="TeamDataImpl"
    /**
     * Coroutines job
     */
    private var mBackgroundJob = SupervisorJob()

    /**
     * Coroutine scope required for launch and this call be used to cancel all coroutine launch
     */
    private var mViewModelScope= CoroutineScope(Dispatchers.IO +mBackgroundJob)

    override suspend fun getTeamList(): ArrayList<IplTeam> {
        var iplReadData : IplReadData ? = null
        val work=  mViewModelScope.async {
                val gson = Gson()
                val jsonResponse = MyApp.getAppObject()?.assets?.open("teamlist.json")?.bufferedReader().use { it!!.readText() }
                LogUtils.i(mTag,"JSON:: 1 Team is $jsonResponse Thread is ${Thread.currentThread()}")
            iplReadData = gson.fromJson(jsonResponse, IplReadData::class.java)
            }
        work.await()
        LogUtils.i(mTag,"JSON:: 2 Thread is ${Thread.currentThread()}")
        return  iplReadData!!.mArrayList!!
    }
}