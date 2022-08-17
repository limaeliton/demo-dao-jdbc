package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;
	
	// a classe já recebe a conecção
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?) " ,
					Statement.RETURN_GENERATED_KEYS);
									// O Todos os dados vem o OBJ
					st.setString(1, obj.getName());
					st.setString(2, obj.getEmail());
					st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
					st.setDouble(4, obj.getBaseSalary());
					st.setInt(5, obj.getDepartment().getId());

					int rowsAffected = st.executeUpdate();
					
					if(rowsAffected > 0 ) {
						ResultSet rs = st.getGeneratedKeys();
						
						if(rs.next()) {
							// no posição 1 pois vai ser a primeira coluna da chave getGeneratedKeys();
							int id = rs.getInt(1);
							// Atribuir o id gerado dentro obj
							obj.setId(id);
						}
						
						DB.closeResultSet(rs);
						
					}
					else {
						throw new DbException(" Unexpected error! No rows affected!");
					}
		}
		catch (SQLException error) {
			throw new DbException(error.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	
	@Override
	public void update(Seller obj) {
		
PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ? ");
									// O Todos os dados vem o OBJ
					st.setString(1, obj.getName());
					st.setString(2, obj.getEmail());
					st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
					st.setDouble(4, obj.getBaseSalary());
					st.setInt(5, obj.getDepartment().getId());
					st.setInt(6, obj.getId());

					 st.executeUpdate();			
					
		}
		catch (SQLException error) {
			throw new DbException(error.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement  st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
		
		
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;
				
			}
		
		return null;
	}
		
		catch(SQLException error) {
			throw new DbException(error.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}															// foi propagada a exceção
		private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
			Seller obj = new Seller();
			obj.setId(rs.getInt("Id"));
			obj.setName(rs.getString("Name"));
			obj.setEmail(rs.getString("Email"));
			obj.setBaseSalary(rs.getDouble("BaseSalary"));
			obj.setBirthDate(rs.getDate("BirthDate"));
			obj.setDepartment(dep);
			return obj;
	}

															// foi propagada a exceção
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		
		PreparedStatement  st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name ");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			// para percorrer o ResultSet rs em quanto tiver um próximo
			while(rs.next()) {
				
				// está buscando dentro do map se já existe algum departamento com o ID 2
				// se não existir o map.get vai retorna null
				Department dep = map.get(rs.getInt("DepartmentId"));
				// se o departamento existir o map.get vai pegar ele. o if vai dar false e vai reaproveitar o departamento que já existia.
				if(dep == null) {
					dep = instantiateDepartment(rs);
					// salvar o departamento dentro do map  para da próxima vez verificar que ele já existe.
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				// vai instância o seller apontando para o dep. seja ele existente ou um novo departamente instanciado no if.
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
				
			}
		
		return list;
	}
		
		catch(SQLException error) {
			throw new DbException(error.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement  st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name ");
		
		
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			// para percorrer o ResultSet rs em quanto tiver um próximo
			while(rs.next()) {
				
				// está buscando dentro do map se já existe algum departamento com o ID 2
				// se não existir o map.get vai retorna null
				Department dep = map.get(rs.getInt("DepartmentId"));
				// se o departamento existir o map.get vai pegar ele. o if vai dar false e vai reaproveitar o departamento que já existia.
				if(dep == null) {
					dep = instantiateDepartment(rs);
					// salvar o departamento dentro do map  para da próxima vez verificar que ele já existe.
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				// vai instância o seller apontando para o dep. seja ele existente ou um novo departamente instanciado no if.
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
				
			}
		
		return list;
	}
		
		catch(SQLException error) {
			throw new DbException(error.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	
	}

	
}
