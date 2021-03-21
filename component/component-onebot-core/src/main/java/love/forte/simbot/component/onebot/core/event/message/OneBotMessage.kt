/*
 *
 *  * Copyright (c) 2020. ForteScarlet All rights reserved.
 *  * Project  component-onebot
 *  * File     OneBotMessageEvent.kt
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

package love.forte.simbot.component.onebot.core.event.message

import love.forte.simbot.component.onebot.core.event.OneBotEvent
import love.forte.simbot.component.onebot.core.event.SubType
import love.forte.simbot.component.onebot.core.event.SubTypeContainer
import love.forte.simbot.component.onebot.core.segment.OneBotMessageSegment


/**
 *
 * onebot 消息类型事件接口。
 *
 * 可参考 [消息事件](https://github.com/howmanybots/onebot/blob/master/v12-draft/specs/event/message.md)
 *
 */
public interface OneBotMessage<S : OneBotMessage.MessageEventSubType> : OneBotEvent, SubTypeContainer<S> {

    /**
     * 事件发生的时间戳.
     */
    override val time: Long

    /**
     * 收到事件的机器人 QQ 号
     */
    override val selfId: Long


    override val postType: String
        get() = "message"

    /**
     * 消息类型。
     *
     * [消息事件](https://github.com/howmanybots/onebot/blob/master/v12-draft/specs/event/message.md)中的两个消息事件均存在此属性。
     * 目前来看，可能存在的值为:
     * - `private`
     * - `group`
     *
     */
    val messageType: String


    /**
     * 消息ID。
     */
    val messageId: String


    /**
     * 消息段内容。
     *
     * 如果类型为 [love.forte.simbot.component.onebot.core.segment.OneBotStringSegment],
     * 则说明消息类型是一个 **字符串** 消息。
     *
     *
     * 其他的则为各自对应的消息段类型。
     *
     */
    val message: OneBotMessageSegment<*>


    /**
     * 原始消息内容。
     */
    val rawMessage: String


    /**
     *
     * 消息事件子类型。
     *
     *
     *
     * 可能的值有：
     *
     * - 私聊消息 (private)
     *    - `friend`
     *    - `group`
     *    - `other`
     *
     * - 群消息 (group)
     *    - `normal`
     *    - `anonymous`
     *    - `notice`
     *
     *
     */
    val subType: String


    /**
     * 用户(发送者)对应账号
     */
    val userId: Long


    /**
     * 发送人信息.
     */
    val sender: Sender


    /**
     * 消息类型下子实现类对应的子类型接口。
     */
    interface MessageEventSubType : SubType


    /**
     * 消息发送者的信息。
     */
    interface Sender {

        /** 发送者 QQ 号 */
        val userId: Long?

        /** 昵称 */
        val nickname: String?

        /**
         * 性别, male 或 female 或 unknown
         * 获取不到则得到null。
         */
        val sex: String?

        /**
         * 年龄.
         */
        val age: Int?

    }

}