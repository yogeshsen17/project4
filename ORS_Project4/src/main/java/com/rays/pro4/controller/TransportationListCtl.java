package com.rays.pro4.controller;

import java.io.IOException; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.TransportationBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Model.TransportationModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "TransportationListCtl", urlPatterns = { "/ctl/TransportationListCtl" })
public class TransportationListCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TransportationListCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {

		TransportationModel tmodel = new TransportationModel();

		try {
			List tlist = tmodel.list();
			request.setAttribute("tlist", tlist);
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		TransportationModel model = new TransportationModel();
		Map<Integer, String> map = new HashMap<Integer, String>();

		map.put(1, "Airway");
		map.put(2, "Railway");
		map.put(3, "Road");
		
		String val = map.get(1);
		
		System.out.println(val+"=================================");

		request.setAttribute("cate", map);

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		TransportationBean bean = new TransportationBean();

		bean.setId(DataUtility.getLong(request.getParameter("id"))); 
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		bean.setMode(DataUtility.getString(request.getParameter("mode"))); 
		bean.setDate(DataUtility.getDate(request.getParameter("date")));
		bean.setCost(DataUtility.getInt(request.getParameter("cost")));
		bean.setId(DataUtility.getLong(request.getParameter("tlist")));


		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TransportationListCtl doGet Start");
		List list = null;
		List nextList = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		TransportationBean bean = (TransportationBean) populateBean(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		TransportationModel model = new TransportationModel();

		try {
			list = model.search(bean, pageNo, pageSize);
			System.out.println("list" + list);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (Exception e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("TransportationListCtl doGet End");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TransportationListCtl doPost Start");

		List list;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));
		TransportationBean bean = (TransportationBean) populateBean(request);
		String[] ids = request.getParameterValues("ids");

		TransportationModel model = new TransportationModel();

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
			pageNo--;
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TRANSPORTATION_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TRANSPORTATION_LIST_CTL, request, response);
			return;
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;
			if (ids != null && ids.length > 0) {
				TransportationBean deletebean = new TransportationBean();
				for (String id : ids) {
					deletebean.setId(DataUtility.getInt(id));
					try {
						model.delete(deletebean);
					} catch (Exception e) {
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					}

					ServletUtility.setSuccessMessage("Transportation is Deleted Successfully", request);
				}
			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		try {
			list = model.search(bean, pageNo, pageSize);
			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

		} catch (Exception e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
			ServletUtility.setErrorMessage("No record found ", request);
		}
		ServletUtility.setList(list, request);
		ServletUtility.setBean(bean, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);
		log.debug("TransportationListCtl doGet End");

	}

	@Override
	protected String getView() {
		return ORSView.TRANSPORTATION_LIST_VIEW;
	}

}
