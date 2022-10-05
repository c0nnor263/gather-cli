package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common

import android.os.CountDownTimer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.utils.enums.DataKey

internal class TimerPool(private val lifecycle: Lifecycle) : DefaultLifecycleObserver {
    private val pool = mutableMapOf<String, CountDownTimer>()

    init {
        lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        clear()
        lifecycle.removeObserver(this)

    }

    fun add(key: DataKey, onFinish: () -> Unit) {
        val timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                onFinish()
                remove(key)
            }
        }
        pool.put(key.name, timer.start())?.cancel()
    }

    fun addTemp(id: String, onFinish: () -> Unit) {
        val timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                onFinish()
                removeTemp(id)
            }
        }
        pool.put(
            id + "_temp",
            timer.start()
        )?.cancel()
    }

    fun remove(key: DataKey) {
        pool.remove(key.name)?.cancel()
    }

    fun removeTemp(id: String) {
        pool.remove(id + "_temp")?.cancel()
    }

    fun clear() {
        pool.forEach { it.value.cancel() }
        pool.clear()
    }
}