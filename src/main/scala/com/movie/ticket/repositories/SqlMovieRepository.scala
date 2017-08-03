package com.movie.ticket.repositories

import com.movie.ticket.models.RepositoryInteractionStatus._
import com.movie.ticket.models.{Movie, RepositoryInteractionStatus}
import scalikejdbc._
import scalikejdbc.config._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Alexander on 01.08.2017.
  */
class SqlMovieRepository(implicit val executionContext: ExecutionContext) extends MovieRepository {
  init()

  private[repositories] def destroy(): Unit = {
    DB autoCommit { implicit session =>
      sql"drop table movies;".stripMargin.execute().apply()
    }

    DBs.closeAll()
  }

  private def init(): Unit = {
    DBs.setupAll()

    DB autoCommit { implicit session =>
      sql"""
         |create table if not exists movies(
         |id bigint AUTO_INCREMENT,
         |imdbId varchar(255) not null,
         |totalSeats int default 100 not null ,
         |reservedSeats int default 0 not null ,
         |screenId varchar(255) not null ,
         |moviesTitle varchar(255) not null
         |);
         |CREATE UNIQUE INDEX if not exists imdbId_screenId ON movies (imdbId, screenId);
         """.stripMargin.execute().apply()
    }
  }

  override def reserve(imdbId: String, screenId: String): Future[RepositoryInteractionStatus] = Future {
    DB autoCommit { implicit session =>
      sql"""
         UPDATE movies
         SET reservedSeats = (select reservedSeats + 1 from movies where totalSeats - reservedSeats > 0 and imdbId = ${imdbId} and screenId = ${screenId})
         WHERE imdbId = ${imdbId} and screenId = ${screenId};
        """.execute().apply()
    }
    RepositoryInteractionStatus.Success
  }.recover { case _: Throwable => RepositoryInteractionStatus.Failure }

  override def create(movieToCreate: Movie): Future[RepositoryInteractionStatus] = Future {
    DB autoCommit { implicit session =>
      sql"""
        insert into movies(imdbId, totalSeats, reservedSeats, screenId, moviesTitle)
           values (${movieToCreate.imdbId}, ${movieToCreate.totalSeats}, ${movieToCreate.reservedSeats}, ${movieToCreate.screenId}, ${movieToCreate.movieTitle});
         """.update().apply()
    }
    RepositoryInteractionStatus.Success
  }.recover { case _: Throwable => RepositoryInteractionStatus.Failure }

  override def read(imdbId: String, screenId: String): Future[Option[Movie]] = Future {
    DB readOnly { implicit session =>
      sql"select * from movies where imdbId=${imdbId} and screenId=${screenId}".map(movieMapper).single().apply()
    }
  }.recover { case _: Throwable => None }

  private val movieMapper: WrappedResultSet => Movie = {
    rs: WrappedResultSet =>
      Movie(
        rs.longOpt("id"),
        rs.string("imdbId"),
        rs.int("totalSeats"),
        rs.int("reservedSeats"),
        rs.string("screenId"),
        rs.string("moviesTitle")
      )
  }
}
