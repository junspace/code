// Copyright 2012 i-MD. All rights reserved.

package com.imd.home.routes.client

import org.scalatra._

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.imd.home.ImdSupport
import com.imd.home.controllers.DocSearchController
import com.imd.home.controllers.UtilController
import com.imd.webframework.ForbiddenException
import com.imd.webframework.UnauthorizedException
import com.imd.docsearch.fulltext.{FetchStatus, FetchDocStatus, FullTextFetchInfo}
import com.imd.docsearch.fulltext.{UserDocLimit, ADTYPE, ClientType, FullTextDownloadInfo}
import com.imd.docsearch.fulltext.{DownloadFullTextPdfStream}
import com.imd.user.controllers.UserController
import com.imd.home.controllers.docsearch.UserDocsController
import com.imd.user.data.scalawrapper.{UserInfo => SUserInfo}
import com.imd.user.controllers.{UserController => ImdUserController}

import com.imd.docsearch.fulltext.download.DownloadStatus
import com.imd.home.data.docsearch.SimpleQuery
import com.imd.home.helpers.Helper
import com.imd.home.routes.docsearch.BaseDocsFulltextMixin
import java.io.OutputStream
import java.util.ArrayList

import com.imd.util.thrift.ThriftSerializer;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Handler doc search.
 *
 * @author huajie.wu@i-md.com (Wu Huajie)
 */
trait ClientDocSearchMixin extends BaseDocsFulltextMixin {
  self: ScalatraServlet =>

  private val logger = LoggerFactory.getLogger(this.getClass)

  import com.imd.app.cache.Cache
  import com.imd.util.time.Time
  import com.imd.util.time.Conversions._

  get("/client/latest/?") {
    val offset = params.getOrElse("offset", "0").toInt
    val limit = params.getOrElse("limit", "10").toInt
    Cache.getOrElse(
      "latest_" + offset + "_" + limit + "_" + Time.now.inMinutes,
      1.minutes.inSeconds,
      ThriftSerializer.toJson(DocSearchController.getLatest(offset, limit)))
  }

  get("/client/ad/?") {
    Cache.getOrElse(
      "ad_" + Time.now.inHours,
      1.hours.inSeconds,
      ThriftSerializer.toJson(DocSearchController.getAD))
  }

}
