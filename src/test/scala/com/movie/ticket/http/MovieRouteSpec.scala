package com.movie.ticket.http

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import com.movie.ticket.models.{CreateMovieRequest, ReserveRequest}
import com.movie.ticket.repositories.SqlMovieRepository
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by Alexander on 02.08.2017.
  */
class MovieRouteSpec extends WordSpec with Matchers with ScalatestRouteTest {
  private val route = new MovieRoute(new SqlMovieRepository()).route

  private val ok = "ok"

  "MovieRoute" should {
    "register new movie" in {
      val jsonRequest = ByteString(CreateMovieRequest("i17", 10, "m16", "movieTitle").asJson.noSpaces)

      Post("/register",
        HttpEntity(MediaTypes.`application/json`, jsonRequest)) ~> route ~> check {
        status.intValue() shouldEqual 200
        responseAs[String] shouldEqual ok
      }
    }

    "reserve a seat" in {
      val jsonRequest = ByteString(ReserveRequest("i17", "m16").asJson.noSpaces)

      Post("/reserve",
        HttpEntity(MediaTypes.`application/json`, jsonRequest)) ~> route ~> check {
        status.intValue() shouldEqual 200
        responseAs[String] shouldEqual ok
      }
    }

    "retrieve info about existing movie" in {
      Get("/info?imdbId=i17&screenId=m16") ~> route ~> check {
        status.intValue() shouldEqual 200
        responseAs[String] shouldEqual 
          """{"id":1,"imdbId":"i17","totalSeats":10,"reservedSeats":1,"screenId":"m16","movieTitle":"movieTitle"}"""
      }
    }

    "retrieve non existing movie" in {
      Get("/info?imdbId=i17&screenId=m11") ~> route ~> check {
        status.intValue() shouldEqual 200
        responseAs[String] shouldEqual "Couldn't find the movie"
      }
    }
  }
}
