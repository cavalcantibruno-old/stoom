package br.com.stoom.domain.service;

import java.util.List;

import br.com.stoom.domain.model.Endereco;

public interface EnderecoService {
	
	Endereco createOrUpdate(Endereco endereco);
	List<Endereco> findAll();
	void delete(Long id);
	
}
