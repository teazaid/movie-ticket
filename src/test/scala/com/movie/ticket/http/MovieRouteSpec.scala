package com.movie.ticket.http

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import com.movie.ticket.models.{RegisterMovieRequest, ReserveSeatRequest}
import com.movie.ticket.repositories.SqlMovieRepository
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

/**
  * Created by Alexander on 02.08.2017.
  */
class MovieRouteSpec extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfterAll {
  private val repository = new SqlMovieRepository()
  private val route = new MovieRoute(repository).route

  private val ok = "ok"

  "MovieRoute" should {
    "register new movie" in {
      val registerRequest = ByteString(RegisterMovieRequest("i17", 10, "m16", Some("movieTitle")).asJson.noSpaces)

      Post("/register",
        HttpEntity(MediaTypes.`application/json`, registerRequest)) ~> route ~> check {
        status.intValue() shouldEqual 200
        responseAs[String] shouldEqual ok
      }
    }

    "reserve a seat" in {
      val reserveSeat = ByteString(ReserveSeatRequest("i17", "m16").asJson.noSpaces)

      Post("/reserve",
        HttpEntity(MediaTypes.`application/json`, reserveSeat)) ~> route ~> check {
        status.intValue() shouldEqual 200
        responseAs[String] shouldEqual ok
      }
    }

    "retrieve info about existing movie" in {
      Get("/info?imdbId=i17&screenId=m16") ~> route ~> check {
        status.intValue() shouldEqual 200
        responseAs[String] shouldEqual 
          """{"imdbId":"i17","availableSeats":9,"reservedSeats":1,"screenId":"m16","movieTitle":"movieTitle"}"""
      }
    }

    "retrieve non existing movie" in {
      Get("/info?imdbId=i17&screenId=m11") ~> route ~> check {
        status.intValue() shouldEqual 200
        responseAs[String] shouldEqual "Couldn't find the movie"
      }
    }
  }

  override protected def afterAll(): Unit = {
    repository.destroy()
  }
}
