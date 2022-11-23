package com.compose.myipl

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.compose.myipl.data.IplTeam
import com.compose.myipl.data.SortType
import com.compose.myipl.repository.TeamDataImpl
import com.compose.myipl.ui.theme.HeaderColor
import com.compose.myipl.ui.theme.MyIPLTheme
import com.compose.myipl.utils.AppConstants
import com.compose.myipl.viewmodel.IplViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author Himanshu.Mistri
 * https://www.t20worldcup.com/standings
 *
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity(),CoroutineScope {

    //private var mIplTeamList = ArrayList<IplTeam>()
    private var mIplViewModel: IplViewModel ? = null
    private var mHandler = Handler()
    private var mHeaderHeight=58.dp

    @Inject lateinit var mIplDataImpl: TeamDataImpl

    /**
     * Coroutines job
     */
    private var mJob = SupervisorJob()

    private lateinit var job: Job

    /**
     * Coroutine scope required for launch and this call be used to cancel all coroutine launch
     */
    private var mViewModelScope= CoroutineScope(Dispatchers.Main +mJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        val model: IplViewModel by viewModels()
        mIplViewModel = model
        setContent {
            MyIPLTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    IplList(mIplViewModel!!)
                }
            }
        }

        /*mHandler.postDelayed({
            if(mIplViewModel!!.isEmpty()){

                for (i in 0..10){

                    val teamName ="Team India $i"
                    val ipTeam = IplTeam(teamName = teamName)

                    mIplViewModel?.addTeam(ipTeam)

                }
               // mIplViewModel?.ad(mIplViewModel?.getIplList()!!)
            }
        },1000L)*/


       launch {
           val teamList = mIplDataImpl.getTeamList()
           mIplViewModel?.addAll(teamList)
        }

    }

    //@OptIn(ExperimentalFoundationApi::class, ExperimentalUnitApi::class)
    @Composable
    fun IplList(iplViewModel: IplViewModel){
        val mSortAscDscNameIs  = remember {
            mutableStateOf(AppConstants.SORT_ASC)
        }
        val mSortAscDscWonIs  = remember {
            mutableStateOf(AppConstants.SORT_ASC)
        }
        val mSortAscDscLostIs  = remember {
            mutableStateOf(AppConstants.SORT_ASC)
        }
        val mSortAscDscPlayedIs  = remember {
            mutableStateOf(AppConstants.SORT_ASC)
        }

        val isBoarder = iplViewModel.getImageBoarder()

        var imgModifier = Modifier
            .size(dimensionResource(id = R.dimen.img_width), 90.dp)

        if(isBoarder){
            imgModifier = imgModifier.border(
                width = 1.dp, color = colorResource(
                    id = R . color . black
                )
            )
        }

        mIplViewModel = iplViewModel
        //Check if IPLList is Empty
        if(iplViewModel.isEmpty()){
            Log.i("TAG","List is Empty")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                //Put your Compose View here to make it center in this Box
                Text(text = "No IPL Team available to show", color = Color.Magenta, fontFamily = FontFamily.Default, fontSize = 18.sp)
            }

        }else{
            
            Column(modifier = Modifier.fillMaxSize()) {

                MyIPLTheme() {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(mHeaderHeight)
                        .background(HeaderColor), verticalAlignment = Alignment.CenterVertically) {
                        Row(modifier = Modifier
                            .weight(0.4F)
                            .clickable(enabled = true) {
                                if (mSortAscDscNameIs.value == AppConstants.SORT_ASC) {
                                    mSortAscDscNameIs.value = AppConstants.SORT_DESC
                                    mIplViewModel?.performSorting(SortType.NAME, false)
                                } else {
                                    mSortAscDscNameIs.value = AppConstants.SORT_ASC
                                    mIplViewModel?.performSorting(SortType.NAME, true)
                                }
                            }, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(id = R.string.team), style = MaterialTheme.typography.h1, modifier = Modifier
                                .fillMaxWidth(0.7F)
                                .padding(start = 10.dp), fontWeight = FontWeight.Bold)
                            Image(painter = if(mSortAscDscNameIs.value == AppConstants.SORT_ASC) {
                                painterResource(id = R.drawable.sort_by_alpha)
                            }else{
                                painterResource(id = R.drawable.sort_by_alpha)
                                 }, contentDescription = mSortAscDscNameIs.value )
                        }
                        
                        Row(modifier = Modifier.weight(0.15F)) {
                            Text(text = stringResource(id = R.string.played), style = MaterialTheme.typography.h1, textAlign = TextAlign.Center, modifier = Modifier.background(color = Color.Magenta))
                        }
                        Row(modifier = Modifier.weight(0.25F),verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(id = R.string.won), style = MaterialTheme.typography.h1, modifier = Modifier.fillMaxWidth(0.7F))
                            Image(painter = if(mSortAscDscWonIs.value == AppConstants.SORT_ASC) {
                                painterResource(id = R.drawable.north)
                            }else{
                                painterResource(id = R.drawable.south)
                            }, contentDescription = mSortAscDscWonIs.value )
                        }
                        Row(modifier = Modifier.weight(0.25F),verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(id = R.string.lost), style = MaterialTheme.typography.h1,modifier = Modifier.fillMaxWidth(0.7F)
                                .background(color = Color.Magenta))
                            Image(painter = if(mSortAscDscLostIs.value == AppConstants.SORT_ASC) {
                                painterResource(id = R.drawable.north)
                            }else{
                                painterResource(id = R.drawable.south)
                            }, contentDescription = mSortAscDscLostIs.value )
                        }
                    }
                }
                LazyColumn(contentPadding = PaddingValues(start = 5.dp, end = 5.dp, bottom = 5.dp)){
                    items(iplViewModel.getList()) {  item: IplTeam ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White), elevation = 5.dp) {
                            //modifier = Modifier.background(color = Color.LightGray) Check Row Color
                            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                                //Column 1
                                Row(modifier = Modifier.weight(0.5F),verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(model = item.url,
                                        contentDescription =item.teamName, modifier = imgModifier , placeholder = painterResource(
                                            id = R.drawable.image_not_supported) )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(text = item.teamName, color = Color.Black, fontFamily = FontFamily.Serif, fontSize = 14.sp)
                                }
                                //Column 2
                                Row(modifier = Modifier.weight(0.10F)) {
                                    Text(text = item.matchPlayed.toString(), color = Color.Black, fontFamily = FontFamily.Serif, fontSize = 18.sp)
                                }
                                Row(modifier = Modifier.weight(0.20F)) {
                                    Text(text = item.matchWin.toString(), color = Color.Green, fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.weight(0.20F)) {
                                    Text(text = item.matchLoss.toString(), color = Color.Red, fontFamily = FontFamily.Serif, fontSize = 18.sp,fontWeight = FontWeight.Bold)
                                }

                            }


                        }

                    }
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() =  Dispatchers.Main + job


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}




@OptIn(ExperimentalUnitApi::class)
@Composable
fun CenterView(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        //Put your Compose View here to make it center in this Box
        Text(text = "I will in center of the view",
            color = Color.Magenta,
            fontFamily = FontFamily.Default,
            fontSize = TextUnit(18.0F,
            TextUnitType.Sp))
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyIPLTheme {
       CenterView()
    }
}