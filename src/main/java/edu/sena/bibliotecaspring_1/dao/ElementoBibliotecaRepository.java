package edu.sena.bibliotecaspring_1.dao;

import edu.sena.bibliotecaspring_1.model.ElementoBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementoBibliotecaRepository extends JpaRepository<ElementoBiblioteca, Integer> {
}