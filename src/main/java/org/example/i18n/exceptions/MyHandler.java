package org.example.i18n.exceptions;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.example.i18n.consts.CommonConstant;
import org.example.i18n.domain.vo.CommonVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                String fieldName = fieldError.getField();
                String enumValues = getEnumValues(e, fieldName);
                joiner.add(fieldName + ": " + defaultMessage + enumValues);
            } else {
                joiner.add(objectError.getObjectName() + ": " + defaultMessage);
            }
        }
        String errorMessage = joiner.toString();
        log.info("[-MyHandler:customGenericExceptionHnadler-]errorMessage:{}", errorMessage);
        return new CommonVo<Void>().setCode(500).setMessage(errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonVo<Void> customGenericExceptionHnadler(HttpMessageNotReadableException e) {
        String errorMessage = e.getMessage();
        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException focusE = (InvalidFormatException) e.getCause();
            Object[] enumValues = focusE.getTargetType().getEnumConstants();
            if (ArrayUtil.isNotEmpty(enumValues)) {
                errorMessage = "您输入的 " + focusE.getValue().toString() + " 枚举值有误,接受的枚举值包含[" +
                        Stream.of(enumValues)
                                .map(Object::toString)
                                .collect(Collectors.joining(","))
                        + "]";
                return new CommonVo<Void>().setCode(500).setMessage(errorMessage);
            }
        }
        return new CommonVo<Void>().setCode(500).setMessage(errorMessage);
    }

    @NotNull
    private String getEnumValues(MethodArgumentNotValidException e, String fieldName) {
        try {
            Class clazz = e.getParameter().getParameterType();
            Field field = ReflectUtil.getField(clazz, fieldName);
            Object[] enumConstants = field.getType().getEnumConstants();
            return ",可填入的值包括[" + Stream.of(enumConstants).map(Object::toString).collect(Collectors.joining(",")) + "]";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
