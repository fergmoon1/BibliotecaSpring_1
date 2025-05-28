package edu.sena.bibliotecaspring_1.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Libro extends ElementoBiblioteca {
    private String isbn;
    private int numeroPaginas;
    private String genero;
    private String editorial;

    public Libro() {
        this.setTipo("Libro");
    }

    public Libro(String titulo, String autor, int anoPublicacion, String isbn, int numeroPaginas, String genero, String editorial) {
        this.setTipo("Libro");
        this.setTitulo(titulo);
        this.setAutor(autor);
        this.setAnoPublicacion(anoPublicacion);
        this.isbn = isbn;
        this.numeroPaginas = numeroPaginas;
        this.genero = genero;
        this.editorial = editorial;
    }
}