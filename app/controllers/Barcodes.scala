package controllers

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.lang.IllegalArgumentException
import javax.inject._

import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
import org.krysalis.barcode4j.impl.upcean.EAN13Bean
import play.api.mvc.{ AbstractController, Action, ControllerComponents }

@Singleton
class Barcodes @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  val ImageResolution = 144

  def barcode(ean: Long) = Action {
    val MimeType = "image/png"
    try {
      val imageData = ean13BarCode(ean, MimeType)
      Ok(imageData).as(MimeType)
    } catch {
      case e: IllegalArgumentException => {
        BadRequest(s"Couldn't generate bar code. Error: $e.getMessage")
      }
    }
  }

  def ean13BarCode(ean: Long, mimeType: String): Array[Byte] = {
    val output: ByteArrayOutputStream = new ByteArrayOutputStream
    val canvas: BitmapCanvasProvider = new BitmapCanvasProvider(
      output,
      mimeType,
      ImageResolution,
      BufferedImage.TYPE_BYTE_BINARY,
      false,
      0
    )

    new EAN13Bean().generateBarcode(canvas, String valueOf ean)
    canvas.finish

    output.toByteArray
  }
}
