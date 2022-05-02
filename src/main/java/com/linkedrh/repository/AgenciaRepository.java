package com.linkedrh.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.linkedrh.connection.jdbc.SingleConnection;
import com.linkedrh.model.Agencia;

@Repository
public class AgenciaRepository {

	private static final String FIND_BY_ID = "SELECT id, nome, endereco FROM agencia WHERE id = ?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	SingleConnection singleConnection;

	public Agencia findById(Long agenciaId) {
		PreparedStatement statement;
		Agencia agencia = null;
		try {
			statement = singleConnection.get().prepareStatement(FIND_BY_ID);
			statement.setLong(1, agenciaId);
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()){
				agencia = new Agencia();
				agencia.setId(resultado.getLong("id"));
				agencia.setNome(resultado.getString("nome"));
				agencia.setEndereco(resultado.getString("endereco"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return agencia;
	}
}
