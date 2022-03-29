package org.example.i18n.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.entity.PropertiesMap;
import org.example.i18n.mapper.PropertiesMapMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangyichun
 * @since 2022/1/1 21:12
 */
@Slf4j
@RestController
@RequestMapping("/propertiesMap")
public class PropertiesMapController {
    @Autowired
    private PropertiesMapMapper propertiesMapMapper;

    @RequestMapping("selectList")
    @ResponseBody
    public List<PropertiesMap> selectList() {
        return propertiesMapMapper.selectList(null);
    }
}
