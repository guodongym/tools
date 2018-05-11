package com.bitnei.tools.common;

import com.bitnei.tools.annotation.Cell;
import com.bitnei.tools.annotation.Sheet;
import com.bitnei.tools.entity.CellInfo;
import com.bitnei.tools.entity.CustomSheet;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Excel导出工具
 *
 * @author zhaogd
 */
public class ExcelExporter {

    private static final int DEFAULT_PAGE_SIZE = 10000;

    /**
     * 导出空excel
     *
     * @param out 输出流
     * @throws Exception 异常抛出
     */
    public static void exportEmpty(OutputStream out) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        workbook.createSheet();
        workbook.setSheetName(0, "无数据");
        workbook.write(out);
        out.flush();
        out.close();
    }

    /**
     * 导出定制化sheet,每个sheet内容可以不同
     *
     * @param customSheets 定制化实体
     * @param out          输出流
     * @param pageSize     每页条数
     * @throws Exception 异常抛出
     */
    public static void exportCustomSheet(List<CustomSheet> customSheets, OutputStream out, int pageSize) throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();

            int sheetIndex = 0;
            for (CustomSheet customSheet : customSheets) {
                List<?> records = customSheet.getRecords();
                int pageCount = getPageCount(pageSize, records.size());
                HSSFCellStyle headerStyle = generatorHeaderStyle(workbook);
                HSSFCellStyle cellStyle = generatorCellStyle(workbook);
                List<CellInfo> cellInfos = getCellInfos(customSheet.getClazz());

                // 如果没数据只写头
                if (records.size() == 0) {
                    HSSFSheet sheet = workbook.createSheet();
                    workbook.setSheetName(sheetIndex, customSheet.getSheetName() + 1);
                    generatorHeader(sheet, cellInfos, headerStyle);
                    sheetIndex++;
                } else {
                    for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                        HSSFSheet sheet = workbook.createSheet();
                        workbook.setSheetName(sheetIndex, customSheet.getSheetName() + (pageIndex + 1));
                        generatorHeader(sheet, cellInfos, headerStyle);
                        List<?> data = records.subList(pageSize * pageIndex, getTargetPageSize(records, pageSize, pageIndex));
                        generatorCells(data, sheet, cellInfos, cellStyle);

                        sheetIndex++;
                    }
                }
            }
            workbook.write(out);
        } finally {
            out.flush();
            out.close();
        }
    }


    public static void export(List<?> records, Class<?> clazz, OutputStream out) throws Exception {
        export(records, clazz, out, DEFAULT_PAGE_SIZE);
    }

    public static void export(List<?> records, Class<?> clazz, OutputStream out, int pageSize) throws IOException, IllegalAccessException {
        if (records == null || clazz == null) {
            return;
        }
        Sheet aSheet = clazz.getAnnotation(Sheet.class);
        int pageCount = getPageCount(pageSize, records.size());
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle headerStyle = generatorHeaderStyle(workbook);
        HSSFCellStyle cellStyle = generatorCellStyle(workbook);
        List<CellInfo> cellInfos = getCellInfos(clazz);
        try {
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                HSSFSheet sheet = workbook.createSheet();
                workbook.setSheetName(pageIndex, aSheet.name() + (pageIndex + 1));
                generatorHeader(sheet, cellInfos, headerStyle);
                List<?> data = records.subList(pageSize * pageIndex, getTargetPageSize(records, pageSize, pageIndex));
                generatorCells(data, sheet, cellInfos, cellStyle);
            }
            workbook.write(out);
        } finally {
            out.flush();
            out.close();
        }
    }

    private static int getTargetPageSize(List<?> records, int pageSize, int pageIndex) {
        boolean extraPage = pageSize * pageIndex + pageSize > records.size();
        if (extraPage) {
            return records.size();
        }
        return pageSize * pageIndex + pageSize;
    }

    private static int getPageCount(int pageSize, int count) {
        boolean extraPage = count % pageSize != 0;
        if (extraPage) {
            return count / pageSize + 1;
        }
        return count / pageSize;
    }

    private static HSSFCellStyle generatorHeaderStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor((short) 22);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static HSSFCellStyle generatorCellStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static void generatorHeader(HSSFSheet sheet, List<CellInfo> cellInfos, HSSFCellStyle headerStyle) {
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < cellInfos.size(); i++) {
            CellInfo cellInfo = cellInfos.get(i);
            sheet.setColumnWidth(i, 30 * 256);
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(cellInfo.getName());
            cell.setCellStyle(headerStyle);
        }
    }

    private static void generatorCells(List<?> records, HSSFSheet sheet, List<CellInfo> cellInfos, HSSFCellStyle cellStyle) throws IllegalAccessException {
        for (short i = 0; i < records.size(); ++i) {
            Object record = records.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            for (int j = 0; j < cellInfos.size(); ++j) {
                CellInfo cellInfo = cellInfos.get(j);
                HSSFCell cell = row.createCell(j);
                Field field = cellInfo.getField();
                cell.setCellStyle(cellStyle);
                Object value = field.get(record);
                if (value == null) {
                    value = cellInfo.getDefaultValue();
                }
                Class<?> type = field.getType();
                if (String.class.equals(type)) {
                    String stringValue = (String) value;
                    boolean reg = Pattern.matches("\\d{1,18}", stringValue);
                    if (reg) {
                        cell.setCellValue(Long.parseLong(stringValue.trim()));
                    } else {
                        cell.setCellValue(stringValue.trim());
                    }
                } else if (Character.class.equals(type) || char.class.equals(type)) {
                    cell.setCellValue(String.valueOf(value));
                } else if (Integer.class.equals(type) || int.class.equals(type)) {
                    cell.setCellValue((Integer) value);
                } else if (Double.class.equals(type) || double.class.equals(type)) {
                    cell.setCellValue((Double) value);
                } else if (Float.class.equals(type) || float.class.equals(type)) {
                    cell.setCellValue((Float) value);
                } else if (Short.class.equals(type) || short.class.equals(type)) {
                    cell.setCellValue((Short) value);
                } else if (Long.class.equals(type) || long.class.equals(type)) {
                    cell.setCellValue((Long) value);
                } else if (Byte.class.equals(type) || byte.class.equals(type)) {
                    cell.setCellValue((Long) value);
                } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                    cell.setCellValue((Boolean) value);
                } else if (Date.class.equals(type)) {
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else {
                        Date date = (Date) value;
                        DateFormat format = new SimpleDateFormat(cellInfo.getFormat());
                        cell.setCellValue(format.format(date));
                    }
                }
            }
        }
    }

    private static List<CellInfo> getCellInfos(Class<?> clazz) {
        List<CellInfo> cellInfos = new ArrayList<>();

        List<Field> fields = getAllFields(clazz);
        for (Field field : fields) {
            Cell cell = field.getAnnotation(Cell.class);
            if (cell == null) {
                continue;
            }
            field.setAccessible(true);
            CellInfo cellInfo = new CellInfo();
            cellInfo.setName(cell.name());
            cellInfo.setOrder(cell.order());
            cellInfo.setField(field);
            cellInfo.setFormat(cell.format());
            cellInfo.setDefaultValue(cell.defaultValue());
            cellInfos.add(cellInfo);
        }
        Collections.sort(cellInfos);
        return cellInfos;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        addAllFields(clazz, allFields);
        return allFields;
    }

    private static void addAllFields(Class<?> clazz, List<Field> allFields) {
        Field[] fields = clazz.getDeclaredFields();
        allFields.addAll(Arrays.asList(fields));
        Class<?> superclass = clazz.getSuperclass();
        if (Object.class.equals(superclass)) {
            return;
        }
        addAllFields(superclass, allFields);
    }
}