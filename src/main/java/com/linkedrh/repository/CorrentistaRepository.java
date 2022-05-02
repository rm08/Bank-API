package com.linkedrh.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.linkedrh.connection.jdbc.SingleConnection;
import com.linkedrh.model.ContaCorrente;
import com.linkedrh.model.Correntista;

@Repository
public class CorrentistaRepository {
	
	private static final String FIND_BY_CPF = "SELECT id, nome, cpf, nascimento FROM correntista WHERE cpf = ?";
	private static final String SEARCH_CLIENT = "SELECT C.ID, C.NOME, C.CPF, C.NASCIMENTO, CC.ID AS \"Numero da Conta\", CC.ID_AGENCIA "
			+ "FROM correntista AS C "
			+ "LEFT JOIN contacorrente AS CC ON C.ID = CC.ID_CORRENTISTA "
			+ "WHERE CPF = ?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	SingleConnection singleConnection;
	
	public CorrentistaRepository() {
	}
	
	public Correntista findByCpf(String correntistaCpf) {
		PreparedStatement statement;
		Correntista correntista = null;
		try {
			statement = singleConnection.get().prepareStatement(FIND_BY_CPF);
			statement.setString(1, correntistaCpf);
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()){
				correntista = new Correntista();
				correntista.setId(resultado.getLong("id"));
				correntista.setNome(resultado.getString("nome"));
				correntista.setCpf(resultado.getString("cpf"));
				correntista.setNascimento(resultado.getDate("nascimento"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return correntista;
	}
	
	public Correntista consultaDados(String cpf) {
		PreparedStatement statement;
		Correntista correntista = null;
		List<ContaCorrente> contas = new ArrayList<>();
		ContaCorrente conta = null;
		try {
			statement = singleConnection.get().prepareStatement(SEARCH_CLIENT);
			statement.setString(1, cpf);
			
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				if (correntista == null) {
					correntista = new Correntista();
					correntista.setId(result.getLong(1));
					correntista.setNome(result.getString(2));
					correntista.setCpf(result.getString(3));
					correntista.setNascimento(result.getDate(4));
				}
				conta = new ContaCorrente();
				conta.setId(result.getLong(5));
				conta.setAgenciaId(result.getLong(6));
				
				correntista.addConta(conta);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return correntista;
	}

}