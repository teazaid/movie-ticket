package com.movie.ticket.models

/**
  * Created by Alexander on 01.08.2017.
  */
case class Movie(id: Option[Long] = None,
                 imdbId: String,
                 totalSeats: Long,
                 reservedSeats: Long,
                 screenId: String,
                 movieTitle: String)
