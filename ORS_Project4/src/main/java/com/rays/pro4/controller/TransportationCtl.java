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
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.TransportationModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "TransportationCtl", urlPatterns = { "/ctl/TransportationCtl" })
public class TransportationCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(TransportationCtl.class);

	@Override
	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "description"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("description"))) {
			request.setAttribute("description", "Description contains alphabet only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("mode"))) {
			request.setAttribute("mode", PropertyReader.getValue("error.require", "Mode"));
			pass = false;
		} 
		
		if (DataValidator.isNull(request.getParameter("date"))) {
			request.setAttribute("date", PropertyReader.getValue("error.require", "Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("date"))) {
			request.setAttribute("date", PropertyReader.getValue("error.date", "Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("cost"))) {
			request.setAttribute("cost", PropertyReader.getValue("error.require", "Cost"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("cost"))) {
			request.setAttribute("cost", PropertyReader.getValue("error.integer", "Cost"));
			pass = false;
		}

		return pass;
	}
	
	@Override
	protected void preload(HttpServletRequest request) {
		Map<Integer, String> map = new HashMap();
		map.put(1, "Airway");
		map.put(2, "Railway");
		map.put(3, "Road");
		request.setAttribute("cate", map);
	}

	protected BaseBean populateBean(HttpServletRequest request) {
		TransportationBean bean = new TransportationBean();

		bean.setId(DataUtility.getLong(request.getParameter("id"))); 
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		bean.setMode(DataUtility.getString(request.getParameter("mode"))); 
		bean.setDate(DataUtility.getDate(request.getParameter("date")));
		bean.setCost(DataUtility.getInt(request.getParameter("cost")));

		return bean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));
		TransportationModel model = new TransportationModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id != 0 && id > 0) {
			TransportationBean bean = null;
			try {
				bean = model.findByPK(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ServletUtility.setBean(bean, request);
		}

		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("TransportationCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		TransportationModel model = new TransportationModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			TransportationBean bean = (TransportationBean) populateBean(request);

			try {
				if (id > 0) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Transportation is successfully Updated", request);

				} else {
					System.out.println("1");
					long pk = model.add(bean);
					System.out.println("2");
					bean = model.findByPK(pk);
					ServletUtility.setBean(bean, request);
					System.out.println("3");
					ServletUtility.setSuccessMessage("Transportation is successfully Added", request);
					System.out.println("4");
				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			TransportationBean bean = (TransportationBean) populateBean(request);
			try {
				model.delete(bean);
				ServletUtility.redirect(ORSView.TRANSPORTATION_CTL, request, response);
				return;
			} catch (Exception e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.TRANSPORTATION_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("TransportationCtl Method doPostEnded");
	}

	@Override
	protected String getView() {
		return ORSView.TRANSPORTATION_VIEW;
	}
}
