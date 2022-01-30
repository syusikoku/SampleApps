https://www.jianshu.com/p/80d2d527d47f

https://www.jianshu.com/u/06f6a534adf4

Column 线性布局≈ Android LinearLayout-VERTICAL
Row 水平布局≈Android LinearLayout-HORIZONTAL
Box帧布局≈Android FrameLayout，可将一个元素放在另一个元素上，如需在 Row 中设置子项的位置，请设置 horizontalArrangement 和 verticalAlignment 参数。对于 Column，请设置 verticalArrangement 和 horizontalAlignment 参数



Modifier可以修改控件的位置、高度、边距、对齐方式等等
//`padding` 设置各个UI的padding。padding的重载的方法一共有四个。
Modifier.padding(10.dp) // 给上下左右设置成同一个值
Modifier.padding(10.dp, 11.dp, 12.dp, 13.dp) // 分别为上下左右设值
Modifier.padding(10.dp, 11.dp) // 分别为上下和左右设值
Modifier.padding(InnerPadding(10.dp, 11.dp, 12.dp, 13.dp))// 分别为上下左右设值
//这里设置的值必须为`Dp`，`Compose`为我们在Int中扩展了一个方法`dp`，帮我们转换成`Dp`。
//`plus` 可以把其他的Modifier加入到当前的Modifier中。
Modifier.plus(otherModifier) // 把otherModifier的信息加入到现有的modifier中
//`fillMaxHeight`,`fillMaxWidth`,`fillMaxSize` 类似于`match_parent`,填充整个父layout。
Modifier.fillMaxHeight() // 填充整个高度
//`width`,`heigh`,`size` 设置Content的宽度和高度。
Modifier.width(2.dp) // 设置宽度
Modifier.height(3.dp)  // 设置高度
Modifier.size(4.dp, 5.dp) // 设置高度和宽度
//`widthIn`, `heightIn`, `sizeIn` 设置Content的宽度和高度的最大值和最小值。
Modifier.widthIn(2.dp) // 设置最大宽度
Modifier.heightIn(3.dp) // 设置最大高度
Modifier.sizeIn(4.dp, 5.dp, 6.dp, 7.dp) // 设置最大最小的宽度和高度
//`gravity` 在`Column`中元素的位置。
Modifier.gravity(Alignment.CenterHorizontally) // 横向居中
Modifier.gravity(Alignment.Start) // 横向居左
Modifier.gravity(Alignment.End) // 横向居右
//`rtl`, `ltr` 开始布局UI的方向。
Modifier.rtl  // 从右到左
//更多Modifier学习：https://developer.android.com/jetpack/compose/modifiers-list


@Preview的注解中比较常用的参数如下：

name: String: 为该Preview命名，该名字会在布局预览中显示。
showBackground: Boolean: 是否显示背景，true为显示。
backgroundColor: Long: 设置背景的颜色。
showDecoration: Boolean: 是否显示Statusbar和Toolbar，true为显示。
group: String: 为该Preview设置group名字，可以在UI中以group为单位显示。
fontScale: Float: 可以在预览中对字体放大，范围是从0.01。
widthDp: Int: 在Compose中渲染的最大宽度，单位为dp。
heightDp: Int: 在Compose中渲染的最大高度，单位为dp。

