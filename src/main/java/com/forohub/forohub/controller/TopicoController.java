package com.forohub.forohub.controller;

import com.forohub.forohub.domain.topicos.*;
import com.forohub.forohub.domain.topicos.dto.*;
import com.forohub.forohub.domain.topicos.validaciones.ValidadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private ValidadorService validadorService;

    @Tag(name = "post", description = "Metodos POST de API de topicos")
    @Operation(
            summary = "Registrar topico",
            description = "Registrar topico no existente en base a su titulo y/o mensaje. La respuesta el topico registrado con su respectivo id, titulo, mensaje, status, autor, nombreCurso y fecha"
    )
    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetallesTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico, UriComponentsBuilder uriComponentsBuilder) {
        var topico = validadorService.registrarTopico(datosRegistroTopico);
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosDetallesTopico(topico));
    }

    @Tag(name = "get", description = "Metodos GET de API de topicos")
    @Operation(
            summary = "Listar topicos",
            description = "Listar todos los topicos. La respuesta es una lista de topicos con su respectivo id, titulo, mensaje, status, autor, nombreCurso y fecha"
    )
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(Pageable pageable) {
        var topicos = topicoRepository.findAll(pageable)
                .map(DatosListadoTopico::new);
        return ResponseEntity.ok(topicos);
    }

    @Tag(name = "get", description = "Metodos GET de API de topicos")
    @Operation(
            summary = "Listar topicos por fecha",
            description = "Listar topicos por su fecha de manera ascendiente. La respuesta es una lista de topicos con su respectivo id, titulo, mensaje, status, autor, nombreCurso y fecha"
    )
    @GetMapping("/filtrar1")
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicosForFecha(@PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.ASC) Pageable pageable) {
        var topicos = topicoRepository.findAll(pageable)
                .map(DatosListadoTopico::new);
        return ResponseEntity.ok(topicos);
    }

    @Tag(name = "get", description = "Metodos GET de API de topicos")
    @Operation(
            summary = "Listar topicos por nombre de curso y año",
            description = "Listar topicos por nombre de curso y año especifico con criterios de busqueda. La respuesta es una lista de topicos con su respectivo id, titulo, mensaje, status, autor, nombreCurso y fecha"
    )
    @GetMapping("/filtrar2")
    public ResponseEntity listarTopicosPorNombreCursoYAño(@RequestParam String curso, @RequestParam Integer año) {
        var topico = topicoRepository.findByNombreCursoAndFechaAño(curso, año);
        return ResponseEntity.ok(new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getAutor().getLogin(),
                topico.getNombreCurso(),
                topico.getFecha()
        ));
    }

    @Tag(name = "get", description = "Metodos GET de API de topicos")
    @Operation(
            summary = "Detallar un topico",
            description = "Detallar un topico especifico por su id. La respuesta es un objeto con su respectivo id, titulo, mensaje, status, autor, nombreCurso y fecha"
    )
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> detallarTopico(@PathVariable Long id) {
        var topico = validadorService.validarExistencia(id);
        return ResponseEntity.ok(new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getAutor().getLogin(),
                topico.getNombreCurso(),
                topico.getFecha()
        ));
    }

    @Tag(name = "put", description = "Metodos PUT de API de topicos")
    @Operation(
            summary = "Actualizar un topico",
            description = "Actualizar un topico existente. La respuesta es un objeto del topico actualizado con el id, titulo, mensaje, status, autor, nombreCurso y fecha"
    )
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {
        var topico = validadorService.validarExistencia(id);
        topico.actualizar(datosActualizarTopico);
        return ResponseEntity.ok(new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getAutor().getLogin(),
                topico.getNombreCurso(),
                topico.getFecha()
        ));
    }

    @Tag(name = "delete", description = "Metodos DELETE de API de topicos")
    @Operation(summary = "Eliminar un topico")
    @DeleteMapping("/{id}")
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        var topicoOptional = validadorService.validarExistencia(id);
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
