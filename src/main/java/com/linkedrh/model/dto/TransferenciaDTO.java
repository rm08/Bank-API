package com.linkedrh.model.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransferenciaDTO {
	private Long contaOrigem;
	private Long agenciaOrigem;
	private BigDecimal valor;
	private Long contaDestino;
	private Long agenciaDestino;
}


