package org.example.i18n.domain.vo;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统一响应对象
 *
 * @author wangyichun
 * @since 2021/12/30 9:07
 */
@Data
@Accessors(chain = true)
public class CommonVo<T> {
    private int code = 200;
    private String message = "请求成功";
    private T data;

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
