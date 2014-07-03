package si.urbas.pless.json

import play.api.libs.json.{DefaultWrites, JsValue, Writes}

import scala.collection.JavaConversions._

trait JavaDefaultWrites extends DefaultWrites {

  implicit object JavaIntegerWrites extends Writes[java.lang.Integer] {
    override def writes(number: java.lang.Integer): JsValue = IntWrites.writes(number)
  }

  implicit object JavaLongWrites extends Writes[java.lang.Long] {
    override def writes(number: java.lang.Long): JsValue = LongWrites.writes(number)
  }

  implicit object JavaFloatWrites extends Writes[java.lang.Float] {
    override def writes(number: java.lang.Float): JsValue = FloatWrites.writes(number)
  }

  implicit object JavaDoubleWrites extends Writes[java.lang.Double] {
    override def writes(number: java.lang.Double): JsValue = DoubleWrites.writes(number)
  }

  implicit def javaListWrites[T: Writes]: Writes[java.util.List[T]] = new Writes[java.util.List[T]] {
    override def writes(theList: java.util.List[T]): JsValue = {
      traversableWrites[T].writes(theList)
    }
  }

  implicit def javaMapWrites[T: Writes]: Writes[java.util.Map[String, T]] = new Writes[java.util.Map[String, T]] {
    override def writes(theMap: java.util.Map[String, T]): JsValue = {
      mapWrites[T].writes(theMap.toMap)
    }
  }

  implicit def javaIntegerMapWrites[T: Writes]: Writes[java.util.Map[java.lang.Integer, T]] = new Writes[java.util.Map[java.lang.Integer, T]] {
    override def writes(theMap: java.util.Map[java.lang.Integer, T]): JsValue = {
      mapWrites[T].writes(theMap.map{
        case (key, value) => (key.toString, value)
      }.toMap)
    }
  }

  implicit def javaLongMapWrites[T: Writes]: Writes[java.util.Map[java.lang.Long, T]] = new Writes[java.util.Map[java.lang.Long, T]] {
    override def writes(theMap: java.util.Map[java.lang.Long, T]): JsValue = {
      mapWrites[T].writes(theMap.map{
        case (key, value) => (key.toString, value)
      }.toMap)
    }
  }

}
