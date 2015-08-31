// Copyright 2011 i-MD. All rights reserved.

package com.imd.app.cache

import com.imd.util.time.Time

import org.apache.thrift.TBase

import scala.collection.mutable.{ Map => MMap, SynchronizedMap, HashMap }


/**
 * Basic internal implementation of the Cache API.
 *
 * Idea from Play2.0
 * https://github.com/playframework/Play20/blob/master/framework/play/src/main/scala/play/api/cache/Cache.scala
 *
 * @author timgreen@i-md.com (Tim Green)
 */
class BasicCache extends CacheApi {

  /** http://www.scala-lang.org/docu/files/collections-api/collections_11.html */
  private def makeMap: MMap[String, (Int, Any)] =
    new HashMap[String, (Int, Any)] with SynchronizedMap[String, (Int, Any)]

  private lazy val cache = makeMap

  override def getOrElse[T <: AnyRef](key: String, expiration: Int, elseValue: => T)
    (implicit m: Manifest[T]): T = getFromMap[T](key) match {
    case Some(v) => v
    case None =>
      val v = elseValue
      setToMap(key, expiration, v)
      v
  }

  override def set[T <: AnyRef](key: String, expiration: Int, value: T)
    (implicit m: Manifest[T]): T = {
    setToMap(key, expiration, value)
    value
  }

  override def update[T <: AnyRef](key: String, expiration: Int, value: T)
    (implicit m: Manifest[T]): T = {
    setToMap(key, expiration, value)
    value
  }

  override def clear()= {
    cache.clear()
  }

  private def setToMap(key: String, expiration: Int, value: Any) {
    cache += key -> (Time.now.inSeconds + expiration, value)
  }

  private def getFromMap[T](key: String)(implicit m: Manifest[T]): Option[T] = {
    val value = cache.get(key).filter(_._1 >= Time.now.inSeconds).headOption
    // delete a key only if it's expired
    value match {
      case Some((_, v)) =>
        if (m.erasure.isAssignableFrom(v.getClass)) Some(v.asInstanceOf[T]) else None
      case None =>
        cache -= key
        None
    }
  }
}
