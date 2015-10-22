import akka.actor.{ActorSystem, Props, ActorRef, Actor}

// chengtao (cheng.tao@yottabyte.cn)
// 2015/10/22
// Copyright 2015 Yottabyte
//

case class StartMessage
case class PingMessage
case class PongMessage
case class StopMessage

class Ping(pong: ActorRef) extends Actor {
  var count = 0

  def incrementAndPrint() = {
    count = count + 1
    println("ping")
  }

  def receive = {
    case StartMessage =>
      incrementAndPrint()
      pong ! PingMessage
    case PongMessage =>
      incrementAndPrint()
      if (count > 99) {
        sender ! StopMessage
        println("ping stoped")
        context.stop(self)
      } else {
        sender ! PingMessage
      }
  }
}

class Pong extends Actor {
  def receive = {
    case PingMessage =>
      println("Pong")
      sender ! PongMessage
    case StopMessage =>
      println("Pong stoped")
      context.stop(self)
  }
}

object PingPong extends App {
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping(pong)), name = "ping")
  // start them going
  ping ! StartMessage
}
