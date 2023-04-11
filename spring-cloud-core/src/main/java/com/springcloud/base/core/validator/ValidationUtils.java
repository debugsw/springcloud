package com.springcloud.base.core.validator;

import com.springcloud.base.core.exception.PropertyValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Author: ls
 * @Description: 验证工具类，提供通过注释方式的字段验证，遵循hibernate规范
 * @Date: 2023/1/28 10:50
 */
@Slf4j
public class ValidationUtils {

    /**
     * 使用hibernate的注解来进行验证
     */
    private static final Validator VALIDATOR = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 对该对象进行验证
     *
     * @param obj
     * @param <T>
     */
    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> set = VALIDATOR.validate(obj);
        if (null != set && !set.isEmpty()) {
            for (ConstraintViolation<T> cv : set) {
                log.warn("Field validation failed {}.{}: {}", obj.getClass().getSimpleName(), cv.getPropertyPath(), cv.getMessage());
                throw new PropertyValidatorException(cv.getPropertyPath() + cv.getMessage());
            }
        }
    }

    /**
     * 对该对象的指定字段进行验证
     *
     * @param obj
     * @param propertyName
     * @param <T>
     */
    public static <T> void validateProperty(T obj, String propertyName) {
        Set<ConstraintViolation<T>> set = VALIDATOR.validateProperty(obj, propertyName);

        if (null != set && !set.isEmpty()) {
            for (ConstraintViolation<T> cv : set) {
                log.warn("Field validation failed {}.{}: {}", obj.getClass().getSimpleName(), propertyName, cv.getMessage());
                throw new PropertyValidatorException(cv.getMessage());
            }
        }
    }

}
