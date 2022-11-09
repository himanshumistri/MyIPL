package com.compose.myipl.viewmodel

import androidx.compose.runtime.mutableStateListOf

import androidx.lifecycle.ViewModel
import com.compose.myipl.data.IplTeam

class IplViewModel :ViewModel() {
    private var mMutableIplList = mutableStateListOf<IplTeam>()
    /*init {
        mMutableIplList.clear()
    }*/

    /*fun setIplList(list:ArrayList<IplTeam>){
        mMutableIplList.value = list
    }*/

    fun addTeam(iplTeam : IplTeam){
        mMutableIplList.add(iplTeam)
    }

    fun addAll(listItem: ArrayList<IplTeam>){
        mMutableIplList.addAll(listItem)
    }

    fun isEmpty() : Boolean{
       return mMutableIplList.isEmpty()
    }
   /* fun getList(): androidx.compose.runtime.mutableStateListOf<IplTeam> {
        return mMutableIplList
    }*/

    fun getList() : List<IplTeam>{
        return mMutableIplList.toList()
    }

    fun getIplList():MutableListIterator<IplTeam>{
        return  mMutableIplList.listIterator()
    }

}