package com.example.googletech.layouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


/**
 * Material 组件和布局
 * https://developer.android.com/jetpack/compose/layouts/material?hl=zh-cn
 */
class MaterialAndLayoutsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            testHello()
        }
    }

    @Preview
    @Composable
    fun testHello() {
        Text("Hello")
    }

    @Preview
    @Composable
    fun MyApp() {
        MaterialTheme() {
            Column() {
                Button(onClick = { /*TODO*/ }) {
                    Text("测试按钮")
                }
                Spacer(modifier = Modifier.requiredHeight(5.dp))
                Card(
                    elevation = 10.dp,
                    modifier = Modifier
                        .requiredWidth(85.dp)
                        .aspectRatio(16F / 9),
                ) {
                    Text("测试Card", textAlign = TextAlign.Center)
                }

            }
        }
    }
}


@Preview
@Composable
fun testButton() {
    MaterialTheme() {
        Button(
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp)
        ) {
            Icon(
                Icons.Filled.Favorite, contentDescription = "Favorite", modifier = Modifier.size
                    (ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text("Like")
        }
    }
}


@Preview
@Composable
fun testExtendFloatActionButton() {
    MaterialTheme {
        ExtendedFloatingActionButton(
            icon = {
                Icon(
                    Icons.Filled.Favorite, contentDescription =
                    "Favorite"
                )
            },
            text = { Text("Like") },
            onClick = { /*TODO*/ },
        )
    }
}

/**
 * 需要调整
 */
@Preview
@Composable
fun testScaffold() {
    MaterialTheme() {
        Scaffold(
            topBar = {
                TopAppBar(modifier = Modifier.fillMaxWidth()) {
                    Text("首页", textAlign = TextAlign.Center)
                }
            },
            bottomBar = {
                Box() {
                    BottomAppBar() {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        ) {
                            Icon(Icons.Filled.Menu, contentDescription = null)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(Icons.Filled.Search, contentDescription = null)
                                Spacer(modifier = Modifier.requiredWidth(10.dp))
                                Icon(Icons.Filled.Attractions, contentDescription = null)
                            }
                        }
                    }
//                    Row(
//                        horizontalArrangement = Arrangement.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        // https://blog.csdn.net/Eqiqi/article/details/121859544
//                        FloatingActionButton(
//                            modifier = Modifier
//                                .size(60.dp)
//                                .offset(y = -30.dp),
//                            onClick = { /*TODO*/ },
//                            interactionSource = MutableInteractionSource()
//                        ) {
//                            Icon(Icons.Filled.PlusOne, contentDescription = null)
//                        }
//                    }
                }
            }
        ) {
            Box(
                Modifier
                    .padding(horizontal = 16.dp)
                    .background(color = Color.DarkGray)
            )
        }
    }
}