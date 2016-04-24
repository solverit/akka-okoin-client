package com.awar

import java.io.IOException

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future

/**
  * @author Solverit
  */

object Starter extends App {

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  lazy val apiFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnectionHttps(config.getString("services.trade-api.host"), config.getInt("services.trade-api.port"))

  def apiRequest(request: HttpRequest): Future[HttpResponse] =
    Source.single(request)
      .via(apiFlow)
      .runWith(Sink.head)

  def fetchInfo(symbol: String): Future[Either[String, String]] = {
    val apiurl = config.getString("services.trade-api.trade")
    apiRequest(RequestBuilding.Get(apiurl+"?symbol="+symbol)).flatMap { response =>
      response.status match {
        case OK =>
          Unmarshal(response.entity).to[String].map(Right(_))
        case BadRequest =>
          Future.successful(Left(s"$symbol: incorrect Symbol format"))
        case _ =>
          Unmarshal(response.entity).to[String].flatMap { entity =>
            val error = s"Trade request failed with status code ${response.status} and entity $entity"
            logger.error(error)
            Future.failed(new IOException(error))
          }
      }
    }
  }

  val res = fetchInfo("btc_cny").map {
    case Right(info) => logger.info("Result: {}", info)
    case Left(errorMessage) => logger.info("Result: {}", errorMessage)
  }
}


