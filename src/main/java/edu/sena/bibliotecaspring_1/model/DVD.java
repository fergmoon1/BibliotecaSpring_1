package edu.sena.bibliotecaspring_1.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("DVD")
@Table(name = "dvd")
public class DVD extends ElementoBiblioteca {
    @Column(name = "duracion")
    private int duracion;

    @Column(name = "genero")
    private String genero;

    public DVD() {
        super();
    }

    public DVD(String titulo, String autor, int anoPublicacion, int duracion, String genero) {
        super(titulo, autor, anoPublicacion); // Llama al constructor de ElementoBiblioteca
        this.duracion = duracion;
        this.genero = genero;
    }

    // Getters y Setters
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}