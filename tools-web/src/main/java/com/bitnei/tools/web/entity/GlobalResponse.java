package com.bitnei.tools.web.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * GlobalResponse : 通用响应对象, 封装数据如下
 * <p>
 * {"meta":{"code":200,"message":"创建成功"},"data":{"id":"5308e9c2-a4ce-4dca-9373-cc1ffe63d5f9","name":"Apple Watch SPORT","description":"Sport 系列的表壳材料为轻巧的银色及深空灰色阳极氧化铝金属，强化 Ion-X 玻璃材质为显示屏提供保护。搭配高性能 Fluoroelastomer 表带，共有 5 款缤纷色彩。"}}
 *
 * @author http://bitnei.cn
 * @since 2015-04-16 17:47
 */
@Data
public class GlobalResponse<T> {

    private Meta meta;

    private T data;

    public static GlobalResponse<?> success(Object data) {
        return new GlobalResponse<>(HttpStatus.OK.value(), "操作成功", data);
    }

    public GlobalResponse(T data) {
        this.meta = new Meta(HttpStatus.OK.value(), "操作成功");
        this.data = data;
    }

    public GlobalResponse(Integer code, String message) {
        this.meta = new Meta(code, message);
    }

    public GlobalResponse(Integer code, String message, T data) {
        this.meta = new Meta(code, message);
        this.data = data;
    }
}
