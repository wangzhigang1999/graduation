package com.bupt.graduation.entity;


import java.io.Serializable;

/**
 * 接口返回统一数据结构
 *
 * @author wangz
 */

public class Resp implements Serializable {
    private boolean success = true;
    private Integer code = 200;
    private String msg;
    private Object data;

    @Override
    public String toString() {
        return "Resp{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Resp(String msg, Object data) {
        this.data = data;
        this.msg = msg;
    }

    public Resp(int code, String msg, Object data) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Resp(boolean success, Integer code, String msg, Object data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Resp() {
    }
}