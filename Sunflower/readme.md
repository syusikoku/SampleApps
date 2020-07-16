
首页视差效果实现
CollapsingToolbarLayout
 app:layout_scrollFlags="scroll|snap"

scroll:将此布局和滚动时间关联。这个标识要设置在其他标识之前，没有这个标识则布局不会滚动且其他标识设置无效。

 snap:当一个滚动事件结束，如果视图是部分可见的，那么它将被滚动到收缩或展开。例如，如果视图只有底部25%显示，它将折叠。相反，如果它的底部75%可见，那么它将完全展开。


toolbar
  app:layout_collapseMode="parallax"
    off：这个是默认属性，布局将正常显示，没有折叠的行为。
    pin：CollapsingToolbarLayout折叠后，此布局将固定在顶部。
    parallax：CollapsingToolbarLayout折叠时，此布局也会有视差折叠效果。

jetpack
https://developer.android.com/jetpack

kotlin知识点
https://developer.android.com/kotlin/get-started
kotlin基本使用:
  https://www.jianshu.com/p/27646c6561a7

疑问点：
suspend fun
plurals 用法


fragment xml中使用livedata数据有问题
https://blog.csdn.net/bobo_zai/article/details/104424784
 mBinding.lifecycleOwner = this





协程知识点
https://developer.android.com/kotlin/coroutines
资料
https://www.jianshu.com/p/2659bbe0df16


ktx操作
https://developer.android.com/kotlin/ktx
