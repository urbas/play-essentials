package si.urbas.pless.users.json

import si.urbas.pless.users.PlessUser
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.Date

object PlessUserJsonViews {

  def publicUserInfo(user: PlessUser): JsValue = {
    publicUserInfoWriter.writes(user)
  }

  def publicUserInfo(userJson: String): PlessUser = {
    publicUserInfoReader.reads(Json.parse(userJson)).get
  }

  private lazy val publicUserInfoWriter: Writes[PlessUser] = {
    ((JsPath \ "id").write[Long] and
      (JsPath \ "email").write[String] and
      (JsPath \ "username").write[String] and
      (JsPath \ "creationDate").write[Date])(
        (user) =>
          (user.getId, user.getEmail, user.getUsername, user.getCreationDate)
      )
  }

  private lazy val publicUserInfoReader: Reads[PlessUser] = {
    ((JsPath \ "id").read[Long] and
      (JsPath \ "email").read[String] and
      (JsPath \ "username").read[String] and
      (JsPath \ "creationDate").read[Date])(
        (id, email, username, creationDate) =>
          new PlessUser(id, email, username, null, null, creationDate, false, null)
      )
  }

}
