package com.menelthar.ecommerce.service;

import java.util.Optional;

import com.menelthar.ecommerce.model.Usuario;

public interface IUsuarioService {
	
	Optional<Usuario> findById(Integer id);
	
}
