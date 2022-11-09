package com.compose.myipl.repository

import com.compose.myipl.data.IplTeam

interface TeamDataRepository {

    suspend fun getTeamList(): ArrayList<IplTeam>
}