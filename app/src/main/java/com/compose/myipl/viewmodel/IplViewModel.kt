package com.compose.myipl.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import com.compose.myipl.data.IplTeam
import com.compose.myipl.data.SortType
import com.compose.myipl.utils.LogUtils

class IplViewModel :ViewModel() {
    private val  mTag="IplViewModel"
    private var mMutableIplList = mutableStateListOf<IplTeam>()
    private var mImgBoarderState = mutableStateOf(false)
    private var mActiveSortType = mutableStateOf(SortType.NAME)

    init {
        //Default Sort Type would be Team Name sorting
        mActiveSortType.value = SortType.NAME
        LogUtils.i(mTag,"Init Default Sort type when app lunch ${mActiveSortType.value }")
    }


    /*init {
        mMutableIplList.clear()
    }*/

    /*fun setIplList(list:ArrayList<IplTeam>){
        mMutableIplList.value = list
    }*/

    fun setSortType(sortType: SortType){
        mActiveSortType.value = sortType
    }

    fun getSortType(): SortType{
        return mActiveSortType.value
    }

    fun addTeam(iplTeam : IplTeam){
        mMutableIplList.add(iplTeam)
    }

    fun performSorting(sortType: SortType, isAsc: Boolean){

        when(sortType){

            SortType.NAME->{
                val list =mMutableIplList.sortedWith(Comparator { t1, t2 ->
                    if(isAsc){
                        t1.teamName.compareTo(t2.teamName)
                    }else{
                        t2.teamName.compareTo(t1.teamName)
                    }
                })
                addAll(list.toMutableList())

            }
            SortType.WON->{
                val list =mMutableIplList.sortedWith(Comparator { t1, t2 ->
                    if(isAsc){
                        t1.matchWin.compareTo(t2.matchWin)
                    }else{
                        t2.matchWin.compareTo(t1.matchWin)
                    }
                })
                addAll(list.toMutableList())
            }
            SortType.LOST->{
                val list =mMutableIplList.sortedWith(Comparator { t1, t2 ->
                    if(isAsc){
                        t1.matchLoss.compareTo(t2.matchLoss)
                    }else{
                        t2.matchLoss.compareTo(t1.matchLoss)
                    }
                })
                addAll(list.toMutableList())

            }
            else -> {

            }
        }

    }

    fun addAll(listItem: ArrayList<IplTeam>){
        mMutableIplList.clear()
        mMutableIplList.addAll(listItem)
    }

    private fun addAll(listItem: MutableList<IplTeam>){
        mMutableIplList.clear()
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

    fun getImageBoarder(): Boolean{
        return mImgBoarderState.value
    }

    fun setImageBoarder(isBoarder: Boolean){
        mImgBoarderState.value = isBoarder
    }

}