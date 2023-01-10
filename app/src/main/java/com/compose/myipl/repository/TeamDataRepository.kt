package com.compose.myipl.repository


import androidx.lifecycle.MutableLiveData
import com.compose.myipl.data.IplTeam

interface TeamDataRepository {

    suspend fun getTeamList(): MutableLiveData<ArrayList<IplTeam>>

}