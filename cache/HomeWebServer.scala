// Copyright 2012 i-MD. All rights reserved.

package com.imd.home

import com.imd.app.BaseApp
import com.imd.app.JettyWebServerSupport
import com.imd.app.cache.Cache
import com.imd.util.time.Time
import com.imd.util.time.Conversions._
import java.util.Timer
import java.util.TimerTask
import java.util.Date

import org.eclipse.jetty.servlet.{ServletContextHandler, ServletHolder}

/**
 * clear cache task
 */
class ClearCacheTask extends TimerTask {
  override def run() {
    println("clear cache!")
    Cache.clear()
  }
}

/**
 * i-MD Home Web Server.
 *
 * @author timgreen@i-md.com (Tim Green)
 */
object HomeWebServer extends BaseApp("HomeWebServer") with JettyWebServerSupport {

  override def configWebServer(context: ServletContextHandler) {
    context.addServlet(new ServletHolder(new Router), "/*")
  }

  addSettings(
    HomeWebSetting,
    com.imd.app.data.mongo.MongoDbSetting,
    com.imd.docsearch.article.DocArticleOpServiceClientSetting,
    com.imd.docsearch.fulltext.FullTextServiceClientSetting,
    com.imd.docsearch.indexing.storage.RedisIdManagerSetting,
    com.imd.docsearch.service.SearchServiceClientSetting,
    com.imd.docsearch.service.docinfo.DocInfoServiceClientSetting,
    com.imd.docsearch.service.similardocs.SimilarDocsServiceClientSetting,
    com.imd.home.data.docsearch.SuggestSolrClientSetting,
    com.imd.i18n.I18nSetting,
    com.imd.loadbalancer.service.LoadBalancerServiceClientSetting,
    com.imd.mailserver.MailServiceClientSetting,
    com.imd.sitesearch.SiteSearchServiceClientSetting,
    com.imd.sms.SMSServiceClientSetting,
    com.imd.util.crypto.AuthSetting,
    com.imd.sms.SMSServiceClientSetting,
    com.imd.mailserver.MailServiceClientSetting,
    com.imd.user.service.UserServiceClientSetting
  )

  addRunBlock { args =>
    val timer = new Timer()
    timer.schedule(new ClearCacheTask(), new Date(), 24.hours.inMilliseconds)
  }
}
