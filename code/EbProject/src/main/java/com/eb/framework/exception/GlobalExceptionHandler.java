package com.eb.framework.exception;

import com.eb.framework.utils.Res;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 自定义全局异常处理器(处理常见异常)
 * @Author
 * @Date 2020/5/4 10:40
 * @Version
 **/

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权限校验失败 如果请求为ajax返回json，普通请求跳转页面
     */
//    @ExceptionHandler(AuthorizationException.class)
//    public Object handleAuthorizationException(HttpServletRequest request, AuthorizationException e)
//    {
//        log.error(e.getMessage(), e);
//        if (ServletUtils.isAjaxRequest(request))
//        {
//            return AjaxResult.error(PermissionUtils.getMsg(e.getMessage()));
//        }
//        else
//        {
//            ModelAndView modelAndView = new ModelAndView();
//            modelAndView.setViewName("error/unauth");
//            return modelAndView;
//        }
//    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Res handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Res.error("不支持'" + e.getMethod() + "'请求");
    }

    /**
     * 请求参数异常
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Res handleException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return Res.error("接口请求参数异常,请检查传递参数");
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Res unKnowErr(RuntimeException e) {
        log.error("未知的运行时异常:", e);
        return Res.error("未知的运行时异常:" + e.getMessage());
    }


    /**
     * 自定义系统异常处理(根据不同的模块划分)
     */
    @ExceptionHandler(BaseException.class)
    public Res handleException(BaseException e) {
        // 如果是BaseException相关
        if (e instanceof BaseException) {
            StringBuilder errMsg = new StringBuilder();
            errMsg.append("异常触发所属模块:" + e.getModule() + ";");
            errMsg.append("异常触发错误代码:" + e.getCode() + ";");
            errMsg.append("异常触发错误消息:" + e.getMsg() + ";");
            errMsg.append("异常触发参数列表:" + e.getParams() + ";");
            // 打印日志信息
            log.error(errMsg.toString(), e);
        }
//        return AjaxResultUtil.error(ResultEnum.SERVER_ERROR);
        return Res.error(e.getCode(), e.getMsg());
    }

    // 自定义数据校验异常BindException
    /**
     * 处理Get请求中,使用@Valid验证路径中请求实体校验失败后抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public Res BindExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
        return Res.error(e.getMessage());
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Res resolveConstraintViolationException(ConstraintViolationException ex) {
//        WebResult errorWebResult = new WebResult(WebResult.FAILED);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ConstraintViolation constraintViolation : constraintViolations) {
                msgBuilder.append(constraintViolation.getMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
//            errorWebResult.setInfo(errorMessage);
//            return errorWebResult;
            return Res.error(errorMessage);

        }
        return Res.error(ex.getMessage());
//        errorWebResult.setInfo(ex.getMessage());
//        return errorWebResult;
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常
     **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Res resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        WebResult errorWebResult = new WebResult(WebResult.FAILED);
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
//            errorWebResult.setInfo(errorMessage);
//            return errorWebResult;
            return Res.error(errorMessage);
        }
//        errorWebResult.setInfo(ex.getMessage());
//        return errorWebResult;
        return Res.error(ex.getMessage());

    }


    /**
     * 处理Shiro权限拦截异常
     * 如果返回JSON数据格式则@ControllerAdvice与@ResponseBody注解结合使用
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public Res defaultErrorHandler() {
        return Res.error("处理Shiro权限拦截异常");
    }

    /**
     * shiro相关异常过滤：密码校验异常
     **/
    @ExceptionHandler(IncorrectCredentialsException.class)
    public Res handleIncorrectCredentialsException(IncorrectCredentialsException e) {
        log.error(e.getMessage(), e);
        return Res.error("shiro相关异常过滤：密码校验异常");
    }

    @ExceptionHandler(AuthenticationException.class)
    public Res handleAuthenticationException(AuthenticationException e) {
        // 如果是BaseException相关
        if (e instanceof AccountException) {
            log.error(e.getMessage(), e);
            return Res.error("登录认证异常");
        }else{
            // 打印日志信息
            log.error(e.toString(), e);
            return Res.error(e.getMessage());
        }
    }
}
