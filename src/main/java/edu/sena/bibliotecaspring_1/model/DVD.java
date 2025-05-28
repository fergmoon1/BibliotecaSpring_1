package edu.sena.bibliotecaspring_1.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DVD extends ElementoBiblioteca {
    private int duracion;
    private String genero;

    public DVD() {
        this.setTipo("DVD");
    }

    public DVD(String titulo, String autor, int anoPublicacion, int duracion, String genero) {
        this.setTipo("DVD");
        this.setTitulo(titulo);
        this.setAutor(autor);
        this.setAnoPublicacion(anoPublicacion);
        this.duracion = duracion;
        this.genero = genero;
    }
}