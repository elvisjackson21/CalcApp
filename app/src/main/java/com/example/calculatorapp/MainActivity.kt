package com.example.calculatorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.calculatorapp.ui.theme.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.calculatorapp.ui.theme.CalculatorAppTheme
import com.example.calculatorapp.ui.theme.Cyan
import java.math.RoundingMode
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {

    private var resultPresent: Boolean = false
    private var equationAfterResult: Boolean = false

    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorAppTheme {
                val darkModeEnabled by LocalTheme.current.darkMode.collectAsState()
                val textColor = if (darkModeEnabled) Color(0xffffffff) else Color(0xff212121)
                val themeViewModel = LocalTheme.current
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.secondary
                ) {

                    val calculatorButtons = remember {
                        mutableStateListOf(
                            CalculatorButton("%", CalculatorButtonType.Normal),
                            CalculatorButton("(", CalculatorButtonType.Normal),
                            CalculatorButton(")", CalculatorButtonType.Normal),
                            CalculatorButton("÷", CalculatorButtonType.Normal),

                            CalculatorButton("7", CalculatorButtonType.Normal),
                            CalculatorButton("8", CalculatorButtonType.Normal),
                            CalculatorButton("9", CalculatorButtonType.Normal),

                            CalculatorButton("x", CalculatorButtonType.Normal),

                            CalculatorButton("4", CalculatorButtonType.Normal),
                            CalculatorButton("5", CalculatorButtonType.Normal),
                            CalculatorButton("6", CalculatorButtonType.Normal),

                            CalculatorButton("-", CalculatorButtonType.Normal),

                            CalculatorButton("1", CalculatorButtonType.Normal),
                            CalculatorButton("2", CalculatorButtonType.Normal),
                            CalculatorButton("3", CalculatorButtonType.Normal),

                            CalculatorButton("+", CalculatorButtonType.Normal),

                            CalculatorButton("Clr", CalculatorButtonType.Reset),
                            CalculatorButton("0", CalculatorButtonType.Normal),
                            CalculatorButton(".", CalculatorButtonType.Normal),

                            CalculatorButton("=", CalculatorButtonType.Action),
                        )
                    }
                    val (uiText,setUiText) = remember {
                        mutableStateOf("0")
                    }
                    LaunchedEffect(uiText){
                        if (uiText.startsWith("0") && uiText != "0"){
                            setUiText(uiText.substring(1))
                        }
                    }
                    val (uiPreText,setUiPreText) = remember {
                        mutableStateOf("0")
                    }
                    LaunchedEffect(uiPreText){
                        if (uiPreText.startsWith("0") && uiText != "0"){
                            setUiText(uiPreText.substring(1))
                        }
                    }
                    var (input,setInput) = remember {
                        mutableStateOf<String?>(null)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Column {
                            Text(modifier=Modifier.padding(horizontal = 16.dp), text = uiPreText, lineHeight = 64.sp, maxLines = 1, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
                            Text(modifier=Modifier.padding(horizontal = 8.dp), text = uiText, lineHeight = 64.sp, maxLines = 1, fontSize = 48.sp, fontWeight = FontWeight.Bold, color = textColor)
                            Spacer(modifier=Modifier.height(16.dp).background(MaterialTheme.colorScheme.primary))
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                columns = GridCells.Fixed(4),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(16.dp),
                            ) {
                                items(calculatorButtons) {
                                    //var equationAfterResult = false
                                    //var resultPresent = true

                                    var test = ""
                                    CalcButton(

                                        button = it,
                                        textColor = textColor,
                                        onClick = {
                                            when(it.type){

                                                CalculatorButtonType.Normal->{
                                                    Log.d("Calculator", "resultPresent: $resultPresent")
                                                    Log.d("Calculator", "equationAfterResult: $equationAfterResult")
                                                    Log.d("uitext", "text: $uiText")


                                                    runCatching {
                                                        if(resultPresent){
                                                            if((it.text == "." || it.text!!.isDigitsOnly()) && !equationAfterResult){

                                                                setUiText("0")
                                                                setUiPreText
                                                                setInput(null)
                                                                viewModel.resetAll()
                                                                resultPresent = !resultPresent
                                                                equationAfterResult = false
                                                            }
                                                            else{
                                                                if(it.text == "x"){
                                                                    it.text = "*"
                                                                }
                                                                if(it.text == "%"){
                                                                    it.text = "/100"
                                                                }
                                                                if(it.text == "÷") {
                                                                    it.text = "/"
                                                                }
                                                                setUiText(uiText.toInt().toString()+it.text)

                                                                if(it.text == "*"){
                                                                    it.text = "x"
                                                                }
                                                                if(it.text == "/100"){
                                                                    it.text = "%"
                                                                }
                                                                if(it.text == "/"){
                                                                    it.text = "÷"
                                                                }
                                                                equationAfterResult = true
                                                                resultPresent = false
                                                            }

                                                        }
                                                        else if(!resultPresent){
                                                            if(it.text == "x"){
                                                                it.text = "*"
                                                            }
                                                            if(it.text == "%"){
                                                                it.text = "/100"
                                                            }
                                                            if(it.text == "÷") {
                                                                it.text = "/"
                                                            }
                                                            setUiText(uiText.toInt().toString()+it.text)

                                                            if(it.text == "*"){
                                                                it.text = "x"
                                                            }
                                                            if(it.text == "/100"){
                                                                it.text = "%"
                                                            }
                                                            if(it.text == "/"){
                                                                it.text = "÷"
                                                            }
                                                        }


                                                    }.onFailure { throwable->
                                                        setUiText(uiText+it.text)
                                                    }


                                                }
                                                CalculatorButtonType.Action->{

                                                    runCatching {
                                                        //setUiText(uiText.toInt().toString()+it.text)
                                                        if (it.text == "="){

                                                            var result: String
                                                            viewModel.setFirstNumber(uiText)
                                                            //result = viewModel.getResult().toString()
                                                            Log.d("Calculator", "resultPresent: $resultPresent")
                                                            resultPresent = true
                                                            Log.d("Calculator", "resultPresent: $resultPresent")
                                                            equationAfterResult = false

                                                            if(viewModel.getResult() % 1 == 0.0) {
                                                                result = viewModel.getResult().toInt().toString()
                                                            }
                                                            else{
                                                                val testRes = viewModel.getResult()
                                                                val rounding = testRes.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()
                                                                Log.d("Calculator", "test result: $rounding")
                                                                result = rounding.toString()
                                                            }

                                                            setUiText(result.toString())
                                                            setInput(null)
                                                            viewModel.resetAll()

                                                        }
                                                    }.onFailure { throwable->
                                                        setUiText(uiText+it.text)
                                                    }
                                                    if (input != null){
                                                        viewModel.setAction(it.text!!)
                                                        setInput(null)
                                                    }


                                                }
                                                CalculatorButtonType.Reset->{
                                                    setUiText("0")
                                                    setInput(null)
                                                    viewModel.resetAll()
                                                    resultPresent = false
                                                    equationAfterResult = false
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp), contentAlignment = Alignment.TopCenter) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 15.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                modifier=Modifier.size(20.dp).clickable {
                                    themeViewModel.toggleTheme()
                                },
                                painter = painterResource(id = R.drawable.ic_nightmode),
                                contentDescription = null,
                                tint = if (darkModeEnabled) Color.Gray.copy(alpha = .5f) else Color.Gray
                            )

                            Icon(
                                modifier=Modifier.size(20.dp).clickable {
                                    themeViewModel.toggleTheme()
                                },
                                painter = painterResource(id = R.drawable.ic_darkmode),
                                contentDescription = null,
                                tint = if (!darkModeEnabled) Color.Gray.copy(alpha = .5f) else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CalcButton(

    button: CalculatorButton,
    textColor: Color,
    onClick: () -> Unit,

    ) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxHeight()
            .aspectRatio(1f)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        val contentColor =
            if (button.type == CalculatorButtonType.Normal && button.text!!.isDigitsOnly())
                textColor
            else if (button.type == CalculatorButtonType.Action)
                Red
            else
                Cyan
        if (button.text != null) {
            Text(
                button.text!!,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = if (button.type == CalculatorButtonType.Action) 25.sp else 20.sp
            )
        } else {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = button.icon!!,
                contentDescription = null,
                tint = contentColor
            )
        }
    }
}


data class CalculatorButton(
    var text: String? = null,
    val type: CalculatorButtonType,
    val icon: ImageVector? = null,
)

enum class CalculatorButtonType {
    Normal, Action, Reset
}