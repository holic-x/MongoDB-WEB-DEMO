package com.eb.framework.exception;


import cn.hutool.http.HttpStatus;
import lombok.Data;

/**
 * @ClassName BaseException
 * @Description 异常基类
 * @Author
 * @Date 2020/5/4 15:53
 * @Version
 **/
@Data
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 指定操作参数列表
     */
    private Object[] params;

    /**
     * 错误消息
     */
    private String msg;

    public BaseException() {
    }

    public BaseException(String module, Integer code, Object[] params, String msg) {
        super(msg);
        this.module = module;
        this.code = code;
        this.params = params;
        this.msg = msg;
    }

    public BaseException(String module, Integer code, Object[] params, String msg, Throwable e) {
        // 填充异常信息
        super(msg, e);
        this.module = module;
        this.code = code;
        this.params = params;
        this.msg = msg;
    }

    public BaseException(String module, Integer code, Object[] params) {
        this(module, code, params, null, null);
    }

    public BaseException(String module, Integer code, String msg) {
        this(module, code, null, msg);
    }

    // 自定义异常统一处理为500
    public BaseException(String module, String msg) {
        this(module, HttpStatus.HTTP_INTERNAL_ERROR, null, msg);
    }

    public BaseException(String module, String msg, Throwable e) {
        super(msg, e);
        this.module = module;
    }

    public BaseException(String module, String msg, Object[] params) {
        this(module, HttpStatus.HTTP_INTERNAL_ERROR, params, msg);
    }
}
