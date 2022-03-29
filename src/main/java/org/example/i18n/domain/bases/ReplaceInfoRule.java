package org.example.i18n.domain.bases;

import java.util.ArrayList;
import java.util.List;

public interface ReplaceInfoRule {
    List<ReplaceInfo> getReplaceInfos();

    void setReplaceInfos(List<ReplaceInfo> replaceInfoList);

    default void addReplaceInfo(ReplaceInfo replaceInfo) {
        List<ReplaceInfo> replaceInfos = this.getReplaceInfos();
        if (replaceInfos == null) {
            replaceInfos = new ArrayList<>();
        }
        replaceInfos.add(replaceInfo);
        this.setReplaceInfos(replaceInfos);
    }

    default void addReplaceInfo(String fileName, String errorInfo) {
        addReplaceInfo(new ReplaceInfo()
                .setFileName(fileName)
                .setError(errorInfo));
    }

    default void addReplaceInfo(String fileName, String origin, String modified) {
        addReplaceInfo(new ReplaceInfo()
                .setFileName(fileName)
                .addOrigin(origin)
                .setModified(modified));
    }

    default void addReplaceInfo(String fileName, List<String> origins, String modified) {
        addReplaceInfo(new ReplaceInfo()
                .setFileName(fileName)
                .setOrigin(origins)
                .setModified(modified));
    }

    default void addReplaceInfo(String fileName, List<String> origins, String modified, String error) {
        addReplaceInfo(new ReplaceInfo()
                .setFileName(fileName)
                .setOrigin(origins)
                .setModified(modified)
                .setError(error));
    }
}
