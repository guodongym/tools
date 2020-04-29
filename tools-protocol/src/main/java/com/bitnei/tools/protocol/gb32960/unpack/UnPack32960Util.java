package com.bitnei.tools.protocol.gb32960.unpack;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.ReferenceCountUtil;

import java.util.Map;

/**
 * 报文解析工具类
 *
 * @author zhaogd
 * @date 2020/4/29
 */
public class UnPack32960Util {

    public static Map<String, String> unPack(String hexString) {
        return unPack(ByteBufUtil.decodeHexDump(hexString));
    }

    public static Map<String, String> unPack(byte[] bytes) {
        final ByteBuf src = PooledByteBufAllocator.DEFAULT.buffer();
        try {
            src.writeBytes(bytes);



        } finally {
            ReferenceCountUtil.release(src);
        }

        return Maps.newHashMap();
    }
}
