/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.eb.framework.utils;

import cn.hutool.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 * 自定义方法封装返回数据
 */
public class Res extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public Res() {
		put("code", 0);
		put("msg", "success");
	}
	
	public static Res error() {
		return error(HttpStatus.HTTP_INTERNAL_ERROR, "未知异常，请联系管理员");
	}
	
	public static Res error(String msg) {
		return error(HttpStatus.HTTP_INTERNAL_ERROR, msg);
	}
	
	public static Res error(int code, String msg) {
		Res r = new Res();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static Res ok(String msg) {
		Res r = new Res();
		r.put("msg", msg);
		return r;
	}
	
	public static Res ok(Map<String, Object> map) {
		Res r = new Res();
		r.putAll(map);
		return r;
	}
	
	public static Res ok() {
		return new Res();
	}

	public Res put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
