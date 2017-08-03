package com.movie.ticket.repositories

import com.movie.ticket.models.Movie
import com.movie.ticket.models.Status._

import scala.concurrent.Future

/**
  * Created by Alexander on 02.08.2017.
  */
trait MovieRepository {
  def create(movieToCreate: Movie): Future[Status]

  def find(imdbId: String, screenId: String): Future[Option[Movie]]

  def reserveSeat(imdbId: String, screenId: String): Future[Status]
}
