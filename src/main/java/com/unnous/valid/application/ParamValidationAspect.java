package com.unnous.valid.application;

import com.unnous.valid.annotation.valid.function.NotNull;
import com.unnous.valid.annotation.valid.function.ValidParam;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 参数校验注解类（Aspect）
 */
@EnableAspectJAutoProxy
@Aspect
@Component
@Slf4j
public class ParamValidationAspect {

    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void paramCheck(JoinPoint joinPoint) throws Exception {
        StringBuilder sb = new StringBuilder();
        //获取参数对象
        Object[] args = joinPoint.getArgs();
        //获取方法参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            // 获取参数的类型
            Class<?> parameterType = parameter.getType();
            //Java自带基本数据类型的包装类（例如Integer）或者是String类型的参数处理方式
            if (parameterType.isPrimitive() || parameterType.isAssignableFrom(String.class)) {
                NotNull notBlank = parameter.getAnnotation(NotNull.class);
                if (notBlank != null && args[i] == null) {
                    sb.append(parameter.getName() + ":" + notBlank.value() + "\n");
                }
                //TODO
                continue;
            }
            /*
             * 没有标注@ValidParam注解，或者是HttpServletRequest、HttpServletResponse、HttpSession时，都不做处理
             */
            if (parameterType.isAssignableFrom(HttpServletRequest.class) ||
                    parameterType.isAssignableFrom(HttpSession.class) ||
                    parameterType.isAssignableFrom(HttpServletResponse.class) ||
                    parameter.getAnnotation(ValidParam.class) == null) {
                continue;
            }

            // 获取类型所对应的参数对象，实际项目中Controller中的接口不会传两个相同的自定义类型的参数，所以此处直接使用findFirst()
            Object arg = Arrays.stream(args).filter(ar -> parameterType.isAssignableFrom(ar.getClass())).findFirst().get();

            // 请求参数Data集合
            List<Object> objects = new ArrayList();

            // 存储参数的所有成员变量
            Field[] declaredFields = {};

            // 如果参数类型为集合，则取出集合中存放的类型
            if (parameterType.isAssignableFrom(List.class)) {
                String typeName = parameter.getParameterizedType().getTypeName();
                Matcher matcher = Pattern.compile("<(.*)>").matcher(typeName);
                boolean matchFlag = matcher.find();
                if (matchFlag) {
                    Class<?> aClass = Class.forName(matcher.group(1));
                    // 集合中存放的是几本数据类型的包装类或者String。则你进行校验
                    if(aClass.isPrimitive() || aClass.isAssignableFrom(String.class)){
                        continue;
                    }else{
                        declaredFields = aClass.getDeclaredFields();
                    }
                }
                objects.addAll((List) arg);
            } else if (parameterType.isAssignableFrom(Map.class)) {
                continue;
            } else {
                //得到参数的所有成员变量
                declaredFields = parameterType.getDeclaredFields();
                objects.add(arg);
            }

            // 获取ValidParam参数
            String[] fields = parameter.getAnnotation(ValidParam.class).value();

            // 过滤需要检验的属性
            if (fields.length > 0) {
                List<java.lang.String> fieldList = Arrays.asList(fields);
                declaredFields = Arrays.stream(declaredFields)
                        .filter(e -> fieldList.contains(e.getName()))
                        .collect(Collectors.toList())
                        .toArray(new Field[]{});
            }
            // 循环请求参数集合，校验每个参数
            for (int y = 0; y < objects.size(); y++) {
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(objects.get(y));
                    Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
                    //校验标有@NotNull注解的字段
                    NotNull notBlank = field.getAnnotation(NotNull.class);
                    if (notBlank != null) {
                        if (fieldValue == null) {
                            sb.append(field.getName() + ":" + notBlank.value() + "\n");
                        }
                    }
                }
            }


        }
        if (sb.length() > 0)
            throw new RuntimeException(sb.toString());

    }

}
