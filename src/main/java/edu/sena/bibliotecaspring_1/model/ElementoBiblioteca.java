package edu.sena.bibliotecaspring_1.model;

import jakarta.persistence.*;

@Entity
@Table(name = "elemento_biblioteca")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class ElementoBiblioteca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "autor", nullable = false)
    private String autor;

    @Column(name = "ano_publicacion", nullable = false)
    private int anoPublicacion;

    @Column(name = "tipo", insertable = false, updatable = false)
    private String tipo;

    public ElementoBiblioteca() {}

    public ElementoBiblioteca(String titulo, String autor, int anoPublicacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public int getAnoPublicacion() { return anoPublicacion; }
    public void setAnoPublicacion(int anoPublicacion) { this.anoPublicacion = anoPublicacion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}