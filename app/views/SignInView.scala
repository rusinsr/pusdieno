package views

import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import controllers.routes
import play.api.i18n.{Lang, Messages}
import play.api.mvc.RequestHeader
import play.twirl.api.Html
import views.styles.{CommonStyleSheet, SignInStyleSheet}

import scalatags.Text.all._
import scala.concurrent.{ExecutionContext, Future}
import scalatags.Text
import scalacss.DevDefaults._
import scalacss.ScalatagsCss._

object SignInView {

  def apply(socialProviderRegistry: SocialProviderRegistry)
           (implicit request: RequestHeader, messages: Messages, lang: Lang, ec: ExecutionContext): Future[Html] = {

    val headers = Seq(
      SignInStyleSheet.render[scalatags.Text.TypedTag[String]],
      CommonStyleSheet.render[scalatags.Text.TypedTag[String]]
    )

    val errorSpot: Frag = request.flash.get("error") match {
      case Some(errorMessage) => div(`class` := "alert alert-danger center-block", maxWidth := 480)(
        errorMessage
      )
      case None => UnitFrag(Unit)
    }

    val body: Text.TypedTag[String] = div(`class` := "container", paddingTop := 100)(
      div(`class` := "panel panel-default center-block", maxWidth := 480)(
        div(`class` := "panel-heading")(messages("signin")),
        div(`class` := "panel-body")(
          a(href := routes.SocialAuthController.authenticate("google").url)(
            img(src := "/assets/images/google_sign_in.png", `class` := "img-responsive center-block")
          ),
          div(`class` := "text-center")(messages("or")),
          a(href := routes.SocialAuthController.authenticate("facebook").url)(
            img(src := "/assets/images/facebook_sign_in.png", `class` := "img-responsive center-block")
          )
        )
      ),
      errorSpot
    )

    MainTemplate(messages("signin"), "signin", headers, body)
  }
}