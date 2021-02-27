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

package love.forte.simbot.component.lovelycat.utils

import catcode.CAT_HEAD
import catcode.CatCodeUtil
import catcode.CatEncoder
import catcode.Neko
import catcode.codes.Nyanko


/**
 *
 * 可爱猫特殊符号转cat码。
 * 一般就是at消息。
 *
 * @author ForteScarlet
 */
public object LovelyCatCodeUtil {

    /**
     * 将一段可能存在at消息的文本切割为 neko list.
     * 其中包括 text 类型的neko。
     * [@at,nickname=法欧特斯卡雷特,wxid=wxid_bqy1ezxxkdat22]
     *
     * 由于可爱猫的at消息不存在转义等，因此可能会出现 “at注入”的情况。
     *
     */
    fun splitAtOnTextToNeko(text: String): List<Neko> {
        val firstOf = text.indexOf("[@at")
        return if (firstOf < 0) {
            listOf(CatCodeUtil.toNeko("text", false, "text=${CatEncoder.encodeParams(text)}"))
        } else {
            // println(text.replace("[@at", "${CAT_HEAD}at"))
            // 存在 at, replace.
            CatCodeUtil.split(text.replace("[@at", "${CAT_HEAD}at")) {
                if (startsWith(CAT_HEAD)) {
                    // println("cat: $this")
                    Nyanko.byCode(this.replace(",wxid=", ",code="))
                } else {
                    // is text.
                    // println("txt: $this")
                    CatCodeUtil.toNeko("text", false, "text=${CatEncoder.encodeParams(this)}")
                }
            }
        }
    }

}