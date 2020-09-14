package com.vinpin.kcretrofitutils.sample

import android.app.Application
import com.vinpin.kcretrofitutils.KcRetrofitUtils

/**
 * author : VinPin
 * e-mail : hearzwp@163.com
 * time   : 2020/9/14 15:02
 * desc   :
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化
        KcRetrofitUtils.init(this)
    }
}
