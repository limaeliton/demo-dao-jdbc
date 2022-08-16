package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	// metodo que retorna o tipo de interface
	public static SellerDao createSellerDao( ) {
		// internamente vai inst�ncia uma implementa��o
		return new SellerDaoJDBC(DB.getConnection());
		
	}

}
