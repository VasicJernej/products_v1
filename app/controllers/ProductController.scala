package controllers

import javax.inject._

import play.api._
import play.api.data.{ Form, Forms }
import play.api.i18n.I18nComponents
import play.api.mvc.Flash
import play.api.mvc._

import models.Product

@Singleton
class ProductController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc)
  with play.api.i18n.I18nSupport {
  def list = Action { implicit request =>
    Ok(views.html.products.list(Product.findAll))
  }

  def show(ean: Long) = Action {
    implicit request =>
      Product.findByEan(ean) map {
        product => Ok(views.html.products.details(product))
      } getOrElse (NotFound)
  }

  private val productForm: Form[Product] = {
    Form(
      Forms.mapping(
        "ean" -> Forms.longNumber.verifying(
          "validation.ean.duplicate",
          Product.findByEan(_).isEmpty
        ),
        "name" -> Forms.nonEmptyText,
        "description" -> Forms.nonEmptyText
      )(Product.apply)(Product.unapply)
    )
  }

  def save = Action { implicit request =>
    {
      productForm.bindFromRequest().fold(
        hasErrors = {
          form =>
            Redirect(routes.ProductController.newProduct()).flashing(
              Flash(form.data) + ("error" -> request.messages.apply("validation.errors"))
            )
        },
        success = { newProduct =>
          {
            Product.add(newProduct)
            val message = request.messages.apply("products.new.success", newProduct.name)
            Redirect(routes.ProductController.show(newProduct.ean)).flashing("success" -> message)
          }
        }
      )
    }
  }

  def newProduct = Action { implicit request =>
    {
      val form = if (request.flash.get("error").isDefined) {
        productForm.bind(request.flash.data)
      } else productForm
      Ok(views.html.products.editProduct(form))
    }
  }
}
