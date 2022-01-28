package com.example.percentsupport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.percentsupport.databinding.ActivityComplexadaptationBinding

class ComplexadaptationActivity : BaseActivity() {

    lateinit var binding: ActivityComplexadaptationBinding

    val largeUrl: String =
        "https://img1.baidu.com/it/u=396106756,1736879362&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500"


    var sampleImgUrl: String ="https://img0.baidu.com/it/u=3002723803,1830235692&fm=253&fmt=auto&app=138&f=JPEG?w=750&h=500"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView<ActivityComplexadaptationBinding>(this, R.layout.activity_complexadaptation)

        setTitle("复杂适配")

        Glide.with(this).load(sampleImgUrl).into(binding.image1)
        Glide.with(this).load(largeUrl).into(binding.image2)

    }
}