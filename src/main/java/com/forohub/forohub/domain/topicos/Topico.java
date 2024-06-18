package com.forohub.forohub.domain.topicos;

import com.forohub.forohub.domain.autores.Autor;
import com.forohub.forohub.domain.topicos.dto.DatosActualizarTopico;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Topico")
@Table(name = "topicos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fecha;
    @Enumerated(EnumType.STRING)
    private Estado status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    @Column(name = "nombre_curso")
    private String nombreCurso;

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Estado getStatus() {
        return status;
    }

    public Autor getAutor() {
        return autor;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public Topico(String titulo, String mensaje, LocalDateTime fecha, Estado status, Autor autor, String nombreCurso) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.status = status;
        this.autor = autor;
        this.nombreCurso = nombreCurso;
    }

    public void actualizar(DatosActualizarTopico datosActualizarTopico) {
        if (datosActualizarTopico.titulo() != null) {
            this.titulo = datosActualizarTopico.titulo();
        }
        if (datosActualizarTopico.mensaje() != null) {
            this.mensaje = datosActualizarTopico.mensaje();
        }
        if (datosActualizarTopico.nombreCurso() != null) {
            this.nombreCurso = datosActualizarTopico.nombreCurso();
        }
    }
}
