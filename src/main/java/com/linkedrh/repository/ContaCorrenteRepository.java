package com.linkedrh.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.linkedrh.connection.jdbc.SingleConnection;
import com.linkedrh.model.ContaCorrente;

@Repository
public class ContaCorrenteRepository {
	
	private static final String NEXT = "SELECT nextval('contacorrente_id_seq')";
	private static final String CREATE_BANK_ACCOUNT = "INSERT INTO contacorrente (id, id_correntista, id_agencia, limite, saldo, ativa) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String FIND_ACCOUNT = "SELECT id, id_correntista, id_agencia, saldo, ativa FROM CONTACORRENTE WHERE id_correntista = ? AND id_agencia = ?";
	private static final String ADD_SALDO = "UPDATE contacorrente SET saldo=? WHERE id = ?";
	private static final String DISABLE = "UPDATE contacorrente SET ativa = 'F' WHERE id = ?";
	private static final String FIND = "SELECT id, id_correntista, id_agencia, saldo, limite, ativa FROM CONTACORRENTE WHERE id = ? AND id_agencia = ?";
	
	@Autowired
	SingleConnection singleConnection;

	public ContaCorrente salvar(ContaCorrente contaCorrente) {
		PreparedStatement statement;
		try {
			ResultSet result = singleConnection.get().prepareStatement(NEXT).executeQuery();
			result.next();
			contaCorrente.setId(result.getLong(1));
			statement = singleConnection.get().prepareStatement(CREATE_BANK_ACCOUNT);
			statement.setLong(1, contaCorrente.getId());
			statement.setLong(2, contaCorrente.getCorrentistaId());
			statement.setLong(3, contaCorrente.getAgenciaId());
			statement.setBigDecimal(4, contaCorrente.getLimite());
			statement.setBigDecimal(5, contaCorrente.getSaldo());
			statement.setString(6, contaCorrente.isAtiva() ? "T" : "F");
			
			statement.executeUpdate();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return contaCorrente;
	}
	
	
	
	public ContaCorrente adicionarSaldo(ContaCorrente contaCorrente) {
		PreparedStatement statement;
		try {
			statement = singleConnection.get().prepareStatement(ADD_SALDO);
			statement.setBigDecimal(1, contaCorrente.getSaldo());
			statement.setLong(2, contaCorrente.getId());
			
			statement.executeUpdate();
				
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return contaCorrente;
	}

	public ContaCorrente findByCorrentistaIdAndAgenciaId(Long correntistaId, Long agenciaId) {
		PreparedStatement statement;
		ContaCorrente contaCorrente = null;
		try {
			statement = singleConnection.get().prepareStatement(FIND_ACCOUNT);
			statement.setLong(1, correntistaId);
			statement.setLong(2, agenciaId);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				contaCorrente = new ContaCorrente();
				contaCorrente.setId(result.getLong(1));
				contaCorrente.setCorrentistaId(result.getLong(2));
				contaCorrente.setAgenciaId(result.getLong(3));
				contaCorrente.setSaldo(result.getBigDecimal(4));
				contaCorrente.setAtiva("T".equals(result.getString(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contaCorrente;
	}
	
	public ContaCorrente realizarSaque(ContaCorrente contaCorrente) {
		PreparedStatement statement;
		try {
			ResultSet result = singleConnection.get().prepareStatement(NEXT).executeQuery();
			result.next();
			contaCorrente.setId(result.getLong(1));
			statement = singleConnection.get().prepareStatement(ADD_SALDO);
			statement.setBigDecimal(1, contaCorrente.getSaldo());
			statement.setLong(2, contaCorrente.getId());
			
			statement.executeUpdate();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return contaCorrente;
	}
	
	public ContaCorrente desativarConta(ContaCorrente contaCorrente) {
		PreparedStatement statement;
		try {
			statement = singleConnection.get().prepareStatement(DISABLE);
			statement.setLong(1, contaCorrente.getId());
			
			statement.executeUpdate();
				
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return contaCorrente;
	}
	
	public ContaCorrente findByIdAndAgenciaId(Long id, Long agenciaId) {
		PreparedStatement statement;
		ContaCorrente contaCorrente = null;
		try {
			statement = singleConnection.get().prepareStatement(FIND);
			statement.setLong(1, id);
			statement.setLong(2, agenciaId);
			
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				contaCorrente = new ContaCorrente();
				contaCorrente.setId(result.getLong(1));
				contaCorrente.setCorrentistaId(result.getLong(2));
				contaCorrente.setAgenciaId(result.getLong(3));
				contaCorrente.setSaldo(result.getBigDecimal(4));
				contaCorrente.setLimite(result.getBigDecimal(5));
				contaCorrente.setAtiva("T".equals(result.getString(6)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contaCorrente;
	}
	
}
	

