package com.movie.ticket.models

/**
  * Created by Alexander on 02.08.2017.
  */
case class CreateMovieRequest(imdbId: String,
                              availableSeats: Long,
                              screenId: String,
                              movieTitle: String)


