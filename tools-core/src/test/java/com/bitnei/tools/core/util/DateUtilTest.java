package com.bitnei.tools.core.util;

import com.bitnei.tools.core.entity.DateFormatEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created on 2019/6/6.
 *
 * @author zhaogd
 */
class DateUtilTest {

    @Test
    void parseToLocalDateTime() {
        final LocalDateTime localDateTime = DateUtil.parseToLocalDateTime("2019-06-11 05:45:23", DateFormatEnum.DATE_TIME.getFormat());

        Assertions.assertEquals("2019-06-11T05:45:23", localDateTime.toString());
    }
    @Test
    void parseToLocalDate() {
        final LocalDate localDate = DateUtil.parseToLocalDate("2019-06-11", DateFormatEnum.DATE.getFormat());

        Assertions.assertEquals("2019-06-11", localDate.toString());
    }


    @Test
    void convertToZonedDateTime() {
        final LocalDateTime localDateTime = DateUtil.parseToLocalDateTime("2019-06-11 05:45:23", DateFormatEnum.DATE_TIME.getFormat());
        final String zonedDateTime = DateUtil.convertToZonedDateTime(localDateTime);

        Assertions.assertEquals("2019-06-11T05:45:23+08:00", zonedDateTime);
    }


    @Test
    void convertToZonedDate() {
        final LocalDate localDate = DateUtil.parseToLocalDate("2019-06-11", DateFormatEnum.DATE.getFormat());
        final String zonedDateTime = DateUtil.convertToZonedDate(localDate);

        Assertions.assertEquals("2019-06-11+08:00", zonedDateTime);
    }
}