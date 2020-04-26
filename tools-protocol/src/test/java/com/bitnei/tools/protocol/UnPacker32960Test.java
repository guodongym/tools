package com.bitnei.tools.protocol;

import com.bitnei.tools.protocol.unpack.general.UnPacker32960;
import com.bitnei.tools.protocol.unpack.general.bean.DataItem;
import com.bitnei.tools.protocol.unpack.general.bean.DataMessage;
import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * Created on 2020/3/30.
 *
 * @author zhaogd
 */
class UnPacker32960Test {

    @Test
    public void parserLogin() {
        String message = "232301fe4c4748433456314439484532303434303601001e140215171f29008a3839383630313137373935393530303839323938010063";
        UnPacker32960 unpacker = UnPacker32960.fromClassPathXmlFile("32960.xml");
        DataMessage dataMessage = unpacker.unPack(message);
        Assertions.assertNotNull(dataMessage);
    }

    @Test
    public void parserRealInfo() {
        String message = "232302fe4c474843345631443548453230353631370101aa140409071f110102030103b6000002940b402aee5f010600c70000020101033b4e414fd85f0bb82738030000000000000006646464646464003c01003c01003c0101050006f00572026130bd0601052ee0010c03e801013a010a2e0700000000000000010000000601000000080801010b402aee00780001780cf00cf00cf00cf02ee00cf00cf00cf00cf00cf00cf003e80cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf0090101003c3a3a3a3a3a3a3a3a3a2e3a3a3a3a3a3a3a3a3a3a3a303a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3aea";
        UnPacker32960 unpacker = UnPacker32960.fromClassPathXmlFile("32960.xml");
        DataMessage dataMessage = unpacker.unPack(message);
        final Map<String, String> stringStringMap = dataMessage.toSimple();

        final List<DataItem> dataItemList = dataMessage.getDataItemList();
        for (DataItem dataItem : dataItemList) {
            System.out.println(dataItem.getSeqNo() + "," + dataItem.getName() + "," + dataItem.getVal() + "," + dataItem.getSrcCode());
        }
        Assertions.assertNotNull(dataMessage);
    }

    public void testRealInfo() {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 10000; i++) {
            String message = "232302fe4c474843345631443548453230353631370101aa140409071f110102030103b6000002940b402aee5f010600c70000020101033b4e414fd85f0bb82738030000000000000006646464646464003c01003c01003c0101050006f00572026130bd0601052ee0010c03e801013a010a2e0700000000000000010000000601000000080801010b402aee00780001780cf00cf00cf00cf02ee00cf00cf00cf00cf00cf00cf003e80cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf00cf0090101003c3a3a3a3a3a3a3a3a3a2e3a3a3a3a3a3a3a3a3a3a3a303a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3aea";
            UnPacker32960 unPacker = new UnPacker32960();
            DataMessage dataMessage = unPacker.unPack(message);
            final Map<String, String> stringStringMap = dataMessage.toSimple();
        }

        System.out.println(stopwatch.toString());
    }
}