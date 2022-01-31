package com.bupt.graduation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口返回统一数据结构
 *
 * @author wangz
 */
@NoArgsConstructor
@Data
public class Resp implements Serializable {
    private boolean success = true;
    private Integer code = 200;
    private String msg;
    private Object data;

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
}