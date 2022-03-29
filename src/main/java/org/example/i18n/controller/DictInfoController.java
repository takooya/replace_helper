package org.example.i18n.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.entity.DictInfo;
import org.example.i18n.mapper.DictInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.i18n.consts.CommonConstant.PROJECT_SOURCE_PATH_KEY;

/**
 * 页面控制
 *
 * @author wangyichun
 * @since 2021/12/29 12:41
 */
@Slf4j
@RestController
@RequestMapping("/dictInfo")
public class DictInfoController {
    @Autowired
    private DictInfoMapper dictInfoMapper;

    @PostMapping("/insert")
    public int insert(@RequestBody DictInfo dictInfo) {
        return dictInfoMapper.insert(dictInfo);
    }

    @PostMapping("/update")
    public int updateByCode(@RequestBody DictInfo dictInfo) {
        return dictInfoMapper.updateById(dictInfo);
    }

    @GetMapping("/selectList")
    public List<DictInfo> selectList() {
        return dictInfoMapper.selectList(null);
    }

    @GetMapping("/initList")
    public List<DictInfo> initList() {
        return dictInfoMapper.selectList(new LambdaQueryWrapper<DictInfo>()
                .eq(DictInfo::getParentCode, PROJECT_SOURCE_PATH_KEY));
    }
}
