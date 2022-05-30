package com.compose.myipl

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.myipl.data.IplTeam
import com.compose.myipl.ui.theme.MyIPLTheme
import com.compose.myipl.viewmodel.IplViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    //private var mIplTeamList = ArrayList<IplTeam>()
    private var mIplViewModel: IplViewModel ? = null
    private var mHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        mHandler.postDelayed({
            if(mIplViewModel!!.isEmpty()){

                for (i in 0 .. 10){

                    val teamName ="Team $i"
                    val ipTeam = IplTeam(teamName = teamName)

                    mIplViewModel?.addTeam(ipTeam)

                }
               // mIplViewModel?.ad(mIplViewModel?.getIplList()!!)
            }
        },1000L)


    }

    //@OptIn(ExperimentalFoundationApi::class, ExperimentalUnitApi::class)
    @Composable
    fun IplList(iplViewModel: IplViewModel){
        mIplViewModel = iplViewModel
        //Check if IPLList is Empty
        if(iplViewModel.isEmpty()){
            Log.i("TAG","List is Empty")
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                //Put your Compose View here to make it center in this Box
                Text(text = "No IPL Team available to show", color = Color.Magenta, fontFamily = FontFamily.Default, fontSize = 18.sp)
            }

        }else{
            LazyColumn(contentPadding = PaddingValues(10.dp)){
                items(iplViewModel.getList()) {  item: IplTeam ->
                    Text(text = item.teamName, color = Color.Black, fontFamily = FontFamily.Serif, fontSize = 18.sp)
                }
            }
        }
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