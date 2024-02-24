package com.gestioncursos.controller;
import com.gestioncursos.entity.Curso;
import com.gestioncursos.reports.CursoExporterExcel;
import com.gestioncursos.reports.CursoExporterPDF;
import com.gestioncursos.repository.CursoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.tags.Param;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public String home(){
        return "redirect:/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model,
                                      @RequestParam(name = "keyword", required = false) String keyword,
                                      @RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "size", defaultValue = "3") int size) {
        try{
            List<Curso> cursos = new ArrayList<>();
            Pageable paging = PageRequest.of(page-1,size);
            Page<Curso> pageCursos = null;
            if(keyword = null) {
                pageCursos = cursosRepository.findAll(paging);
            }else{
                pageCursos = cursoRepository.findByTituloContainingIgnoreCase(keyword.paging);
                model.addAttribute("keyword",keyword);
            }
            cursos = pageCursos.getContent();
            model.addAttribute("cursos",cursos);
            model.addAttribute("currentPage",pageCursos.getNumber()+1);
            model.addAttribute("totalItems",pageCursos.getTotalElements());
            model.addAttribute("totalPages",pageCursos.getTotalPages());
            model.addAttribute("pageSize",size);
        }catch (Exception exception){
            model.addAttribute("message",exception.getMessage());
        }
        return "cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model){
        List<Curso> cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);
        return "cursos";
    }

    @GetMapping("/cursos/nuevo")
    public String agregarCurso(Model model){
        Curso curso = new Curso();
        curso.setPublicado(true);
        model.addAttribute("curso", curso);
        model.addAttribute("pageTitle", "Nuevo curso");
        return "curso_form";
    }

    @PostMapping("/cursos/save")
    public String guardarCurso(Curso curso, RedirectAttributes redirectAttributes){
        try{
            cursoRepository.save(curso);
            redirectAttributes.addFlashAttribute("message", "El curso ha sido guardado con éxito");
        }catch(Exception e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/cursos";
    }

    @RequestMapping("/export/excel")
    public void generarReporteExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cursos_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<Curso> cursos = cursoRepository.findAll();
        CursoExporterExcel exporterExcel = new CursoExporterExcel(cursos);
        exporterExcel.export(response);
    }

    @RequestMapping("/export/pdf")
    public void generarReportePdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=cursos_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        List<Curso> cursos = cursoRepository.findAll();
        CursoExporterPDF exporterExcel = new CursoExporterPDF(cursos);
        exporterExcel.export(response);
    }
}