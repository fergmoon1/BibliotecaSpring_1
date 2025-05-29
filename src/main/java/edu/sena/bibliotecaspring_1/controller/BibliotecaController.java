package edu.sena.bibliotecaspring_1.controller;

import edu.sena.bibliotecaspring_1.model.ElementoBiblioteca;
import edu.sena.bibliotecaspring_1.model.Libro;
import edu.sena.bibliotecaspring_1.model.Revista;
import edu.sena.bibliotecaspring_1.model.DVD;
import edu.sena.bibliotecaspring_1.dao.ElementoBibliotecaRepository;
import edu.sena.bibliotecaspring_1.util.BibliotecaException;
import edu.sena.bibliotecaspring_1.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/biblioteca")
public class BibliotecaController {

    @Autowired
    private ElementoBibliotecaRepository elementoRepository;

    @GetMapping
    public String listarElementos(Model model) {
        try {
            List<ElementoBiblioteca> elementos = elementoRepository.findAll();
            model.addAttribute("elementos", elementos);
            model.addAttribute("tituloFiltro", "Todos los Elementos");
        } catch (Exception e) {
            Logger.logError("Error al listar elementos", e);
            model.addAttribute("error", "Error al listar elementos: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/libros")
    public String listarLibros(Model model) {
        try {
            List<ElementoBiblioteca> libros = elementoRepository.findAllByTipo("LIBRO");
            model.addAttribute("elementos", libros);
            model.addAttribute("tituloFiltro", "Libros");
        } catch (Exception e) {
            Logger.logError("Error al listar libros", e);
            model.addAttribute("error", "Error al listar libros: " + e.getMessage());
            model.addAttribute("elementos", new ArrayList<>());
        }
        return "index";
    }

    @GetMapping("/dvds")
    public String listarDvds(Model model) {
        try {
            List<ElementoBiblioteca> dvds = elementoRepository.findAllByTipo("DVD");
            model.addAttribute("elementos", dvds);
            model.addAttribute("tituloFiltro", "DVDs");
        } catch (Exception e) {
            Logger.logError("Error al listar DVDs", e);
            model.addAttribute("error", "Error al listar DVDs: " + e.getMessage());
            model.addAttribute("elementos", new ArrayList<>());
        }
        return "index";
    }

    @GetMapping("/revistas")
    public String listarRevistas(Model model) {
        try {
            List<ElementoBiblioteca> revistas = elementoRepository.findAllByTipo("REVISTA");
            model.addAttribute("elementos", revistas);
            model.addAttribute("tituloFiltro", "Revistas");
        } catch (Exception e) {
            Logger.logError("Error al listar revistas", e);
            model.addAttribute("error", "Error al listar revistas: " + e.getMessage());
            model.addAttribute("elementos", new ArrayList<>());
        }
        return "index";
    }

    @GetMapping("/filtrarPorTipo")
    public String filtrarPorTipo(@RequestParam(required = false) String tipo, Model model) {
        try {
            List<ElementoBiblioteca> elementos;
            if (tipo != null && !tipo.isEmpty()) {
                String tipoDiscriminador;
                switch (tipo) {
                    case "Libro":
                        tipoDiscriminador = "LIBRO";
                        break;
                    case "Revista":
                        tipoDiscriminador = "REVISTA";
                        break;
                    case "DVD":
                        tipoDiscriminador = "DVD";
                        break;
                    default:
                        throw new BibliotecaException("Tipo no válido: " + tipo);
                }
                elementos = elementoRepository.findAllByTipo(tipoDiscriminador);
                model.addAttribute("tituloFiltro", "Mostrando: " + tipo + "s");
            } else {
                elementos = elementoRepository.findAll();
                model.addAttribute("tituloFiltro", "Todos los Elementos");
            }
            model.addAttribute("elementos", elementos);
        } catch (Exception e) {
            Logger.logError("Error al filtrar por tipo: " + tipo, e);
            model.addAttribute("error", "Error al filtrar elementos: " + e.getMessage());
            model.addAttribute("elementos", new ArrayList<>());
        }
        return "index";
    }

    @GetMapping("/agregar")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("tipos", new String[]{"Libro", "Revista", "DVD"});
        return "formulario-agregar";
    }

    @PostMapping("/agregar")
    public String agregarElemento(@RequestParam String tipo,
                                  @RequestParam String titulo,
                                  @RequestParam String autor,
                                  @RequestParam int anoPublicacion,
                                  @RequestParam(required = false) String isbn,
                                  @RequestParam(required = false) Integer numeroPaginas,
                                  @RequestParam(required = false) String generoLibro,
                                  @RequestParam(required = false) String editorial,
                                  @RequestParam(required = false) Integer numeroEdicion,
                                  @RequestParam(required = false) String categoria,
                                  @RequestParam(required = false) Integer duracion,
                                  @RequestParam(required = false) String generoDVD,
                                  RedirectAttributes redirectAttributes) {
        try {
            Logger.logInfo("Intentando agregar elemento - Tipo: " + tipo + ", Título: " + titulo +
                    ", Autor: " + autor + ", Año: " + anoPublicacion +
                    ", ISBN: " + isbn + ", Páginas: " + numeroPaginas + ", Género Libro: " + generoLibro +
                    ", Editorial: " + editorial + ", Edición: " + numeroEdicion + ", Categoría: " + categoria +
                    ", Duración: " + duracion + ", Género DVD: " + generoDVD);

            ElementoBiblioteca nuevoElemento = crearElemento(tipo, titulo, autor, anoPublicacion, isbn, numeroPaginas,
                    generoLibro, editorial, numeroEdicion, categoria, duracion, generoDVD);
            elementoRepository.save(nuevoElemento);
            Logger.logInfo("Elemento agregado con éxito: " + titulo);
            redirectAttributes.addFlashAttribute("mensaje", "Elemento agregado con éxito");
        } catch (BibliotecaException e) {
            Logger.logError("Error al agregar elemento", e);
            redirectAttributes.addFlashAttribute("error", "Error al agregar elemento: " + e.getMessage());
        } catch (Exception e) {
            Logger.logError("Error inesperado al agregar elemento", e);
            redirectAttributes.addFlashAttribute("error", "Error inesperado al agregar elemento: " + e.getMessage());
        }
        return "redirect:/biblioteca";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        try {
            ElementoBiblioteca elemento = elementoRepository.findById(id)
                    .orElseThrow(() -> new BibliotecaException("Elemento no encontrado con ID: " + id));
            model.addAttribute("elemento", elemento);
            model.addAttribute("tipos", new String[]{"Libro", "Revista", "DVD"});
            // Añadir el tipo específico al modelo para que la plantilla lo use
            String tipo = elemento.getTipo().equals("LIBRO") ? "Libro" :
                    elemento.getTipo().equals("REVISTA") ? "Revista" :
                            elemento.getTipo().equals("DVD") ? "DVD" : "";
            model.addAttribute("tipoSeleccionado", tipo);
        } catch (BibliotecaException e) {
            Logger.logError("Error al cargar elemento para edición ID: " + id, e);
            model.addAttribute("error", "Error al cargar elemento: " + e.getMessage());
            return "redirect:/biblioteca";
        }
        return "formulario-editar";
    }

    @PostMapping("/editar/{id}")
    public String actualizarElemento(@PathVariable int id,
                                     @RequestParam String tipo,
                                     @RequestParam String titulo,
                                     @RequestParam String autor,
                                     @RequestParam int anoPublicacion,
                                     @RequestParam(required = false) String isbn,
                                     @RequestParam(required = false) Integer numeroPaginas,
                                     @RequestParam(required = false) String generoLibro,
                                     @RequestParam(required = false) String editorial,
                                     @RequestParam(required = false) Integer numeroEdicion,
                                     @RequestParam(required = false) String categoria,
                                     @RequestParam(required = false) Integer duracion,
                                     @RequestParam(required = false) String generoDVD,
                                     RedirectAttributes redirectAttributes) {
        try {
            ElementoBiblioteca elementoExistente = elementoRepository.findById(id)
                    .orElseThrow(() -> new BibliotecaException("Elemento no encontrado con ID: " + id));

            // Verificar si el tipo ha cambiado
            String tipoActual = elementoExistente.getTipo();
            String tipoNuevo;
            switch (tipo) {
                case "Libro":
                    tipoNuevo = "LIBRO";
                    break;
                case "Revista":
                    tipoNuevo = "REVISTA";
                    break;
                case "DVD":
                    tipoNuevo = "DVD";
                    break;
                default:
                    throw new BibliotecaException("Tipo no válido: " + tipo);
            }

            if (!tipoActual.equals(tipoNuevo)) {
                // Si el tipo cambió, creamos un nuevo elemento y eliminamos el antiguo
                ElementoBiblioteca nuevoElemento = crearElemento(tipo, titulo, autor, anoPublicacion, isbn, numeroPaginas,
                        generoLibro, editorial, numeroEdicion, categoria, duracion, generoDVD);
                nuevoElemento.setId(id); // Mantenemos el mismo ID
                elementoRepository.delete(elementoExistente);
                elementoRepository.save(nuevoElemento);
            } else {
                // Si el tipo no cambió, actualizamos los campos del elemento existente
                elementoExistente.setTitulo(titulo);
                elementoExistente.setAutor(autor);
                elementoExistente.setAnoPublicacion(anoPublicacion);

                if (elementoExistente instanceof Libro) {
                    Libro libro = (Libro) elementoExistente;
                    libro.setIsbn(isbn != null ? isbn : "");
                    libro.setNumeroPaginas(numeroPaginas != null ? numeroPaginas : 0);
                    libro.setGenero(generoLibro != null ? generoLibro : "");
                    libro.setEditorial(editorial != null ? editorial : "");
                } else if (elementoExistente instanceof Revista) {
                    Revista revista = (Revista) elementoExistente;
                    revista.setNumeroEdicion(numeroEdicion != null ? numeroEdicion : 0);
                    revista.setCategoria(categoria != null ? categoria : "");
                } else if (elementoExistente instanceof DVD) {
                    DVD dvd = (DVD) elementoExistente;
                    dvd.setDuracion(duracion != null ? duracion : 0);
                    dvd.setGenero(generoDVD != null ? generoDVD : "");
                }
                elementoRepository.save(elementoExistente);
            }

            Logger.logInfo("Elemento actualizado ID: " + id);
            redirectAttributes.addFlashAttribute("mensaje", "Elemento actualizado con éxito");
        } catch (BibliotecaException e) {
            Logger.logError("Error al actualizar elemento ID: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar elemento: " + e.getMessage());
        } catch (Exception e) {
            Logger.logError("Error inesperado al actualizar elemento ID: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error inesperado al actualizar elemento: " + e.getMessage());
        }
        return "redirect:/biblioteca";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarElemento(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            ElementoBiblioteca elemento = elementoRepository.findById(id)
                    .orElseThrow(() -> new BibliotecaException("Elemento no encontrado con ID: " + id));
            elementoRepository.delete(elemento);
            Logger.logInfo("Elemento eliminado ID: " + id);
            redirectAttributes.addFlashAttribute("mensaje", "Elemento eliminado con éxito");
        } catch (BibliotecaException e) {
            Logger.logError("Error al eliminar elemento ID: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar elemento: " + e.getMessage());
        }
        return "redirect:/biblioteca";
    }

    @GetMapping("/buscarTitulo")
    public String buscarPorTitulo(@RequestParam String titulo, Model model) {
        try {
            List<ElementoBiblioteca> resultados = elementoRepository.findByTituloContainingIgnoreCase(titulo);
            if (resultados.isEmpty()) {
                model.addAttribute("error", "No se encontraron elementos para el título: " + titulo);
            } else {
                model.addAttribute("elementos", resultados);
                model.addAttribute("tituloFiltro", "Resultados para título: " + titulo);
            }
        } catch (Exception e) {
            Logger.logError("Error al buscar por título", e);
            model.addAttribute("error", "Error al buscar: " + e.getMessage());
            model.addAttribute("elementos", new ArrayList<>());
            model.addAttribute("tituloFiltro", "Error en la búsqueda");
        }
        return "index";
    }

    @GetMapping("/buscarAutor")
    public String buscarPorAutor(@RequestParam String autor, Model model) {
        try {
            List<ElementoBiblioteca> resultados = elementoRepository.findByAutorContainingIgnoreCase(autor);
            if (resultados.isEmpty()) {
                model.addAttribute("error", "No se encontraron elementos para el autor: " + autor);
            } else {
                model.addAttribute("elementos", resultados);
                model.addAttribute("tituloFiltro", "Resultados para autor: " + autor);
            }
        } catch (Exception e) {
            Logger.logError("Error al buscar por autor", e);
            model.addAttribute("error", "Error al buscar: " + e.getMessage());
            model.addAttribute("elementos", new ArrayList<>());
            model.addAttribute("tituloFiltro", "Error en la búsqueda");
        }
        return "index";
    }

    @GetMapping("/buscarGenero")
    public String buscarPorGenero(@RequestParam String genero, Model model) {
        try {
            List<Libro> libros = elementoRepository.findLibrosByGeneroContainingIgnoreCase(genero);
            List<DVD> dvds = elementoRepository.findDVDsByGeneroContainingIgnoreCase(genero);
            List<ElementoBiblioteca> resultados = Stream.concat(libros.stream(), dvds.stream())
                    .collect(Collectors.toList());
            if (resultados.isEmpty()) {
                model.addAttribute("error", "No se encontraron elementos para el género: " + genero);
            } else {
                model.addAttribute("elementos", resultados);
                model.addAttribute("tituloFiltro", "Resultados para género: " + genero);
            }
        } catch (Exception e) {
            Logger.logError("Error al buscar por género", e);
            model.addAttribute("error", "Error al buscar: " + e.getMessage());
            model.addAttribute("elementos", new ArrayList<>());
            model.addAttribute("tituloFiltro", "Error en la búsqueda");
        }
        return "index";
    }

    @GetMapping("/buscarEditorial")
    public String buscarPorEditorial(@RequestParam String editorial, Model model) {
        try {
            List<Libro> libros = elementoRepository.findLibrosByEditorialContainingIgnoreCase(editorial);
            List<Revista> revistas = elementoRepository.findRevistasByEditorialContainingIgnoreCase(editorial);
            List<ElementoBiblioteca> resultados = Stream.concat(libros.stream(), revistas.stream())
                    .collect(Collectors.toList());
            if (resultados.isEmpty()) {
                model.addAttribute("error", "No se encontraron elementos para la editorial: " + editorial);
            } else {
                model.addAttribute("elementos", resultados);
                model.addAttribute("tituloFiltro", "Resultados para editorial: " + editorial);
            }
        } catch (Exception e) {
            Logger.logError("Error al buscar por editorial", e);
            model.addAttribute("error", "Error al buscar: " + e.getMessage());
            model.addAttribute("elementos", new ArrayList<>());
            model.addAttribute("tituloFiltro", "Error en la búsqueda");
        }
        return "index";
    }

    private ElementoBiblioteca crearElemento(String tipo, String titulo, String autor, int anoPublicacion,
                                             String isbn, Integer numeroPaginas, String generoLibro, String editorial,
                                             Integer numeroEdicion, String categoria, Integer duracion, String generoDVD)
            throws BibliotecaException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new BibliotecaException("El título no puede estar vacío");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new BibliotecaException("El autor no puede estar vacío");
        }
        if (anoPublicacion <= 0) {
            throw new BibliotecaException("El año de publicación debe ser mayor que 0");
        }

        switch (tipo) {
            case "Libro":
                if (numeroPaginas == null || numeroPaginas <= 0) {
                    throw new BibliotecaException("Número de páginas inválido");
                }
                return new Libro(titulo, autor, anoPublicacion, isbn, numeroPaginas, generoLibro, editorial);
            case "Revista":
                if (numeroEdicion == null || numeroEdicion <= 0) {
                    throw new BibliotecaException("Número de edición inválido");
                }
                return new Revista(titulo, autor, anoPublicacion, numeroEdicion, categoria, editorial, 0);
            case "DVD":
                if (duracion == null || duracion <= 0) {
                    throw new BibliotecaException("Duración inválida");
                }
                return new DVD(titulo, autor, anoPublicacion, duracion, generoDVD);
            default:
                throw new BibliotecaException("Tipo de elemento no válido: " + tipo);
        }
    }
}