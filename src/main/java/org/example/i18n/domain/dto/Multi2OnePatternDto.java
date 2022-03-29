package org.example.i18n.domain.dto;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 循环文件工具类
 *
 * @author wangyichun
 * @since 2022/1/4 12:56
 */
@Accessors(chain = true)
public class Multi2OnePatternDto {
    public Multi2OnePatternDto() {
        this.recordLines = new HashMap<>();
    }

    @SuppressWarnings("UnusedReturnValue")
    public Multi2OnePatternDto clean() {
        this.recordLines.clear();
        return this;
    }

    public boolean isEmpty() {
        return recordLines.isEmpty();
    }

    /**
     * 记录匹配行
     * key: 1,2,3
     * value: 匹配到的实际数据
     */
    private Map<Integer, String> recordLines;

    public void addRecordLine(int key, String value) {
        if (this.recordLines == null) {
            this.recordLines = new HashMap<>();
        }
        this.recordLines.put(key, value);
    }

    public int size() {
        return CollUtil.size(this.recordLines);
    }

    public Collection<String> values() {
        return this.recordLines.values();
    }
}
