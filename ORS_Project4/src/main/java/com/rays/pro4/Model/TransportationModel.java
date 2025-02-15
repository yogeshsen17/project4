package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rays.pro4.Bean.CartBean;
import com.rays.pro4.Bean.TransportationBean;
import com.rays.pro4.Util.JDBCDataSource;

public class TransportationModel {
	
	public Integer nextPK() throws Exception {

		int pk = 0;

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_transportation");

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			pk = rs.getInt(1);
		}

		rs.close();

		return pk + 1;
	}
	
	public long add(TransportationBean bean) throws Exception {

		int pk = 0;

		Connection conn = JDBCDataSource.getConnection();

		pk = nextPK();

		conn.setAutoCommit(false);

		PreparedStatement pstmt = conn.prepareStatement("insert into st_transportation values(?,?,?,?,?)");

		pstmt.setInt(1, pk);
		pstmt.setString(2, bean.getDescription());
		pstmt.setString(3, bean.getMode());
		pstmt.setDate(4, new java.sql.Date(bean.getDate().getTime()));
		pstmt.setInt(5, bean.getCost());

		int i = pstmt.executeUpdate();
		System.out.println("Transportation Add Successfully " + i);
		conn.commit();
		pstmt.close();

		return pk;
	}
	
	public void delete(TransportationBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();

		conn.setAutoCommit(false);

		PreparedStatement pstmt = conn.prepareStatement("delete from st_transportation where id = ?");

		pstmt.setLong(1, bean.getId());

		int i = pstmt.executeUpdate();
		System.out.println("Transportation delete successfully " + i);
		conn.commit();

		pstmt.close();
	}
	
	public void update(TransportationBean bean) throws Exception {

		Connection conn = JDBCDataSource.getConnection();

		conn.setAutoCommit(false); // Begin transaction

		PreparedStatement pstmt = conn.prepareStatement(
				"update st_transportation set description = ?, mode = ?, date = ?, cost = ? where id = ?");

		pstmt.setString(2, bean.getDescription());
		pstmt.setString(3, bean.getMode());
		pstmt.setDate(4, new java.sql.Date(bean.getDate().getTime()));
		pstmt.setInt(5, bean.getCost());
		pstmt.setLong(1, bean.getId());



		int i = pstmt.executeUpdate();

		System.out.println("Transportation update successfully " + i);

		conn.commit();
		pstmt.close();

	}
	
	public TransportationBean findByPK(long pk) throws Exception {

		String sql = "select * from st_transportation where id = ?";
		TransportationBean bean = null;

		Connection conn = JDBCDataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setLong(1, pk);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			bean = new TransportationBean();
			bean.setId(rs.getLong(1));
			bean.setDescription(rs.getString(2));
			bean.setMode(rs.getString(3));
			bean.setDate(rs.getDate(4));
			bean.setCost(rs.getInt(5));

		}

		rs.close();

		return bean;
	}
	
	public List search(TransportationBean bean, int pageNo, int pageSize) throws Exception {

		StringBuffer sql = new StringBuffer("select * from st_transportation where 1=1");
		if (bean != null) {

			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" AND customerName like '" + bean.getDescription() + "%'");
			}

			if (bean.getMode() != null && bean.getMode().length() > 0) {
				sql.append(" AND product like '" + bean.getMode() + "%'");
			}

			if (bean.getDate() != null && bean.getDate().getTime() > 0 ) {
				Date d = new Date(bean.getDate().getTime());
				sql.append(" AND Date = " +d );
			}
			if (bean.getId() > 0) {
				sql.append(" AND ID = " + bean.getId());
			}
			
			if (bean.getCost() > 0) {
				sql.append(" AND COST = " + bean.getCost());
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

			bean = new TransportationBean();
			bean.setId(rs.getLong(1));
			bean.setDescription(rs.getString(2));
			bean.setMode(rs.getString(3));
			bean.setDate(rs.getDate(4));
			bean.setCost(rs.getInt(5));

			list.add(bean);

		}
		rs.close();

		return list;

	}
	
	public List list() throws Exception {

		ArrayList list = new ArrayList();

		StringBuffer sql = new StringBuffer("select * from st_transportation");

		Connection conn = JDBCDataSource.getConnection();

		PreparedStatement pstmt = conn.prepareStatement(sql.toString());
		ResultSet rs = pstmt.executeQuery();
		TransportationBean bean = null;

		while (rs.next()) {

			bean = new TransportationBean();
			bean.setId(rs.getLong(1));
			bean.setDescription(rs.getString(2));
			bean.setMode(rs.getString(3));
			bean.setDate(rs.getDate(4));
			bean.setCost(rs.getInt(5));

			list.add(bean);

		}

		rs.close();

		return list;
	}







}
