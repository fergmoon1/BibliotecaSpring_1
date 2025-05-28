package edu.sena.bibliotecaspring_1.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class ElementoBiblioteca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tipo;
    private String titulo;
    private String autor;
    private int anoPublicacion;
}