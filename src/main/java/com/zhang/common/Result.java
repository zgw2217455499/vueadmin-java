package com.zhang.common;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;

@Data
public class Result {
    private int code;
    private String msg;
    private Object data;

    public static Result succ(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result succ(Object data) {
        return succ(200, "操作成功", data);
    }

    public static Result fail(String msg) {
        return succ(400, msg, null);
    }
}
