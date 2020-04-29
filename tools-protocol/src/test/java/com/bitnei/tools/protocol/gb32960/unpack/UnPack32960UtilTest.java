package com.bitnei.tools.protocol.gb32960.unpack;

import com.alibaba.fastjson.JSON;
import com.bitnei.tools.protocol.gb32960.unpack.bean.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created on 2020/4/29.
 *
 * @author zhaogd
 */
class UnPack32960UtilTest {

    @Test
    void unPack() {
        String data = "232302fe4c474843345631443548453230353631370101aa140409071f110102030103b6000002940b402aee5f010600c70000020101033b4e414fd85f0bb82738030000000000000006646464646464003c01003c01003c0101050006f00572026130bd0601052ee0010c03e801013a010a2e0700000000000000010000000601000000080801010b402aee00780001780cf00cf00cf00cf02ee00cf00cf00cf00cf00cf00cf003e80cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf0090101003c3a3a3a3a3a3a3a3a3a2e3a3a3a3a3a3a3a3a3a3a3a303a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3aea";
        final Message message = UnPack32960Util.unPack(data);
        System.out.println(JSON.toJSONString(message, true));
    }
}