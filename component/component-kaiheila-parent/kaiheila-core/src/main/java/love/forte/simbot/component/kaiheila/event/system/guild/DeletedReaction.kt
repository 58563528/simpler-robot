package love.forte.simbot.component.kaiheila.event.system.guild

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.component.kaiheila.objects.ReactionEmoji

/**
 *
 * 频道内用户取消reaction
 *
 * `deleted_reaction`
 *
 * @author ForteScarlet
 */
@Serializable
class DeletedReactionExtraBody(
    @SerialName("channel_id")
    val channelId: String,
    val emoji: ReactionEmoji,
) : GuildEventExtraBody