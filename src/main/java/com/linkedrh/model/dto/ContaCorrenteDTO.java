package com.linkedrh.model.dto;

import java.math.BigDecimal;

import com.linkedrh.model.Correntista;

import lombok.Data;

@Data
public class ContaCorrenteDTO {
	private int agencia;
	private Correntista correntista;
	private BigDecimal limite;
	private BigDecimal saldoInicial;
}
