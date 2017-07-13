package com.multilanguage.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.multilanguage.entities.User;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
@Controller
public class UserController {
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public ModelAndView user(Locale locale){
		return new ModelAndView("userform","user",new User());
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("user") User user, BindingResult result) {
		if(result.hasErrors()) {
			return "userform";
		}
		System.out.println("name " + user.getName());
		System.out.println("age " + user.getAge());
		return "success";
	}
	/*=== begin print pdf ===*/
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView inventoryMaterialReport(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView model = new ModelAndView();
		String jsonData = params.get("jsondata");

		if (jsonData == null) {
			
			List<User> lstFinal = new ArrayList<User>();
			User objuser = new User();
			lstFinal.add(objuser);
			// lstFinal empty: pdf blank
			JRDataSource jrDatasource = new JRBeanCollectionDataSource(lstFinal);
			model.addObject("datasource", jrDatasource);

			model.addObject("SUBREPORT_DIR", request.getServletContext().getRealPath("/WEB-INF/rptTemplate") + File.separator);
			model.addObject("urlImage", request.getServletContext().getRealPath("/WEB-INF/images/Logo.png"));
			
			model.addObject("Branch", "Hội Sở");
			model.addObject("Address", "Hội Sở");
			model.addObject("Date", "12/12/2012");
			model.addObject("Time", "15:15:15");
			model.addObject("SumSoLuong", 12.5);
			model.addObject("SumTotal", 45.5);
			
			model.addObject("MaLoHang", "LH456789");
			model.addObject("DienGiai", "Nhập hàng");
		}
		JasperReportsPdfView reportView = getJasperView("/WEB-INF/rptTemplate"+ File.separator + "LoHangThongKe.jrxml", request);
		model.setView(reportView);
		return model;
	}
	
	public static JasperReportsPdfView getJasperView(String jrxml, HttpServletRequest request) {
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		JasperReportsPdfView jasperView = new JasperReportsPdfView();
		jasperView.setUrl(jrxml);
		jasperView.setApplicationContext(ctx);
		return jasperView;
	}
	/*=== begin print pdf directly ===*/
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	private @ResponseBody int printPdfDirect(HttpServletRequest request) {
		// https://community.oracle.com/thread/4024595
		JasperReport jasperReport;
		JasperPrint jasperPrint;
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		List<User> lstFinal = new ArrayList<User>();
		User objuser = new User();
		lstFinal.add(objuser);
		// lstFinal empty: pdf blank
		JRDataSource jrDatasource = new JRBeanCollectionDataSource(lstFinal);
		
		jasperParameter.put("SUBREPORT_DIR", request.getServletContext().getRealPath("/WEB-INF/rptTemplate") + File.separator);
		jasperParameter.put("urlImage", request.getServletContext().getRealPath("/WEB-INF/images/Logo.png"));
		jasperParameter.put("DateF", "");
		jasperParameter.put("DateE", "");
		jasperParameter.put("SumSoLuong", 00.00);
		jasperParameter.put("SumTotal", 00.00);
		jasperParameter.put("Branch", "");
		jasperParameter.put("Address", "524 ton that tung");
		try {
			String rptFilePath = "/WEB-INF/rptTemplate"+ File.separator + "LoHangThongKe.jrxml";
			jasperReport = JasperCompileManager.compileReport(request.getServletContext().getRealPath(rptFilePath));
			jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameter, jrDatasource);
//			File dir = new File(request.getServletContext().getRealPath(/WEB-INF/rptTemplate));
//			File dirPDF = new File(request.getServletContext().getRealPath(/WEB-INF/rptTemplate));
//			if (!dir.exists())
//				dir.mkdirs();
//			if (!dirPDF.exists())
//				dirPDF.mkdirs();
//			JasperExportManager.exportReportToPdfFile(jasperPrint, request.getServletContext().getRealPath("/WEB-INF/rptTemplate") + File.separator + "Test");
			JasperPrintManager.printReport(jasperPrint, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 1;
	}
	
}