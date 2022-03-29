package org.example.i18n.pages;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面控制
 *
 * @author wangyichun
 * @since 2021/12/29 12:41
 */
@Slf4j
@Controller
@RequestMapping("/html")
public class PageController {

    @GetMapping("/index")
    public String insert(ModelMap modelMap) {
        modelMap.addAllAttributes(MapUtil
                .builder("name", "name")
                .put("website", "website")
                .put("language", "language")
                .build());
        return "index";
    }
}
