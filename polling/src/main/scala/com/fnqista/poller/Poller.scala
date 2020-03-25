package com.fnqista.poller

import cats.effect.IO.Async
import monix.eval.Task
import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global
import monix.reactive.observers.Subscriber
import monix.reactive.observers.Subscriber.Sync
import monix.reactive.{Consumer, Observable, OverflowStrategy}

import concurrent.duration._
import scala.concurrent.Await
import scala.util.{Random, Try}

object Poller extends App {
  def rand(low: Int, high: Int): Int = {
    val res = Random.nextInt((high - low) + 1) + low
    if (res == 1) throw new RuntimeException("failed")
    res
  }

  private val future = Observable.eval(rand(1, 3))
    .dump("out")
    .delayExecution(2.seconds)
    .restartUntil(_ == 3)
    .onErrorRestart(3)
    .firstL
    .runToFuture

  Await.result(future, 100.seconds)


  //  Observable.create(OverflowStrategy.Unbounded) {
  //    sub => Task.deferFuture(sub.onNext(rand(1, 5)))
  //        .runToFuture(sub.scheduler)
  //  }

  //  val source = Observable.range(0,1000)
  //  val logger = Consumer.foreach[Long](x => println(s"Elem: $x"))
  //
  //  val task = source.consumeWith(logger)
  //  val task = Observable.fromIterable(1 to 3)
  //    .map(i => i + 2)
  //    .map(i => i * 3)
  //
  //  val res = task.consumeWith(Consumer.foreach[Int](i => Console println i))
  //  Await.result(res.runToFuture, 10.seconds)


  //  val task = Observable.fromIterable(1 to 3)
  //    .map(i => i + 2)
  //    .map(i => i * 3)
  //    .sum
  //    .firstL
  //
  //
  ////  val cancelable = tick.subscribe()
  //
  //  val res = Await.result(task.runToFuture, 10.seconds)
  //  Console println res
  //  Thread.sleep(5000)
}
