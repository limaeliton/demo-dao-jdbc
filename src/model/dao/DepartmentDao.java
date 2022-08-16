package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	// vai inserir no banco de dados esse objeto que for enviado como parâmetro de entrada.
	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	// operação responsável por consultar no banco um objeto com esse ID se existir vai retorna senão retorna NULL
	Department findById(Integer id);
	// pra retornar todos os departamentos.
	List<Department> findAll();

}
