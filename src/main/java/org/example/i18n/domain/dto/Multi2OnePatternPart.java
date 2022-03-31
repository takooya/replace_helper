package org.example.i18n.domain.dto;

import cn.hutool.core.collection.CollUtil;
import lombok.experimental.Accessors;
import org.example.i18n.utils.rowformat.DealRowInfo;

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
public class Multi2OnePatternPart {

    /**
     * 记录匹配行
     * key: 1,2,3
     * value: 匹配到的实际数据
     */
    private Map<Integer, DealRowInfo> recordLines;

    public Multi2OnePatternPart() {
        this.recordLines = new HashMap<>();
    }

    @SuppressWarnings("UnusedReturnValue")
    public Multi2OnePatternPart clean() {
        this.recordLines.clear();
        return this;
    }

    public boolean isEmpty() {
        return recordLines.isEmpty();
    }

    public void addRecordLine(int key, DealRowInfo value) {
        if (this.recordLines == null) {
            this.recordLines = new HashMap<>();
        }
        this.recordLines.put(key, value);
    }

    public int size() {
        return CollUtil.size(this.recordLines);
    }

    public Collection<DealRowInfo> values() {
        return this.recordLines.values();
    }
}
