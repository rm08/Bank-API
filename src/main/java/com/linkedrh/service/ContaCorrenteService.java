package com.linkedrh.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkedrh.model.ContaCorrente;
import com.linkedrh.model.dto.ContaCorrenteDTO;
import com.linkedrh.model.dto.TransferenciaDTO;
import com.linkedrh.repository.ContaCorrenteRepository;

@Service
public class ContaCorrenteService {
	@Autowired
	ContaCorrenteRepository repository;

	@Autowired
	AgenciaService agenciaService;
	
	@Autowired
	CorrentistaService correntistaService;
	
	
	public ContaCorrente novaConta(ContaCorrenteDTO contaCorrente) throws Exception {
		if (contaCorrente.getAgencia() == 0) throw new Exception("Agencia não encontrada");
		
		var conta = new ContaCorrente();
		conta.setAgenciaId(Long.parseLong(String.valueOf(contaCorrente.getAgencia())));
		var agencia = agenciaService.findById(conta.getAgenciaId());
		if (agencia == null) {
			throw new Exception("Agencia não encontrada");
		}	
		conta.setAgencia(agencia);	
		conta.setCorrentista(contaCorrente.getCorrentista());
		conta.setLimite(contaCorrente.getLimite());
		conta.setSaldo(contaCorrente.getSaldoInicial());
		
		return novaConta(conta);
	}
	
	public ContaCorrente novaConta(ContaCorrente contaCorrente) throws Exception {
		if(!(contaCorrente.getSaldo().compareTo(BigDecimal.ZERO) > 0)) {
			throw new Exception("Saldo não é superior a 0");
		}
		return repository.salvar(contaCorrente);
	}
	
	public ContaCorrente consultaSaldo(ContaCorrente contaCorrente) throws Exception {		
		if (contaCorrente.getAgenciaId() == null) {
			throw new Exception ("Agencia não encontrada");
		}	
		
		var agencia = agenciaService.findById(contaCorrente.getAgenciaId());
		
		ContaCorrente conta = findByCorrentistaIdAndAgenciaId(contaCorrente.getCorrentistaId(), agencia.getId());
		
		if (conta == null) {
			throw new Exception ("Conta não encontrada");
		}	
		
		return conta;
	}
	
	private ContaCorrente findByCorrentistaIdAndAgenciaId(Long correntistaId, Long agenciaId) {
		return repository.findByCorrentistaIdAndAgenciaId(correntistaId, agenciaId);
	}

	public ContaCorrente adicionarSaldo(ContaCorrente contaCorrente) throws Exception {
		if (contaCorrente.getAgenciaId() == 0) throw new Exception("Conta não encontrada");
		
		Long agenciaId = Long.parseLong(String.valueOf(contaCorrente.getAgenciaId()));
		
		var agencia = agenciaService.findById(agenciaId);
		if (agencia == null) {
			throw new Exception("Conta não encontrada");
		}
		
		var conta = consultaSaldo(contaCorrente);
		
		if (conta == null) {
			throw new Exception("Conta não encontrada");
		}
		
		conta.addSaldo(contaCorrente.getSaldo());
		
		return novoSaldo(conta);
	}
	
	public ContaCorrente novoSaldo(ContaCorrente contaCorrente) throws Exception {
		if (contaCorrente.getSaldo().compareTo(BigDecimal.ZERO) < 0 && !contaCorrente.isSpecial()) {
			throw new Exception("Sua conta não é especial");
		}
		
		return repository.adicionarSaldo(contaCorrente);
	}
	
	public ContaCorrente removeSaldo(ContaCorrente contaCorrente) throws Exception {
		if (contaCorrente.getAgenciaId() == 0) throw new Exception("Conta não encontrada");
		
		Long agenciaId = Long.parseLong(String.valueOf(contaCorrente.getAgenciaId()));
		
		var agencia = agenciaService.findById(agenciaId);
		if (agencia == null) {
			throw new Exception("Conta não encontrada");
		}
		
		var conta = consultaSaldo(contaCorrente);
		
		if (conta == null) {
			throw new Exception("Conta não encontrada");
		}
		
		if(contaCorrente.getSaldo().compareTo(conta.getSaldo().add(conta.getLimite())) > 0) {
			throw new Exception("Você não tem saldo + limite suficiente");
		}
		
		conta.rmvSaldo(contaCorrente.getSaldo());
		
		return novoSaldo(conta);
	}
	
	public void desativarConta(ContaCorrente contaCorrente) throws Exception {		
		if (contaCorrente.getAgenciaId() == null) {
			throw new Exception ("Agencia não informada");
		}	
		
		ContaCorrente conta = findByCorrentistaIdAndAgenciaId(contaCorrente.getCorrentistaId(), contaCorrente.getAgenciaId());
		
		if (conta == null) {
			throw new Exception ("Conta não encontrada");
		}	
		
		if (!conta.isAtiva()) {
			throw new Exception ("Conta já inativa");
		}
		
		repository.desativarConta(conta);
	}

	public void transferir(TransferenciaDTO transferencia) throws Exception {
		if (transferencia.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new Exception ("Valor menor ou igual a zero");
		}	
		ContaCorrente contaOrigem = findByIdAndAgenciaId(transferencia.getContaOrigem(), transferencia.getAgenciaOrigem());
		if (contaOrigem == null) {
			throw new Exception ("Conta não existe");
		}
		ContaCorrente contaDestino = findByIdAndAgenciaId(transferencia.getContaDestino(), transferencia.getAgenciaDestino());
		if (contaDestino == null) {
			throw new Exception ("Conta não existe");
		}
		
		contaOrigem.setSaldo(transferencia.getValor());
		removeSaldo(contaOrigem);
		
		contaDestino.setSaldo(transferencia.getValor());
		adicionarSaldo(contaDestino);
	}

	private ContaCorrente findByIdAndAgenciaId(Long contaOrigem, Long agenciaOrigem) {
		return repository.findByIdAndAgenciaId(contaOrigem, agenciaOrigem);
	}
	
}
