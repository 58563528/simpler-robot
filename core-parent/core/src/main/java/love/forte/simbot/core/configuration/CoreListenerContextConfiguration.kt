/*
 *
 *  * Copyright (c) 2020. ForteScarlet All rights reserved.
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

package love.forte.simbot.core.configuration

import love.forte.common.ioc.annotation.ConfigBeans
import love.forte.simbot.api.SimbotExperimentalApi
import love.forte.simbot.core.listener.ListenerContextFactoryImpl
import love.forte.simbot.listener.ListenerContextFactory


/**
 *
 * 配置监听上下文相关。
 *
 * @author ForteScarlet
 */
@ConfigBeans("coreListenerContextConfiguration")
public class CoreListenerContextConfiguration {

    // /**
    //  * 使用单例对象 [CoreContextMapFactory]。
    //  */
    // @CoreBeans("coreContextMapFactory")
    // fun coreContextMapFactory(): ContextMapFactory = CoreContextMapFactory


    /**
     * 配置 [ListenerContextFactoryImpl]
     */
    @OptIn(SimbotExperimentalApi::class)
    @CoreBeans("coreListenerContextFactory")
    fun coreListenerContextFactory(): ListenerContextFactory = ListenerContextFactoryImpl

}