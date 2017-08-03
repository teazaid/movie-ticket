package com.movie.ticket.repositories

import com.movie.ticket.models.{Movie, RepositoryInteractionStatus}
import org.scalatest.{BeforeAndAfterAll, FunSpec}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Alexander on 01.08.2017.
  */
class SqlMovieRepositorySpec extends FunSpec with BeforeAndAfterAll {

  private val FirstMovie = Movie(imdbId = "imdbId",
    totalSeats = 100,
    reservedSeats = 0,
    screenId = "screenId",
    movieTitle = "movieTitle")

  private val NoMoreReservationMovie = Movie(imdbId = "imdbId",
    totalSeats = 100,
    reservedSeats = 100,
    screenId = "screenId1",
    movieTitle = "movieTitle1")

  private val movieRepository = new SqlMovieRepository()

  private val Timeout = 1.second

  describe("MovieRepository") {
    it("should create a new movie and be able to read it") {
      val createMovieResult = Await.result(movieRepository.create(FirstMovie), Timeout)
      assert(createMovieResult == RepositoryInteractionStatus.Success)

      val readMovieResult = Await.result(movieRepository.read(FirstMovie.imdbId, FirstMovie.screenId), Timeout)
      assert(readMovieResult.isDefined)
      val res = readMovieResult.get
      assert(res == FirstMovie.copy(id = res.id))
    }

    it("should create a new movie which already exists") {
      val createMovieResult = Await.result(movieRepository.create(FirstMovie), Timeout)
      assert(createMovieResult == RepositoryInteractionStatus.Failure)
    }

    it("should read un existing movie") {
      val readMovieResult = Await.result(movieRepository.read(FirstMovie.imdbId + 1, FirstMovie.screenId), Timeout)
      assert(readMovieResult.isEmpty)
    }

    it("should make a reservation") {
      val reserveMovieResult = Await.result(movieRepository.reserve(FirstMovie.imdbId, FirstMovie.screenId), Timeout)
      assert(reserveMovieResult == RepositoryInteractionStatus.Success)

      val readMovieResult = Await.result(movieRepository.read(FirstMovie.imdbId, FirstMovie.screenId), Timeout)
      assert(readMovieResult.isDefined)
      val res = readMovieResult.get
      assert(res == FirstMovie.copy(id = res.id, reservedSeats = 1))
    }

    it("should reserve movie with no space for reservation") {
      Await.result(movieRepository.create(NoMoreReservationMovie), Timeout)
      assert(Await.result(movieRepository.reserve(NoMoreReservationMovie.imdbId, NoMoreReservationMovie.screenId), Timeout) == RepositoryInteractionStatus.Failure)
    }
  }

  override protected def afterAll(): Unit = {
    movieRepository.destroy()
  }
}
