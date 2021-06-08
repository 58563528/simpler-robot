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

package love.forte.simbot.component.mirai.message

import catcode.CatCodeUtil
import catcode.Neko
import cn.hutool.core.io.FileUtil
import love.forte.simbot.api.message.MessageContentBuilder
import love.forte.simbot.api.message.MessageContentBuilderFactory
import love.forte.simbot.api.message.containers.AccountCodeContainer
import love.forte.simbot.mark.InstantInit
import love.forte.simbot.processor.RemoteResourceContext
import love.forte.simbot.processor.RemoteResourceInProcessor
import love.forte.simbot.processor.SuspendRemoteResourceInProcessor
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * [MiraiMessageContentBuilder]'s factory.
 */
public sealed class MiraiMessageContentBuilderFactory : MessageContentBuilderFactory {
    /** Mirai组件所使用的消息构建器。 */
    abstract override fun getMessageContentBuilder(): MiraiMessageContentBuilder

    /** 普通的图片上传策略。 */
    internal class MiraiMessageContentBuilderFactoryImgNormal(private val remoteResourceInProcessor: RemoteResourceInProcessor) : MiraiMessageContentBuilderFactory() {
        override fun getMessageContentBuilder(): MiraiMessageContentBuilder = MiraiMessageContentBuilderImgNormal(remoteResourceInProcessor)
    }

    /** 优先尝试通过一个任意的群进行上传的图片上传策略。 */
    internal class MiraiMessageContentBuilderFactoryImgGroupFirst(private val remoteResourceInProcessor: RemoteResourceInProcessor) : MiraiMessageContentBuilderFactory() {
        override fun getMessageContentBuilder(): MiraiMessageContentBuilder = MiraiMessageContentBuilderImgGroupFirst(remoteResourceInProcessor)
    }

    companion object {
        fun instance(imgGroupFirst: Boolean = false, remoteResourceInProcessor: RemoteResourceInProcessor): MiraiMessageContentBuilderFactory {
            return if (imgGroupFirst) {
                MiraiMessageContentBuilderFactoryImgGroupFirst(remoteResourceInProcessor)
            } else {
                MiraiMessageContentBuilderFactoryImgNormal(remoteResourceInProcessor)
            }
        }
    }
}


/**
 * mirai对 [MessageContentBuilder] 的实现。
 * @author ForteScarlet -> https://github.com/ForteScarlet
 */
@Suppress("unused")
public sealed class MiraiMessageContentBuilder : MessageContentBuilder {
    private val texts = StringBuilder()
    private val contentList = mutableListOf<MiraiMessageContent>()
    private fun checkText() {
        if (texts.isNotEmpty()) {
            contentList.add(MiraiSingleMessageContent(PlainText(texts)))
            texts.clear()
        }
    }

    override fun clear(): MiraiMessageContentBuilder {
        texts.clear()
        contentList.clear()
        return this
    }

    override fun text(text: CharSequence): MiraiMessageContentBuilder {
        texts.append(text)
        return this
    }

    override fun atAll(): MiraiMessageContentBuilder {
        checkText()
        contentList.add(MiraiSingleMessageContent(AtAll))
        return this
    }

    private fun at0(code: Long): MiraiMessageContentBuilder {
        checkText()
        contentList.add(MiraiSingleAtMessageContent(code))
        return this
    }

    override fun at(code: String): MiraiMessageContentBuilder = at0(code.toLong())
    override fun at(code: Long): MiraiMessageContentBuilder = at0(code)
    override fun at(code: AccountCodeContainer): MiraiMessageContentBuilder = at(code.accountCodeNumber)

    private fun face0(id: Int): MiraiMessageContentBuilder {
        checkText()
        contentList.add(MiraiSingleMessageContent(Face(id)))
        return this
    }

    override fun face(id: String): MiraiMessageContentBuilder = face0(id.toInt())
    override fun face(id: Int): MiraiMessageContentBuilder = face0(id)

    override fun imageLocal(path: String, flash: Boolean): MiraiMessageContentBuilder {
        val file: File = FileUtil.file(path)?.takeIf { it.exists() } ?: throw FileNotFoundException(path)
        val imageNeko: Neko = CatCodeUtil
            .getNekoBuilder("image", true)
            .key("file").value(path)
            .key("type").value("local")
            .apply {
                if (flash) {
                    key("flash").value(true)
                }
            }.build()
        val imageContent = imageLocal0(file, imageNeko, flash)
        checkText()
        contentList.add(imageContent)
        return this
    }

    abstract fun imageLocal0(file: File, imageNeko: Neko, flash: Boolean): MiraiMessageContent


    override fun imageUrl(url: String, flash: Boolean): MiraiMessageContentBuilder {
        val imageNeko: Neko = CatCodeUtil
            .getNekoBuilder("image", true)
            .key("file").value(url)
            .key("type").value("network")
            .apply {
                if (flash) {
                    key("flash").value(true)
                }
            }.build()
        imageUrl0(url, imageNeko, flash).apply {
            checkText()
            contentList.add(this)
        }
        return this
    }

    abstract fun imageUrl0(url: String, imageNeko: Neko, flash: Boolean): MiraiMessageContent


    override fun image(input: InputStream, flash: Boolean): MiraiMessageContentBuilder {
        val imageNeko: Neko = CatCodeUtil
            .getNekoBuilder("image", true)
            .key("type").value("stream")
            .apply {
                if (flash) {
                    key("flash").value(true)
                }
            }.build()
        image0(input, imageNeko, flash).apply {
            checkText()
            contentList.add(this)
        }
        return this
    }

    @InstantInit
    abstract fun image0(input: InputStream, imageNeko: Neko, flash: Boolean): MiraiMessageContent


    override fun image(imgData: ByteArray, flash: Boolean): MiraiMessageContentBuilder {
        val input = ByteArrayInputStream(imgData)
        return image(input, flash)
    }


    /**
     * 直接追加一个 [MiraiMessageContent].
     */
    fun messageContent(content: MiraiMessageContent): MiraiMessageContentBuilder {
        checkText()
        contentList.add(content)
        return this
    }

    /**
     * 直接追加一个mirai原生 [Message] 实例。
     */
    fun singleMessage(singleMessage: SingleMessage): MiraiMessageContentBuilder {
        checkText()
        contentList.add(MiraiSingleMessageContent(singleMessage))
        return this
    }

    /**
     * 直接追加一个mirai原生 [Message] 实例。
     */
    fun message(message: Message): MiraiMessageContentBuilder {
        when (message) {
            is SingleMessage -> {
                checkText()
                contentList.add(MiraiSingleMessageContent(message))
            }
            is MessageChain -> {
                checkText()
                contentList.add(MiraiMessageChainContent(message))
            }
            else -> {
                throw IllegalArgumentException("Unknown message type. Message must be SingleMessage or MessageChain.")
            }
        }
        return this
    }

    /**
     * 通过消息构建函数构建一个 [SingleMessage]
     * for kt.
     */
    @JvmSynthetic
    fun message(neko: Neko?, messageBlock: suspend (Contact) -> SingleMessage): MiraiMessageContentBuilder {
        val msg = MiraiSingleMessageContent(messageBlock, neko)
        checkText()
        contentList.add(msg)
        return this
    }

    @Suppress("FunctionName")
    @JvmName("messageLazy")
    fun __messageBlocking(neko: Neko?, messageBlock: (Contact) -> SingleMessage): MiraiMessageContentBuilder {
        val msg = MiraiSingleMessageContent({ c -> messageBlock(c) }, neko)
        checkText()
        contentList.add(msg)
        return this
    }


    override fun build(): MiraiMessageContent {
        checkText()
        return MiraiListMessageContent(contentList.toList())
    }
}


@Suppress("NOTHING_TO_INLINE")
internal inline fun Contact.findAnyGroup(): Group? {
    return bot.groups.firstOrNull()
        ?: Bot.instancesSequence.flatMap { b -> b.groups }.firstOrNull()
}

/**
 * [MiraiMessageContentBuilder] 实现。
 * @author ForteScarlet -> https://github.com/ForteScarlet
 */
internal class MiraiMessageContentBuilderImgGroupFirst(private val remoteResourceInProcessor: RemoteResourceInProcessor) : MiraiMessageContentBuilder() {

    override fun imageLocal0(file: File, imageNeko: Neko, flash: Boolean): MiraiImageMessageContent {
        return MiraiImageMessageContent(flash, imageNeko) { contact ->
            contact.findAnyGroup()?.let { group ->
                file.uploadAsImage(group)
            } ?: file.uploadAsImage(contact)
        }
    }

    override fun imageUrl0(url: String, imageNeko: Neko, flash: Boolean): MiraiMessageContent {
        val urlContext = RemoteResourceContext(url)

        var input: InputStream? = null
        if (remoteResourceInProcessor !is SuspendRemoteResourceInProcessor) {
            input = remoteResourceInProcessor.processor(urlContext)
        }

        return MiraiImageMessageContent(flash, imageNeko) { contact ->
            val i = input ?: (remoteResourceInProcessor as SuspendRemoteResourceInProcessor).suspendableProcessor(urlContext)
            i.use { stream ->
                contact.findAnyGroup()?.let { group ->
                    stream.uploadAsImage(group)
                } ?: stream.uploadAsImage(contact)
            }


        }
    }

    @InstantInit
    override fun image0(input: InputStream, imageNeko: Neko, flash: Boolean): MiraiMessageContent {
        val resource = input.toExternalResource()
        return MiraiImageMessageContent(flash, imageNeko) { contact ->
            resource.use { res ->
                contact.findAnyGroup()?.uploadImage(res) ?: contact.uploadImage(res)
            }
        }
    }
}


/**
 * [MiraiMessageContentBuilder] 实现。其中，对于图片的上传为正常的上传模式，即当前为好友就使用好友上传，是群就使用群上传。
 * @author ForteScarlet -> https://github.com/ForteScarlet
 */
internal class MiraiMessageContentBuilderImgNormal(private val remoteResourceInProcessor: RemoteResourceInProcessor) : MiraiMessageContentBuilder() {
    override fun imageLocal0(file: File, imageNeko: Neko, flash: Boolean): MiraiImageMessageContent {
        return MiraiImageMessageContent(flash, imageNeko) { file.uploadAsImage(it) }
    }

    override fun imageUrl0(url: String, imageNeko: Neko, flash: Boolean): MiraiMessageContent {
        val urlContext = RemoteResourceContext(url)

        var input: InputStream? = null
        if (remoteResourceInProcessor !is SuspendRemoteResourceInProcessor) {
            input = remoteResourceInProcessor.processor(urlContext)
        }

        return MiraiImageMessageContent(flash, imageNeko) { contact ->
            val i = input ?: (remoteResourceInProcessor as SuspendRemoteResourceInProcessor).suspendableProcessor(urlContext)
            i.use { stream -> stream.uploadAsImage(contact) }
        }
    }

    @InstantInit
    override fun image0(input: InputStream, imageNeko: Neko, flash: Boolean): MiraiMessageContent {
        val resource = input.toExternalResource()
        return MiraiImageMessageContent(flash, imageNeko) { contact ->
            resource.use { contact.uploadImage(it) }
        }
    }

    // @LazyInit
    // @Suppress("BlockingMethodInNonBlockingContext")
    // override fun image1(
    //     inputStreamMotionActuator: InputStreamMotionActuator<InputStream>,
    //     imageNeko: Neko,
    //     flash: Boolean,
    // ): MiraiMessageContent {
    //
    //     return MiraiImageMessageContent(flash, imageNeko) { contact ->
    //         var resource: ExternalResource? = null
    //
    //         // // upload action.
    //         // val action: (InputStream) -> Unit =
    //
    //         try {
    //             inputStreamMotionActuator.invoke { inp ->
    //                 resource = inp.toExternalResource()
    //             }
    //         } catch (ioe: IOException) {
    //             throw IllegalStateException("Image resource init failed", ioe)
    //         }
    //
    //         resource?.use { contact.uploadImage(it) } ?: throw IllegalStateException("Image resource not init.")
    //
    //     }
    //
    //
    // }

}