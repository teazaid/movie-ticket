package com.movie.ticket.repositories

import com.movie.ticket.models.{CreateMovieStatus, Movie}
import org.scalatest.{BeforeAndAfterAll, FunSpec}

import scalikejdbc.config._
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by Alexander on 01.08.2017.
  */
class MovieRepositorySpec extends FunSpec with BeforeAndAfterAll {
  private val FirstMovie = Movie(imdbId = "imdbId",
    availableSeats = 100,
    screenId = "screenId",
    movieTitle = "movieTitle")

  private val Timeout = 1.second

  describe("MovieRepository") {
    it("should create a new movie and be able to read it") {
      val movieRepository = new SqlMovieRepository()

      val createMovieResult = Await.result(movieRepository.create(FirstMovie), Timeout)
      assert(createMovieResult == CreateMovieStatus.Success)
    }
  }

  override protected def afterAll(): Unit = {
    DBs.setupAll()
  }

  override protected def beforeAll(): Unit = super.beforeAll()
}
