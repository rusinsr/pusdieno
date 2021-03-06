package views

import play.api.i18n.{Lang, Messages}
import play.api.mvc.RequestHeader
import play.twirl.api.Html

import scala.concurrent.{ExecutionContext, Future}
import scalatags.Text.all._
import play.api.mvc.Results._

object ErrorView {

  def apply(errorTitle: String, errorMessage: String, image: String = "server_error")
           (implicit messages: Messages): Html = {

    val headers = UnitFrag(Unit)

    val body: Frag = div(`class` := "content", paddingTop := 100)(
      div(`class` := "row")(
        div(`class` := "col-md-6")(
          div(`class` := "alert alert-danger center-block")(
            strong(errorTitle),
            br,
            textarea(errorMessage)
          )
        ),
        div(`class` := "col-md-6")(
          img(src := s"/assets/images/$image.png", `class` := "img-responsive center-block")
        )
      )
    )

    MainTemplate.apply("Error", "error", headers, body, None)
  }


  def unimplemented(optionalMessage: String = "")(implicit messages: Messages): Html = {
    val headers = UnitFrag(Unit)

    val body: Frag = div(`class` := "content", paddingTop := 100)(
      div(`class` := "row")(
        div(`class` := "col-md-6")(
          strong(paddingTop := 100)(messages("error.unimplemented")),
          optionalMessage
        ),
        div(`class` := "col-md-6")(
          img(src := "/assets/images/server_error.png", `class` := "img-responsive center-block")
        )
      )
    )

    MainTemplate.apply("Page not found", "error", headers, body, None)
  }

  def unauthorized(message: String)(implicit messages: Messages) =
    Forbidden(ErrorView(messages("error.forbidden_access"), message, "unauthorized_error"))
}
