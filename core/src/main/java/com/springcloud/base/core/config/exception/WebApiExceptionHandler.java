package com.springcloud.base.core.config.exception;

import com.springcloud.base.core.exception.DefaultException;
import com.springcloud.base.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author: ls
 * @Date: 2020/1/14 17:04
 * @Description: web端 统一异常处理
 * 针对异常封装的时候,仅仅只有body json 中的code去表示访问是否异常
 */
@Slf4j
@ControllerAdvice
@ConditionalOnProperty(
        prefix = "common.web.exception",
        name = {"enable"},
        matchIfMissing = true,
        havingValue = "true"
)
public class WebApiExceptionHandler {
    /**
     * 错误异常携带的头
     */
    public static final String TFT_EXCEPTION_FLAG = "SC-EXCEPTION-FLAG";

    /**
     * 默认生产环境的下标为Prod
     * 当非生产环境的时候，可以将错误信息携带到前台进行快速定位
     */
    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    @ResponseBody
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<Result<?>> exceptionHandler(Exception exception) {
        // 这种异常是开发者始料未及的异常，一般会做日志记录。
        log.error("【未知错误】：" + exception.getMessage(), exception);
        return this.wrapperResponseEntity(
                this.wrapErrorInfo(
                        Result.error("系统内部错误，管理员正在修复。"),
                        exception
                )
        );
    }

    @ResponseBody
    @ExceptionHandler({DefaultException.class})
        public ResponseEntity<Result<?>> defaultException(DefaultException defaultException) {
            // 这种异常都是业务异常，又开发者自行曝出，所以直接返回给调用者
            log.warn("【异常警告】：" + defaultException.getMessage(), defaultException);
            return this.wrapperResponseEntity(
                    this.wrapErrorInfo(
                            Result.error(defaultException)
                            , defaultException
                    )
        );
    }

    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Result<?>> missingServletRequestParameterException(MissingServletRequestParameterException
                                                                                     missingServletRequestParameterException) {
        // 参数验证异常
        log.debug(missingServletRequestParameterException.getMessage());
        return this.wrapperResponseEntity(
                this.wrapErrorInfo(
                        Result.error(missingServletRequestParameterException.getMessage())
                        , missingServletRequestParameterException
                )
        );
    }

//    @ResponseBody
//    @ExceptionHandler({FeignOptionalDecoder.RemoteFeignDecoderException.class})
//    public ResponseEntity<Result<?>> feignRemoteException(FeignOptionalDecoder.RemoteFeignDecoderException
//                                                                  remoteFeignDecoderException) {
//
//        if (remoteFeignDecoderException.getCause() instanceof DefaultException) {
//            log.error("【异常警告】：" + remoteFeignDecoderException.getCause().getMessage(), remoteFeignDecoderException.getCause());
//            return this.wrapperResponseEntity(
//                    this.wrapErrorInfo(
//                            Result.error(remoteFeignDecoderException.getCause().getMessage()),
//                            (DefaultException) remoteFeignDecoderException.getCause()
//                    )
//            );
//        } else {
//            log.error("【异常警告】：" + remoteFeignDecoderException.getMessage(), remoteFeignDecoderException);
//            return this.wrapperResponseEntity(
//                    this.wrapErrorInfo(
//                            Result.error("系统内部错误，管理员正在修复。"),
//                            remoteFeignDecoderException
//                    )
//            );
//        }
//    }

    /**
     * 将错误异常包装
     *
     * @param result    result
     * @param exception 异常
     */
    private Result<?> wrapErrorInfo(Result<?> result, Exception exception) {
        if ("test".equals(activeProfile) || "dev".equals(activeProfile)) {
            StringWriter stringWriter = new StringWriter();
            exception.printStackTrace(new PrintWriter(stringWriter));
            result.setErr(stringWriter.toString());
        }
        return result;
    }

    /**
     * 错误提包装
     *
     * @param result 内容
     * @return 错误提
     */
    private ResponseEntity<Result<?>> wrapperResponseEntity(Result<?> result) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(TFT_EXCEPTION_FLAG, result.getCode());
        return new ResponseEntity<Result<?>>(result, httpHeaders, HttpStatus.OK);
    }
}
