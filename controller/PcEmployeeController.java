package kr.totalcall.controller.pc;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.totalcall.service.BranchService;
import kr.totalcall.service.EmployeeService;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/pc/employee")
public class PcEmployeeController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private BranchService branchService;
	
	@RequestMapping(value = "setEmployee", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setEmployee(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String vnum = (String) paramMap.get("vnum");
		String employeeName = (String) paramMap.get("employeeName");
		String employeeId = (String) paramMap.get("employeeId");
		String password = (String) paramMap.get("password");
		String workType = (String) paramMap.get("workType");
		String vehicle = (String) paramMap.get("vehicle");
		String vehicleNum = (String) paramMap.get("vehicleNum");
		String phone = (String) paramMap.get("phone");
		String tel = (String) paramMap.get("tel");
		String jumin = (String) paramMap.get("jumin");
		String address = (String) paramMap.get("address");
		String memo = (String) paramMap.get("memo");
		String inDate = (String) paramMap.get("inDate");
		String outDate = (String) paramMap.get("outDate");
		String reworkDate = (String) paramMap.get("reworkDate");
		int stopChk = (Integer) paramMap.get("stopChk");
		int outChk = (Integer) paramMap.get("outChk");
		String license = (String) paramMap.get("license");
		String licenseNum = (String) paramMap.get("licenseNum");
		int defaultCharge = (Integer) paramMap.get("defaultCharge");
		String feeType = (String) paramMap.get("feeType");
		
		Map<String, Object> resultMap = employeeService.setEmployee(branchNum, vnum, employeeName, employeeId,
				password, workType, vehicle, vehicleNum, phone, tel, jumin, address, memo, inDate, outDate, reworkDate,
				stopChk, outChk, license, licenseNum, defaultCharge, feeType);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "setEmployeeCompany", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setEmployeeCompany(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		int branchNum = (Integer) paramMap.get("branchNum");
		String compNum = (String) paramMap.get("compNum");
		String birthDate = (String) paramMap.get("birthDate");
		String compName = (String) paramMap.get("compName");
		String compCeo = (String) paramMap.get("compCeo");
		String compAddr = (String) paramMap.get("compAddr");
		String compClass = (String) paramMap.get("compClass");
		String compType = (String) paramMap.get("compType");
		String modifyId = (String) paramMap.get("modifyId");
		String certifyYn = (String) paramMap.get("certifyYn");
		String certifyImg = (String) paramMap.get("certifyImg");
		
		Map<String, Object> resultMap = employeeService.setEmployeeCompany(employeeNum, branchNum, compNum, birthDate,
				compName, compCeo, compAddr, compClass, compType, modifyId, certifyYn, certifyImg);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getEmployeeCompanyCert", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getEmployeeCompanyCert(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		
		List<Map<String, Object>> employeeMapList = employeeService.getEmployeeCompanyCert(branchNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeMapList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getEmployeeCompanyByNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getEmployeeCompanyByNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		
		Map<String, Object> employeeMap = employeeService.getEmployeeCompanyByNum(employeeNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getEmployeeCompanyByTotalcall", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getEmployeeCompanyByTotalcall(
			@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		
		List<Map<String, Object>> employeeMap = employeeService.getEmployeeCompanyByTotalcall(branchNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeMap, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "loginEmployeeCompany", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> loginEmployeeCompany(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String compNum = (String) paramMap.get("compNum");
		String vnum = (String) paramMap.get("vnum");
		
		Map<String, Object> employeeMap = employeeService.loginEmployeeCompany(compNum, vnum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getTaxInvoiceHistory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getTaxInvoiceHistory(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		String year = (String) paramMap.get("year");
		
		List<Map<String, Object>> employeeMap = employeeService.getTaxInvoiceHistory(employeeNum, year);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeMap, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getTaxInvoiceHistoryAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getTaxInvoiceHistoryAll(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String year = (String) paramMap.get("year");
		String month = (String) paramMap.get("month");
		
		Map<String, Object> employeeMap = employeeService.getTaxInvoiceHistoryAll(branchNum, year, month);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getTaxInvoice", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getTaxInvoice(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		int branchNum = (Integer) paramMap.get("branchNum");
		
		Map<String, Object> employeeMap = employeeService.getTaxInvoice(employeeNum, branchNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverByNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDriverByNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		
		Map<String, Object> employeeMap = employeeService.getDriverByNum(employeeNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverByVnum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDriverByVnum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String vnum = (String) paramMap.get("vnum");
		
		Map<String, Object> employeeMap = employeeService.getDriverByVnum(branchNum, vnum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getRecentlyDriverByNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getRecentlyDriverByNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		
		Map<String, Object> employeeMap = employeeService.getRecentlyDriverByNum(employeeNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverByPhone", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDriverByPhone(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String phone = (String) paramMap.get("phone");
		
		Map<String, Object> employeeMap = employeeService.getDriverByPhone(phone);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverByBranchNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getDriverByBranchNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		
		List<Map<String, Object>> employeeList = employeeService.getDriverByBranchNum(branchNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverByNameInShareGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getDriverByNameInShareGroup(
			@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String driverName = (String) paramMap.get("driverName");
		int branchNum = (Integer) paramMap.get("branchNum");
		
		List<Map<String, Object>> employeeMap = employeeService.getDriverByNameInShareGroup(driverName, branchNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeMap, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverByVnumInShareGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getDriverByVnumInShareGroup(
			@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String driverVnum = (String) paramMap.get("driverVnum");
		int branchNum = (Integer) paramMap.get("branchNum");
		
		List<Map<String, Object>> employeeMap = employeeService.getDriverByVnumInShareGroup(driverVnum, branchNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeMap, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverConnect", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getDriverConnect(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		int type = (Integer) paramMap.get("type");
		
		List<Map<String, Object>> employeeMap = employeeService.getDriverConnect(branchNum, type);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeMap, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverWorking", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getDriverWorking(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int driverNum = (Integer) paramMap.get("driverNum");
		
		List<Map<String, Object>> employeeMap = employeeService.getDriverWorking(driverNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeMap, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getDriverInBounds", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getDriverInBounds(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int driverNum = (Integer) paramMap.get("driverNum");
		int branchNum = (Integer) paramMap.get("branchNum");
		double latitudeFrom = (Double) paramMap.get("latitudeFrom");
		double latitudeTo = (Double) paramMap.get("latitudeTo");
		double longitudeFrom = (Double) paramMap.get("longitudeFrom");
		double longitudeTo = (Double) paramMap.get("longitudeTo");
		
		List<Map<String, Object>> employeeMap = employeeService.getDriverInBounds(driverNum, branchNum, latitudeFrom,
				latitudeTo, longitudeFrom, longitudeTo);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				employeeMap, HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 관제(control) 화면에서 기사 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getDriverByControl", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDriverByControl(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		int branchType = (Integer) paramMap.get("branchType");
		String vnum = (String) paramMap.get("vnum");
		int vehicleType = (Integer) paramMap.get("vehicleType");
		
		Map<String, Object> employeeMap = employeeService.getDriverByControl(branchNum, branchType, vnum, vehicleType);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getEmployeeByNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getEmployeeByNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		
		Map<String, Object> employeeMap = employeeService.getEmployeeByNum(employeeNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getEmployeeById", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getEmployeeById(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String id = (String) paramMap.get("id");
		
		Map<String, Object> employeeMap = employeeService.getEmployeeById(id);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(employeeMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getEmpOptions", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getEmpOptions(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int empNum = (Integer) paramMap.get("empNum");
		
		Map<String, Object> empOptionsMap = employeeService.getEmpOptions(empNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(empOptionsMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getChargeHistory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getChargeHistory(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		
		List<Map<String, Object>> resultList = employeeService.getChargeHistory(employeeNum, startDate, endDate);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getEmployeeChargeHistory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getEmployeeChargeHistory(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		
		List<Map<String, Object>> resultList = employeeService.getEmployeeChargeHistory(branchNum, startDate, endDate);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "searchEmployee", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> searchEmployee(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		int callgroupNum = (Integer) paramMap.get("callgroupNum");
		int workType = (Integer) paramMap.get("workType");
		int vehicleType = (Integer) paramMap.get("vehicleType");
		int workStatusType = (Integer) paramMap.get("workStatusType");
		String searchWord = (String) paramMap.get("searchWord");
		
		List<Map<String, Object>> resultList = employeeService.searchEmployee(branchNum, callgroupNum, workType,
				vehicleType, workStatusType, searchWord);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "searchEmployeeExcel", method = RequestMethod.GET)
	public void searchEmployeeExcel(int branchNum, int callgroupNum, int workType, int vehicleType, int workStatusType,
			String searchWord, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("branchNum = " + branchNum);
		logger.info("callgroupNum = " + callgroupNum);
		logger.info("workType = " + workType);
		logger.info("vehicleType = " + vehicleType);
		logger.info("workStatusType = " + workStatusType);
		searchWord = URLDecoder.decode(searchWord, "UTF-8");
		logger.info("searchWord = " + searchWord);
		
		Workbook wb = employeeService.searchEmployeeExcel(branchNum, callgroupNum, workType, vehicleType,
				workStatusType, searchWord);
		
		// file name 설정
		Map<String, Object> branchMap = branchService.getBranchByBranchNum(branchNum);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String fileName = branchMap.get("name") + "_직원관리_(생성-" + sdf.format(date) + ")" + ".xls";
		
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
	
	@RequestMapping(value = "searchCompanyCharge", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> searchCompanyCharge(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String startMonth = (String) paramMap.get("startMonth");
		String endMonth = (String) paramMap.get("endMonth");
		String searchWord = (String) paramMap.get("searchWord");
		
		List<Map<String, Object>> resultList = employeeService.searchCompanyCharge(branchNum, startMonth, endMonth,
				searchWord);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modEmployee", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modEmployee(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		int branchNum = (Integer) paramMap.get("branchNum");
		String vnum = (String) paramMap.get("vnum");
		String employeeName = (String) paramMap.get("employeeName");
		String employeeId = (String) paramMap.get("employeeId");
		String password = (String) paramMap.get("password");
		String workType = (String) paramMap.get("workType");
		String vehicle = (String) paramMap.get("vehicle");
		String vehicleNum = (String) paramMap.get("vehicleNum");
		String phone = (String) paramMap.get("phone");
		String tel = (String) paramMap.get("tel");
		String jumin = (String) paramMap.get("jumin");
		String address = (String) paramMap.get("address");
		String memo = (String) paramMap.get("memo");
		String inDate = (String) paramMap.get("inDate");
		String outDate = (String) paramMap.get("outDate");
		String reworkDate = (String) paramMap.get("reworkDate");
		int stopChk = (Integer) paramMap.get("stopChk");
		int outChk = (Integer) paramMap.get("outChk");
		String license = (String) paramMap.get("license");
		String licenseNum = (String) paramMap.get("licenseNum");
		int defaultCharge = (Integer) paramMap.get("defaultCharge");
		String feeType = (String) paramMap.get("feeType");
		
		Map<String, Object> resultMap = employeeService.modEmployee(employeeNum, branchNum, vnum, employeeName,
				employeeId, password, workType, vehicle, vehicleNum, phone, tel, jumin, address, memo, inDate, outDate,
				reworkDate, stopChk, outChk, license, licenseNum, defaultCharge, feeType);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modEmployeeCharge", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modEmployeeCharge(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		int deposit = (Integer) paramMap.get("deposit");
		int withdraw = (Integer) paramMap.get("withdraw");
		String chargeMemo = (String) paramMap.get("chargeMemo");
		String chargeType = (String) paramMap.get("chargeType");
		String modifyId = (String) paramMap.get("modifyId");
		
		Map<String, Object> resultMap = employeeService.modEmployeeCharge(employeeNum, modifyId, chargeType, deposit,
				withdraw, chargeMemo);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modEmpOptions", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modEmpOptions(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int empNum = (Integer) paramMap.get("empNum");
		int dateSub = (Integer) paramMap.get("dateSub");
		String dateSubDesc = (String) paramMap.get("dateSubDesc");
		int monSub = (Integer) paramMap.get("monSub");
		String monSubDesc = (String) paramMap.get("monSubDesc");
		int monSubDate = (Integer) paramMap.get("monSubDate");
		
		Map<String, Object> empOptionsMap = employeeService.modEmpOptions(empNum, dateSub, dateSubDesc, monSub,
				monSubDesc, monSubDate);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(empOptionsMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modChkInternet", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modChkInternet(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		int chkInternet = (Integer) paramMap.get("chkInternet");
		
		Map<String, Object> resultMap = employeeService.modChkInternet(employeeNum, chkInternet);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modKeyphone", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modKeyphone(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		String keyphone = (String) paramMap.get("keyphone");
		
		Map<String, Object> resultMap = employeeService.modKeyphone(employeeNum, keyphone);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modOtherKeyphone", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modOtherKeyphone(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		int branchNum = (Integer) paramMap.get("branchNum");
		String keyphone = (String) paramMap.get("keyphone");
		
		Map<String, Object> resultMap = employeeService.modOtherKeyphone(employeeNum, branchNum, keyphone);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modNoticeNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modNoticeNum(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		int noticeNum = (Integer) paramMap.get("noticeNum");
		
		Map<String, Object> empOptionsMap = employeeService.modNoticeNum(employeeNum, noticeNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(empOptionsMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modAccount", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modAccount(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int targetNum = (Integer) paramMap.get("targetNum");
		String autoSendMoneyYn = (String) paramMap.get("autoSendMoneyYn");
		String withholdingYn = (String) paramMap.get("withholdingYn");
		String bankCode = (String) paramMap.get("bankCode");
		String account = (String) paramMap.get("account");
		String accountName = (String) paramMap.get("accountName");
		
		Map<String, Object> resultMap = employeeService.modAccount(targetNum, autoSendMoneyYn, withholdingYn, bankCode,
				account, accountName);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modEmployeeCompanyCertYn", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modEmployeeCompanyCertYn(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int num = (Integer) paramMap.get("num");
		
		Map<String, Object> resultMap = employeeService.modEmployeeCompanyCertYn(num);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modEmployeeCompanyYn", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modEmployeeCompanyYn(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int num = (Integer) paramMap.get("num");
		
		Map<String, Object> resultMap = employeeService.modEmployeeCompanyYn(num);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 접수자 로그인
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String userId = (String) paramMap.get("userId");
		String userPw = (String) paramMap.get("userPw");
		
		Map<String, Object> resultMap = employeeService.login(userId, userPw);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 접속 정보
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getConnection", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getConnection(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		String internetRecvNoti = (String) paramMap.get("internetRecvNoti");
		String internetCardNoti = (String) paramMap.get("internetCardNoti");
		
		Map<String, Object> resultMap = employeeService.getConnection(employeeNum, internetRecvNoti, internetCardNoti);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 접수자 로그아웃
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		
		Map<String, Object> resultMap = employeeService.logout(employeeNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "delEmployee", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delEmployee(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int employeeNum = (Integer) paramMap.get("employeeNum");
		
		Map<String, Object> empOptionsMap = employeeService.delEmployee(employeeNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(empOptionsMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "delEmployeeCompany", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delEmployeeCompany(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int num = (Integer) paramMap.get("num");
		
		Map<String, Object> empOptionsMap = employeeService.delEmployeeCompany(num);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(empOptionsMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getWithholdingHistory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getWithholdingHistory(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		
		int branchCallGroupNum = (Integer) paramMap.get("branchCallGroupNum");
		int branchNum = (Integer) paramMap.get("branchNum");
		String startMonth = (String) paramMap.get("startMonth");
		String endMonth = (String) paramMap.get("endMonth");
		
		List<Map<String, Object>> resultList = employeeService.getWithholdingHistory(branchCallGroupNum, branchNum,
				startMonth, endMonth);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getWithholdingHistoryExcel", method = RequestMethod.GET)
	public void getWithholdingHistoryExcel(int branchCallGroupNum, int branchNum, String startMonth, String endMonth,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("branchCallGroupNum = " + branchCallGroupNum);
		logger.info("branchNum = " + branchNum);
		logger.info("startMonth = " + startMonth);
		logger.info("endMonth = " + endMonth);
		
		Workbook wb = employeeService.getWithholdingHistoryExcel(branchCallGroupNum, branchNum, startMonth, endMonth);
		
		// file name 설정
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
		String fileName = "원천징수내역_" + startMonth + "~" + endMonth + "(생성-" + sdf.format(date) + ")" + ".xls";
		
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
	
}
