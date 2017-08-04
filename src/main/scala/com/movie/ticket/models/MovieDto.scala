package com.movie.ticket.models

/**
  * Created by Alexander on 05.08.2017.
  */
case class MovieDto(
                    imdbId: String,
                    availableSeats: Long,
                    reservedSeats: Long,
                    screenId: String,
                    movieTitle: String)
