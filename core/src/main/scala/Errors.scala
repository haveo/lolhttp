package lol.http

import scala.concurrent.duration.{ FiniteDuration }

/** Panics are errors that should not occur or should not be handled.
  * @param msg the error message.
  */
case class Panic(msg: String) extends RuntimeException(msg)

/** Allows to panic. */
object Panic {

  /** Panic!!! */
  def !!!(msg: String = "Unexpected behavior") = throw Panic(msg)
}

/** Expected errors.
  *
  * See the companion object for all defined errors.
  *
  * @param code the error code.
  * @param msg the error message.
  */
case class Error(code: Int, msg: String) extends RuntimeException(msg) {
  override def equals(other: Any) = other match {
    case Error(otherCode, _) if otherCode == code => true
    case _ => false
  }
}

/** All expected errors. */
object Error {

  /** An HTTP client could not the response because the connection was closed before. */
  val ConnectionClosed = Error(1, "Connection closed")

  /** An HTTP client is already closed and cannot handle new requests. */
  val ClientAlreadyClosed = Error(2, "Client already closed")

  /** A [[Content]] stream has already been consumed. */
  val StreamAlreadyConsumed = Error(3, "The content stream has already been consumed")

  /** It is not possible to upgrade the connection because the server did not allow it. */
  val UpgradeRefused = Error(4, "Connection upgrade was denied by the server")

  /** Auto-redirect is only supported for GET requests. */
  val AutoRedirectNotSupported = Error(6, "Automatic redirects is only allowed for GET requests")

  /** The HTTP request was exepctec to contain a valid `Host` HTTP header. */
  val HostHeaderMissing = Error(7, "The Host header was missing in the request")

  /** The classpath resource was missing so it is not possible to encode it. */
  val ClasspathResourceMissing = Error(8, "Classpath resource does not exist")

  /** The url matcher pattern is invalid. */
  def InvalidUrlMatcher(msg: String = "") = Error(9, s"Invalid url matcher pattern: $msg")

  /** The status code was unexpected. */
  def UnexpectedStatus(msg: String = "Unexpected status code") = Error(10, msg)

  /** An operaion timed out. **/
  def Timeout(duration: FiniteDuration = FiniteDuration(0, "ms")) = Error(11, s"Operation timed out after $duration")

  /** The content type was unexpected. **/
  def UnexpectedContentType(msg: String = "Unexpected content type") = Error(12, msg)
}
