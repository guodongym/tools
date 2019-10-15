package com.bitnei.tools.gb32960.deflection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created on 2019/10/11.
 *
 * @author zhaogd
 */
class DeflectionUtilTest {
    private Map<String, String> dataMap = new HashMap<>();

    @BeforeEach
    void setUp() {
        dataMap.put("2103", "MTo1OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OF81OA==");
        dataMap.put("2114", "200|120");
        dataMap.put("2115", "555");
        dataMap.put("2302", "200");
        dataMap.put("2303", "21234");
        dataMap.put("2304", "200");
        dataMap.put("2306", "12345");
        dataMap.put("2311", "21234");
        dataMap.put("2609", "200");
        dataMap.put("2612", "200");
        dataMap.put("2614", "12345");
        dataMap.put("2502", "108843090");
        dataMap.put("2503", "30260076");
    }

    @Test
    void doDeflection() {
        DeflectionUtil.doDeflection(dataMap);
        validate(dataMap);
    }

    @Test
    void doDeflectionWithObjectMap() {
        Map<String, Object> objectMap = new HashMap<>();
        dataMap.forEach(objectMap::put);
        final Map<String, String> stringMap = DeflectionUtil.doDeflectionWithObjectMap(objectMap);
        validate(stringMap);
    }

    private void validate(Map<String, String> data) {
        final String expected2103 = "MToxOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOF8xOA==";
        assertEquals(expected2103, data.get("2103"));

        assertEquals("160|80", data.get("2114"));
        assertEquals("15.5", data.get("2115"));
        assertEquals("160", data.get("2302"));
        assertEquals("1234", data.get("2303"));
        assertEquals("160", data.get("2304"));
        assertEquals("234.5", data.get("2306"));
        assertEquals("123.4", data.get("2311"));
        assertEquals("160", data.get("2609"));
        assertEquals("160", data.get("2612"));
        assertEquals("234.5", data.get("2614"));
        assertEquals("108.84309", data.get("2502"));
        assertEquals("30.260076", data.get("2503"));

        // 验证数据不合法
        {
            dataMap.put("2402", "");
            dataMap.put("2103", "");
            DeflectionUtil.doDeflection(dataMap);

            assertEquals("", dataMap.get("2402"));
            assertEquals("", dataMap.get("2103"));
        }
    }
}