package br.com.luizssb.esapienschallenge

import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

abstract class LiveDataUnitTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected open val countDownLatch = CountDownLatch(1)

    protected fun defaultLock() {
        countDownLatch.await(1, TimeUnit.SECONDS)
    }

    protected fun defaultUnlock() {
        countDownLatch.countDown()
    }
}