package si.urbas.pless.util

import play.api.libs.json.{Json, JsObject, JsString}
import play.mvc.Results.Status
import si.urbas.pless.json.JsonResults

object ApiResponses {

  val RESPONSE_FIELD_MESSAGE = "message"
  val RESPONSE_FIELD_ERROR = "error"

  val success = JsonResults.okJson(Json.obj())
  val error = JsonResults.badRequestJson(Json.obj())

  def message(message: String): Status = JsonResults.okJson(JsObject(Seq(
    RESPONSE_FIELD_MESSAGE -> JsString(message)
  )))

  def error(errorMessage: String): Status = JsonResults.badRequestJson(JsObject(Seq(
    RESPONSE_FIELD_ERROR -> JsString(errorMessage)
  )))
}
