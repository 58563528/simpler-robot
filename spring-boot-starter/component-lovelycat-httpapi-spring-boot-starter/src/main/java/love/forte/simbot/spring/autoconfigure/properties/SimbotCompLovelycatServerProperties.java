/*
 *
 *  * Copyright (c) 2020. ForteScarlet All rights reserved.
 *  * Project  component-onebot
 *  * File     SimbotCompLovelycatConfigurationProperties.java
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

package love.forte.simbot.spring.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用于映射可爱猫组件的配置文件用的类，但是会被使用。
 * @see love.forte.simbot.component.lovelycat.configuration.LovelyCatServerProperties
 * @see love.forte.simbot.spring.lovelycat.configuration.LovelycatServerConfiguration
 * @author ForteScarlet
 */
@Component
@ConfigurationProperties(prefix = "simbot.component.lovelycat.server")
@lombok.Setter
@lombok.Getter
public class SimbotCompLovelycatServerProperties {

    /**
     * 是否启用http服务器。此项配置决定的是是否开启可爱猫组件中的默认http服务器。
     * 在springboot中，如果依赖了 spring-web， 则没有特殊情况，直接选择false，且默认为false。
     */
    private boolean enable = false;

    /**
     * 开启http服务的端口号。
     * 当 {@link #enable} 为true的时候，此属性代表额外开启的http服务器所监听的端口号。
     */
    private int port = 8080;

    /**
     * http服务器的监听地址。
     */
    private String path = "/lovelycat";
}
