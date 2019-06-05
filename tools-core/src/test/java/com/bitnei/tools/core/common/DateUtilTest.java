package com.bitnei.tools.core.common;

import com.bitnei.tools.core.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author zhaogd
 * @Date 2018/1/16
 */
class DateUtilTest {

    @Test
    void getMonthBetween() throws ParseException {
        List<String> monthBetween = DateUtil.getMonthBetween("201710", "201802");
        String[] month = {"201710", "201711", "201712", "201801", "201802"};
        Assertions.assertArrayEquals(monthBetween.toArray(), month);
    }

}