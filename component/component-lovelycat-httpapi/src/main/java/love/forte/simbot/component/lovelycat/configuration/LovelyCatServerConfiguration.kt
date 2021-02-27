/*
 *
 *  * Copyright (c) 2020. ForteScarlet All rights reserved.
 *  * Project  simple-robot-S
 *  * File     LovelyCatServerConfiguration.kt
 *  *
 *  * You can contact the author through the following channels:
 *  * github https://github.com/ForteScarlet
 *  * gitee  https://gitee.com/ForteScarlet
 *  * email  ForteScarlet@163.com
 *  * QQ     1149159218
 *  *
 *  *
 *
 */

package love.forte.simbot.component.lovelycat.configuration

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import love.forte.common.ioc.DependCenter
import love.forte.common.ioc.InstanceBeanDepend
import love.forte.common.ioc.annotation.ConfigBeans
import love.forte.common.ioc.annotation.Depend
import love.forte.simbot.component.lovelycat.LovelyCatApiManager
import love.forte.simbot.component.lovelycat.LovelyCatHttpServer
import love.forte.simbot.component.lovelycat.LovelyCatKtorHttpServer
import love.forte.simbot.component.lovelycat.message.event.LovelyCatParser
import love.forte.simbot.core.configuration.ComponentBeans
import love.forte.simbot.listener.MsgGetProcessor
import love.forte.simbot.serialization.json.JsonSerializerFactory

/**
 * lovely cat server配置。
 * @property jsonSerializerFactory JsonSerializerFactory
 */
@ConfigBeans("lovelyCatServerConfiguration")
public class LovelyCatServerConfiguration{

    @Depend
    lateinit var jsonSerializerFactory: JsonSerializerFactory

    @Depend
    lateinit var dependCenter: DependCenter


    /**
     *
     * 注入lovely cat的http server 实例。
     *
     * @param applicationEngineFactory ApplicationEngineFactory<ApplicationEngine, out Configuration>
     * @param lovelyCatServerProperties LovelyCatServerProperties
     * @return LovelyCatHttpServer
     */
    @ComponentBeans("lovelyCatHttpServer")
    public fun lovelyCatHttpServer(
        lovelyCatServerProperties: LovelyCatServerProperties,
        lovelyCatParser: LovelyCatParser,
        apiManager: LovelyCatApiManager,
        msgGetProcessor: MsgGetProcessor,
    ): LovelyCatHttpServer {
        //
        val applicationEngineFactory: ApplicationEngineFactory<ApplicationEngine, out ApplicationEngine.Configuration> =
            dependCenter.getOrNull(ApplicationEngineFactory::class.java) ?: run {
                Netty.also {
                    dependCenter.register(InstanceBeanDepend("applicationEngineFactory", instance = it))
                }
            }


        return LovelyCatKtorHttpServer(
            lovelyCatParser,
            applicationEngineFactory,
            apiManager,
            jsonSerializerFactory,
            msgGetProcessor,
            lovelyCatServerProperties
        )
    }

}

