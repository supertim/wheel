// chengtao (cheng.tao@yottabyte.cn)
// 2015/10/22
// Copyright 2015 Yottabyte
//

import akka.actor.{Props, ActorSystem, Actor}
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._


class HelloActor extends Actor {
  def receive = {
    case "hello" =>
      println("hello back to you")
    case _ => println("huh?")
  }
}

class AskActor extends Actor {
  def receive = {
    case "hello" => sender ! "hello back to you"
    case _ => println("huh?")
  }
}

case class AskMessage

object Main extends App {
  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")

  helloActor ! "hello"
  helloActor ! "world"

  val askActor1 = system.actorOf(Props[AskActor], name = "askactor")
  implicit val timeout = Timeout(5)
  val f1 = (askActor1 ? "hello").mapTo[String]

  val result = Await.result(f1, 1 second)

  println("result:" + result)
}
