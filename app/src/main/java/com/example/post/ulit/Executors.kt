package com.example.post.ulit

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
private val IO_EXECUTOR_WITH_DELAY = Executors.newSingleThreadExecutor()

fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}

fun ioThreadDelay(f: () -> Unit) {
    IO_EXECUTOR_WITH_DELAY.execute {
        Thread.sleep(1000)
        f()
    }
}
class MainThreadExecutor : Executor {
    private val mHandler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
        mHandler.post(command)
    }
}