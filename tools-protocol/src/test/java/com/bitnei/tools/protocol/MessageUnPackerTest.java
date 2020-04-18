package com.bitnei.tools.protocol;

import com.bitnei.tools.protocol.unpack.bean.DataItem;
import com.bitnei.tools.protocol.unpack.bean.DataMessage;
import com.bitnei.tools.protocol.unpack.MessageUnPacker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Created on 2020/3/30.
 *
 * @author zhaogd
 */
class MessageUnPackerTest {

    @Test
    public void parserLogin() {
        String message = "232301fe4c4748433456314439484532303434303601001e140215171f29008a3839383630313137373935393530303839323938010063";
        MessageUnPacker unpacker = MessageUnPacker.fromClassPathXmlFile("32960.xml");
        DataMessage dataMessage = unpacker.unpack32960(message);
        Assertions.assertNotNull(dataMessage);
    }

    @Test
    public void parserRealInfo() {
        String message = "232302fe4c474843345631443548453230353631370101aa140409071f110102030103b6000002940b402aee5f010600c70000020101033b4e414fd85f0bb82738030000000000000006646464646464003c01003c01003c0101050006f00572026130bd0601052ee0010c03e801013a010a2e0700000000000000010000000601000000080801010b402aee00780001780cf00cf00cf00cf02ee00cf00cf00cf00cf00cf00cf003e80cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf0090101003c3a3a3a3a3a3a3a3a3a2e3a3a3a3a3a3a3a3a3a3a3a303a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3aea";
        MessageUnPacker unpacker = MessageUnPacker.fromClassPathXmlFile("32960.xml");
        DataMessage dataMessage = unpacker.unpack32960(message);
        final List<DataItem> dataItemList = dataMessage.getDataItemList();
        for (DataItem dataItem : dataItemList) {
            System.out.println(dataItem.getSeqNo() + "," + dataItem.getName() + "," + dataItem.getVal() + "," + dataItem.getSrcCode());
        }
        Assertions.assertNotNull(dataMessage);
    }
}