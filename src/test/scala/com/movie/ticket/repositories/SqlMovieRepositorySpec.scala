package com.movie.ticket.repositories

import com.movie.ticket.models.{Movie, Status}
import org.scalatest.{BeforeAndAfterAll, FunSpec}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Alexander on 01.08.2017.
  */
class SqlMovieRepositorySpec extends FunSpec with BeforeAndAfterAll {

  private val NewMovie = Movie(imdbId = "imdbId",
    totalSeats = 100,
    reservedSeats = 0,
    screenId = "screenId",
    movieTitle = "movieTitle")

  private val MovieNotAvailableForReservation = Movie(imdbId = "imdbId",
    totalSeats = 100,
    reservedSeats = 100,
    screenId = "screenId1",
    movieTitle = "movieTitle1")

  private val movieRepository = new SqlMovieRepository()
  private val Timeout = 1.second

  describe("MovieRepository") {
    it("should create a new movie and be able to read it") {
      val createdMovie = Await.result(movieRepository.create(NewMovie), Timeout)
      assert(createdMovie == Status.Success)

      val readMovieResult = Await.result(movieRepository.find(NewMovie.imdbId, NewMovie.screenId), Timeout)
      assert(readMovieResult.isDefined)
      val result = readMovieResult.get
      assert(result == NewMovie.copy(id = result.id))
    }

    it("should create a new movie which already exists") {
      assert(Await.result(movieRepository.create(NewMovie), Timeout) == Status.Failure)
    }

    it("should read non existing movie") {
      assert(Await.result(movieRepository.find(NewMovie.imdbId + 1, NewMovie.screenId), Timeout).isEmpty)
    }

    it("should make a reservation") {
      assert(Await.result(movieRepository.reserveSeat(NewMovie.imdbId, NewMovie.screenId), Timeout) == Status.Success)

      val readMovieOpt = Await.result(movieRepository.find(NewMovie.imdbId, NewMovie.screenId), Timeout)
      assert(readMovieOpt.isDefined)
      val readMovie = readMovieOpt.get
      assert(readMovie == NewMovie.copy(id = readMovie.id, reservedSeats = 1))
    }

    it("should reserve movie with no space for reservation") {
      Await.result(movieRepository.create(MovieNotAvailableForReservation), Timeout)
      assert(Await.result(movieRepository.reserveSeat(MovieNotAvailableForReservation.imdbId, MovieNotAvailableForReservation.screenId), Timeout) == Status.Failure)
    }
  }

  override protected def afterAll(): Unit = {
    movieRepository.destroy()
  }
}
