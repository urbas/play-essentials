package si.urbas.pless.util

import play.api.libs.json.{JsString, Json, JsObject}

object ApiResponses {
  val RESPONSE_FIELD_MESSAGE = "message"
  val RESPONSE_FIELD_ERROR = "error"

  def message(message: String): JsObject = JsObject(Seq(
    RESPONSE_FIELD_MESSAGE -> JsString(message)
  ))

  def error(errorMessage: String): JsObject = JsObject(Seq(
    RESPONSE_FIELD_ERROR -> JsString(errorMessage)
  ))
}
