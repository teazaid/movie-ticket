import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.movie.ticket.http.MovieRoute
import com.movie.ticket.repositories.SqlMovieRepository

import scala.io.StdIn

/**
  * Created by Alexander on 02.08.2017.
  */
object Boot {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val movieRepository = new SqlMovieRepository()
    val movieRoute = new MovieRoute(movieRepository)

    val bindingFuture = Http().bindAndHandle(movieRoute.route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
