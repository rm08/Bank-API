package com.linkedrh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkedrh.model.Correntista;
import com.linkedrh.repository.CorrentistaRepository;

@Service
public class CorrentistaService {
	@Autowired
	CorrentistaRepository repository;
	
	public Correntista findByCpf(String cpf) {
		return repository.consultaDados(cpf);
	}
	
	public Correntista consultaDados(Correntista correntista) throws Exception {		
		if (correntista.getCpf() == null) {
			throw new Exception ("CPF não informado");
		}	
		
		var cliente = findByCpf(correntista.getCpf());
		
		if (cliente == null) {
			throw new Exception ("CPF não cadastrado");
		}
			
		return cliente;
	}
	
	public Correntista consultaDados(String cpf) {		
		return repository.consultaDados(cpf);
	}
}
