package com.bupt.graduation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口返回统一数据结构
 *
 * @author wangz
 */
@AllArgsConstructor
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
}