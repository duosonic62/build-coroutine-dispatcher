package com.example.originaldispatchers

import kotlinx.coroutines.*
import java.util.concurrent.Executors

fun main() {
    Sample().sample()
}

class Sample {
    fun sample() {
        runBlocking {
            val threads = HashSet<String>()
            val jobs: MutableList<Deferred<Unit>> = mutableListOf()
            for (i in 1..300) {
                jobs.add(
                    CoroutineScope(OriginalDispatcher.IO + SupervisorJob()).async {
                        threads.add(Thread.currentThread().toString())
                        println("Loop START: $i ${Thread.currentThread()}")
                        Thread.sleep(1000)
                        println("Loop END  : $i ${Thread.currentThread()}")
                    }
                )
            }
            jobs.forEach { it.await() }
            println(threads.size)
        }
    }
}

object OriginalDispatcher {
    val IO =
//        ThreadPoolExecutor(
//        10,
//        10,
//        10L,
//        TimeUnit.SECONDS,
//        SynchronousQueue()
//    ).asCoroutineDispatcher()
    Executors.newCachedThreadPool()
        .asCoroutineDispatcher()
}