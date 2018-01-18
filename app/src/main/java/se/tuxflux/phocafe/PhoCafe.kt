package se.tuxflux.phocafe

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree


class PhoCafe : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Timber.d("Debug print active!")
        }
    }
}
