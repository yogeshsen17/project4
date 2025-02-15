package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.PrescriptionBean;
import com.rays.pro4.Util.JDBCDataSource;

public class PrescriptionModel {
	
	public Integer nextPK() throws Exception {

		int pk = 0;

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_prescription");

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			pk = rs.getInt(1);
		}

		rs.close();

		return pk + 1;
	}

	public long add(PrescriptionBean bean) throws Exception {

		int pk = 0;

		Connection conn = JDBCDataSource.getConnection();

		pk = nextPK();

		conn.setAutoCommit(false);

		PreparedStatement pstmt = conn.prepareStatement("insert into st_prescription values(?,?,?,?,?)");

		pstmt.setInt(1, pk);
		pstmt.setString(2, bean.getName());
		pstmt.setString(3, bean.getDecase());
		pstmt.setDate(4, new Date(bean.getDate().getTime()));
		pstmt.setInt(5, bean.getCapacity());

		int i = pstmt.executeUpdate();
		System.out.println("prescription Add Successfully " + i);
		conn.commit();
		pstmt.close();

		return pk;
	}

	public void delete(PrescriptionBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();

		conn.setAutoCommit(false);

		PreparedStatement pstmt = conn.prepareStatement("delete from st_prescription where id = ?");

		pstmt.setLong(1, bean.getId());

		int i = pstmt.executeUpdate();
		System.out.println("prescription delete successfully " + i);
		conn.commit();

		pstmt.close();
	}

	public void update(PrescriptionBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();

		conn.setAutoCommit(false); // Begin transaction

		PreparedStatement pstmt = conn.prepareStatement(
				"update st_prescription set Name = ?, Decase = ?, Date = ?, Capacity = ? where id = ?");

		pstmt.setString(1, bean.getName());
		pstmt.setString(2, bean.getDecase());
		pstmt.setDate(3, new java.sql.Date(bean.getDate().getTime()));
		pstmt.setInt(4, bean.getCapacity());
		pstmt.setLong(5, bean.getId());


		int i = pstmt.executeUpdate();

		System.out.println("prescription update successfully " + i);

		conn.commit();
		pstmt.close();

	}

	public PrescriptionBean findByPK(long pk) throws Exception {

		String sql = "select * from st_prescription where id = ?";
		PrescriptionBean bean = null;

		Connection conn = JDBCDataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setLong(1, pk);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			bean = new PrescriptionBean();
			bean.setId(rs.getLong(1));
			bean.setName(rs.getString(2));
			bean.setDecase(rs.getString(3));
			bean.setDate(rs.getDate(4));
			bean.setCapacity(rs.getInt(5));

		}

		rs.close();

		return bean;
	}

	public List search(PrescriptionBean bean, int pageNo, int pageSize) throws Exception {

		StringBuffer sql = new StringBuffer("select * from st_prescription where 1=1");
		System.out.println("1");
		if (bean != null) {
			
			System.out.println("2");

			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
				System.out.println("3");

			}

//			if (bean.getDecase() != null && bean.getDecase().length() > 0) {
//				sql.append(" AND prescriptionAmmount like '" + bean.getDecase() + "%'");
//			}

			

			if (bean.getDate() != null && bean.getDate().getTime() > 0) {
				Date d = new Date(bean.getDate().getTime());
				sql.append(" and date = '" + d + "'");
				System.out.println("done");
			}
//			if (bean.getDecase().length() > 0) {
//				sql.append(" AND prescriptionAmmount like '" + bean.getDecase() + "%'");
//			}
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}

		}

		if (pageSize > 0) {

			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);

		}

		System.out.println("sql query search >>= " + sql.toString());
		List list = new ArrayList();

		Connection conn = JDBCDataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql.toString());

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			bean = new PrescriptionBean();
			bean.setId(rs.getLong(1));
			bean.setName(rs.getString(2));
			bean.setDecase(rs.getString(3));
			bean.setDate(rs.getDate(4));
			bean.setCapacity(rs.getInt(5));


			list.add(bean);

		}
		rs.close();

		return list;

	}

	public List list() throws Exception {

		ArrayList list = new ArrayList();

		StringBuffer sql = new StringBuffer("select * from st_prescription");

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement(sql.toString());
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			PrescriptionBean bean = new PrescriptionBean();

			bean.setId(rs.getLong(1));
			bean.setName(rs.getString(2));
			bean.setDecase(rs.getString(3));
			bean.setDate(rs.getDate(4));
			bean.setCapacity(rs.getInt(5));

			list.add(bean);

		}

		rs.close();

		return list;
	}


}
