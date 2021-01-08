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

package love.forte.test.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.events.GroupMsg;

/**
 * @author ForteScarlet
 */
@Beans
// @OnPrivate
public class TestListener {


    @OnGroup
    public void lis3(GroupMsg msg) {
        GroupAccountInfo accountInfo = msg.getAccountInfo();
        System.out.println(accountInfo.getAccountNickname() + ":" + accountInfo.getAccountTitle());
    }

}
