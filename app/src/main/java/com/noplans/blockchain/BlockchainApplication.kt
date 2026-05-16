package com.noplans.blockchain

import android.app.Application
import timber.log.Timber

class BlockchainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.i("BlockchainApplication initialized")
    }
}
