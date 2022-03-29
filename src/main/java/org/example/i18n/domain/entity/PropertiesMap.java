package org.example.i18n.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @TableName properties_map
 */
@TableName(value = "properties_map")
@Data
@Accessors(chain = true)
public class PropertiesMap implements Serializable {
    private String pkey;
    private String value;
    private String filename;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}