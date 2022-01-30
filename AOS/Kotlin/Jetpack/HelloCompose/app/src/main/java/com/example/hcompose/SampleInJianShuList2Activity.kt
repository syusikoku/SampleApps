package com.example.hcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


/**
 * 资料:
 *   https://www.jianshu.com/p/c49565f298f0
 *
 *   还需要补充
 */
class SampleInJianShuList2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            testText()
        }

    }
}


@Preview
@Composable
fun testText() {
    Text(
        text = "Hello Android",
        modifier = Modifier.padding(10.dp),
        color = Color.Blue,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.LineThrough,
        onTextLayout = {},
        fontStyle = FontStyle.Italic,
        maxLines = 1
    )

}

@Preview
@Composable
fun testButton1() {
    Button(
        onClick = { },
        modifier = Modifier.padding(12.dp),
        border = BorderStroke(2.dp, Color.Red),
    ) {
        Text("测试按钮-1")
    }
}


@Preview
@Composable
fun testImage() {


}

/**
 * 显示空白区域
 */
@Preview
@Composable
fun testSpacer() {
    Spacer(modifier = Modifier.requiredWidth(16.dp))
}