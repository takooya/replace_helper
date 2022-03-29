package org.example.i18n.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.util.Date;
import java.util.Properties;

import static org.example.i18n.consts.CommonConstant.TIME_FORMAT;

/**
 * mybatis拦截器
 *
 * @author wangyichun
 * @since 2021/8/13
 */
@SuppressWarnings("unused")
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class,
                Object.class
        })})
public class CreateTimeInterceptor implements Interceptor {

    @SuppressWarnings({"java:S108", "java:S125", "AlibabaLowerCamelCaseVariableNaming"})
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (SqlCommandType.INSERT == sqlCommandType) {
            // 判断mapper注解，是否需要拼接条件
            Object parameter = invocation.getArgs()[1];
            if (BeanUtil.beanToMap(parameter).containsKey("createTime")) {
                String curTime = DateUtil.format(new Date(), TIME_FORMAT);
                BeanUtil.setFieldValue(parameter, "createTime", curTime);
                log.debug("[-CreateTimeInterceptor-].intercept:拦截器插入创建时间,被插入方法={}", mappedStatement.getId());
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        //此处可以接收到配置文件的property参数
    }
}