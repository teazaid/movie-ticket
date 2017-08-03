package com.movie.ticket.repositories

import com.movie.ticket.models.Movie
import com.movie.ticket.models.RepositoryInteractionStatus._

import scala.concurrent.Future

/**
  * Created by Alexander on 02.08.2017.
  */
trait MovieRepository {
  def create(movieToCreate: Movie): Future[RepositoryInteractionStatus]
  def read(imdbId: String, screenId: String): Future[Option[Movie]]
  def reserve(imdbId: String, screenId: String): Future[RepositoryInteractionStatus]
}
