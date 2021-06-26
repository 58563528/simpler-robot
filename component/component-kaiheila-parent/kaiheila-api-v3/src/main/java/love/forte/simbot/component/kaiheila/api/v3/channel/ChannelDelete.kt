/*
 *
 *  * Copyright (c) 2021. ForteScarlet All rights reserved.
 *  * Project  simple-robot
 *  * File     MiraiAvatar.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *
 */

package love.forte.simbot.component.kaiheila.api.v3.channel

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.component.kaiheila.api.*


/**
 *
 * [删除频道](https://developer.kaiheila.cn/doc/http/channel#%E5%88%A0%E9%99%A4%E9%A2%91%E9%81%93)
 *
 *
 * @author ForteScarlet
 */
public class ChannelDeleteReq(channelId: String) : ChannelApiReq<EmptyResp> {
    companion object Key : ApiData.Req.Key by key("/channel/delete") {
        private val ROUTE = listOf("channel", "delete")
    }

    override val method: HttpMethod
        get() = HttpMethod.Post

    override val key: ApiData.Req.Key get() = Key

    override val dataSerializer: DeserializationStrategy<EmptyResp> = emptyResp()

    override fun route(builder: RouteInfoBuilder) {
        builder.apiPath = ROUTE
    }

    override val body: Any = Body(channelId)

    @Serializable
    private data class Body(@SerialName("channel_id") val channelId: String)

}