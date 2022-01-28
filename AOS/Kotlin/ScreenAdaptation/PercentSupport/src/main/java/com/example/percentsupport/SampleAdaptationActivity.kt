package com.example.percentsupport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SampleAdaptationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_adaptation)

        setTitle("简单适配")
    }
}