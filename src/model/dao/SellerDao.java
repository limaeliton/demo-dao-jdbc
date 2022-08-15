package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	
	// vai inserir no banco de dados esse objeto que for enviado como parâmetro de entrada.
		void insert(Seller obj);
		void update(Seller obj);
		void deleteById(Integer id);
		// operação responsável por consultar no banco um objeto com esse ID se existir vai retorna senão retorna NULL
		Seller findById(Integer id);
		// pra retornar todos os departamentos.
		List<Seller> findAll();


}
