package com.kartinimedia.albumcantik.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kartinimedia.albumcantik.R
import com.kartinimedia.albumcantik.utils.GetDataServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startService

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startService<GetDataServices>()

        GlobalScope.launch {
            delay(4000)
            startActivity<MainActivity>()
            finish()
        }

    }

    override fun onBackPressed() {
        //
    }
}
