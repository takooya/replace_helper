package org.example.i18n.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.vo.CommonVo;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 请求到达记录
 *
 * @author wangyichun
 * @since 2021/9/3 14:01
 */
@Slf4j
@Component
@WebFilter(filterName = " requestFilter ", urlPatterns = "/*")
public class RequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (Objects.equals(request.getRequestURI(), "/actuator/health")) {
            chain.doFilter(request, response);
            return;
        }
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        log.debug("=========================== {} {} ===========================",
                requestWrapper.getMethod(), requestWrapper.getRequestURL().toString());
        chain.doFilter(requestWrapper, responseWrapper);
        String contentType = responseWrapper.getContentType();
        // contentType为application/json，才封装响应
        if ((CharSequenceUtil.isNotBlank(contentType) && contentType.contains(ContentType.JSON.getValue()))
                || contentType == null) {
            byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
            String responseStr = new String(contentAsByteArray);
            JSON data;
            if (JSONUtil.isJsonObj(responseStr)) {
                data = JSONUtil.parseObj(responseStr);
                if (((JSONObject) data).get("code") != null
                        && ((JSONObject) data).get("message") != null
                        && CharSequenceUtil.isNotBlank(((JSONObject) data).get("message").toString())) {
                    responseWrapper.copyBodyToResponse();
                    return;
                }
            } else if (JSONUtil.isJsonArray(responseStr)) {
                data = JSONUtil.parseArray(responseStr);
            } else if (contentType == null && StrUtil.isBlank(responseStr)) {
                responseWrapper.setContentType(ContentType.JSON.getValue());
                data = new JSONObject(MapUtil.of("WARNING", "响应数据为空!"));
            } else {
                throw new RuntimeException("未知异常");
            }
            CommonVo<JSON> result = new CommonVo<JSON>().setData(data);
            ByteArrayInputStream bais = IoUtil.toStream(result.toString(), StandardCharsets.UTF_8);
            response.setContentLength(bais.available());
            IoUtil.copy(bais, response.getOutputStream());
        } else {
            responseWrapper.copyBodyToResponse();
        }
        // 将响应重新写入
    }
}
