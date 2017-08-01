package com.movie.ticket.repositories

import com.movie.ticket.models.CreateMovieStatus._
import com.movie.ticket.models.Movie

import scala.concurrent.Future

/**
  * Created by Alexander on 02.08.2017.
  */
trait MovieRepository {
  def create(movieToCreate: Movie): Future[CreateMovieStatus]
  def read(imdbId: String): Future[Option[Movie]]
}
