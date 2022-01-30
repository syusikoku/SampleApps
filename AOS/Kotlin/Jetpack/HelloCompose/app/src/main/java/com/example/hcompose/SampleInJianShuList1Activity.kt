package com.example.hcompose

import android.os.Bundle
import android.preference.PreferenceActivity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.hcompose.ui.theme.HelloComposeTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


/**
 * 资料:
 * https://www.jianshu.com/p/80d2d527d47f
 */
class SampleInJianShuList1Activity : ComponentActivity() {
    // 点击事件计数器
    private var clickCount: Int = 0

    private var paddingSize = 10.dp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HelloComposeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    DefaultPreview()
                }
            }
        }
    }


    @Composable
    fun Greeting(name: String) {
        Text("Hello $name!")
    }


    /**
     * 预览组合控件
     */
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        HelloComposeTheme {
            Column(modifier = Modifier.padding(horizontal = paddingSize, vertical = paddingSize)) {
                Greeting("Jetpack Compose")
                Spacer(modifier = Modifier.size(paddingSize))
                ClickCounter(clickCount) {
                    clickCount++
                    Log.e("test", "点击次数")
                }
            }
        }
    }

    /**
     * 按钮
     */
    @Composable
    fun ClickCounter(clicks: Int, onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text("I've been clicked $clicks times!")
        }
    }

    /**
     * 组合函数
     *   这里需要改造
     */
    @Preview
    @Composable
    fun ListComposable() {
        val list: List<String> = listOf("API文档", "系统版本", "Activity生命周期", "Fragment生命周期", "ADB用法")
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column() {
                for (item in list) {
                    Text("Item: $item")
                }
            }
            Text("Count: ${list.size}")
        }
    }

    /**
     * ConstraintLayout
     * 预览未看到文字的效果
     */
    @Preview
    @Composable
    fun testConstraintLayout1() {
        ConstraintLayout() {
            // 通过createRefs创建三个引用
            val (imageRef, nameRef) = createRefs()
            Image(painter = painterResource(id = R.mipmap.test1),
                contentDescription = "图",
                // //通过constrainAs将Image与imageRef绑定,并增加约束
                modifier = Modifier
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .size(100.dp)
                    .clip(shape = RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop)
            Text(text = "名称", modifier = Modifier
                .constrainAs(nameRef) {
                    top.linkTo(imageRef.top, 12.dp)
                    start.linkTo(imageRef.end, 12.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .fillMaxWidth(),
                color = Color.Red,
                fontSize = 18.sp,
                maxLines = 1,
                textAlign = TextAlign.Left,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun testScrollLayouts() {
    val msgList = generateListMsg()
    Column() {
        Text("默认滚动-Column", modifier = Modifier.padding(horizontal = 16.dp))
        testColumnScroll(msgList)
    }
}

@Preview
@Composable
fun testLazyColumn() {
    val msgList = generateListMsg()
    Column() {
        Text("高效滚动", modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))
        testLayColumnScroll(msgList)
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

/**
 * 列表： 可以滚动的布局
 * 布局并无法实现重用，可能导致性能问题
 * 推荐使用:
 * LazyColumn/LazyRow==RecylerView/listView 列表布局，解决了滚动时的性能问题，LazyColumn 和 LazyRow 之间的区别就在于它们的列表项布局和滚动方向不同
 */

@Composable
fun testColumnScroll(messages: List<String>) {
    // 我们可以使用 verticalScroll() 修饰符使 Column 可滚动
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        messages.forEach { msg -> MessageRow(msg) }
    }
}

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
fun testListGrid() {
    WordsGrid(
        listOf(
            "社会",
            "财经",
            "军事",
            "历史文化",
            "科技",
            "汽车",
            "房产",
            "体育",
            "娱乐",
            "健康",
            "面试",
            "Studio3",
            "动画",
            "自定义View",
            "性能优化",
            "速度",
            "gradle",
            "Camera 相机",
            "代码混淆",
            "安全",
            "逆向",
            "加固",
        )
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordsGrid(words: List<String>) {
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 128.dp)) {
        items(words) { word ->
            PhotoItem(word)
        }
    }
}

@Composable
fun PhotoItem(word: String) {
    Text(word, modifier = Modifier.padding(18.dp))
}


@Preview
@Composable
fun testCustomLayout() {
    //drawCircle 画圆
    //drawRectangle 画矩形
    //drawLine //画线
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawCircle(
            color = Color.Blue,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            radius = size.minDimension / 4
        )
    }
}