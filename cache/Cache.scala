// Copyright 2011 i-MD. All rights reserved.

package com.imd.app.cache

import org.apache.thrift.TBase

object Cache extends CacheApi {

  private lazy val cacheProxy: CacheApi = getProxy
  private def getProxy: CacheApi = {
    // TODO(timgreen): current only have one Impls
    new BasicCache
  }

  /** {@inhertDoc} */
  override def getOrElse[T <: AnyRef](key: String, expiration: Int, elseValue: => T)
    (implicit m: Manifest[T]): T = {
    cacheProxy.getOrElse(key, expiration, elseValue)
  }

  /** {@inhertDoc} */
  override def set[T <: AnyRef](key: String, expiration: Int, value: T)
    (implicit m: Manifest[T]): T = {
    cacheProxy.set(key, expiration, value)
  }

  /** {@inhertDoc} */
  override def update[T <: AnyRef](key: String, expiration: Int, value: T)
    (implicit m: Manifest[T]): T = {
    cacheProxy.update(key, expiration, value)
  }

  /** {@inhertDoc} */
  override def clear()= {
    cacheProxy.clear()
  }
}
