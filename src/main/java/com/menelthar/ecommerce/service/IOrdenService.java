package com.menelthar.ecommerce.service;

import java.util.List;

import com.menelthar.ecommerce.model.Orden;
import com.menelthar.ecommerce.model.Usuario;

public interface IOrdenService {

	List<Orden> findAll();
	Orden save(Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);
	
}
