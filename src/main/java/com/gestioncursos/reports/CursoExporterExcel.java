package com.gestioncursos.reports;

import com.gestioncursos.entity.Curso;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CursoExporterExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Curso> cursos;

    public CursoExporterExcel(List<Curso> cursos) {
        this.cursos = cursos;
        this.workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        this.sheet = this.workbook.createSheet("Cursos");
        XSSFRow row = this.sheet.createRow(0);
        CellStyle style = this.workbook.createCellStyle();
        XSSFFont font = this.workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16.0);
        style.setFont(font);
        this.createCell(row, 0, "ID", style);
        this.createCell(row, 1, "Titulo", style);
        this.createCell(row, 2, "Descripcion", style);
        this.createCell(row, 3, "Nivel", style);
        this.createCell(row, 4, "Estado de publicacion", style);
    }

    private void createCell(XSSFRow row, int columnCount, Object value, CellStyle style) {
        this.sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((double)(Integer)value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean)value);
        } else {
            cell.setCellValue((String)value);
        }

        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = this.workbook.createCellStyle();
        XSSFFont font = this.workbook.createFont();
        font.setFontHeight(14.0);
        style.setFont(font);
        Iterator var4 = this.cursos.iterator();

        while(var4.hasNext()) {
            Curso curso = (Curso)var4.next();
            XSSFRow row = this.sheet.createRow(rowCount++);
            int columnCount = 0;
            this.createCell(row, columnCount++, curso.getId(), style);
            this.createCell(row, columnCount++, curso.getTitulo(), style);
            this.createCell(row, columnCount++, curso.getDescripcion(), style);
            this.createCell(row, columnCount++, curso.getNivel(), style);
            this.createCell(row, columnCount++, curso.isPublicado(), style);
        }

    }

    public void export(HttpServletResponse response) throws IOException {
        this.writeHeaderLine();
        this.writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        this.workbook.write(outputStream);
        this.workbook.close();
        outputStream.close();
    }
}