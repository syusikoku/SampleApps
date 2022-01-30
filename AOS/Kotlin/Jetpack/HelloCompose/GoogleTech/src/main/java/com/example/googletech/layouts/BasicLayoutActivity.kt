package com.example.googletech.layouts

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.googletech.R


/**
 * 基础布局使用
 * https://developer.android.com/jetpack/compose/layouts/basics?hl=zh-cn
 */
class BasicLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {
                ArtistCard()
                testImgList()
            }
        }
    }

    @Preview
    @Composable
    fun ArtistCard() {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Alfred Sisley")
            Text("3 minutes ago")
        }
    }

    @Preview
    @Composable
    fun testArtistCard2() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.mipmap.test1),
                contentDescription = "test",
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.requiredWidth(5.dp))
            Column {
                Text("Alfred Sisley", fontSize = 16.sp)
                Spacer(modifier = Modifier.requiredHeight(5.dp))
                Text("3 minutes ago", fontSize = 14.sp)
            }
        }
    }


    /**
     * 需要改善
     */
    @Preview
    @Composable
    fun testartistCard3() {
        Box {
            Image(
                painter = painterResource(R.mipmap.test1),
                contentDescription = "test",
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
            )
            Icon(painter = painterResource(R.mipmap.test1), contentDescription = "", modifier = Modifier.size(12.dp))
        }
    }

    @Preview
    @Composable
    fun testArtistCard4() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(R.mipmap.test1),
                contentDescription = "test",
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.requiredWidth(5.dp))
            Column {
                Text("Alfred Sisley", fontSize = 16.sp)
                Spacer(modifier = Modifier.requiredHeight(5.dp))
                Text("3 minutes ago", fontSize = 14.sp)
            }
        }
    }
}


@Preview
@Composable
fun testIconRows() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Icon(Icons.Outlined.Favorite, contentDescription = null, tint = Color.Blue)
        Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.Blue)
        Icon(Icons.Sharp.Favorite, contentDescription = null, tint = Color.Green)
        Icon(Icons.TwoTone.Favorite, contentDescription = null, tint = Color.Red)
        Icon(Icons.Rounded.Favorite, contentDescription = null, tint = Color.Black)
    }
}


@Preview
@Composable
fun testSearchComponent() {
    SearchResult()
}


@Composable
fun SearchResult() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(320.dp, 20.dp)
                .fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(16.dp))
        }
        Text("搜索", textAlign = TextAlign.End)
    }
}


/**
 * https://blog.csdn.net/u013710752/article/details/121499769
 * 图片显示不出来
 */
@Preview
@Composable
fun testImgList() {
    var imgUrl: String = "http://pic-bucket.ws.126.net/photo/0003/2021-11-16/GOTKEOOU00AJ0003NOS.jpg"
    Column() {
        Image(
            painter = rememberImagePainter(
                data = imgUrl,
            ), contentDescription = null, modifier = Modifier.size(36.dp)
        )
        Image(
            painter = rememberImagePainter(
                data = "http://pic-bucket.ws.126.net/photo/0003/2021-11-16/GOTKEOOU00AJ0003NOS.jpg",
                builder = {
                    //圆形图片
                },
            ),
            contentDescription = null, modifier = Modifier.size(36.dp)
        )
    }
}

data class CardInfo(
    val title: String,
    val desc: String,
    val iconRes: Int,
    val cardBgRes: Int
)


@Preview
@Composable
fun testArtistCard() {
    showArtistCard(CardInfo("Alfred Sisley", "3 minutes ago", R.mipmap.test1, R.mipmap.test2)) {}
}

@Composable
fun showArtistCard(cardInfo: CardInfo, onClick: () -> Unit) {
    val padding = 16.dp
    Column(
        Modifier
            .clickable(onClick = onClick)
            .padding(padding)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.test1),
                contentDescription = null,
                modifier = Modifier
                    .size
                        (48.dp)
                    .clip(shape = RoundedCornerShape(24.dp))
            )
            Spacer(modifier = Modifier.requiredWidth(10.dp))
            Column() {
                Text(cardInfo.title)
                Spacer(modifier = Modifier.requiredHeight(5.dp))
                Text(cardInfo.desc)
            }
        }
        Spacer(modifier = Modifier.size(padding))
        Card(elevation = 4.dp) {
            Image(
                painter = painterResource(id = R.mipmap.test2),
                contentDescription = null
            )
        }
    }
}

// ---------------------- 自适应布局  ---------------------- //

@Preview
@Composable
fun WithConstraintsComposable() {
    BoxWithConstraints() {
        Text("My minHeight is $minHeight while my maxWidth is $maxWidth")
    }
}

/**
 * 测试用的列表数据
 */
@Composable
private fun generateListMsg() = listOf(
    "Jetpack 是一个由多个库组成的套件，可帮助开发者遵循最佳做法、减少样板代码并编写可在各种 Android 版本和设备中一致运行的代码，让开发者可将精力集中于真正重要的编码工作。",
    "现在，您可以在 RC 中使用 WindowManager 库来支持不同类型的设备（例如可折叠设备）或多窗口环境。",
    "Paging 3 内置了对 Kotlin 协程和数据流的支持，并为 Compose 集成奠定了基础。此版本侧重于减少实现中的样板代码。",
    "Wear Watchface 现已为稳定版，并成为我们新推荐的 WearOS 表盘开发专用库了！与旧版穿戴式设备支持库相比，该库纳入了诸多新功能。",
    "Watch how Android experts go through the Basics of Jetpack Compose and answer audience questions live. Go hands-on and learn the fundamentals of declarative UI, working with state, layouts, and theming. You'll see",
    "Watch how Android experts migrate an existing app to Jetpack Compose and answer audience questions live. Walk through a practical migration of a View-based app to Jetpack Compose to understand how to",
    "Watch Android experts code, tackle programming challenges, and answer your questions live. Learn to apply your preexisting Compose knowledge to the new Compose for Wear OS. Walk through demos of",
    "Jetpack Compose 是用于构建原生 Android 界面的新工具包。它可简化并加快 Android 上的界面开发，使用更少的代码、强大的工具和直观的 Kotlin API，快速让应用生动而精彩。",
    "编写更少的代码会影响到所有开发阶段：作为代码撰写者，需要测试和调试的代码会更少，出现 bug 的可能性也更小，您就可以专注于解决手头的问题；作为审核人员或维护人员，您需要阅读、理解、审核和维护的代码就更少。",
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun testLayColumnScroll(messages: List<String>) {
    // 调整内边距和item间距
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        // 粘性标题
        stickyHeader {
            testListHeader()
        }
        // 内容条目
        items(messages) { item -> MessageRow(item) }
    }
}

@Composable
fun testListHeader() {
    MessageRow("粘性标题-我是头部")
}

@Composable
fun MessageRow(msg: String) {
    Text(msg, modifier = Modifier.padding(10.dp))
    Divider(
        color = Color.DarkGray, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
    )
}

@Preview
@Composable
fun HomeScreen() {
    val menus: List<String> = listOf("Home", "Discovery", "Around me", "Messages", "Contacts", "Settings", "Sign Out")
    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Blue)
                    .requiredHeight(45.dp)
            ) {
                Text("首页", textAlign = TextAlign.Center, color = Color.White)
            }
        },
        drawerBackgroundColor = Color.Cyan,
        drawerElevation = 10.dp,
        drawerContent = {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(menus) { menuItem ->
                    Text(
                        menuItem,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        modifier = Modifier.requiredHeight(45.dp)
                    )
                }
            }
        }
    ) {
        testLayColumnScroll(generateListMsg())
    }
}