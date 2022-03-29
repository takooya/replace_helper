package org.example.i18n.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.i18n.domain.entity.PropertiesMap;
import org.springframework.stereotype.Repository;

/**
 * @author wangyichun
 * @since 2021/12/29 11:13
 */
@Mapper
@Repository
public interface PropertiesMapMapper extends BaseMapper<PropertiesMap> {
}
