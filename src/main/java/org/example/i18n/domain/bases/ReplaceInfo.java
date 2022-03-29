package org.example.i18n.domain.bases;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Data
@Accessors(chain = true)
public class ReplaceInfo {
    public ReplaceInfo() {
    }

    public ReplaceInfo(String fileName, List<String> origin, String modified, String error) {
        this.fileName = fileName;
        this.origin = origin;
        this.modified = modified;
        this.error = error;
    }

    public ReplaceInfo(String fileName, String origin, String modified) {
        this.fileName = fileName;
        //noinspection ArraysAsListWithZeroOrOneArgument
        this.origin = Arrays.asList(origin);
        this.modified = modified;
    }

    public ReplaceInfo(String fileName, String modified) {
        this.fileName = fileName;
        this.modified = modified;
    }

    /**
     * 当前文件名
     */
    private String fileName;
    /**
     * 原始行数据
     */
    private List<String> origin;
    /**
     * 修改后行数据
     */
    private String modified;
    /**
     * 修改行时发成的错误
     */
    private String error;

    public ReplaceInfo setOrigin(List<String> origin) {
        this.origin = origin;
        return this;
    }

    public ReplaceInfo addOrigin(String origin) {
        //noinspection ArraysAsListWithZeroOrOneArgument
        this.origin = Arrays.asList(origin);
        return this;
    }
}
