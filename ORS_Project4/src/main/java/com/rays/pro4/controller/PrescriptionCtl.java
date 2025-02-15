package com.rays.pro4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.PrescriptionBean;
import com.rays.pro4.Model.PrescriptionModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "PrescriptionCtl", urlPatterns = { "/ctl/PrescriptionCtl" })
public class PrescriptionCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("pctl Validate");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("Name"))) {
			request.setAttribute("Name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Decase"))) {
			request.setAttribute("Decase", PropertyReader.getValue("error.require", "Decase"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Date"))) {
			request.setAttribute("Date", PropertyReader.getValue("error.require", "Date"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("Capacity"))) {
			request.setAttribute("Capacity", PropertyReader.getValue("error.require", "Capacity"));
			pass = false;
		}

		return pass;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		PrescriptionBean bean = new PrescriptionBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setName(DataUtility.getString(request.getParameter("Name")));

		bean.setDecase(DataUtility.getString(request.getParameter("Decase")));

		bean.setDate(DataUtility.getDate(request.getParameter("Date")));

		bean.setCapacity(DataUtility.getInt(request.getParameter("Capacity")));

		return bean;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = DataUtility.getString(request.getParameter("operation"));

		PrescriptionModel model = new PrescriptionModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println("prescription Edit Id >= " + id);

		if (id != 0 && id > 0) {

			System.out.println("in id > 0  condition " + id);
			PrescriptionBean bean = null;

			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		String op = DataUtility.getString(request.getParameter("operation"));

		long id = DataUtility.getLong(request.getParameter("id"));

		System.out.println(">>>><<<<>><<><<><<><>**********" + id + op);

		PrescriptionModel model = new PrescriptionModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			PrescriptionBean bean = (PrescriptionBean) populateBean(request);

			if (id > 0) {

				try {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("prescription is successfully Updated", request);
				} catch (Exception e) {
					System.out.println("prescription not update");
					e.printStackTrace();
				}

			} else {

				try {
					long pk = model.add(bean);
					//ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("prescription is successfully Added", request);
//					bean.setId(pk);
				} catch (Exception e) {
					System.out.println("prescription not added");
					e.printStackTrace();
				}

			}

		}else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.PRESCRIPTION_LIST_CTL, request, response);
			return;
}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {

		return ORSView.Prescription_VIEW;
	}

}