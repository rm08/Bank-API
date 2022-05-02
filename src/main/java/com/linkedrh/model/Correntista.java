package com.linkedrh.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Correntista {

	private Long id;
	private String nome;
	private String cpf;
	private Date nascimento;	
	private List<ContaCorrente> contas = new ArrayList<>();
	
	public Correntista findByCpf(String cpf) {
		return findByCpf(cpf);
	}
	
	public void addConta(ContaCorrente conta) {
		this.contas.add(conta);
	}
	
}
