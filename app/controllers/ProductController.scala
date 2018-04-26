package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.i18n.I18nComponents

import models.Product

@Singleton
class ProductController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc)
  with play.api.i18n.I18nSupport {

  def list = Action { implicit request =>
    Ok(views.html.list(Product.findAll))
  }
}