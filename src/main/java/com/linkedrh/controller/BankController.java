package com.linkedrh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linkedrh.model.ContaCorrente;
import com.linkedrh.model.Correntista;
import com.linkedrh.model.dto.ContaCorrenteDTO;
import com.linkedrh.model.dto.TransferenciaDTO;
import com.linkedrh.service.ContaCorrenteService;
import com.linkedrh.service.CorrentistaService;

@RestController
@RequestMapping("bank")
public class BankController {
	 
	 @Autowired
	 ContaCorrenteService contaCorrenteService;
	 
	 @Autowired
	 CorrentistaService correntistaService;
	 
	 @PostMapping
	 public ResponseEntity<?> contaCorrente(@RequestBody ContaCorrenteDTO contaCorrente) {		 
		try {
			return new ResponseEntity<>(contaCorrenteService.novaConta(contaCorrente), HttpStatus.CREATED);
		} catch (Exception e) {
			if (e.getMessage().equals("Agencia não encontrada")) return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);	
			return new ResponseEntity<>	(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	 }  

	 @GetMapping("consulta")
	 public ResponseEntity<?> contaCorrente(@RequestBody ContaCorrente contaCorrente) {		 
		try {
			return new ResponseEntity<>(contaCorrenteService.consultaSaldo(contaCorrente), HttpStatus.OK);
		} catch (Exception e) {
			if (e.getMessage().equals("Conta não encontrada")) return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);	
			return new ResponseEntity<>	(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	 } 
	 
	 @PatchMapping("deposito")
	 public ResponseEntity<?> addSaldo(@RequestBody ContaCorrente contaCorrente) {		 
		try {
			return new ResponseEntity<>(contaCorrenteService.adicionarSaldo(contaCorrente), HttpStatus.OK);
		} catch (Exception e) {
			switch (e.getMessage()) {
			case "Saldo não é superior a 0":
				return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);		
			case "Conta não encontrada":			
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			default:
				return new ResponseEntity<>	(e.getMessage(), HttpStatus.BAD_REQUEST);
			}				
		}
	 }  
	 
	 @PatchMapping("saque")
	 public ResponseEntity<?> rmvSaldo(@RequestBody ContaCorrente contaCorrente) {		 
		try {
			return new ResponseEntity<>(contaCorrenteService.removeSaldo(contaCorrente), HttpStatus.OK);
		} catch (Exception e) {
			switch (e.getMessage()) {
			case "Você não tem saldo + limite suficiente":
				return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);		
			case "Conta não encontrada":			
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			default:
				return new ResponseEntity<>	(e.getMessage(), HttpStatus.BAD_REQUEST);
			}				
		}
	 }  
	 
	 @DeleteMapping("desativar")
	 public ResponseEntity<?> desativarConta(@RequestBody ContaCorrente contaCorrente) {		 
		try {
			contaCorrenteService.desativarConta(contaCorrente);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			switch (e.getMessage()) {
			case "Conta não encontrada":
				return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);		
			case "Conta já inativa":			
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_MODIFIED);
			default:
				return new ResponseEntity<>	(e.getMessage(), HttpStatus.BAD_REQUEST);
			}				
		}
	 }
	 
	 @GetMapping("cliente")
	 public ResponseEntity<?> correntista(@RequestBody Correntista correntista) {		 
		try {
			return new ResponseEntity<>(correntistaService.consultaDados(correntista), HttpStatus.OK);
		} catch (Exception e) {
			if (e.getMessage().equals("Conta não encontrada")) return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);	
			return new ResponseEntity<>	(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	 } 
	 
	 @PatchMapping("transferir")
	 public ResponseEntity<?> transferir(@RequestBody TransferenciaDTO transferencia) {		 
		try {
			contaCorrenteService.transferir(transferencia);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			switch (e.getMessage()) {
			case "Valor menor ou igual a zero":
				return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);		
			case "Conta não existe":			
				return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
			default:
				return new ResponseEntity<>	(e.getMessage(), HttpStatus.BAD_REQUEST);
			}				
		}
	 }
}
