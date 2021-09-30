package love.forte.simbot.component.kaiheila.event.system.guild

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 自己退出服务器
 *
 * `self_exited_guild`
 *
 * @author ForteScarlet
 */
@Serializable
public data class SelfExitedGuildExtraBody(
    @SerialName("guild_id")
    val guildId: String
) : GuildEventExtraBody