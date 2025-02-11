package com.sena.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")

public class UsuarioController {
	
	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("/registro")
	public String createUsser() {
		
		return "usuario/registro";
	}
	
	
	@PostMapping("/save")
	public String save(Usuario usuario,Model model ) {
		LOGGER.info("usuario a registrar: {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		
		return"redirect:/";
	}
}
