package com.rays.proj4.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rays.pro4.Bean.PrescriptionBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.PrescriptionModel;

public class PrescriptionTest {
public static PrescriptionModel model= new PrescriptionModel();
	
	
	public static void main(String[] args) throws Exception {
	   // testadd();
		//testDelete();
		
		//testFindByPk();
		//testUpdate();
		testsearch();
		//testlist();
		
	}
	
	
	
	
	public static void testadd() throws DuplicateRecordException {
		
		try {
			PrescriptionBean bean= new PrescriptionBean();
			SimpleDateFormat sdf= new SimpleDateFormat("MM-dd-yyyy");
			bean.setId(2);
			bean.setName("2Health");
			bean.setDecase("health");
			bean.setDate(sdf.parse("01-01-2005"));
			bean.setCapacity(45);
			
			
			long pk=model.add(bean);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void testDelete() throws Exception {
		try {
			PrescriptionBean bean = new PrescriptionBean();
			long pk=2L;
			bean.setId(2);
			model.delete(bean);
			System.out.println("Test Deleted");
			/*
			 * CourseBean deleteBean=model.findByPK(pk); if(deleteBean == null) {
			 * System.out.println("Test Delete fail"); }
			 */
		}catch(ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void testFindByPk() {
		PrescriptionBean bean=new PrescriptionBean();
		long pk=1L;
		try {
			bean=model.findByPK(pk);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bean==null) {
			System.out.println("test findbypk fail");
		}
		System.out.println(bean.getId());
		System.out.println(bean.getName());
		System.out.println(bean.getDecase());
		System.out.println(bean.getDate());
		System.out.println(bean.getCapacity());
	}
	
	public static void testUpdate() throws Exception {
		try {
			PrescriptionBean bean = model.findByPK(1L);
			bean.setName("unhealthy");
			//bean.setDescription("commerce");
			model.update(bean);
			System.out.println("update succ");
			/*
			 * CourseBean updateBean=model.testFindByPK(2L); if(!
			 * "ram".equals(updateBean.getName())){ System.out.println("test update fail");
			 * }
			 */
			 
		}catch(ApplicationException e) {
			e.printStackTrace();
		}catch(DuplicateRecordException e) {
			e.printStackTrace();
		}
	}
	public static void testsearch() throws Exception {
		try {
			PrescriptionBean bean = new PrescriptionBean();
			List list = new ArrayList();
			list=model.search(bean, 0, 0);
			
			Iterator it=list.iterator();
			while(it.hasNext()) {
				bean = (PrescriptionBean) it.next();
				System.out.println(bean.getName());
//				System.out.println(bean.getDecase());
				System.out.println(bean.getCapacity());
			}
		}catch(ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	
	  public static void testlist() throws Exception { try { 
		  PrescriptionBean bean = new PrescriptionBean();
		  List list = new ArrayList();
		  list =model.list();
	  if(list.size() < 0) { 
		  System.out.println("test list fail");
		  } 
	  Iterator it=list.iterator();
	  while(it.hasNext()) {
		  bean=(PrescriptionBean) it.next();
	  System.out.println(bean.getName());
	  System.out.println(bean.getDecase());
	  System.out.println(bean.getCapacity());
	  
	  }
	  
	  }catch(ApplicationException e) {
		  e.printStackTrace(); 
		  } 
	  }

}
