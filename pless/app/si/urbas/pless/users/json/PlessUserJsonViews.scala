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
    ((JsPath \ PlessUser.ID_FIELD).write[Long] and
      (JsPath \ PlessUser.EMAIL_FIELD).write[String] and
      (JsPath \ PlessUser.USERNAME_FIELD).write[String] and
      (JsPath \ PlessUser.CREATION_DATE_FIELD).write[Date])(
        (user) =>
          (user.getId, user.getEmail, user.getUsername, user.getCreationDate)
      )
  }

  private lazy val publicUserInfoReader: Reads[PlessUser] = {
    ((JsPath \ PlessUser.ID_FIELD).read[Long] and
      (JsPath \ PlessUser.EMAIL_FIELD).read[String] and
      (JsPath \ PlessUser.USERNAME_FIELD).read[String] and
      (JsPath \ PlessUser.CREATION_DATE_FIELD).read[Date])(
        (id, email, username, creationDate) =>
          new PlessUser(id, email, username, null, null, creationDate, false, null)
      )
  }

}
