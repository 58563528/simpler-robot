/*
 * Copyright (c) 2020. ForteScarlet All rights reserved.
 * Project  parent
 * File     HttpTemplate.kt
 *
 * You can contact the author through the following channels:
 * github https://github.com/ForteScarlet
 * gitee  https://gitee.com/ForteScarlet
 * email  ForteScarlet@163.com
 * QQ     1149159218
 */

@file:JvmName("HttpTemplateUtil")
package love.forte.simbot.http.template


/**
 * 简易的 http 客户端，提供部分最常见的一些请求方式。
 * @author ForteScarlet -> https://github.com/ForteScarlet
 */
public interface HttpTemplate {

    /**
     * get请求。
     * @param responseType 响应body封装类型。如果为null则认为忽略返回值，则response中的getBody也为null。
     */
    fun <T> get(url: String, responseType: Class<T>): HttpResponse<T>

    /**
     * get请求。
     * @param responseType 响应body封装类型。
     * @param headers 请求头信息。
     */
    fun <T> get(url: String, headers: HttpHeaders?, responseType: Class<T>): HttpResponse<T>

    /**
     * get请求。
     * @param responseType 响应body封装类型。
     * @param headers 请求头信息。
     * @param requestParam 请求参数。
     */
    fun <T> get(url: String, headers: HttpHeaders?, requestParam: Map<String, Any?>?, responseType: Class<T>): HttpResponse<T>

    /**
     * post/json 请求。
     * @param responseType 响应body封装类型。
     */
    fun <T> post(url: String, responseType: Class<T>): HttpResponse<T>

    /**
     * post/json 请求。
     * @param responseType 响应body封装类型。
     * @param headers 请求头信息。
     */
    fun <T> post(url: String, headers: HttpHeaders?, responseType: Class<T>): HttpResponse<T>

    /**
     * post/json 请求。
     * @param responseType 响应body封装类型。
     * @param headers 请求头信息。
     * @param requestBody 请求参数，一个对象实例，或者一个json字符串。
     */
    fun <T> post(url: String, headers: HttpHeaders?, requestBody: Any?, responseType: Class<T>): HttpResponse<T>

    /**
     * post/form 请求。
     * @param responseType 响应body封装类型。
     */
    fun <T> form(url: String, responseType: Class<T>): HttpResponse<T>

    /**
     * post/form 请求。
     * @param responseType 响应body封装类型。
     * @param headers 请求头信息。
     */
    fun <T> form(url: String, headers: HttpHeaders?, responseType: Class<T>): HttpResponse<T>

    /**
     * post/form 请求。
     * @param responseType 响应body封装类型。
     * @param headers 请求头信息。
     * @param requestForm 请求参数，一个对象实例，此对象实例只会获取其中一层字段值作为表单提交，不会像json那样嵌套获取。如果字段对应的是一个其他的实例，则会直接获取其toString的值。
     */
    fun <T> form(url: String, headers: HttpHeaders?, requestForm: Map<String, Any?>?, responseType: Class<T>): HttpResponse<T>


    /**
     * 使用请求实例请求。
     */
    fun <T> request(request: HttpRequest<T>): HttpResponse<T>

    /**
     * 请求多个，其中，如果 [HttpRequest.responseType] 为null，则其请求结果将不会出现返回值中。
     * @param parallel 是否异步请求
     * @return 全部的响应结果，其顺序为 [requests] 中的顺序。
     */
    fun requestAll(parallel: Boolean, vararg requests: HttpRequest<*>): List<HttpResponse<*>>


}


/**
 * http 的请求相应简易模型。
 */
public interface HttpResponse<T> {
    /**
     * 响应状态码
     */
    val statusCode: Int


    /**
     * 响应体
     */
    val body: T


    /**
     * 获取响应的请求头信息。
     */
    val headers: HttpHeaders


    /**
     * 如果响应出现错误等情况，则此处可能为响应的错误信息。
     */
    val message: String?
}


public data class HttpResponseData<T>(
    override val statusCode: Int,
    override val body: T,
    override val headers: HttpHeaders,
    override val message: String?
): HttpResponse<T>



















