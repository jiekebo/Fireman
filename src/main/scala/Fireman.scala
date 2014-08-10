import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.github.sstone.amqp.Amqp.{DeclareQueue, QueueParameters}
import com.github.sstone.amqp.{Amqp, ChannelOwner, ConnectionOwner}
import com.rabbitmq.client.AMQP.Queue
import com.rabbitmq.client.ConnectionFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object Fireman {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("mySystem")
    implicit val timeout: Timeout = 1.second
    val connFactory = new ConnectionFactory()
    connFactory.setUri("amqp://guest:guest@localhost/%2F")
    val conn = system.actorOf(ConnectionOwner.props(connFactory, 1 second))
    val channel = ConnectionOwner.createChildActor(conn, ChannelOwner.props())

    val Amqp.Ok(_, Some(result: Queue.DeclareOk)) = Await.result(
      (channel ? DeclareQueue(QueueParameters(name = "#", passive = true))).mapTo[Amqp.Ok], 5 seconds
    )

    println("there are %d messages in the queue named %s".format(result.getMessageCount, result.getQueue))
  }
}
