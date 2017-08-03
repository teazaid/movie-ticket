package com.movie.ticket.http

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import com.movie.ticket.models.{CreateMovieRequest, Movie, ReserveRequest}
import com.movie.ticket.repositories.MovieRepository
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

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
        entity(as[CreateMovieRequest]) { request =>
          val registeredMovie = movieRepository.create(Movie(
            None,
            request.imdbId,
            request.availableSeats,
            0,
            request.screenId,
            request.movieTitle
          ))

          onComplete(registeredMovie) { _ =>
            complete(HttpEntity(ContentTypes.`application/json`, "ok"))
          }
        }
      }
    } ~ path("reserve") {
      post {
        entity(as[ReserveRequest]) { request =>
          onComplete(movieRepository.reserve(request.imdbId, request.screenId)) { _ =>
            complete(HttpEntity(ContentTypes.`application/json`, "ok"))
          }
        }
      }
    } ~ path("info") {
      parameters('imdbId, 'screenId) { (imdbId, screenId) =>
        get {
          onComplete(movieRepository.read(imdbId, screenId)) { movieTry =>
            val movieInfo = movieTry.toOption.flatten.map(_.asJson.noSpaces).getOrElse("Couldn't find the movie")
            complete(HttpEntity(ContentTypes.`application/json`, movieInfo))
          }
        }
      }
    }
}
