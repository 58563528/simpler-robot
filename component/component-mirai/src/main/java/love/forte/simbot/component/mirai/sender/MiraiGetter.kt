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

package love.forte.simbot.component.mirai.sender

import love.forte.simbot.api.SimbotExperimentalApi
import love.forte.simbot.api.message.containers.AccountCodeContainer
import love.forte.simbot.api.message.containers.BotContainer
import love.forte.simbot.api.message.containers.BotInfo
import love.forte.simbot.api.message.containers.GroupCodeContainer
import love.forte.simbot.api.message.events.MsgGet
import love.forte.simbot.api.message.results.*
import love.forte.simbot.api.sender.AdditionalApi
import love.forte.simbot.api.sender.Getter
import love.forte.simbot.api.sender.GetterFactory
import love.forte.simbot.component.mirai.MiraiBotAccountInfo
import love.forte.simbot.component.mirai.additional.GetterInfo
import love.forte.simbot.component.mirai.additional.MiraiGetterAdditionalApi
import love.forte.simbot.component.mirai.message.result.*
import love.forte.simbot.http.template.HttpTemplate
import net.mamoe.mirai.Bot


public class MiraiGetterFactory(private val http: HttpTemplate) : GetterFactory {
    override fun getOnMsgGetter(msg: MsgGet, def: Getter.Def): Getter =
        MiraiGetter(Bot.getInstance(msg.botInfo.botCodeNumber), http, defGetter = def)

    override fun getOnBotGetter(bot: BotContainer, def: Getter.Def): Getter =
        MiraiGetter(Bot.getInstance(bot.botInfo.botCodeNumber), http, defGetter = def)
}

public class MiraiGetter(
    private val bot: Bot,
    private val http: HttpTemplate,
    private val defGetter: Getter,
) : Getter {

    private lateinit var _getterInfo: GetterInfo
    private val getterInfo: GetterInfo
    get() {
        if (!::_getterInfo.isInitialized) {
            synchronized(this) {
                if (!::_getterInfo.isInitialized) {
                    _getterInfo = GetterInfo(bot, http)
                }
            }
        }
        return _getterInfo
    }

    @OptIn(SimbotExperimentalApi::class)
    override val authInfo: AuthInfo
        get() = MiraiAuthInfo(UnsafeViolenceAndroidBotCookieUtils.cookies(bot))

    override val botInfo: BotInfo get() = MiraiBotAccountInfo.getInstance(bot)


    /**
     * mirai-获取好友信息。
     */
    private fun getFriendInfo0(code: Long): FriendInfo {
        return bot.friendOrNull(code)?.let { f -> MiraiFriendInfo(f) }
            ?: bot.getStranger(code)?.let { s -> MiraiStrangerInfo(s) }
            ?: throw NoSuchElementException("No such friend or stranger $code from bot ${bot.id}")
    }

    override fun getFriendInfo(code: String): FriendInfo = getFriendInfo0(code.toLong())
    override fun getFriendInfo(code: Long): FriendInfo = getFriendInfo0(code)
    override fun getFriendInfo(code: AccountCodeContainer): FriendInfo = getFriendInfo(code.accountCodeNumber)


    /**
     * mirai - 群友信息。
     */
    private fun getMemberInfo0(group: Long, code: Long): GroupMemberInfo {
        return MiraiGroupMemberInfo(bot.member(group, code))
    }

    override fun getMemberInfo(group: String, code: String): GroupMemberInfo =
        getMemberInfo0(group.toLong(), code.toLong())

    override fun getMemberInfo(group: Long, code: Long): GroupMemberInfo = getMemberInfo0(group, code)
    override fun getMemberInfo(group: GroupCodeContainer, code: AccountCodeContainer): GroupMemberInfo =
        getMemberInfo(group.groupCodeNumber, code.accountCodeNumber)

    /**
     * mirai - 群信息
     */
    private fun getGroupInfo0(group: Long): GroupFullInfo = MiraiGroupFullInfo(bot.group(group))
    override fun getGroupInfo(group: String): GroupFullInfo = getGroupInfo0(group.toLong())
    override fun getGroupInfo(group: Long): GroupFullInfo = getGroupInfo0(group)
    override fun getGroupInfo(group: GroupCodeContainer): GroupFullInfo = getGroupInfo(group.groupCodeNumber)


    override fun getFriendList(cache: Boolean, limit: Int): FriendList = MiraiFriendList(bot, limit)


    override fun getGroupList(cache: Boolean, limit: Int): GroupList = MiraiGroupList(bot, limit)


    /**
     * mirai - group member list.
     */
    private fun getGroupMemberList0(group: Long, limit: Int): GroupMemberList =
        MiraiGroupMemberList(bot.group(group), limit)

    override fun getGroupMemberList(group: String, cache: Boolean, limit: Int): GroupMemberList =
        getGroupMemberList0(group.toLong(), limit)

    override fun getGroupMemberList(group: Long, cache: Boolean, limit: Int): GroupMemberList =
        getGroupMemberList0(group, limit)

    override fun getGroupMemberList(group: GroupCodeContainer, cache: Boolean, limit: Int): GroupMemberList =
        getGroupMemberList(group.groupCodeNumber, cache, limit)


    /**
     * mirai - ban list.
     */
    private fun getBanList0(group: Long, limit: Int): MuteList = MiraiMuteList(bot.group(group), limit)
    override fun getBanList(group: String, cache: Boolean, limit: Int): MuteList = getBanList0(group.toLong(), limit)
    override fun getBanList(group: Long, cache: Boolean, limit: Int): MuteList = getBanList0(group, limit)
    override fun getBanList(group: GroupCodeContainer, cache: Boolean, limit: Int): MuteList =
        getBanList(group.groupCodeNumber, cache, limit)


    /**
     * mirai - group note list.
     * 注：mirai仅支持获取入群公告。(mirai 1.3.2)
     */
    private fun getGroupNoteList0(group: Long): GroupNoteList =
        MiraiGroupNoteList(bot.group(group))

    override fun getGroupNoteList(group: String, cache: Boolean, limit: Int): GroupNoteList =
        getGroupNoteList0(group.toLong())

    override fun getGroupNoteList(group: Long, cache: Boolean, limit: Int): GroupNoteList =
        getGroupNoteList0(group)

    override fun getGroupNoteList(group: GroupCodeContainer, cache: Boolean, limit: Int): GroupNoteList =
        getGroupNoteList(group.groupCodeNumber, cache, limit)


    /**
     * 额外API
     */
    override fun <R : Result> additionalExecute(additionalApi: AdditionalApi<R>): R {
        if (additionalApi is MiraiGetterAdditionalApi) {
            return additionalApi.execute(getterInfo)
        }

        return super.additionalExecute(additionalApi)
    }


}

