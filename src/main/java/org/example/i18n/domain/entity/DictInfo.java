package org.example.i18n.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @TableName dict_info
 */
@TableName(value = "dict_info")
@Data
@Accessors(chain = true)
public class DictInfo implements Serializable {
    @TableId
    private String code;
    private String parentCode;
    private String value;
    private String parentValue;
    private String createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}