package com.bitnei.tools.protocol.general.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author chenpeng
 * @date 2019-12-26 16:28
 */
@Data
@Builder
public class BitGroup {

    /**
     * 源字节数据
     */
    private int intValue;


    /**
     *
     * @param intValue
     */
    public BitGroup(int intValue){
       this.intValue = intValue;
    }

    /**
     * 获取指定位置的key
     * @param pos
     * @return
     */
    public int value(final int pos, final int len){

        int mask = 0x00;
        for ( int i = 0; i < len; i++){
            mask |= (1 << pos+(1));
        }

        return ( intValue & mask ) >> pos;
    }


    public int value(final int pos){
        return value(pos, 1);
    }
}
