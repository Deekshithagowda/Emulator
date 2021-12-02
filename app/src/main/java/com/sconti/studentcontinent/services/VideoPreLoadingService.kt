package com.sconti.studentcontinent.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheKeyFactory
import com.google.android.exoplayer2.upstream.cache.CacheUtil
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.sconti.studentcontinent.ContinentApplication
import com.sconti.studentcontinent.R
import com.sconti.studentcontinent.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class VideoPreLoadingService :
        IntentService(VideoPreLoadingService::class.java.simpleName) {
    private lateinit var mContext: Context
    private var simpleCache: SimpleCache? = null
    private var cachingJob: Job? = null
    private var videosList: ArrayList<String>? = null

    override fun onHandleIntent(intent: Intent?) {
        mContext = applicationContext
        simpleCache = ContinentApplication.getInstance().simpleCache

        if (intent != null) {
            val extras = intent.extras
            videosList = extras?.getStringArrayList(AppUtils.Constants.VIDEO_LIST)

            if (!videosList.isNullOrEmpty()) {
                preCacheVideo(videosList)
            }
        }
    }

    private fun preCacheVideo(videosList: ArrayList<String>?) {
        var videoUrl: String? = null
        if (!videosList.isNullOrEmpty()) {
            videoUrl = videosList[0]
            videosList.removeAt(0)
        } else {
            stopSelf()
        }
        if (!videoUrl.isNullOrBlank()) {
            val videoUri = Uri.parse(videoUrl)
            val dataSpec = DataSpec(videoUri)
            val defaultCacheKeyFactory = CacheUtil.DEFAULT_CACHE_KEY_FACTORY
            val progressListener =
                    CacheUtil.ProgressListener { requestLength, bytesCached, newBytesCached ->
                        val downloadPercentage: Double = (bytesCached * 100.0 / requestLength)
                        Log.d("Tasg", "Data length:"+downloadPercentage)
                    }
            val dataSource: DataSource =
                    DefaultDataSourceFactory(
                            mContext,
                            Util.getUserAgent(this, getString(R.string.app_name))).createDataSource()

            cachingJob = GlobalScope.launch(Dispatchers.IO) {
                cacheVideo(dataSpec, defaultCacheKeyFactory, dataSource, progressListener)
                preCacheVideo(videosList)
            }
        }
    }

    private fun cacheVideo(
            dataSpec: DataSpec,
            defaultCacheKeyFactory: CacheKeyFactory?,
            dataSource: DataSource,
            progressListener: CacheUtil.ProgressListener
    ) {
        CacheUtil.cache(
                dataSpec,
                simpleCache,
                defaultCacheKeyFactory,
                dataSource,
                progressListener,
                null
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cachingJob?.cancel()
    }
}