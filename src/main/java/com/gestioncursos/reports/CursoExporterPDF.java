package com.gestioncursos.reports;

import com.gestioncursos.entity.Curso;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CursoExporterPDF {
    private List<Curso> listaCursos;

    public CursoExporterPDF(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5.0F);
        Font font = FontFactory.getFont("Helvetica");
        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Titulo", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Descripcion", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Nivel", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Publicado", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        Iterator var2 = this.listaCursos.iterator();

        while(var2.hasNext()) {
            Curso curso = (Curso)var2.next();
            table.addCell(String.valueOf(curso.getId()));
            table.addCell(curso.getTitulo());
            table.addCell(curso.getDescripcion());
            table.addCell(String.valueOf(curso.getNivel()));
            table.addCell(String.valueOf(curso.isPublicado()));
        }

    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont("Helvetica-Bold");
        font.setSize(18.0F);
        font.setColor(Color.BLUE);
        Paragraph p = new Paragraph("Lista de cursos", font);
        p.setAlignment(1);
        document.add(p);
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{1.3F, 3.5F, 3.5F, 2.0F, 1.5F});
        table.setSpacingBefore(10.0F);
        this.writeTableHeader(table);
        this.writeTableData(table);
        document.add(table);
        document.close();
    }
}
