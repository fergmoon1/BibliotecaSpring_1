package edu.sena.bibliotecaspring_1.controller;

import edu.sena.bibliotecaspring_1.dao.ElementoBibliotecaRepository;
import edu.sena.bibliotecaspring_1.model.ElementoBiblioteca;
import edu.sena.bibliotecaspring_1.model.Libro;
import edu.sena.bibliotecaspring_1.model.Revista;
import edu.sena.bibliotecaspring_1.model.DVD;
import edu.sena.bibliotecaspring_1.util.BibliotecaException;
import edu.sena.bibliotecaspring_1.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

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
        } catch (Exception e) {
            Logger.logError("Error al listar elementos", e);
            model.addAttribute("error", "Error al listar elementos: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/filtrarPorTipo")
    public String filtrarPorTipo(@RequestParam(required = false) String tipo, Model model) {
        try {
            List<ElementoBiblioteca> elementos = elementoRepository.findAll();
            if (tipo != null && !tipo.isEmpty()) {
                elementos = elementos.stream()
                        .filter(e -> {
                            if ("Libro".equals(tipo)) return e instanceof Libro;
                            if ("Revista".equals(tipo)) return e instanceof Revista;
                            if ("DVD".equals(tipo)) return e instanceof DVD;
                            return false;
                        })
                        .toList();
                model.addAttribute("tituloFiltro", "Mostrando: " + tipo + "s");
            }
            model.addAttribute("elementos", elementos);
        } catch (Exception e) {
            Logger.logError("Error al filtrar por tipo: " + tipo, e);
            model.addAttribute("error", "Error al filtrar elementos: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/agregar")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("elemento", new Libro());
        model.addAttribute("tipos", new String[]{"Libro", "Revista", "DVD"});
        return "formulario";
    }

    @PostMapping("/agregar")
    public String agregarElemento(@ModelAttribute ElementoBiblioteca elemento, @RequestParam String tipo,
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
            ElementoBiblioteca nuevoElemento = crearElemento(tipo, elemento.getTitulo(), elemento.getAutor(),
                    elemento.getAnoPublicacion(), isbn, numeroPaginas, generoLibro, editorial,
                    numeroEdicion, categoria, duracion, generoDVD);
            elementoRepository.save(nuevoElemento);
            Logger.logInfo("Elemento agregado: " + elemento.getTitulo());
            redirectAttributes.addFlashAttribute("mensaje", "Elemento agregado con éxito");
        } catch (BibliotecaException e) {
            Logger.logError("Error al agregar elemento", e);
            redirectAttributes.addFlashAttribute("error", "Error al agregar elemento: " + e.getMessage());
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
        } catch (BibliotecaException e) {
            Logger.logError("Error al cargar elemento para edición ID: " + id, e);
            model.addAttribute("error", "Error al cargar elemento: " + e.getMessage());
            return "redirect:/biblioteca";
        }
        return "formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarElemento(@PathVariable int id, @ModelAttribute ElementoBiblioteca elemento,
                                     @RequestParam String tipo,
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
            elementoExistente.setTitulo(elemento.getTitulo());
            elementoExistente.setAutor(elemento.getAutor());
            elementoExistente.setAnoPublicacion(elemento.getAnoPublicacion());
            elementoExistente.setTipo(tipo);

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
            Logger.logInfo("Elemento actualizado ID: " + id);
            redirectAttributes.addFlashAttribute("mensaje", "Elemento actualizado con éxito");
        } catch (BibliotecaException e) {
            Logger.logError("Error al actualizar elemento ID: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar elemento: " + e.getMessage());
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

    @GetMapping("/buscar")
    public String buscarPorGenero(@RequestParam String genero, Model model) {
        try {
            String generoNormalizado = Normalizer.normalize(genero, Normalizer.Form.NFKD)
                    .replaceAll("\\p{M}", "")
                    .toLowerCase();
            String[] palabrasBusqueda = generoNormalizado.split("\\s+");
            List<ElementoBiblioteca> resultados = new ArrayList<>();

            for (ElementoBiblioteca elemento : elementoRepository.findAll()) {
                boolean coincide = false;
                if (elemento instanceof Libro) {
                    String libroGenero = ((Libro) elemento).getGenero();
                    if (libroGenero != null && !libroGenero.trim().isEmpty()) {
                        String libroGeneroNormalizado = Normalizer.normalize(libroGenero, Normalizer.Form.NFKD)
                                .replaceAll("\\p{M}", "")
                                .toLowerCase();
                        for (String palabra : palabrasBusqueda) {
                            if (!palabra.isEmpty() && libroGeneroNormalizado.contains(palabra)) {
                                coincide = true;
                                break;
                            }
                        }
                    }
                } else if (elemento instanceof DVD) {
                    String dvdGenero = ((DVD) elemento).getGenero();
                    if (dvdGenero != null && !dvdGenero.trim().isEmpty()) {
                        String dvdGeneroNormalizado = Normalizer.normalize(dvdGenero, Normalizer.Form.NFKD)
                                .replaceAll("\\p{M}", "")
                                .toLowerCase();
                        for (String palabra : palabrasBusqueda) {
                            if (!palabra.isEmpty() && dvdGeneroNormalizado.contains(palabra)) {
                                coincide = true;
                                break;
                            }
                        }
                    }
                } else if (elemento instanceof Revista) {
                    String revistaCategoria = ((Revista) elemento).getCategoria();
                    if (revistaCategoria != null && !revistaCategoria.trim().isEmpty()) {
                        String revistaCategoriaNormalizado = Normalizer.normalize(revistaCategoria, Normalizer.Form.NFKD)
                                .replaceAll("\\p{M}", "")
                                .toLowerCase();
                        for (String palabra : palabrasBusqueda) {
                            if (!palabra.isEmpty() && revistaCategoriaNormalizado.contains(palabra)) {
                                coincide = true;
                                break;
                            }
                        }
                    }
                }
                if (coincide) {
                    resultados.add(elemento);
                }
            }

            if (resultados.isEmpty()) {
                throw new BibliotecaException("No se encontraron elementos del género: " + genero);
            }
            model.addAttribute("elementos", resultados);
            model.addAttribute("tituloFiltro", "Resultados para género: " + genero);
        } catch (BibliotecaException e) {
            Logger.logError("Error al buscar por género", e);
            model.addAttribute("error", "Error al buscar: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/buscarAutor")
    public String buscarPorAutor(@RequestParam String autor, Model model) {
        try {
            String autorNormalizado = Normalizer.normalize(autor, Normalizer.Form.NFKD)
                    .replaceAll("\\p{M}", "")
                    .toLowerCase();
            String[] palabrasBusqueda = autorNormalizado.split("\\s+");
            List<ElementoBiblioteca> resultados = new ArrayList<>();

            for (ElementoBiblioteca elemento : elementoRepository.findAll()) {
                String elementoAutor = elemento.getAutor();
                if (elementoAutor != null && !elementoAutor.trim().isEmpty()) {
                    String elementoAutorNormalizado = Normalizer.normalize(elementoAutor, Normalizer.Form.NFKD)
                            .replaceAll("\\p{M}", "")
                            .toLowerCase();
                    boolean coincide = false;
                    for (String palabra : palabrasBusqueda) {
                        if (!palabra.isEmpty() && elementoAutorNormalizado.contains(palabra)) {
                            coincide = true;
                            break;
                        }
                    }
                    if (coincide) {
                        resultados.add(elemento);
                    }
                }
            }

            if (resultados.isEmpty()) {
                throw new BibliotecaException("No se encontraron elementos del autor: " + autor);
            }
            model.addAttribute("elementos", resultados);
            model.addAttribute("tituloFiltro", "Resultados para autor: " + autor);
        } catch (BibliotecaException e) {
            Logger.logError("Error al buscar por autor", e);
            model.addAttribute("error", "Error al buscar: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/buscarEditorial")
    public String buscarPorEditorial(@RequestParam String editorial, Model model) {
        try {
            String editorialNormalizado = Normalizer.normalize(editorial, Normalizer.Form.NFKD)
                    .replaceAll("\\p{M}", "")
                    .toLowerCase();
            String[] palabrasBusqueda = editorialNormalizado.split("\\s+");
            List<ElementoBiblioteca> resultados = new ArrayList<>();

            for (ElementoBiblioteca elemento : elementoRepository.findAll()) {
                if (elemento instanceof Libro) {
                    String libroEditorial = ((Libro) elemento).getEditorial();
                    if (libroEditorial != null && !libroEditorial.trim().isEmpty()) {
                        String libroEditorialNormalizado = Normalizer.normalize(libroEditorial, Normalizer.Form.NFKD)
                                .replaceAll("\\p{M}", "")
                                .toLowerCase();
                        boolean coincide = false;
                        for (String palabra : palabrasBusqueda) {
                            if (!palabra.isEmpty() && libroEditorialNormalizado.contains(palabra)) {
                                coincide = true;
                                break;
                            }
                        }
                        if (coincide) {
                            resultados.add(elemento);
                        }
                    }
                }
            }

            if (resultados.isEmpty()) {
                throw new BibliotecaException("No se encontraron elementos de la editorial: " + editorial);
            }
            model.addAttribute("elementos", resultados);
            model.addAttribute("tituloFiltro", "Resultados para editorial: " + editorial);
        } catch (BibliotecaException e) {
            Logger.logError("Error al buscar por editorial", e);
            model.addAttribute("error", "Error al buscar: " + e.getMessage());
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
                return new Revista(titulo, autor, anoPublicacion, numeroEdicion, categoria);
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