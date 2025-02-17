package com.sena.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.sena.ecommerce.model.Usuario;

public interface IUsuarioService {

	public Usuario save(Usuario usuario);

	public Optional<Usuario> get(Integer id);

	public void update(Usuario usuario);

	public void delete(Integer id);

	public Optional<Usuario> findById(Integer id);

	public Optional<Usuario> findByEmail(String email);

	List<Usuario> findAll();
}
