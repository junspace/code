// Copyright 2011 i-MD. All rights reserved.

package com.imd.app.cache

import org.apache.thrift.TBase


trait CacheApi {

  /**
   * @param expiration denote the cache should expired in *expiration* seconds.
   */
  def getOrElse[T <: AnyRef](key: String, expiration: Int, elseValue: => T)
    (implicit m: Manifest[T]): T

  /**
   * @param expiration denote the cache should expired in *expiration* seconds.
   */
  def set[T <: AnyRef](key: String, expiration: Int, value: T)(implicit m: Manifest[T]): T

  /**
   * @param expiration denote the cache should expired in *expiration* seconds.
   */
  def update[T <: AnyRef](key: String, expiration: Int, value: T)(implicit m: Manifest[T]): T

  /**
   * clear the cache 
   */
  def clear()
}
