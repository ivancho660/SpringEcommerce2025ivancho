package com.sena.ecommerce.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;
import com.sena.ecommerce.service.UploadFileService;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/productos")
public class ProductoController {

	// instancia de logger para ver datos de consola
	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	// microservicio imgs

	@Autowired
	private UploadFileService upload;

	// metodo para redirigir a la vista show en el template de productos
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}

	// metodo el que redirige a la vista de creacion de productos
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}

	// metodo de creacion de productos
	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("este es el objeto del producto a guardar en la DB {}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");

		producto.setUsuario(u);

		// validacion imagen de producto
		if (producto.getId() == null) {
			String nombreImagen = upload.saveImages(file, producto.getNombre());
			producto.setImagen(nombreImagen);
		}
		productoService.save(producto);
		return "redirect:/productos";
	}

	// metodo para llenar los imputs de la vista edit
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto p = new Producto();
		Optional<Producto> op = productoService.get(id);
		p = op.get();
		LOGGER.info("Busqueda de prodcuto por id {}", p);
		model.addAttribute("producto", p);
		return "productos/edit";
	}

	// metodo para actualizar los dato de un producto
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("este es el objeto a actualizar el DB {}", producto);
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();

		if (file.isEmpty()) {
			producto.setImagen(p.getImagen());
		} else {
			if (!p.getImagen().equals("default.jpg")) {
				upload.deletImage(p.getImagen());
			}
			String nombreImagen = upload.saveImages(file, p.getNombre());
			producto.setImagen(nombreImagen);
		}

		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}

	// metodo para eliminar
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		Producto p = new Producto();
		p = productoService.get(id).get();
		if (!p.getImagen().equals("defaul.jpg")) {
			upload.deletImage(p.getImagen());
		}
		productoService.delete(id);
		return "redirect:/productos";
	}
	@PostMapping("administrador/searchA")
	public String searchProducto(@RequestParam String nombre, Model model) {
		LOGGER.info("nombre del producto: {}", nombre);
		List<Producto> productos = productoService.findAll().stream()
				.filter(p -> p.getNombre().toUpperCase().contains(nombre.toUpperCase())).collect(Collectors.toList());
		model.addAttribute("productos",productos);
		return "productos/show";
	}
	
}
