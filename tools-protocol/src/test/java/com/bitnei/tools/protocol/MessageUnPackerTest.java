package com.bitnei.tools.protocol;

import com.bitnei.tools.protocol.bean.DataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created on 2020/3/30.
 *
 * @author zhaogd
 */
class MessageUnPackerTest {

    @Test
    public void parserLogin(){
        String message = "232301fe4c4748433456314439484532303434303601001e140215171f29008a3839383630313137373935393530303839323938010063";
        MessageUnPacker unpacker = MessageUnPacker.fromClassPathXmlFile("32960.xml");
        DataMessage dataMessage = unpacker.unpack32960(message);
        Assertions.assertNotNull(dataMessage);
    }

    @Test
    public void parserRealInfo(){
        String message = "232302fe4c4d5047324b4142334a413433313035340101cc1402181000070101030101a30004ab781814279548012e27100c00020101014e56114f6a5217de27880801011814279500a00001a00f200f210f210f200f200f220f230f220f230f220f220f200f240f230f220f220f200f210f210f200f1c0f1d0f1c0f1d0f1a0f1c0f1c0f1b0f1a0f1a0f1b0f1b0f1b0f190f1a0f1a0f190f1a0f190f1a0f150f170f160f150f130f070f130f140f110f120f0d0f150f110f110f120f0b0f120f110f110f110f0f0f100f110f110f100f0f0f0f0f0e0f0f0f100f0e0f0f0f100f0f0f110f0c0f110f110f110f110f120f140f150f160f160f140f150f160f110f130f110f110f220f220f220f230f200f210f200f200f200f210f210f210f200f200f210f200f210f230f1f0f1f0f190f210f200f200f200f210f200f1f0f1f0f200f200f200f200f200f200f1f0f230f210f210f220f220f1f0f200f200f200f210f210f200f200f220f220f210f200f1f0f200f210f220f210f1f0f210f230f230f200f1e0f210f200f210f2009010100103e3f3f3e3e3e3f3e3e3f3f3f3e3f3f3e06010d0f24012e0f0701023f01013e0700000000000000000005000679f1af020a436b800018014000fa0101f40183f2f3e54ec0fdba757893e88cbc9f2a810001005e";
        MessageUnPacker unpacker = MessageUnPacker.fromClassPathXmlFile("32960.xml");
        DataMessage dataMessage = unpacker.unpack32960(message);
        System.out.println(dataMessage.read("2061", "2062", "208200", "3201", "3202"));
        Assertions.assertNotNull(dataMessage);
    }
}