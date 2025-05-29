package edu.sena.bibliotecaspring_1.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("LIBRO")
@Table(name = "libro")
public class Libro extends ElementoBiblioteca {
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "numero_paginas")
    private int numeroPaginas;

    @Column(name = "genero")
    private String genero;

    @Column(name = "editorial")
    private String editorial;

    public Libro() {
        super();
    }

    public Libro(String titulo, String autor, int anoPublicacion, String isbn, int numeroPaginas, String genero, String editorial) {
        super(titulo, autor, anoPublicacion);
        this.isbn = isbn;
        this.numeroPaginas = numeroPaginas;
        this.genero = genero;
        this.editorial = editorial;
    }

    // Getters y Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(int numeroPaginas) { this.numeroPaginas = numeroPaginas; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
}