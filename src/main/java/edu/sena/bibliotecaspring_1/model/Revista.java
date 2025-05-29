package edu.sena.bibliotecaspring_1.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("REVISTA")
@Table(name = "revista")
public class Revista extends ElementoBiblioteca {
    @Column(name = "numero_edicion")
    private int numeroEdicion;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "editorial")
    private String editorial;

    @Column(name = "numero")
    private int numero;

    public Revista() {
        super();
    }

    public Revista(String titulo, String autor, int anoPublicacion, int numeroEdicion, String categoria, String editorial, int numero) {
        super(titulo, autor, anoPublicacion);
        this.numeroEdicion = numeroEdicion;
        this.categoria = categoria;
        this.editorial = editorial;
        this.numero = numero;
    }

    // Getters y Setters
    public int getNumeroEdicion() { return numeroEdicion; }
    public void setNumeroEdicion(int numeroEdicion) { this.numeroEdicion = numeroEdicion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
}