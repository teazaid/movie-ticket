package com.movie.ticket.models

/**
  * Created by Alexander on 01.08.2017.
  */
case class Movie(id: Option[Long] = None,
                 imdbId: String,
                 availableSeats: Long,
                 screenId: String,
                 movieTitle: String)
