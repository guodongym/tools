package com.bitnei.tools.core.util;

import com.bitnei.tools.core.entity.DateFormatEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Created on 2019/6/6.
 *
 * @author zhaogd
 */
class DateUtilTest {

    @Test
    void convertDateStringToDayStartEpochMilli() {
        final long epochMilli = DateUtil.convertDateStringToDayStartEpochMilli("20190101", "yyyyMMdd");
        Assertions.assertEquals(1546272000000L, epochMilli);
    }

    @Test
    void convertDateStringToDayEndEpochMilli() {
        final long epochMilli = DateUtil.convertDateStringToDayEndEpochMilli("20190101", "yyyyMMdd");
        Assertions.assertEquals(1546358399999L, epochMilli);
    }

    @Test
    void testConvertDateTimeToEpochMilli() {
        final long epochMilli = DateUtil.convertDateTimeToEpochMilli(LocalDate.of(2019, 1, 1), LocalTime.MIN);
        Assertions.assertEquals(1546272000000L, epochMilli);
    }

    @Test
    void testConvertDateTimeStringToEpochMilli() {
        final long epochMilli = DateUtil.convertDateTimeStringToEpochMilli("20190101000000", "yyyyMMddHHmmss");
        Assertions.assertEquals(1546272000000L, epochMilli);
    }

    @Test
    void convertDateTimeToEpochMilli() {
        final LocalDateTime localDateTime = LocalDateTime.of(LocalDate.of(2019, 1, 1), LocalTime.MIN);
        final long epochMilli = DateUtil.convertDateTimeToEpochMilli(localDateTime);
        Assertions.assertEquals(1546272000000L, epochMilli);
    }

    @Test
    void testConvertToZonedDateTime() {
        final String zonedDateTime = DateUtil.convertToZonedDateTime("2019-06-11 05:45:23", DateFormatEnum.DATE_TIME.getFormat());
        Assertions.assertEquals("2019-06-11T05:45:23+08:00", zonedDateTime);
    }

    @Test
    void testConvertToZonedDate() {
        final String zonedDateTime = DateUtil.convertToZonedDate("2019-06-11", DateFormatEnum.DATE.getFormat());
        Assertions.assertEquals("2019-06-11+08:00", zonedDateTime);
    }

    @Test
    void testGetMonthBetween() throws ParseException {
        List<String> monthBetween = DateUtil.getMonthBetween("201710", "201802");
        String[] month = {"201710", "201711", "201712", "201801", "201802"};
        Assertions.assertArrayEquals(monthBetween.toArray(), month);
    }

}