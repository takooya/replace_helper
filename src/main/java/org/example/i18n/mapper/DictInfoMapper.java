package org.example.i18n.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.i18n.domain.entity.DictInfo;
import org.springframework.stereotype.Repository;

/**
 * 字典操作
 *
 * @author wangyichun
 * @since 2021/12/30 7:48
 */
@Mapper
@Repository
public interface DictInfoMapper extends BaseMapper<DictInfo> {
}
