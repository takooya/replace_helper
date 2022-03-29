package org.example.i18n.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.example.i18n.domain.bases.CommonLoopBase;

/**
 * 循环文件工具类
 *
 * @author wangyichun
 * @since 2022/1/4 12:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LoopFileVo extends CommonLoopBase {
    private String source;
    private String target;
}
