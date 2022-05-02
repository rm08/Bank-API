package com.linkedrh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkedrh.model.Agencia;
import com.linkedrh.repository.AgenciaRepository;

@Service
public class AgenciaService {
	@Autowired
	AgenciaRepository repository;

	public Agencia findById(Long agenciaId) {
		return repository.findById(agenciaId);
	}
}
