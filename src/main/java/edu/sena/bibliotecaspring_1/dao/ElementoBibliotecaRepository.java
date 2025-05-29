package edu.sena.bibliotecaspring_1.dao;


import edu.sena.bibliotecaspring_1.model.ElementoBiblioteca;
import edu.sena.bibliotecaspring_1.model.Libro;
import edu.sena.bibliotecaspring_1.model.Revista;
import edu.sena.bibliotecaspring_1.model.DVD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementoBibliotecaRepository extends JpaRepository<ElementoBiblioteca, Integer> {
    List<ElementoBiblioteca> findByTituloContainingIgnoreCase(String titulo);

    @Query("SELECT e FROM ElementoBiblioteca e WHERE e.tipo = :tipo")
    List<ElementoBiblioteca> findAllByTipo(String tipo);

    List<ElementoBiblioteca> findByAutorContainingIgnoreCase(String autor);

    @Query("SELECT l FROM Libro l WHERE l.genero LIKE %:genero%")
    List<Libro> findLibrosByGeneroContainingIgnoreCase(String genero);

    @Query("SELECT d FROM DVD d WHERE d.genero LIKE %:genero%")
    List<DVD> findDVDsByGeneroContainingIgnoreCase(String genero);

    @Query("SELECT l FROM Libro l WHERE l.editorial LIKE %:editorial%")
    List<Libro> findLibrosByEditorialContainingIgnoreCase(String editorial);

    @Query("SELECT r FROM Revista r WHERE r.editorial LIKE %:editorial%")
    List<Revista> findRevistasByEditorialContainingIgnoreCase(String editorial);
}