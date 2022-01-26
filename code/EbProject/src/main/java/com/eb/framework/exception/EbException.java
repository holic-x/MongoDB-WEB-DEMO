package com.eb.framework.exception;

/**
 * @ClassName FrameworkException
 * @Description TODO
 * @Author
 * @Date 2020/5/6 9:33
 * @Version
 **/
public class EbException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * @MethodName 功能模块名称(EbProject)
     **/
    private static final String MODULE_NAME = "EbProject";

    public EbException(Integer errCode, Object[] args, String errMsg) {
        super(MODULE_NAME, errCode, args, errMsg);
    }

    public EbException(Integer errCode, String errMsg) {
        super(MODULE_NAME, errCode, null, errMsg);
    }

    public EbException(String errMsg) {
        super(MODULE_NAME, errMsg);
    }

    public EbException(String errMsg, Object[] params) {
        super(MODULE_NAME, errMsg, params);
    }

}
