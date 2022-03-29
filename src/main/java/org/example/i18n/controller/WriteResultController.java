package org.example.i18n.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.domain.entity.WriteResult;
import org.example.i18n.mapper.WriteResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 页面控制
 *
 * @author wangyichun
 * @since 2021/12/29 12:41
 */
@Slf4j
@RestController
@RequestMapping("/writeResult")
public class WriteResultController {
    @Autowired
    private WriteResultMapper writeResultMapper;

    @PostMapping("/insert")
    public int insert(@RequestBody WriteResult wr) {
        return writeResultMapper.insert(wr);
    }

    @PostMapping("/update")
    public int updateByCode(@RequestBody WriteResult wr) {
        return writeResultMapper.updateById(wr);
    }

    @PostMapping("/selectList")
    public List<WriteResult> selectList(@RequestBody WriteResult wr) {
        return writeResultMapper.selectList(
                new LambdaQueryWrapper<WriteResult>()
                        .eq(!CharSequenceUtil.isBlankOrUndefined(wr.getPath()), WriteResult::getPath, wr.getPath())
                        .eq(!CharSequenceUtil.isBlankOrUndefined(wr.getFilePath()), WriteResult::getFilePath, wr.getFilePath())
                        .eq(!CharSequenceUtil.isBlankOrUndefined(wr.getComment()), WriteResult::getComment, wr.getComment())
                        .eq(!CharSequenceUtil.isBlankOrUndefined(wr.getContent()), WriteResult::getContent, wr.getContent()));
    }
}
