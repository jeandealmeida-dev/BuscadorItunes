package com.jeanpaulo.musiclibrary.commons.extensions

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

fun Activity.requireString(key: String): String {
    return intent.requireString(key)
}

fun Activity.requireLong(key: String): Long {
    return intent.requireLong(key)
}

fun Activity.requireBoolean(key: String): Boolean {
    return intent.requireBoolean(key)
}

inline fun <reified T : Parcelable> Activity.requireParcelable(key: String): T {
    return intent.requireParcelable(key)
}

fun Activity.runWithDelay(delay: Long, function: () -> Unit): Disposable =
    Completable
        .complete()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .delay(delay, TimeUnit.MILLISECONDS)
        .doOnComplete {
            runOnUiThread {
                function.invoke()
            }
        }
        .subscribe({}, {})

fun Activity.setupLightStatusBar() {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

//fun ActivityCompat.startCommonActivity(view: View, intent: Intent) {
//    val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//        this,
//        titleElement,
//        imageElement)
//    ActivityCompat.startActivity(this, intent, activityOptions)
//}