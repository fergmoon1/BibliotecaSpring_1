package edu.sena.bibliotecaspring_1.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Revista extends ElementoBiblioteca {
    private int numeroEdicion;
    private String categoria;

    public Revista() {
        this.setTipo("Revista");
    }

    public Revista(String titulo, String autor, int anoPublicacion, int numeroEdicion, String categoria) {
        this.setTipo("Revista");
        this.setTitulo(titulo);
        this.setAutor(autor);
        this.setAnoPublicacion(anoPublicacion);
        this.numeroEdicion = numeroEdicion;
        this.categoria = categoria;
    }
}