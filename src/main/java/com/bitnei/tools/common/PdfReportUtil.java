package com.bitnei.tools.common;

import com.bitnei.tools.annotation.Cell;
import com.bitnei.tools.annotation.Sheet;
import com.bitnei.tools.entity.CellInfo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaogd
 */
public class PdfReportUtil {
    private static Font headFont;
    private static Font keyFont;
    private static Font textFont;

    private static final int MAX_WIDTH = 520;

    static {
        BaseFont bfChinese;
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            headFont = new Font(bfChinese, 10, Font.BOLD);
            keyFont = new Font(bfChinese, 8, Font.BOLD);
            textFont = new Font(bfChinese, 8, Font.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void export(List<?> records, Class<?> clazz, HttpServletResponse response) throws Exception {
        Sheet sheet = clazz.getAnnotation(Sheet.class);
        String titleName = sheet.name();
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/octet-stream");
            String fileName = URLEncoder.encode(titleName, "UTF-8") + ".pdf";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            out.flush();

            Document document = new Document();
            document.setPageSize(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();
            PdfReportUtil.generatePdf(document, records, clazz, titleName);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    private static void generatePdf(Document document, List<?> records, Class<?> clazz, String titleName) throws IllegalAccessException, DocumentException {
        Field[] fields = clazz.getDeclaredFields();
        List<CellInfo> cells = getCellInfos(fields);

        PdfPTable table = createTable(cells.size());
        table.addCell(createCell(titleName, headFont, cells.size()));

        for (CellInfo cell : cells) {
            table.addCell(createCell(cell.getName(), keyFont));
        }
        for (Object obj : records) {
            for (CellInfo cell : cells) {
                Field field = cell.getField();
                Object val = field.get(obj);
                table.addCell(createCell(String.valueOf(val), textFont));
            }
        }
        document.add(table);
        document.close();
    }

    private static List<CellInfo> getCellInfos(Field[] fields) {
        List<CellInfo> cellInfos = new ArrayList<>();
        for (Field field : fields) {
            Cell cell = field.getAnnotation(Cell.class);
            if (cell == null) {
                continue;
            }
            field.setAccessible(true);
            CellInfo cellInfo = new CellInfo();
            cellInfo.setName(cell.name());
            cellInfo.setField(field);
            cellInfos.add(cellInfo);
        }
        return cellInfos;
    }

    private static PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    private static PdfPCell createCell(String value, Font font, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        cell.setBorder(0);
        cell.setPaddingTop(15.0f);
        cell.setPaddingBottom(8.0f);
        return cell;
    }

    private static PdfPTable createTable(int colNumber) {
        PdfPTable table = new PdfPTable(colNumber);
        table.setTotalWidth(MAX_WIDTH);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(1);
        return table;
    }
}