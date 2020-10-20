package br.com.stoom.domain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.stoom.domain.model.Endereco;
import br.com.stoom.domain.repository.EnderecoRepository;
import br.com.stoom.domain.service.EnderecoService;

@Component
public class EnderecoImpl implements EnderecoService {
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	@Override
	public Endereco createOrUpdate(Endereco endereco) {
		return this.enderecoRepository.save(endereco);
	}

	@Override
	public List<Endereco> findAll() {
		return this.enderecoRepository.findAll();
	}

	@Override
	public void delete(Long id) {
		this.enderecoRepository.deleteById(id);
	}
	
	

}
