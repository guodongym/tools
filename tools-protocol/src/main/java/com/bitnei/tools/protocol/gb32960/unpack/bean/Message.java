package com.bitnei.tools.protocol.gb32960.unpack.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 明细数据
 *
 * @author zhaogd
 * @date 2020-02-21 17:32
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -3003879633561191735L;
    /**
     * 起始符
     **/
    private String startMask;
    /**
     * 命令标识
     **/
    private int cmd;
    /**
     * 应答标志
     **/
    private int reply;
    /**
     * 唯一识别码
     **/
    private String vin;
    /**
     * 数据单元加密方式
     **/
    private int encryptMode;
    /**
     * 数据单元长度
     **/
    private int dataLength;
    /**
     * 数据项
     **/
    private Realinfo body;
    /**
     * 校验码
     **/
    private int bcc;
    /**
     * 是否检验成功
     **/
    private boolean validateResult = false;

}
