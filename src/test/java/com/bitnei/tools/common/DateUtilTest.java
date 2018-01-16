package com.bitnei.tools.common;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaogd
 * @Date 2018/1/16
 */
public class DateUtilTest {

    @Test
    public void getMonthBetween() throws ParseException {
        List<String> monthBetween = DateUtil.getMonthBetween("201710", "201802");
        String[] month = {"201710", "201711", "201712", "201801", "201802"};
        Assert.assertArrayEquals(monthBetween.toArray(), month);
    }
}