package com.compose.myipl

import android.os.Bundle
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
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.Observer
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

    private var mIplViewModel: IplViewModel ? = null
    private var mHeaderHeight=58.dp
    private var mSpaceTxtImg = 20.dp

    @Inject lateinit var mIplDataImpl: TeamDataImpl

    private lateinit var job: Job


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
                    color = MaterialTheme.colorScheme.background
                ) {
                    IplList(mIplViewModel!!)
                }
            }
        }

       val dataObserver = Observer<ArrayList<IplTeam>>{
           mIplViewModel?.addAll(it)
           mIplViewModel?.performSorting(mIplViewModel!!.getSortType(),mIplViewModel!!.getAscSort())
       }
       launch {
           mIplDataImpl.getTeamList().observe(this@MainActivity,dataObserver)
        }
    }

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
                Text(text = stringResource(id = R.string.show_no_teams), color = Color.Magenta, fontFamily = FontFamily.Default, fontSize = 18.sp)
            }

        }else{
            
            Column(modifier = Modifier.fillMaxSize()) {

                MyIPLTheme() {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(mHeaderHeight)
                        .background(HeaderColor), verticalAlignment = Alignment.CenterVertically) {
                        Row(modifier = Modifier
                            .weight(0.35F)
                            .clickable(enabled = true) {
                                if (mSortAscDscNameIs.value == AppConstants.SORT_ASC) {
                                    mSortAscDscNameIs.value = AppConstants.SORT_DESC
                                    mIplViewModel?.performSorting(SortType.NAME, false)
                                } else {
                                    mSortAscDscNameIs.value = AppConstants.SORT_ASC
                                    mIplViewModel?.performSorting(SortType.NAME, true)
                                }
                                iplViewModel.setSortType(SortType.NAME)
                            }, verticalAlignment = Alignment.CenterVertically) {
                            Row(modifier = Modifier.fillMaxSize(),verticalAlignment = Alignment.CenterVertically) {
                                Text(text = stringResource(id = R.string.team), style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(start = 10.dp), fontWeight =
                                if(iplViewModel.getSortType() == SortType.NAME){
                                    FontWeight.Bold
                                }else {
                                    FontWeight.Light
                                })
                                Spacer(modifier = Modifier.width(mSpaceTxtImg))
                                Image(painter = if(mSortAscDscNameIs.value == AppConstants.SORT_ASC) {
                                    painterResource(id = R.drawable.sort_by_alpha)
                                }else{
                                    painterResource(id = R.drawable.sort_by_alpha)
                                }, contentDescription = mSortAscDscNameIs.value )
                            }
                        }
                        Row(modifier = Modifier.weight(0.20F)) {
                            //modifier = Modifier.background(color = Color.Magenta)
                            Text(text = stringResource(id = R.string.played), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Light)
                        }
                        Row(modifier = Modifier
                            .weight(0.25F)
                            .clickable(enabled = true) {
                                if (mSortAscDscWonIs.value == AppConstants.SORT_ASC) {
                                    mSortAscDscWonIs.value = AppConstants.SORT_DESC
                                    mIplViewModel?.performSorting(SortType.WON, false)
                                } else {
                                    mSortAscDscWonIs.value = AppConstants.SORT_ASC
                                    mIplViewModel?.performSorting(SortType.WON, true)
                                }
                                iplViewModel.setSortType(SortType.WON)
                            },verticalAlignment = Alignment.CenterVertically) {
                            Row(modifier = Modifier.fillMaxSize(),verticalAlignment = Alignment.CenterVertically) {
                                Text(text = stringResource(id = R.string.won), style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = if(iplViewModel.getSortType() == SortType.WON){
                                        FontWeight.Bold
                                    }else {
                                        FontWeight.Light
                                    })
                                Spacer(modifier = Modifier.width(mSpaceTxtImg))
                                Image(painter = if(mSortAscDscWonIs.value == AppConstants.SORT_ASC) {
                                    painterResource(id = R.drawable.north)
                                }else{
                                    painterResource(id = R.drawable.south)
                                }, contentDescription = mSortAscDscWonIs.value )
                            }
                        }
                        Row(modifier = Modifier
                            .weight(0.25F)
                            .clickable(enabled = true) {
                                if (mSortAscDscLostIs.value == AppConstants.SORT_ASC) {
                                    mSortAscDscLostIs.value = AppConstants.SORT_DESC
                                    mIplViewModel?.performSorting(SortType.LOST, false)
                                } else {
                                    mSortAscDscLostIs.value = AppConstants.SORT_ASC
                                    mIplViewModel?.performSorting(SortType.LOST, true)
                                }
                                iplViewModel.setSortType(SortType.LOST)

                            },verticalAlignment = Alignment.CenterVertically) {
                            //.background(color = Color.Magenta)
                            Row(modifier = Modifier.fillMaxSize(),verticalAlignment = Alignment.CenterVertically) {
                                Text(text = stringResource(id = R.string.lost), style = MaterialTheme.typography.headlineMedium
                                    , fontWeight = if(iplViewModel.getSortType() == SortType.LOST){
                                        FontWeight.Bold
                                    }else {
                                        FontWeight.Light
                                    })
                                Spacer(modifier = Modifier.width(mSpaceTxtImg))
                                Image(painter = if(mSortAscDscLostIs.value == AppConstants.SORT_ASC) {
                                    painterResource(id = R.drawable.north)
                                }else{
                                    painterResource(id = R.drawable.south)
                                }, contentDescription = mSortAscDscLostIs.value )
                            }

                        }
                    }
                }
                LazyColumn(contentPadding = PaddingValues(start = 5.dp, end = 5.dp, bottom = 5.dp)){
                    items(iplViewModel.getList()) {  item: IplTeam ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White), elevation = CardDefaults.cardElevation(5.dp)) {
                            //modifier = Modifier.background(color = Color.LightGray) Check Row Color
                            Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                                //Column 1
                                Row(modifier = Modifier.weight(0.55F),verticalAlignment = Alignment.CenterVertically) {
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
                                Row(modifier = Modifier.weight(0.25F)) {
                                    Text(text = item.matchWin.toString(), color = Color.Green, fontFamily = FontFamily.Serif, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.weight(0.25F)) {
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

        Text(text = "I will be at Top | Left", modifier = Modifier.align(Alignment.TopStart))

        Text(text = "I will be at Top | Right", modifier = Modifier.align(Alignment.TopEnd))
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