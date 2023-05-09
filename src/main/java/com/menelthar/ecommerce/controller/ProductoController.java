package com.menelthar.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.menelthar.ecommerce.model.Producto;
import com.menelthar.ecommerce.model.Usuario;
import com.menelthar.ecommerce.service.ProductoService;
import com.menelthar.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private ProductoService productoService;

	@Autowired
	private UploadFileService uploadFileService;

	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}

	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}

	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el objeto producto {}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);
		// imagen
		if (producto.getId() == null) { // cuando se crea un producto
			String nombreImagen = uploadFileService.saveImage(file);
			producto.setImagen(nombreImagen);
		} else {

		}
		productoService.save(producto);
		return "redirect:/productos";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();

		LOGGER.info("Producto buscado: {}", producto);

		model.addAttribute("producto", producto);

		return "productos/edit";
	}

	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();
		
		if (file.isEmpty()) {// Cuando se edita el producto pero no se modifica la imagen
			producto.setImagen(p.getImagen());
		} else { // Cuando se edita la imagen.
			// eliminar para cuando no sea la imagen por defecto
			if (!p.getImagen().equals("defauil.jpg")) {
				uploadFileService.deleteImage(p.getImagen());
			}

			String nombreImagen = uploadFileService.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}

	@GetMapping("/delete/cart/{id}")
	public String delete(@PathVariable Integer id) {

		Producto p = new Producto();
		p = productoService.get(id).get();

		// eliminar para cuando no sea la imagen por defecto
		if (!p.getImagen().equals("defauil.jpg")) {
			uploadFileService.deleteImage(p.getImagen());
		}

		productoService.delete(id);
		return "redirect:/productos";
	}
}
