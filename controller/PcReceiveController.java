package kr.totalcall.controller.pc;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import kr.totalcall.controller.model.ReceiveModel;
import kr.totalcall.controller.model.ReceiveUpdateModel;
import kr.totalcall.service.BranchService;
import kr.totalcall.service.CustomerAdminService;
import kr.totalcall.service.CustomerGroupService;
import kr.totalcall.service.CustomerMasterService;
import kr.totalcall.service.CustomerService;
import kr.totalcall.service.ReceiveService;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/pc/receive")
public class PcReceiveController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ReceiveService receiveService;
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerGroupService customerGroupService;
	
	@Autowired
	private CustomerAdminService customerAdminService;
	
	@Autowired
	private CustomerMasterService customerMasterService;
	
	@RequestMapping(value = "setReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setReceive(@RequestBody @Valid ReceiveModel receiveModel,
			BindingResult bindingResult) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// logger.info("parameter {}", receiveModel);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			int codeIndex = 2000;
			for (ObjectError error : bindingResult.getAllErrors()) {
				resultMap.put("resultCode", ++codeIndex);
				resultMap.put("resultMessage", error.getDefaultMessage());
			}
		} else {
			resultMap.putAll(receiveService.setReceive(receiveModel));
		}
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "setReceiveExcel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setReceiveExcel(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		// logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		int branchNum = (Integer) paramMap.get("branchNum");
		int clientNum = (Integer) paramMap.get("clientNum");
		String shuttle = (String) paramMap.get("shuttle");
		String quick = (String) paramMap.get("quick");
		String auto = (String) paramMap.get("auto");
		String weight = (String) paramMap.get("weight");
		String payment = (String) paramMap.get("payment");
		String invoiceNum = (String) paramMap.get("invoiceNum");
		String orderNum = (String) paramMap.get("orderNum");
		String item = (String) paramMap.get("item");
		String comment = (String) paramMap.get("comment");
		String startCname = (String) paramMap.get("startCname");
		String startDepartment = (String) paramMap.get("startDepartment");
		String startPerson = (String) paramMap.get("startPerson");
		String startTel = (String) paramMap.get("startTel");
		String startPhone = (String) paramMap.get("startPhone");
		String startSido = (String) paramMap.get("startSido");
		String startGugun = (String) paramMap.get("startGugun");
		String startDong = (String) paramMap.get("startDong");
		String startDongNum = (String) paramMap.get("startDongNum");
		String startDetail = (String) paramMap.get("startDetail");
		String endCname = (String) paramMap.get("endCname");
		String endDepartment = (String) paramMap.get("endDepartment");
		String endPerson = (String) paramMap.get("endPerson");
		String endTel = (String) paramMap.get("endTel");
		String endPhone = (String) paramMap.get("endPhone");
		String endSido = (String) paramMap.get("endSido");
		String endGugun = (String) paramMap.get("endGugun");
		String endDong = (String) paramMap.get("endDong");
		String endDongNum = (String) paramMap.get("endDongNum");
		String endDetail = (String) paramMap.get("endDetail");
		
		Map<String, Object> resultMap = receiveService.setReceiveExcel(branchNum, clientNum, shuttle, quick, auto,
				weight, payment, invoiceNum, orderNum, item, comment, startCname, startDepartment, startPerson,
				startTel, startPhone, startSido, startGugun, startDong, startDongNum, startDetail, endCname,
				endDepartment, endPerson, endTel, endPhone, endSido, endGugun, endDong, endDongNum, endDetail);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getReceive(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		
		String callGroupBranchNums = (String) paramMap.get("callGroupBranchNums");
		int branchNum = (Integer) paramMap.get("branchNum");
		String sDate = (String) paramMap.get("sDate");
		String eDate = (String) paramMap.get("eDate");
		boolean chkStatusAll = (Boolean) paramMap.get("chkStatusAll");
		boolean chkStatusReceive = (Boolean) paramMap.get("chkStatusReceive");
		boolean chkStatusIndividual = (Boolean) paramMap.get("chkStatusIndividual");
		boolean chkStatusWait = (Boolean) paramMap.get("chkStatusWait");
		boolean chkStatusBecha = (Boolean) paramMap.get("chkStatusBecha");
		boolean chkStatusPickup = (Boolean) paramMap.get("chkStatusPickup");
		boolean chkStatusEnd = (Boolean) paramMap.get("chkStatusEnd");
		boolean chkStatusCancel = (Boolean) paramMap.get("chkStatusCancel");
		boolean chkStatusReserve = (Boolean) paramMap.get("chkStatusReserve");
		boolean chkStatusInternet = (Boolean) paramMap.get("chkStatusInternet");
		boolean chkStatusCallpass = (Boolean) paramMap.get("chkStatusCallpass");
		boolean callGroupAll = (Boolean) paramMap.get("callGroupAll");
		boolean chkVehicleTotal = (Boolean) paramMap.get("chkVehicleTotal");
		boolean chkAuto = (Boolean) paramMap.get("chkAuto");
		boolean chkDamas = (Boolean) paramMap.get("chkDamas");
		boolean chkLabo = (Boolean) paramMap.get("chkLabo");
		boolean chkVan = (Boolean) paramMap.get("chkVan");
		boolean chkTruck = (Boolean) paramMap.get("chkTruck");
		String searchWord = (String) paramMap.get("searchWord");
		String searchOption = (String) paramMap.get("searchOption");
		
		List<Map<String, Object>> receiveMapList = receiveService.getReceive(callGroupBranchNums, branchNum, sDate,
				eDate, chkStatusAll, chkStatusReceive, chkStatusIndividual, chkStatusWait, chkStatusBecha,
				chkStatusPickup, chkStatusEnd, chkStatusCancel, chkStatusReserve, chkStatusInternet, chkStatusCallpass,
				callGroupAll, chkVehicleTotal, chkAuto, chkDamas, chkLabo, chkVan, chkTruck, searchWord, searchOption);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				receiveMapList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getReceiveByNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getReceiveByNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		
		Map<String, Object> resultMap = receiveService.getReceiveByNum(receiveNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getReceiveByCallInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getReceiveByCallInfo(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String callDatetime = (String) paramMap.get("callDatetime");
		String callExt = (String) paramMap.get("callExt");
		String callClipNum = (String) paramMap.get("callClipNum");
		
		Map<String, Object> resultMap = receiveService.getReceiveByCallInfo(callDatetime, callExt, callClipNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getReceiveCustomerDriverByNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getReceiveCustomerDriverByNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		
		Map<String, Object> resultMap = receiveService.getReceiveCustomerDriverByNum(receiveNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getCustomerReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getCustomerReceive(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int loginType = (Integer) paramMap.get("loginType");
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		int customerNum = (Integer) paramMap.get("customerNum");
		int customerGroupNum = (Integer) paramMap.get("customerGroupNum");
		int customerAdminNum = (Integer) paramMap.get("customerAdminNum");
		int customerMasterNum = (Integer) paramMap.get("customerMasterNum");
		int branchNum = (Integer) paramMap.get("branchNum");
		String payType = (String) paramMap.get("payType");
		String searchWord = (String) paramMap.get("searchWord");
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 삼성전자판매의 경우 외상건만 보여줌 ( 고객 뷰에서만 )
		String reportType = "";
		if (loginType == -1) {
			if (customerNum > 0) {
				resultList = receiveService.getCustomerReceive(loginType, startDate, endDate, reportType, customerNum,
						branchNum, payType, searchWord);
			} else if (customerGroupNum > 0) {
				resultList = receiveService.getCustomerGroupReceive(loginType, startDate, endDate, reportType,
						customerGroupNum, branchNum, payType, searchWord);
			} else if (customerAdminNum > 0) {
				resultList = receiveService.getCustomerAdminReceive(loginType, startDate, endDate, reportType,
						customerAdminNum, branchNum, payType, searchWord);
			} else if (customerMasterNum > 0) {
				resultList = receiveService.getCustomerMasterReceive(loginType, startDate, endDate, reportType,
						customerMasterNum, branchNum, payType, searchWord);
			}
		} else if (loginType == 0) {
			reportType = customerService.getReportType(customerNum);
			resultList = receiveService.getCustomerReceive(loginType, startDate, endDate, reportType, customerNum,
					branchNum, payType, searchWord);
		} else if (loginType == 1) {
			reportType = customerGroupService.getReportType(customerGroupNum);
			resultList = receiveService.getCustomerGroupReceive(loginType, startDate, endDate, reportType,
					customerGroupNum, branchNum, payType, searchWord);
		} else if (loginType == 2) {
			reportType = customerAdminService.getReportType(customerAdminNum);
			resultList = receiveService.getCustomerAdminReceive(loginType, startDate, endDate, reportType,
					customerAdminNum, branchNum, payType, searchWord);
		} else if (loginType == 3) {
			reportType = customerMasterService.getReportType(customerMasterNum);
			resultList = receiveService.getCustomerMasterReceive(loginType, startDate, endDate, reportType,
					customerMasterNum, branchNum, payType, searchWord);
		}
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getReceiveByDriverNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getReceiveByDriverNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int driverNum = (Integer) paramMap.get("driverNum");
		
		List<Map<String, Object>> receiveMapList = receiveService.getReceiveByDriverNum(driverNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				receiveMapList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "searchCustomerReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> searchCustomerReceive(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		int branchNum = (Integer) paramMap.get("branchNum");
		String payType = (String) paramMap.get("payType");
		String searchWord = (String) paramMap.get("searchWord");
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = receiveService.searchCustomerReceive(startDate, endDate, branchNum, payType, searchWord);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getNonMemberReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getNonMemberReceive(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		int branchNum = (Integer) paramMap.get("branchNum");
		String telNum = (String) paramMap.get("telNum");
		String pass = (String) paramMap.get("pass");
		String searchWord = (String) paramMap.get("searchWord");
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = receiveService.getNonMemberReceive(startDate, endDate, branchNum, telNum, pass, searchWord);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getCustomerReceiveExcel", method = RequestMethod.GET)
	public void getCustomerReceiveExcel(int loginType, String startDate, String endDate, Integer customerNum,
			Integer customerGroupNum, Integer customerAdminNum, Integer customerMasterNum, int branchNum,
			String payType, String searchWord, String field, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("loginType = " + loginType);
		logger.info("startDate = " + startDate);
		logger.info("endDate = " + endDate);
		logger.info("customerNum = " + customerNum);
		logger.info("customerGroupNum = " + customerGroupNum);
		logger.info("customerAdminNum = " + customerAdminNum);
		logger.info("customerMasterNum = " + customerMasterNum);
		logger.info("branchNum = " + branchNum);
		logger.info("field = " + field);
		logger.info("payType = " + payType);
		searchWord = URLDecoder.decode(searchWord, "UTF-8");
		logger.info("searchWord = " + searchWord);
		
		if (customerNum == null) {
			customerNum = 0;
		}
		if (customerGroupNum == null) {
			customerGroupNum = 0;
		}
		if (customerAdminNum == null) {
			customerAdminNum = 0;
		}
		if (customerMasterNum == null) {
			customerMasterNum = 0;
		}
		
		// 각 고객사별 엑셀 컬럼이 달리질 수 있음
		// 삼성전자판매의 경우 외상건만 보여줌 ( 고객 뷰에서만 )
		String reportType = "";
		
		if (loginType == 0) {
			reportType = customerService.getReportType(customerNum);
		} else if (loginType == 1) {
			reportType = customerGroupService.getReportType(customerGroupNum);
		} else if (loginType == 2) {
			reportType = customerAdminService.getReportType(customerAdminNum);
		} else if (loginType == 3) {
			reportType = customerMasterService.getReportType(customerMasterNum);
		}
		
		Workbook wb = receiveService.getCustomerReceiveExcel(loginType, startDate, endDate, reportType, customerNum,
				customerGroupNum, customerAdminNum, customerMasterNum, branchNum, payType, searchWord, field);
		
		// file name 설정
		Map<String, Object> branchMap = branchService.getBranchByBranchNum(branchNum);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String fileName = branchMap.get("name") + "_접수내역_" + startDate + "~" + endDate + "(생성-" + sdf.format(date)
				+ ")" + ".xls";
		
		// 브라우저 별 http header 설정
		String userAgent = request.getHeader("User-Agent");
		logger.info("User-Agent = " + userAgent);
		
		response.setContentType("application/vnd.ms-excel; charset=UTF-8");
		if (userAgent != null && userAgent.indexOf("MSIE 5.5") > -1) {
			// MS IE 5.5 이하
			response.setHeader("Content-Disposition",
					"filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
			response.setHeader("Cache-control", "public,max-age=68400");
		} else if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
			// MS IE (보통은 6.x 이상 가정)
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
			response.setHeader("Cache-control", "public,max-age=68400");
		} else if (userAgent != null && userAgent.indexOf("Trident") > -1) {
			// MS IE 11
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
		} else {
			// 모질라나 오페라
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(fileName.getBytes("UTF-8"), "latin1").replaceAll("\\+", "\\ ") + ";");
		}
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 스트림 출력
		ServletOutputStream out = response.getOutputStream();
		try {
			wb.write(out);
			out.flush();
		} catch (IOException ioe) {
			// if this happens there is probably no way to report the error to the user
			if (!response.isCommitted()) {
				response.setContentType("text/html");
				// show response text now
			}
		}
		out.close();
	}
	
	@RequestMapping(value = "getDriverDoingReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getDriverDoingReceive(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		String exceptEndYn = (String) paramMap.get("exceptEndYn");
		
		List<Map<String, Object>> resultList = receiveService.getDriverDoingReceive(branchNum, startDate, endDate,
				exceptEndYn);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getSharedReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getSharedReceive(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		
		List<Map<String, Object>> resultList = receiveService.getSharedReceive(branchNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getReceiveSign", method = RequestMethod.POST)
	public ResponseEntity<List<Map<String, Object>>> getReceiveSign(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		
		List<Map<String, Object>> receiveSignList = receiveService.getReceiveSign(receiveNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				receiveSignList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getHistory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getHistory(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		
		List<Map<String, Object>> receiveTempList = receiveService.getReceiveHistory(receiveNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				receiveTempList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "callpass", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> callpass(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		String callpassYn = (String) paramMap.get("callpassYn");
		int callpassBranchNum = (Integer) paramMap.get("callpassBranchNum");
		int callpassFare = (Integer) paramMap.get("callpassFare");
		
		Map<String, Object> resultMap = receiveService
				.callpass(receiveNum, callpassYn, callpassBranchNum, callpassFare);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "confirmCallpass", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> confirmCallpass(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		
		Map<String, Object> resultMap = receiveService.confirmCallpass(receiveNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "lockReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> lockReceive(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		String modifyId = (String) paramMap.get("modifyId");
		
		Map<String, Object> resultMap = receiveService.lockReceive(receiveNum, modifyId);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modReceive", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modReceive(@RequestBody @Valid ReceiveUpdateModel receiveUpdateModel,
			BindingResult bindingResult) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			int codeIndex = 2000;
			for (ObjectError error : bindingResult.getAllErrors()) {
				resultMap.put("resultCode", ++codeIndex);
				resultMap.put("resultMessage", error.getDefaultMessage());
			}
		} else {
			resultMap.putAll(receiveService.modReceive(receiveUpdateModel));
		}
		
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modReceiveStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modReceiveStatus(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		String fromStatus = (String) paramMap.get("fromStatus");
		String toStatus = (String) paramMap.get("toStatus");
		int driverNum = (Integer) paramMap.get("driverNum");
		String memberId = (String) paramMap.get("memberId");
		
		Map<String, Object> resultMap = receiveService.modReceiveStatus(receiveNum, fromStatus, toStatus, driverNum,
				memberId);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modReceiveEndTime", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modReceiveEndTime(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		String endTime = (String) paramMap.get("endTime");
		
		Map<String, Object> resultMap = receiveService.modReceiveEndTime(receiveNum, endTime);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modReceiveSign", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modReceiveSign(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int receiveNum = (Integer) paramMap.get("receiveNum");
		String type = (String) paramMap.get("type");
		String sign = (String) paramMap.get("sign");
		
		Map<String, Object> resultMap = receiveService.modReceiveSign(receiveNum, type, sign);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 반복예약 취소 (아직 처리하지 않은 건만을 삭제한다)
	 * 
	 * @param paramMap
	 *            keyReserveRpeat 반복예약키
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "delRepeatReserve", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delRepeatReserve(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		int branchNum = (Integer) paramMap.get("branchNum");
		String keyReserveRpeat = (String) paramMap.get("keyReserveRpeat");
		
		Map<String, Object> resultMap = receiveService.delRepeatReserve(branchNum, keyReserveRpeat);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
}
