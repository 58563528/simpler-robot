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

package love.forte.simbot.component.mirai.message.result

import love.forte.simbot.api.message.containers.DetailAccountInfo
import love.forte.simbot.api.message.containers.FriendAccountInfo
import love.forte.simbot.api.message.containers.Gender
import love.forte.simbot.api.message.results.FriendInfo
import love.forte.simbot.component.mirai.message.MiraiFriendAccountInfo
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Stranger

/**
 *
 * mirai的 [FriendInfo] 实现。
 *
 * @author ForteScarlet -> https://github.com/ForteScarlet
 */
public class MiraiFriendInfo(friend: Friend) :
    FriendInfo,
    FriendAccountInfo, DetailAccountInfo {

    private val friendAccountInfo = MiraiFriendAccountInfo(friend)

    override val accountCode: String get() = friendAccountInfo.accountCode
    override val accountNickname: String get() = friendAccountInfo.accountNickname
    override val accountRemark: String get() = friendAccountInfo.accountRemark
    override val accountAvatar: String get() = friendAccountInfo.accountAvatar
    override val level: Long get() = friendAccountInfo.level
    override val age: Int get() = friendAccountInfo.age
    override val email: String get() = friendAccountInfo.email
    override val phone: String? get() = friendAccountInfo.phone
    override val gender: Gender get() = friendAccountInfo.gender
    override val signature: String get() = friendAccountInfo.signature

    /** mirai不支持获取好友分组。(mirai 2.6) */
    override val grouping: String? get() = null
    override val originalData: String = "MiraiFriendInfo($friend)"
}


/**
 * mirai-bot陌生人的 [FriendInfo] 实现。
 */
public class MiraiStrangerInfo(stranger: Stranger) :
    FriendInfo,
    FriendAccountInfo by MiraiStrangerInfo(stranger) {

    /** mirai不支持获取好友分组。(mirai 2.6) */
    override val grouping: String? get() = null

    override val originalData: String = "MiraiStrangerInfo($stranger)"
}