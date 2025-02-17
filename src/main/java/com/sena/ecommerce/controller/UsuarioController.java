package com.sena.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IOrdenService;
import com.sena.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/usuario")

public class UsuarioController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;

	@GetMapping("/registro")
	public String createUsser() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario, Model model) {
		LOGGER.info("usuario a registrar: {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	//metodo para hacer un inicio de sesion
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		LOGGER.info("Accesos: {}", usuario);
		Optional<Usuario> userEmail = usuarioService.findByEmail(usuario.getEmail());
		LOGGER.info("usuario obtenido :{}", userEmail.get());
		if (userEmail.isPresent()) {
			session.setAttribute("idUsuario", userEmail.get().getId());
			if (userEmail.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			LOGGER.warn("usuario no existe en DB");
		}

		return "redirect:/";
	}
	
	//
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idUsuario");
		return "redirect:/";
	}
	
	//metodo para redirigir a la vista de compras del usuario
	@GetMapping("/compras")
	public String compras(HttpSession session,Model model) {
		
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		model.addAttribute("ordenes",ordenes);
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "usuario/compras";
	}
	//metodo compras
	@GetMapping("/detalle/{id}")
	public String detalleCompra(HttpSession session,Model model, @PathVariable Integer id) {
		//esta es la sesion de usuario o idusuario
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		LOGGER.info("Id de la orden {}", id);
		Optional<Orden> orden = ordenService.findById(id);
		model.addAttribute("detalles",orden.get().getDetalle());
		return "usuario/detallecompra";
	}
	
	
}
