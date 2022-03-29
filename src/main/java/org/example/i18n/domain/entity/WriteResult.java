package org.example.i18n.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文件行记录器
 *
 * @author wangyichun
 * @since 2022/1/4 16:24
 */
@TableName(value = "write_result")
@Data
@Accessors(chain = true)
public class WriteResult {
    private String content;
    @TableField("file_path")
    private String filePath;
    private String comment;
    private String path;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
