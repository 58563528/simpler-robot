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

package love.forte.simbot.component.kaiheila.`object`

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import love.forte.simbot.component.kaiheila.SerializerModuleRegistrar


/**
 *
 * 开黑啦objects - [角色Role](https://developer.kaiheila.cn/doc/objects#%E8%A7%92%E8%89%B2Role)
 *
 * 官方示例：
 * ```json
 * {
 *     "role_id": 11111,
 *     "name": "新角色",
 *     "color": 0,
 *     "position": 5,
 *     "hoist": 0,
 *     "mentionable": 0,
 *     "permissions": 142924296
 * }
 *
 * ```
 *
 * @author ForteScarlet
 */
public interface Role : KhlObjects {

    /** 角色id */
    val roleId: Int

    /** 角色名称 */
    val name: String

    /** 颜色色值 */
    val color: Int

    /** 顺序位置 */
    val position: Int

    /** 是否为角色设定(与普通成员分开显示) */
    val hoist: Int

    /** 是否允许任何人@提及此角色 */
    val mentionable: Int

    /** 权限码 */
    val permissions: Int

    companion object : SerializerModuleRegistrar {
        override fun SerializersModuleBuilder.serializerModule() {
            polymorphic(Role::class) {
                subclass(RoleImpl::class)
                default { RoleImpl.serializer() }
            }
        }
    }

}


@Serializable
@SerialName(RoleImpl.SERIAL_NAME)
public data class RoleImpl(
    @SerialName("role_id")
    override val roleId: Int,
    override val name: String,
    override val color: Int,
    override val position: Int,
    override val hoist: Int,
    override val mentionable: Int,
    override val permissions: Int
) : Role {
    override val originalData: String get() = toString()
    internal companion object {
        const val SERIAL_NAME = "ROLE_I"
    }
}
