package com.movie.ticket.http

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ValidationRejection
import com.movie.ticket.models._
import com.movie.ticket.repositories.MovieRepository
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.util.Success

/**
  * Created by Alexander on 02.08.2017.
  */
class MovieRoute(movieRepository: MovieRepository) {
  import FailFastCirceSupport._
  import io.circe.generic.auto._
  import io.circe.syntax._

  val route =
    path("register") {
      post {
        entity(as[RegisterMovieRequest]) { request =>
          val registeredMovie = movieRepository.create(Movie(
            None,
            request.imdbId,
            request.availableSeats,
            0,
            request.screenId,
            request.movieTitle.getOrElse("")
          ))

          onComplete(registeredMovie) { status =>
            status match {
              case Success(Status.Success) => complete(HttpEntity(ContentTypes.`application/json`, "ok"))
              case _ => reject(ValidationRejection("Failed to register a movie"))
            }
          }
        }
      }
    } ~ path("reserve") {
      post {
        entity(as[ReserveSeatRequest]) { request =>
          onComplete(movieRepository.reserveSeat(request.imdbId, request.screenId)) { status =>
            status match {
              case Success(Status.Success) => complete(HttpEntity(ContentTypes.`application/json`, "ok"))
              case _ => reject(ValidationRejection("Failed to make a reservation"))
            }
          }
        }
      }
    } ~ path("info") {
      parameters('imdbId, 'screenId) { (imdbId, screenId) =>
        get {
          onComplete(movieRepository.find(imdbId, screenId)) { movieTry =>
            val movieInfo = movieTry.toOption.flatten.map{ movie =>
              MovieDto(
                movie.imdbId,
                movie.totalSeats -  movie.reservedSeats,
                movie.reservedSeats,
                movie.screenId,
                movie.movieTitle
              ).asJson.noSpaces
            }.getOrElse("Couldn't find the movie")
            complete(HttpEntity(ContentTypes.`application/json`, movieInfo))
          }
        }
      }
    }
}
