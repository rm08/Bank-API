package com.linkedrh.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ContaCorrente {
	private Long id;
	private Long correntistaId;
	private Correntista correntista;
	private Long agenciaId;
	private Agencia agencia;
	private BigDecimal limite = BigDecimal.ZERO;
	private BigDecimal saldo = BigDecimal.ZERO;
	private boolean ativa = true;
	
	public void setCorrentista(Correntista correntista) {
		this.correntista = correntista;
		this.correntistaId = correntista == null ? null : correntista.getId();
	}
	
	public void addSaldo(BigDecimal value) {
		this.saldo = this.saldo.add(value);
	}
	
	public void rmvSaldo(BigDecimal value) {
		this.saldo = this.saldo.subtract(value);
	}
	
	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
		this.agenciaId = agencia == null ? null : agencia.getId();
	}
	
	public void setSaldo(BigDecimal saldo) throws Exception {
		if (saldo == null) saldo = BigDecimal.ZERO; 
		if (saldo.compareTo(BigDecimal.ZERO) < 0 && !isSpecial()) throw new Exception("Seu saldo não é especial");
		this.saldo = saldo;
	}
	
	public void setLimite(BigDecimal limite) throws Exception {
		if (limite == null) limite = BigDecimal.ZERO; 
		this.limite = limite;
	}
	
	public boolean isSpecial() {
		if (limite == null) limite = BigDecimal.ZERO; 
		return limite.compareTo(BigDecimal.ZERO) > 0;
	}
	
}
