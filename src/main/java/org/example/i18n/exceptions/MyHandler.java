package org.example.i18n.exceptions;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.consts.CommonConstant;
import org.example.i18n.domain.vo.CommonVo;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 异常拦截器
 *
 * @author wangyichun
 * @since 2021/12/30 8:49
 */
@Slf4j
@RestControllerAdvice
public class MyHandler {
    @ExceptionHandler(RuntimeException.class)
    public CommonVo<Void> customGenericExceptionHnadler(RuntimeException exception) {
        exception.printStackTrace();
        boolean stopFlag = false;
        List<Throwable> ths = new ArrayList<>();
        Set<String> chineseErrorMsg = new HashSet<>();
        Throwable temp = exception;
        while (!stopFlag) {
            ths.add(temp);
            stopFlag = ths.stream().anyMatch(throwable -> {
                if (StrUtil.isNotBlank(throwable.getMessage())) {
                    boolean matches = CommonConstant.chineseLP.matcher(throwable.getMessage()).find();
                    if (matches) {
                        chineseErrorMsg.add(throwable.getMessage());
                    }
                }
                return throwable == throwable.getCause();
            });
            temp = temp.getCause();
            if (temp == null) {
                stopFlag = true;
            }
        }
        int code = 500;
        if (exception instanceof JumpOutException) {
            code = ((JumpOutException) exception).getCode();
        }
        if (CollUtil.isNotEmpty(chineseErrorMsg)) {
            return new CommonVo<Void>().setCode(code).setMessage(String.join(",", chineseErrorMsg));
        } else {
            temp = ths.get(ths.size() - 1);
            return new CommonVo<Void>().setCode(code).setMessage(temp.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonVo<Void> customGenericExceptionHnadler(MethodArgumentNotValidException e) {
        StringJoiner joiner = new StringJoiner(",");
        for (ObjectError objectError : e.getBindingResult().getAllErrors()) {
            String defaultMessage = objectError.getDefaultMessage();
            joiner.add(objectError.getObjectName() + ": " + defaultMessage);
        }
        String errorMessage = joiner.toString();
        log.info("[-MyHandler:customGenericExceptionHnadler-]errorMessage:{}", errorMessage);
        return new CommonVo<Void>().setCode(500).setMessage(errorMessage);
    }
}
