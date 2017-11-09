package com.etiansoft.tools.common;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.etiansoft.tools.annotation.Cell;
import com.etiansoft.tools.annotation.Sheet;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfReportUtil {
	private static Font headfont;
	private static Font keyfont;
	private static Font textfont;

	static {
		BaseFont bfChinese;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			headfont = new Font(bfChinese, 10, Font.BOLD);
			keyfont = new Font(bfChinese, 8, Font.BOLD);
			textfont = new Font(bfChinese, 8, Font.NORMAL);
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
			IOUtils.closeQuietly(out);
		}
	}

	private static void generatePdf(Document document, List<?> records, Class<?> clazz, String titleName) throws IllegalAccessException, DocumentException {
		Field[] fields = clazz.getDeclaredFields();
		List<CellInfo> cells = getCellInfos(fields);

		PdfPTable table = createTable(cells.size());
		table.addCell(createCell(titleName, headfont, Element.ALIGN_CENTER, cells.size(), false));

		for (CellInfo cell : cells) {
			table.addCell(createCell(cell.getName(), keyfont, Element.ALIGN_CENTER));
		}
		for (int i = 0; i < records.size(); i++) {
			Object obj = records.get(i);
			for (int j = 0; j < cells.size(); j++) {
				CellInfo cell = cells.get(j);
				Field field = cell.getField();
				Object val = field.get(obj);
				table.addCell(createCell(String.valueOf(val), textfont));
			}
		}
		document.add(table);
		document.close();
	}

	private static List<CellInfo> getCellInfos(Field[] fields) {
		List<CellInfo> cellInfos = new ArrayList<CellInfo>();
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

	private static int maxWidth = 520;

	public static PdfPCell createCell(String value, Font font, int align) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}

	public static PdfPCell createCell(String value, Font font) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}

	public static PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setPhrase(new Phrase(value, font));
		cell.setPadding(3.0f);
		if (!boderFlag) {
			cell.setBorder(0);
			cell.setPaddingTop(15.0f);
			cell.setPaddingBottom(8.0f);
		}
		return cell;
	}

	public static PdfPTable createTable(int colNumber) {
		PdfPTable table = new PdfPTable(colNumber);
		table.setTotalWidth(maxWidth);
		table.setLockedWidth(true);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setBorder(1);
		return table;
	}
}