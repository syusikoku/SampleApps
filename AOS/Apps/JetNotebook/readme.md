资料:

https://developer.android.google.cn/topic/libraries/architecture/adding-components


1. ktx 使用




组件通信时，界面刷新，先用eventbugs实现,以后再做修改



主界面实现时的注意点
Drawerlayout+NavigationView+Navigation
菜单 menu->item->id 要和 navigation -> fragment -> id 对应

Activity和Fragment中显示不同的菜单 资源
Fragment
// 设置菜单 显示
setHasOptionsMenu(true)


override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        e("onCreateOptionsMenu")
        // 先清除，要不然菜单上会出现activity中的菜单
        menu.clear()
        inflater.inflate(R.menu.menu_webview, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
    
kotlin中关于函数的传递:
起源于c++的接口回调

接受函数指针作为参数是这样做的：

private fun uploadImageToParse(file: ParseFile?, saveCall: () -> Unit){
    saveCall.invoke()
}
() 是参数的类型 .

-> Unit 部分是返回类型 .

第二个例子：

fun someFunction (a:Int, b:Float) : Double {
    return (a * b).toDouble()
}

fun useFunction (func: (Int, Float) -> Double) {
    println(func.invoke(10, 5.54421))
}



RecyclerView 的分割线添加


toolbar菜单的隐藏
menu.findById().setVisiaby = GONE




需要重点研究的地方 
 <androidx.coordinatorlayout.widget.CoordinatorLayout
    android:id="@+id/coordinator_layout_navs"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/appbarlayout_navs"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:id="@+id/ctl_navs"
            android:layout_width="match_parent"
            android:layout_height="?actionBarSize">

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar_navs"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:title="@string/app_name" />
        </com.google.android.material.appbar.CollapsingToolbarLayout>
    </com.google.android.material.appbar.AppBarLayout>

    <fragment
        android:id="@+id/fragment_navs"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:layout_behavior="com.google.android.material.appbar.AppBarLayout$ScrollingViewBehavior"
        app:navGraph="@navigation/nav_navs_sample" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
标题 显示不了



CoordinatorLayout+AppBarLayout 提供最佳浏览体验
https://blog.csdn.net/smile_Running/article/details/97137955?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1


databinding表达式的使用

   android:visibility="@{dataItem.desc!=null?View.VISIBLE:View.GONE}"
   
   
在线数据使用这个api
https://gank.io/api
count:[10,50] ==> 默认至少10个
妹子：   https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10

         https://gank.io/api/v2/data/category/Girl/type/Girl/page/40/count/1

Jetnotebook示例源工程:
http://gank.io/api/data/Android/20/1


关于数据源类型的选择:
   如果您加载的网页嵌入了上一页/下一页的键，请使用 PageKeyedDataSource。
     例如，如果您从网络中获取社交媒体帖子，则可能需要将一个 nextPage 令牌从一次加载传递到后续加载。

     如果您需要使用项目 N 中的数据来获取项目 N+1，请使用 ItemKeyedDataSource。
        例如，如果您要为讨论应用获取会话式评论，则可能需要传递最后一条评论的 ID 以获取下一条评论的内容。

    如果您需要从数据存储区中选择的任意位置获取数据页，请使用 PositionalDataSource。该类支持从您选择的任意位置开始请求一组数据项。
        例如，该请求可能会返回从位置 1500 开始的 50 个数据项。
        
        
ViewModelFactory需要参照源工程进行重构



使用Paging从Dao,网络上加载数据      

androidx.paging.ContiguousPagedList
mBoundaryCallback 为空导航，界面上的数据刷新不了  


参考资料:
https://blog.csdn.net/greathfs/category_9911324.html


反思Jetpack分页组件Paging的设计与实现
https://mp.weixin.qq.com/s/QoKwTm0x6hssCzocnDp_1A


关于事件绑定
https://www.jianshu.com/p/17e79095d691

 <Button
        android:id="@+id/btn_bottomnav"
        style="@style/AppTheme.StyleNavButton"
        android:layout_marginBottom="@dimen/dp_25"
        android:onClick="@{(view)-> viewModel.goBottomNavSample(view)}"
        android:text="BottomNavigation"
        app:layout_constraintBottom_toBottomOf="parent"
        tools:ignore="MissingConstraints" />


fun goBottomNavSample(v: View) {
    //  act.forward(BottomNavSampleActivity::class.java)
    v.findNavController().navigate(R.id.bottomNavSampleActivity)
}