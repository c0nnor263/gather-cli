package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common

import android.os.CountDownTimer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.enums.UploadDataType

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

	fun add(key: UploadDataType, onFinish: () -> Unit) {
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

	fun remove(key: UploadDataType) {
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