package com.lordjoe.bridge.akka

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging


class MyActor extends Actor {
  def receive = {
    case value: String => doSomething(value)
    case _ => println("received unknown message")
  }

  def doSomething(s: String) : Unit   = println(s)
}