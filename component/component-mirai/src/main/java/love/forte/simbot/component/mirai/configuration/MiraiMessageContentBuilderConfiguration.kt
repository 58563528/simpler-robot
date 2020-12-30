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

package love.forte.simbot.component.mirai.configuration

import love.forte.common.ioc.annotation.ConfigBeans
import love.forte.simbot.api.message.MessageContentBuilderFactory
import love.forte.simbot.component.mirai.message.MiraiMessageContentBuilderFactory
import love.forte.simbot.core.configuration.ComponentBeans

/**
 *
 * @author ForteScarlet -> https://github.com/ForteScarlet
 */
@ConfigBeans("miraiMessageContentBuilderConfiguration")
public class MiraiMessageContentBuilderConfiguration {

    /**
     * mirai的content builder factory.
     */
    @ComponentBeans(value = "miraiMessageContentBuilderFactory", init = false)
    fun miraiMessageContentBuilderFactory(): MessageContentBuilderFactory = MiraiMessageContentBuilderFactory


}