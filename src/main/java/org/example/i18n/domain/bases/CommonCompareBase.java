package org.example.i18n.domain.bases;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.i18n.utils.rowformat.CodeTypeEnum;
import org.example.i18n.utils.rowformat.EqualTypeEnum;

import javax.validation.constraints.NotNull;

@Accessors(chain = true)
public class CommonCompareBase extends CommonLoopBase {
    @Setter
    @Getter
    @NotNull(message = "代码过滤类型不可以为空")
    private CodeTypeEnum codeType;
    @Setter
    @Getter
    @NotNull(message = "代码比较类型不可以为空")
    private EqualTypeEnum equalType;
}
