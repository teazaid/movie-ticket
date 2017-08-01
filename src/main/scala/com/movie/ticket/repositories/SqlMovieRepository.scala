package com.movie.ticket.repositories

import com.movie.ticket.models.CreateMovieStatus._
import com.movie.ticket.models.{CreateMovieStatus, Movie}

import scala.concurrent.Future

/**
  * Created by Alexander on 01.08.2017.
  */
class SqlMovieRepository extends MovieRepository {
  def create(movieToCreate: Movie): Future[CreateMovieStatus] = {
    Future.successful(CreateMovieStatus.Success)
  }

  override def read(imdbId: String): Future[Option[Movie]] = {
    Future.successful(None)
  }
}
