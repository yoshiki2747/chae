package kr.totalcall.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.totalcall.dao.mapper.DriverMapper;
import kr.totalcall.dao.mapper.EmployeeBranchMapper;
import kr.totalcall.dao.mapper.EmployeeEmployeeChargeMapper;
import kr.totalcall.dao.mapper.EmployeeEmployeeCompanyMapper;
import kr.totalcall.dao.mapper.MessageExMapper;
import kr.totalcall.dao.model.EmployeeCardBranch;
import kr.totalcall.generateddao.mapper.BankMapper;
import kr.totalcall.generateddao.mapper.BranchCallGroupMapper;
import kr.totalcall.generateddao.mapper.BranchChargeMapper;
import kr.totalcall.generateddao.mapper.BranchGroupMapper;
import kr.totalcall.generateddao.mapper.BranchMapper;
import kr.totalcall.generateddao.mapper.CompanyInfoMapper;
import kr.totalcall.generateddao.mapper.EmpOptionsMapper;
import kr.totalcall.generateddao.mapper.EmployeeChargeMapper;
import kr.totalcall.generateddao.mapper.EmployeeChargeOutReqMapper;
import kr.totalcall.generateddao.mapper.EmployeeCompanyMapper;
import kr.totalcall.generateddao.mapper.EmployeeMapper;
import kr.totalcall.generateddao.mapper.MoneyAutoSendConfigMapper;
import kr.totalcall.generateddao.mapper.MoneyOutReqCronMapper;
import kr.totalcall.generateddao.mapper.NoticeMapper;
import kr.totalcall.generateddao.mapper.OptionsMapper;
import kr.totalcall.generateddao.mapper.ReceiveMapper;
import kr.totalcall.generateddao.mapper.ReceiveTempMapper;
import kr.totalcall.generateddao.model.Bank;
import kr.totalcall.generateddao.model.BankExample;
import kr.totalcall.generateddao.model.Branch;
import kr.totalcall.generateddao.model.BranchCallGroup;
import kr.totalcall.generateddao.model.BranchCharge;
import kr.totalcall.generateddao.model.BranchExample;
import kr.totalcall.generateddao.model.BranchGroup;
import kr.totalcall.generateddao.model.CompanyInfo;
import kr.totalcall.generateddao.model.CompanyInfoExample;
import kr.totalcall.generateddao.model.EmpOptions;
import kr.totalcall.generateddao.model.EmpOptionsExample;
import kr.totalcall.generateddao.model.Employee;
import kr.totalcall.generateddao.model.EmployeeCharge;
import kr.totalcall.generateddao.model.EmployeeChargeExample;
import kr.totalcall.generateddao.model.EmployeeChargeOutReq;
import kr.totalcall.generateddao.model.EmployeeChargeOutReqExample;
import kr.totalcall.generateddao.model.EmployeeCompany;
import kr.totalcall.generateddao.model.EmployeeCompanyExample;
import kr.totalcall.generateddao.model.EmployeeExample;
import kr.totalcall.generateddao.model.MoneyAutoSendConfig;
import kr.totalcall.generateddao.model.MoneyOutReqCron;
import kr.totalcall.generateddao.model.MoneyOutReqCronExample;
import kr.totalcall.generateddao.model.Notice;
import kr.totalcall.generateddao.model.NoticeExample;
import kr.totalcall.generateddao.model.Options;
import kr.totalcall.generateddao.model.Receive;
import kr.totalcall.generateddao.model.ReceiveExample;
import kr.totalcall.util.ObjectUtil;
import kr.totalcall.util.TotalcallCodeUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService extends CommonService {
	
	@Autowired
	private ChargeService chargeService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private BankMapper bankMapper;
	
	@Autowired
	private CompanyInfoMapper companyInfoMapper;
	
	@Autowired
	private EmployeeChargeMapper employeeChargeMapper;
	
	@Autowired
	private EmployeeCompanyMapper employeeCompanyMapper;
	
	@Autowired
	private EmployeeEmployeeChargeMapper employeeEmployeeChargeMapper;
	
	@Autowired
	private EmployeeEmployeeCompanyMapper employeeEmployeeCompanyMapper;
	
	@Autowired
	private EmployeeChargeOutReqMapper employeeChargeOutReqMapper;
	
	@Autowired
	private EmpOptionsMapper empOptionsMapper;
	
	@Autowired
	private BranchMapper branchMapper;
	
	@Autowired
	private BranchGroupMapper branchGroupMapper;
	
	@Autowired
	private BranchCallGroupMapper branchCallGroupMapper;
	
	@Autowired
	private BranchChargeMapper branchChargeMapper;
	
	@Autowired
	private DriverMapper driverMapper;
	
	@Autowired
	private ReceiveMapper receiveMapper;
	
	@Autowired
	private ReceiveTempMapper receiveTempMapper;
	
	@Autowired
	private EmployeeBranchMapper employeeBranchMapper;
	
	@Autowired
	private MoneyAutoSendConfigMapper moneyAutoSendConfigMapper;
	
	@Autowired
	private MoneyOutReqCronMapper moneyOutReqCronMapper;
	
	@Autowired
	private NoticeMapper noticeMapper;
	
	@Autowired
	private MessageExMapper messageExMapper;
	
	@Autowired
	private OptionsMapper optionsMapper;
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> registerDriver(int branchNum, String id, String vnum, String name, String vehicleType,
			String vehicleNum, String phone, String jumin, String address) {
		int resultCode = 9999;
		String resultMessage = "";
		String resultBranchTel = "";
		int insertedRow = 0;
		
		EmployeeExample employeeExample = new EmployeeExample();
		employeeExample.createCriteria().andIdEqualTo(id).andDelEqualTo(0);
		employeeExample.setOrderByClause("num DESC");
		List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
		
		if (employeeList.size() > 0) {
			Branch branch = branchMapper.selectByPrimaryKey(employeeList.get(0).getBranchNum());
			resultCode = 3001;
			resultMessage = branch.getName() + "에 등록된 CID입니다.";
			resultBranchTel = branch.getTel();
		} else {
			employeeExample = new EmployeeExample();
			employeeExample.createCriteria().andVnumEqualTo(vnum).andVnumNotEqualTo("").andBranchNumEqualTo(branchNum)
					.andDelEqualTo(0);
			employeeList = employeeMapper.selectByExample(employeeExample);
			if (employeeList.size() > 0) {
				resultCode = 3002;
				resultMessage = "이미 사용중인 기사번호입니다.";
			} else {
				// // 가입 시 충전금 10,000원 증정
				// int charge = 10000;
				// if (branchNum == 246) { // 나눔네트워크 가입 시 충전금 0원
				// charge = 0;
				// }
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				Employee employee = new Employee();
				employee.setBranchNum(branchNum);
				employee.setVnum(vnum);
				employee.setName(name);
				employee.setId(id);
				employee.setPassword("");
				employee.setWorkType("기사");
				employee.setVehicle(TotalcallCodeUtil.getVehicle(vehicleType));
				employee.setVehicleNum(vehicleNum);
				employee.setPhone(phone);
				employee.setTel("");
				employee.setJumin(jumin);
				employee.setAddress(address);
				employee.setMemo("");
				employee.setInDate(sdf.format(date));
				employee.setStopChk(0);
				employee.setOutChk(0);
				employee.setLicense("");
				employee.setLicenseNum("");
				employee.setCharge(0);
				employee.setDel(0);
				employee.setBak(0);
				// NOT NULL 채우기
				employee.setBranchName("");
				employee.setStopDatetime("");
				employee.setReworkDate("");
				employee.setOutDate("");
				employee.setState("");
				employee.setCounty("");
				employee.setTown("");
				employee.setLoginDatetime(date);
				employee.setRefreshDatetime(date);
				employee.setGpsDatetime(date);
				employee.setKeyphone("");
				insertedRow = employeeMapper.insertSelective(employee);
				if (StringUtils.isBlank(vnum)) {
					employee.setVnum(Integer.toString(employee.getNum()));
					employeeMapper.updateByPrimaryKeySelective(employee);
				}
			}
			
			if (insertedRow == 1) {
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		resultMap.put("resultBranchTel", resultBranchTel);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> loginDriver(String id, String brand, String manufacturer, String model, String sdkInt,
			String release, String appDualVer) {
		// 어플 주소 DB
		int addressDatabaseVer = 1;
		
		EmployeeExample employeeExample = new EmployeeExample();
		employeeExample.createCriteria().andIdEqualTo(id).andStopChkEqualTo(0).andOutChkEqualTo(0)
				.andWorkTypeEqualTo("기사").andDelEqualTo(0);
		List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
		if (employeeList.size() < 1) {
			employeeExample = new EmployeeExample();
			employeeExample.createCriteria().andIdEqualTo(id).andStopChkEqualTo(0).andOutChkEqualTo(0)
					.andWorkTypeEqualTo("픽업맨").andDelEqualTo(0);
			employeeList = employeeMapper.selectByExample(employeeExample);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if (employeeList.size() == 1) {
			Date loginDatetime = new Date();
			Employee employee = employeeList.get(0);
			employee.setLoginDatetime(loginDatetime);
			employee.setRefreshDatetime(loginDatetime);
			employee.setBrand(brand);
			employee.setManufacturer(manufacturer);
			employee.setModel(model);
			employee.setSdkInt(sdkInt);
			employee.setRelease(release);
			employee.setAppDualVer(appDualVer);
			employeeMapper.updateByPrimaryKeySelective(employee);
			
			// 전체기사공지 확인
			List<Map<String, Object>> allDriverNoticeList = noticeService.getAllDriverNotice(employee.getNum());
			
			// 개인 사업자 등록 확인
			String companyYn = "N";
			Map<String, Object> employeeCompany = getEmployeeCompanyByNum(employee.getNum());
			if (!"{}".equals(employeeCompany.toString())) {
				companyYn = "Y";
			}
			
			resultMap.put("num", employee.getNum());
			resultMap.put("name", employee.getName());
			resultMap.put("id", employee.getId());
			resultMap.put("workType", employee.getWorkType());
			resultMap.put("vehicle", employee.getVehicle());
			resultMap.put("vehicleNum", employee.getVehicleNum());
			resultMap.put("charge", employee.getCharge());
			resultMap.put("autoTarget1", employee.getAutoTarget1());
			resultMap.put("autoTarget2", employee.getAutoTarget2());
			resultMap.put("autoTarget3", employee.getAutoTarget3());
			resultMap.put("autoTarget4", employee.getAutoTarget4());
			resultMap.put("autoTarget5", employee.getAutoTarget5());
			resultMap.put("branchNum", employee.getBranchNum());
			resultMap.put("allDriverNoticeCount", allDriverNoticeList.size());
			// 어플 주소 DB
			resultMap.put("addressDatabaseVer", addressDatabaseVer);
			resultMap.put("companyYn", companyYn);
			
			// 3만원 미만 충전금 기사 공지 작성
			if (employee.getCharge() < 30000) {
				int noticeType = 3;
				String noticeTitle = "충전잔액알림";
				String noticeText = "충전잔액이 " + employee.getCharge() + "원 남았습니다.";
				
				NoticeExample noticeExample = new NoticeExample();
				noticeExample.createCriteria().andTypeEqualTo(noticeType).andRecvIdEqualTo(employee.getNum())
						.andTitleEqualTo(noticeTitle);
				noticeMapper.deleteByExample(noticeExample);
				
				Notice notice = new Notice();
				notice.setType(noticeType);
				notice.setTitle(noticeTitle);
				notice.setText(noticeText);
				notice.setRecvId(employee.getNum());
				notice.setRecvName(employee.getName());
				notice.setDatetime(new Date());
				notice.setCount(0);
				notice.setBranchNum(1);
				notice.setBranchName("");
				notice.setGroupNo(0);
				notice.setId("");
				notice.setName("");
				noticeMapper.insertSelective(notice);
			}
		}
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> setEmployee(int branchNum, String vnum, String employeeName, String employeeId,
			String password, String workType, String vehicle, String vehicleNum, String phone, String tel,
			String jumin, String address, String memo, String inDate, String outDate, String reworkDate, int stopChk,
			int outChk, String license, String licenseNum, int defaultCharge, String feeType) {
		int resultCode = 9999;
		int insertedRow = 0;
		String checkBranchName = "";
		String checkEmployeeName = "";
		
		if (defaultCharge == 0) {
			defaultCharge = 30000;
		}
		
		// employeeId가 공백이 아니면 같은 id가 있는지 체크 안함.
		if (StringUtils.isNotBlank(employeeId)) {
			// employeeId가 같은지 check 한 후 employeeId가 사용중이면 사용중인 branchName을 리턴함 resultCode = 3001
			checkBranchName = employeeBranchMapper.selectBranchName(employeeId, 0);
		}
		
		if (StringUtils.isBlank(checkBranchName) || null == checkBranchName) {
			EmployeeExample employeeExample = new EmployeeExample();
			employeeExample.createCriteria().andVnumEqualTo(vnum).andBranchNumEqualTo(branchNum);
			List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
			if (employeeList.size() > 0) {
				// vnum이 같은지 check 한 후 같은 값이 있으면 해당 employeeName을 리턴함 resultCode = 3002
				resultCode = 3002;
				checkEmployeeName = employeeList.get(0).getName();
			} else {
				Branch branch = branchMapper.selectByPrimaryKey(branchNum);
				
				if (StringUtils.isBlank(inDate)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					inDate = sdf.format(new Date());
				}
				
				Employee employee = new Employee();
				employee.setBranchNum(branchNum);
				employee.setBranchName(branch.getName());
				employee.setVnum(vnum);
				employee.setName(employeeName);
				employee.setId(employeeId);
				employee.setPassword(password);
				employee.setWorkType(workType);
				employee.setVehicle(vehicle);
				employee.setVehicleNum(vehicleNum);
				employee.setPhone(phone);
				employee.setTel(tel);
				employee.setJumin(jumin);
				employee.setAddress(address);
				employee.setMemo(memo);
				employee.setInDate(inDate);
				employee.setOutDate(outDate);
				employee.setReworkDate(reworkDate);
				employee.setStopDatetime("");
				employee.setStopChk(stopChk);
				employee.setOutChk(outChk);
				employee.setLicense(license);
				employee.setLicenseNum(licenseNum);
				employee.setState("");
				employee.setCounty("");
				employee.setTown("");
				employee.setLoginDatetime(new Date());
				employee.setRefreshDatetime(new Date());
				employee.setGpsDatetime(new Date());
				employee.setKeyphone("");
				employee.setDefaultCharge(defaultCharge);
				employee.setFeeType(feeType);
				employee.setFeeTypeModifyDate(new Date());
				employee.setDel(0);
				employee.setBak(0);
				insertedRow = employeeMapper.insertSelective(employee);
				
				if (StringUtils.isBlank(vnum)) {
					employee.setVnum(Integer.toString(employee.getNum()));
					employeeMapper.updateByPrimaryKeySelective(employee);
				}
				
				if (insertedRow == 1) {
					resultCode = 1000;
				}
			}
		} else {
			resultCode = 3001;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("checkBranchName", checkBranchName);
		resultMap.put("checkEmployeeName", checkEmployeeName);
		resultMap.put("insertedRow", insertedRow);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> setEmployeeCompany(int employeeNum, int branchNum, String compNum, String birthDate,
			String compName, String compCeo, String compAddr, String compClass, String compType, String modifyId,
			String certifyYn, String certifyImg) {
		int resultCode = 9999;
		int insertedRow = 0;
		int updatedRow = 0;
		
		compNum = compNum.replace("-", "");
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		
		if (employee != null) {
			EmployeeCompanyExample employeeCompanyExample = new EmployeeCompanyExample();
			employeeCompanyExample.createCriteria().andEmployeeNumEqualTo(employeeNum).andDelEqualTo(0);
			List<EmployeeCompany> employeeCompanyList = employeeCompanyMapper.selectByExample(employeeCompanyExample);
			
			if (employeeCompanyList.size() > 0) {
				EmployeeCompany employeeCompany = employeeCompanyList.get(0);
				employeeCompany.setBranchNum(branchNum);
				employeeCompany.setCompNum(compNum);
				employeeCompany.setBirthDate(birthDate);
				employeeCompany.setCompName(compName);
				employeeCompany.setCompCeo(compCeo);
				employeeCompany.setCompAddr(compAddr);
				employeeCompany.setCompClass(compClass);
				employeeCompany.setCompType(compType);
				employeeCompany.setModifyId(modifyId);
				employeeCompany.setCertifyYn(certifyYn);
				employeeCompany.setModDatetime(new Date());
				
				updatedRow = employeeCompanyMapper.updateByPrimaryKeySelective(employeeCompany);
				
				if (updatedRow == 1) {
					resultCode = 1000;
				}
			} else {
				EmployeeCompany employeeCompany = new EmployeeCompany();
				employeeCompany.setEmployeeNum(employeeNum);
				employeeCompany.setBranchNum(branchNum);
				employeeCompany.setCompNum(compNum);
				employeeCompany.setBirthDate(birthDate);
				employeeCompany.setCompName(compName);
				employeeCompany.setCompCeo(compCeo);
				employeeCompany.setCompAddr(compAddr);
				employeeCompany.setCompClass(compClass);
				employeeCompany.setCompType(compType);
				employeeCompany.setModifyId(modifyId);
				employeeCompany.setCertifyYn(certifyYn);
				employeeCompany.setCertifyImg(certifyImg);
				employeeCompany.setRegDatetime(new Date());
				
				insertedRow = employeeCompanyMapper.insertSelective(employeeCompany);
				
				if ("Y".equals(certifyYn)) {
					employee.setCompanyYn("Y");
					employeeMapper.updateByPrimaryKeySelective(employee);
				}
				
				if (insertedRow == 1) {
					resultCode = 1000;
				}
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("insertedRow", insertedRow);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	public List<Map<String, Object>> getEmployeeCompanyCert(int branchNum) {
		EmployeeCompanyExample employeeCompanyExample = new EmployeeCompanyExample();
		employeeCompanyExample.createCriteria().andBranchNumEqualTo(branchNum).andCertifyYnEqualTo("N")
				.andDelEqualTo(0);
		List<EmployeeCompany> employeeCompanyList = employeeCompanyMapper.selectByExample(employeeCompanyExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (employeeCompanyList.size() > 0) {
			mapList = ObjectUtil.getMapListToElementList(employeeCompanyList);
		}
		return mapList;
	}
	
	public Map<String, Object> getEmployeeCompanyByNum(int employeeNum) {
		EmployeeCompanyExample employeeCompanyExample = new EmployeeCompanyExample();
		employeeCompanyExample.createCriteria().andEmployeeNumEqualTo(employeeNum).andDelEqualTo(0);
		List<EmployeeCompany> employeeCompanyList = employeeCompanyMapper.selectByExample(employeeCompanyExample);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (employeeCompanyList.size() > 0) {
			EmployeeCompany employeeCompany = employeeCompanyList.get(0);
			if (employeeNum == employeeCompany.getEmployeeNum()) {
				resultMap.put("employeeNum", employeeCompany.getEmployeeNum());
				resultMap.put("branchNum", employeeCompany.getBranchNum());
				resultMap.put("compNum", employeeCompany.getCompNum());
				resultMap.put("birthDate", employeeCompany.getBirthDate());
				resultMap.put("compName", employeeCompany.getCompName());
				resultMap.put("compCeo", employeeCompany.getCompCeo());
				resultMap.put("compAddr", employeeCompany.getCompAddr());
				resultMap.put("compType", employeeCompany.getCompType());
				resultMap.put("compClass", employeeCompany.getCompClass());
				resultMap.put("certifyYn", employeeCompany.getCertifyYn());
				resultMap.put("modifyId", employeeCompany.getModifyId());
			}
		}
		
		return resultMap;
	}
	
	public List<Map<String, Object>> getEmployeeCompanyByTotalcall(int branchNum) {
		List<Map<String, Object>> companyList = employeeEmployeeCompanyMapper
				.selectEmployeeCompanyByTotalcall(branchNum);
		
		if (companyList == null) {
			companyList = new ArrayList<Map<String, Object>>();
		}
		
		return companyList;
	}
	
	public Map<String, Object> loginEmployeeCompany(String compNum, String vnum) {
		Map<String, Object> resultMap = employeeEmployeeCompanyMapper.loginEmployeeCompany(compNum, vnum);
		
		if (resultMap == null) {
			resultMap = new HashMap<String, Object>();
		}
		
		return resultMap;
	}
	
	public List<Map<String, Object>> getTaxInvoiceHistory(int employeeNum, String year) {
		String startDate = year + "-01-01";
		String endDate = year + "-12-31";
		
		List<Map<String, Object>> taxMapList = employeeEmployeeCompanyMapper.selectTaxInvoiceHistory(employeeNum,
				startDate, endDate);
		if (taxMapList.size() == 0) {
			taxMapList = new ArrayList<Map<String, Object>>();
		}
		return taxMapList;
	}
	
	public Map<String, Object> getTaxInvoiceHistoryAll(int branchNum, String year, String month) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> branchMap = new HashMap<String, Object>();
		
		CompanyInfoExample companyInfoExample = new CompanyInfoExample();
		companyInfoExample.createCriteria().andGbnEqualTo("B").andNumEqualTo(branchNum);
		
		List<CompanyInfo> companyInfoList = companyInfoMapper.selectByExample(companyInfoExample);
		if (companyInfoList.size() > 0) {
			branchMap = ObjectUtil.getMapToElement(companyInfoList.get(0));
			resultMap.put("branch", branchMap);
		}
		
		String startMonth = year + month;
		
		List<Map<String, Object>> taxList = employeeEmployeeCompanyMapper.selectTaxInvoiceHistoryAll(branchNum,
				startMonth);
		if (taxList.size() == 0) {
			new ArrayList<Map<String, Object>>();
		}
		
		resultMap.put("taxList", taxList);
		
		return resultMap;
	}
	
	public Map<String, Object> getTaxInvoice(int employeeNum, int branchNum) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> branchMap = new HashMap<String, Object>();
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		
		CompanyInfoExample companyInfoExample = new CompanyInfoExample();
		companyInfoExample.createCriteria().andGbnEqualTo("B").andNumEqualTo(branchNum);
		
		List<CompanyInfo> companyInfoList = companyInfoMapper.selectByExample(companyInfoExample);
		if (companyInfoList.size() > 0) {
			branchMap = ObjectUtil.getMapToElement(companyInfoList.get(0));
			resultMap.put("branch", branchMap);
		}
		
		EmployeeCompanyExample employeeCompanyExample = new EmployeeCompanyExample();
		employeeCompanyExample.createCriteria().andEmployeeNumEqualTo(employeeNum);
		
		List<EmployeeCompany> employeeCompanyList = employeeCompanyMapper.selectByExample(employeeCompanyExample);
		if (employeeCompanyList.size() > 0) {
			employeeMap = ObjectUtil.getMapToElement(employeeCompanyList.get(0));
			resultMap.put("employee", employeeMap);
		}
		
		return resultMap;
	}
	
	public Map<String, Object> getDriverByNum(int employeeNum) {
		Map<String, Object> driverMap = employeeBranchMapper.selectDriverByNum(employeeNum);
		if (driverMap != null) {
			driverMap.put("nowDatetime", new Date());
		}
		return driverMap;
	}
	
	public Map<String, Object> getDriverByVnum(int branchNum, String vnum) {
		EmployeeExample employeeExample = new EmployeeExample();
		employeeExample.createCriteria().andBranchNumEqualTo(branchNum).andVnumEqualTo(vnum).andWorkTypeEqualTo("기사")
				.andDelEqualTo(0);
		List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (employeeList.size() > 0) {
			resultMap = ObjectUtil.getMapToElement(employeeList.get(0));
			resultMap.put("nowDatetime", new Date());
		}
		return resultMap;
	}
	
	public Map<String, Object> getRecentlyDriverByNum(int employeeNum) {
		Map<String, Object> driverMap = driverMapper.selectRecentlyDriverByNum(employeeNum);
		if (driverMap == null) {
			driverMap = new HashMap<String, Object>();
		}
		return driverMap;
	}
	
	public Map<String, Object> getDriverByPhone(String phone) {
		Map<String, Object> driverMap = driverMapper.selectByPhone(phone);
		if (driverMap == null) {
			driverMap = new HashMap<String, Object>();
		}
		return driverMap;
	}
	
	public List<Map<String, Object>> getDriverByBranchNum(int branchNum) {
		EmployeeExample employeeExample = new EmployeeExample();
		employeeExample.createCriteria().andBranchNumEqualTo(branchNum).andWorkTypeEqualTo("기사").andStopChkEqualTo(0)
				.andOutChkEqualTo(0).andDelEqualTo(0);
		employeeExample.setOrderByClause("vnum");
		List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = ObjectUtil.getMapListToElementList(employeeList);
		return mapList;
	}
	
	public List<Map<String, Object>> getDriverByNameInShareGroup(String driverName, int branchNum) {
		List<Map<String, Object>> driverMapList = driverMapper.selectDriverByNameInShareGroup(driverName, branchNum);
		if (driverMapList.size() == 0) {
			driverMapList = new ArrayList<Map<String, Object>>();
		}
		return driverMapList;
	}
	
	public List<Map<String, Object>> getDriverByVnumInShareGroup(String driverVnum, int branchNum) {
		List<Map<String, Object>> driverMapList = driverMapper.selectDriverByVnumInShareGroup(driverVnum, branchNum);
		if (driverMapList.size() == 0) {
			driverMapList = new ArrayList<Map<String, Object>>();
		}
		return driverMapList;
	}
	
	public List<Map<String, Object>> getDriverConnect(int branchNum, int type) {
		List<Map<String, Object>> driverMapList = driverMapper.selectDriverConnect(branchNum, type);
		if (driverMapList.size() == 0) {
			driverMapList = new ArrayList<Map<String, Object>>();
		}
		return driverMapList;
	}
	
	public List<Map<String, Object>> getDriverWorking(int driverNum) {
		List<Map<String, Object>> driverMapList = driverMapper.selectDriverWorking(driverNum);
		if (driverMapList == null) {
			driverMapList = new ArrayList<Map<String, Object>>();
		}
		return driverMapList;
	}
	
	public List<Map<String, Object>> getDriverInBounds(int driverNum, int branchNum, double latitudeFrom,
			double latitudeTo, double longitudeFrom, double longitudeTo) {
		List<Map<String, Object>> driverMapList = driverMapper.selectDriverInBounds(driverNum, branchNum, latitudeFrom,
				latitudeTo, longitudeFrom, longitudeTo);
		if (driverMapList == null) {
			driverMapList = new ArrayList<Map<String, Object>>();
		}
		return driverMapList;
	}
	
	public Map<String, Object> getDriverByControl(int branchNum, int branchType, String vnum, int vehicleType) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> driverMapList = driverMapper.selectDriverByControl(branchNum, branchType, vnum,
				vehicleType);
		if (driverMapList == null) {
			driverMapList = new ArrayList<Map<String, Object>>();
		} else {
			int total = 0;
			int working = 0;
			int notWorking = 0;
			
			for (Map<String, Object> driver : driverMapList) {
				Long cntAll = (Long) driver.get("cntAll");
				if (cntAll > 0) {
					working++;
				} else {
					notWorking++;
				}
				total++;
			}
			
			resultMap.put("total", total);
			resultMap.put("working", working);
			resultMap.put("notWorking", notWorking);
			resultMap.put("driverList", driverMapList);
		}
		return resultMap;
	}
	
	public Map<String, Object> getEmployeeByNum(int employeeNum) {
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (employee != null) {
			resultMap = ObjectUtil.getMapToElement(employee);
		}
		return resultMap;
	}
	
	public Map<String, Object> getEmployeeById(String id) {
		EmployeeExample employeeExample = new EmployeeExample();
		employeeExample.createCriteria().andIdEqualTo(id);
		List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
		
		// TODO : branch_name
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (employeeList.size() > 0) {
			resultMap = ObjectUtil.getMapToElement(employeeList.get(0));
		}
		return resultMap;
	}
	
	public List<EmpOptions> getEmpOptionsList() {
		EmpOptionsExample empOptionsExample = new EmpOptionsExample();
		return empOptionsMapper.selectByExample(empOptionsExample);
	}
	
	public Map<String, Object> getEmpOptions(int empNum) {
		EmpOptions empOptions = empOptionsMapper.selectByPrimaryKey(empNum);
		
		Map<String, Object> resultMap = ObjectUtil.getMapToElement(empOptions);
		return resultMap;
	}
	
	public List<Map<String, Object>> getChargeHistory(int employeeNum, String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date startDateTime = new Date();
		Date endDateTime = new Date();
		Calendar cal = new GregorianCalendar();
		try {
			startDateTime = sdf.parse(startDate);
			cal.setTime(sdf.parse(endDate));
			cal.add(Calendar.DATE, 1);
			endDateTime = cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		EmployeeChargeExample employeeChargeExample = new EmployeeChargeExample();
		employeeChargeExample.createCriteria().andEmployeeNumEqualTo(employeeNum)
				.andDatetimeBetween(startDateTime, endDateTime);
		employeeChargeExample.setOrderByClause("num DESC");
		List<EmployeeCharge> employeeChargeList = employeeChargeMapper.selectByExample(employeeChargeExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = ObjectUtil.getMapListToElementList(employeeChargeList);
		return mapList;
	}
	
	public List<Map<String, Object>> getChargeWithdraw(int employeeNum, String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date startDateTime = new Date();
		Date endDateTime = new Date();
		Calendar cal = new GregorianCalendar();
		try {
			startDateTime = sdf.parse(startDate);
			cal.setTime(sdf.parse(endDate));
			cal.add(Calendar.DATE, 1);
			endDateTime = cal.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		EmployeeChargeExample employeeChargeWithdrawExample = new EmployeeChargeExample();
		employeeChargeWithdrawExample.createCriteria().andEmployeeNumEqualTo(employeeNum).andTypeEqualTo("SEND_MONEY")
				.andDatetimeBetween(startDateTime, endDateTime);
		employeeChargeWithdrawExample.setOrderByClause("num DESC");
		List<EmployeeCharge> employeeChargeWithdrawList = employeeChargeMapper
				.selectByExample(employeeChargeWithdrawExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (employeeChargeWithdrawList.size() > 0) {
			mapList = ObjectUtil.getMapListToElementList(employeeChargeWithdrawList);
		}
		return mapList;
	}
	
	public List<Map<String, Object>> getEmployeeChargeHistory(int branchNum, String startDate, String endDate) {
		List<Map<String, Object>> mapList = employeeEmployeeChargeMapper.selectEmployeeChargeHistory(branchNum,
				startDate, endDate);
		if (mapList.size() == 0) {
			mapList = new ArrayList<Map<String, Object>>();
		}
		return mapList;
	}
	
	public List<Map<String, Object>> searchEmployee(int branchNum, int callgroupNum, int workType, int vehicleType,
			int workStatusType, String searchWord) {
		String vehicle = "";
		switch (vehicleType) {
			case 1:
				vehicle = "'오토'";
				break;
			case 2:
				vehicle = "'다마'";
				break;
			case 3:
				vehicle = "'라보'";
				break;
			case 4:
				vehicle = "'벤'";
				break;
			case 5:
				vehicle = "'1t'";
				break;
			case 6:
				vehicle = "'1.4t'";
				break;
			case 7:
				vehicle = "'2.5t'";
				break;
			case 8:
				vehicle = "'3.5t'";
				break;
			case 9:
				vehicle = "'5t'";
				break;
			case 10:
				vehicle = "'지하철'";
				break;
			case 11:
				vehicle = "'다마', '라보', '벤', '1t', '1.4t', '2.5t', '3.5t', '5t'";
				break;
		}
		
		List<EmployeeCardBranch> employeeList = employeeBranchMapper.selectEmployee(branchNum, callgroupNum, workType,
				vehicle, workStatusType, searchWord);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = ObjectUtil.getMapListToElementList(employeeList);
		return mapList;
	}
	
	public Map<String, Object> getChargeHistoryCount(int employeeNum) {
		EmployeeChargeExample employeeChargeExample = new EmployeeChargeExample();
		employeeChargeExample.createCriteria().andEmployeeNumEqualTo(employeeNum);
		List<EmployeeCharge> employeeChargeList = employeeChargeMapper.selectByExample(employeeChargeExample);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("chargeHistoryCount", employeeChargeList.size());
		return resultMap;
	}
	
	public Map<String, Object> getChargeWithdrawCount(int employeeNum) {
		EmployeeChargeExample employeeChargeExample = new EmployeeChargeExample();
		employeeChargeExample.createCriteria().andEmployeeNumEqualTo(employeeNum).andTypeEqualTo("SEND_MONEY");
		List<EmployeeCharge> employeeChargeList = employeeChargeMapper.selectByExample(employeeChargeExample);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("chargeWithdrawCount", employeeChargeList.size());
		return resultMap;
	}
	
	public Workbook searchEmployeeExcel(int branchNum, int callgroupNum, int workType, int vehicleType,
			int workStatusType, String searchWord) {
		String vehicle = "";
		switch (vehicleType) {
			case 1:
				vehicle = "'오토'";
				break;
			case 2:
				vehicle = "'다마'";
				break;
			case 3:
				vehicle = "'라보'";
				break;
			case 4:
				vehicle = "'벤'";
				break;
			case 5:
				vehicle = "'1t'";
				break;
			case 6:
				vehicle = "'1.4t'";
				break;
			case 7:
				vehicle = "'2.5t'";
				break;
			case 8:
				vehicle = "'3.5t'";
				break;
			case 9:
				vehicle = "'5t'";
				break;
			case 10:
				vehicle = "'지하철'";
				break;
			case 11:
				vehicle = "'다마', '라보', '벤', '1t', '1.4t', '2.5t', '3.5t', '5t'";
				break;
		}
		
		List<EmployeeCardBranch> employeeList = employeeBranchMapper.selectEmployee(branchNum, callgroupNum, workType,
				vehicle, workStatusType, searchWord);
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = ObjectUtil.getMapListToElementList(employeeList);
		
		int rowCount = 0;
		int cellCount = 0;
		int maxCellCount = 0;
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("직원관리");
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.GREEN.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLUE.index);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		
		// 가로 정렬
		// style.setAlignment((short) 1); // 가로 정렬 왼쪽
		style.setAlignment((short) 2); // 가로 정렬 중간
		// style.setAlignment((short) 3); // 가로 정렬 오른쪽
		
		// 세로 정렬
		// style.setVerticalAlignment((short) 0); // 세로 정렬 상단
		style.setVerticalAlignment((short) 1); // 세로 정렬 중단
		// style.setVerticalAlignment((short) 2); // 세로 정렬 하단
		
		HSSFFont font = wb.createFont();
		font.setFontName("맑은 고딕"); // 폰트 이름
		font.setFontHeightInPoints((short) 11); // 폰트 크기
		// font.setColor(IndexedColors.RED.getIndex()); // 폰트 컬러
		// font.setStrikeout(true); // 글자 가운데 라인
		// font.setItalic(true); // 이탤릭체
		// font.setUnderline(HSSFFont.U_SINGLE); // 밑줄
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 볼드체
		style.setFont(font);
		
		HSSFRow row = sheet.createRow(rowCount++);
		
		HSSFCell cell;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("번호");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("이름");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("아이디");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("업무타입");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("차량");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("전화번호");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("휴대폰");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("주민번호");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("입사일");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("퇴사일");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("면허");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("잔액");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("요금제");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("메모");
		
		cellCount = 0;
		
		String feeType = "";
		
		for (Map<String, Object> map : mapList) {
			row = sheet.createRow(rowCount++);
			row.createCell(cellCount++).setCellValue((String) map.get("vnum"));
			row.createCell(cellCount++).setCellValue((String) map.get("name"));
			row.createCell(cellCount++).setCellValue((String) map.get("id"));
			row.createCell(cellCount++).setCellValue((String) map.get("workType"));
			row.createCell(cellCount++).setCellValue((String) map.get("vehicle"));
			row.createCell(cellCount++).setCellValue((String) map.get("tel"));
			row.createCell(cellCount++).setCellValue((String) map.get("phone"));
			row.createCell(cellCount++).setCellValue((String) map.get("jumin"));
			row.createCell(cellCount++).setCellValue((String) map.get("inDate"));
			row.createCell(cellCount++).setCellValue((String) map.get("outDate"));
			row.createCell(cellCount++).setCellValue((String) map.get("license"));
			row.createCell(cellCount++).setCellValue((Integer) map.get("charge"));
			if ("R".equals((String) map.get("feeType"))) {
				feeType = "오더별 요금제";
			} else if ("D".equals((String) map.get("feeType"))) {
				feeType = "정액제";
			}
			row.createCell(cellCount++).setCellValue(feeType);
			row.createCell(cellCount++).setCellValue((String) map.get("memo"));
			
			maxCellCount = cellCount;
			cellCount = 0;
		}
		
		for (int i = 0; i < maxCellCount; i++) {
			sheet.autoSizeColumn(i);
		}
		
		return wb;
	}
	
	public List<Map<String, Object>> searchCompanyCharge(int branchNum, String startMonth, String endMonth,
			String searchWord) {
		List<Map<String, Object>> mapList = employeeEmployeeChargeMapper.searchCompanyCharge(branchNum, startMonth,
				endMonth, searchWord);
		
		if (mapList.size() == 0) {
			mapList = new ArrayList<Map<String, Object>>();
		}
		
		return mapList;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modEmployee(int employeeNum, int branchNum, String vnum, String employeeName,
			String employeeId, String password, String workType, String vehicle, String vehicleNum, String phone,
			String tel, String jumin, String address, String memo, String inDate, String outDate, String reworkDate,
			int stopChk, int outChk, String license, String licenseNum, int defaultCharge, String feeType) {
		int resultCode = 9999;
		int updatedRow = 0;
		String checkBranchName = "";
		String checkEmployeeName = "";
		
		if (defaultCharge == 0) {
			defaultCharge = 30000;
		}
		
		if (StringUtils.isBlank(vnum)) {
			vnum = Integer.toString(employeeNum);
		}
		
		// employeeId가 공백이 아니면 같은 id가 있는지 체크 안함.
		// employeeId가 같은지 check 한 후 employeeId가 사용중이면 사용중인 branchName을 리턴함 resultCode = 3001
		if (StringUtils.isNotBlank(employeeId)) {
			checkBranchName = employeeBranchMapper.selectBranchName(employeeId, employeeNum);
		}
		
		if (StringUtils.isBlank(checkBranchName) || null == checkBranchName) {
			EmployeeExample employeeExample = new EmployeeExample();
			employeeExample.createCriteria().andVnumEqualTo(vnum).andBranchNumEqualTo(branchNum)
					.andNumNotEqualTo(employeeNum).andDelEqualTo(0);
			List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
			if (employeeList.size() > 0) {
				// vnum이 같은지 check 한 후 같은 값이 있으면 해당 employeeName을 리턴함 resultCode = 3002
				resultCode = 3002;
				checkEmployeeName = employeeList.get(0).getName();
			} else {
				Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
				if (employee != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					employee.setName(employeeName);
					employee.setPassword(password);
					employee.setWorkType(workType);
					employee.setVehicle(vehicle);
					employee.setVehicleNum(vehicleNum);
					employee.setPhone(phone);
					employee.setTel(tel);
					employee.setJumin(jumin);
					employee.setAddress(address);
					if (outChk == 1) { // 퇴사 처리
						// 퇴사하면 가상번호, id를 제거하고 기존 id를 메모에 남겨둔다
						employee.setVnum(Integer.toString(employeeNum));
						employee.setMemo(employee.getId() + "-" + memo);
						employee.setId("");
						employee.setOutDate(sdf.format(new Date()));
					} else {
						employee.setVnum(vnum);
						employee.setId(employeeId);
						employee.setMemo(memo);
					}
					if (outChk == 0 && employee.getOutChk() == 1) { // 퇴사 상태에서 재 입사 했을 경우
						employee.setReworkDate(sdf.format(new Date()));
					}
					if (stopChk == 1 && employee.getStopChk() == 0) { // 정상 근무 상태에서 정직 처리 했을 경우
						employee.setStopDatetime(sdf.format(new Date()));
					}
					employee.setStopChk(stopChk);
					employee.setOutChk(outChk);
					employee.setLicense(license);
					employee.setLicenseNum(licenseNum);
					employee.setDefaultCharge(defaultCharge);
					if (!feeType.equals(employee.getFeeType())) { // 요금제가 바꼈을 경우
						employee.setFeeTypeModifyDate(new Date());
					}
					employee.setFeeType(feeType);
					employee.setDel(0);
					employee.setBak(0);
					
					updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
				}
			}
		} else {
			resultCode = 3001;
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("checkBranchName", checkBranchName);
		resultMap.put("checkEmployeeName", checkEmployeeName);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modEmployeeCharge(int employeeNum, String modifyId, String chargeType, int deposit,
			int withdraw, String chargeMemo) {
		int resultCode = 9999;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		
		if (employee != null) {
			chargeService.chargeEmployeeFromId(employee.getNum(), 0, modifyId, chargeType, chargeMemo, 0, deposit,
					withdraw, new Date());
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	public Map<String, Object> modEmpOptions(int empNum, int dateSub, String dateSubDesc, int monSub,
			String monSubDesc, int monSubDate) {
		int resultCode = 9999;
		
		EmpOptions empOptions = empOptionsMapper.selectByPrimaryKey(empNum);
		
		if (empOptions != null) {
			empOptions.setDateSub(dateSub);
			empOptions.setDateSubDesc(dateSubDesc);
			empOptions.setMonSub(monSub);
			empOptions.setMonSubDesc(monSubDesc);
			empOptions.setMonSubDate(monSubDate);
			int updatedRows = empOptionsMapper.updateByPrimaryKeySelective(empOptions);
			if (updatedRows == 1) {
				resultCode = 1000;
			}
		} else {
			empOptions = new EmpOptions();
			empOptions.setEmpNum(empNum);
			empOptions.setDateSub(dateSub);
			empOptions.setDateSubDesc(dateSubDesc);
			empOptions.setMonSub(monSub);
			empOptions.setMonSubDesc(monSubDesc);
			empOptions.setMonSubDate(monSubDate);
			int insertedRows = empOptionsMapper.insertSelective(empOptions);
			if (insertedRows == 1) {
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modChkInternet(int employeeNum, int chkInternet) {
		int resultCode = 9999;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			employee.setChkInternet(chkInternet);
			int updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
			
			if (updatedRow == 1) {
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modKeyphone(int employeeNum, String keyphone) {
		int resultCode = 9999;
		int updatedRows = 0;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			employee.setKeyphone(keyphone);
			updatedRows = employeeMapper.updateByPrimaryKeySelective(employee);
			
			if ("".equals(keyphone)) {
				if (updatedRows == 1) {
					resultCode = 1000;
				}
			} else {
				Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
				// 내부에서만 사용하기 때문에 통합콜 번호(branchNum)가 사용됨. 차후 변경 가능성 있음.
				Branch totalcall = branchMapper.selectByPrimaryKey(1);
				if (branch != null) {
					if (branch.getCallgroupNum() == totalcall.getCallgroupNum()) {
						List<Integer> otherEmployeeList = employeeBranchMapper.selectOtherEmployeeByKeyphone(
								employeeNum, keyphone, branch.getCallgroupNum());
						for (int otherEmployeeNum : otherEmployeeList) {
							Employee otherEmployee = employeeMapper.selectByPrimaryKey(otherEmployeeNum);
							otherEmployee.setKeyphone("");
							updatedRows += employeeMapper.updateByPrimaryKeySelective(otherEmployee);
						}
						if (updatedRows == (otherEmployeeList.size() + 1)) {
							resultCode = 1000;
						}
					} else {
						if (updatedRows == 1) {
							resultCode = 1000;
						}
					}
				} else {
					resultCode = 3001;
				}
			}
		} else {
			resultCode = 3002;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modOtherKeyphone(int employeeNum, int branchNum, String keyphone) {
		int resultCode = 9999;
		int updatedRows = 0;
		
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		// 내부에서만 사용하기 때문에 통합콜 번호(branchNum)가 사용됨. 차후 변경 가능성 있음.
		Branch totalcall = branchMapper.selectByPrimaryKey(1);
		if (branch != null) {
			if (branch.getCallgroupNum() == totalcall.getCallgroupNum()) {
				List<Integer> otherEmployeeList = employeeBranchMapper.selectOtherEmployeeByKeyphone(employeeNum,
						keyphone, branch.getCallgroupNum());
				
				for (int otherEmployeeNum : otherEmployeeList) {
					Employee otherEmployee = employeeMapper.selectByPrimaryKey(otherEmployeeNum);
					otherEmployee.setKeyphone("");
					updatedRows += employeeMapper.updateByPrimaryKeySelective(otherEmployee);
				}
				if (updatedRows == otherEmployeeList.size()) {
					resultCode = 1000;
				}
			}
		} else {
			resultCode = 3001;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modNoticeNum(int employeeNum, int noticeNum) {
		int resultCode = 9999;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			employee.setNoticeNum(noticeNum);
			int updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
			if (updatedRow == 1) {
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modDriverVehicleType(int employeeNum, String vehicleType) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			employee.setVehicle(TotalcallCodeUtil.getVehicle(vehicleType));
			updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modDriverVehicleNum(int employeeNum, String vehicleNum) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			employee.setVehicleNum(vehicleNum);
			updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modDriverAutoTarget(int employeeNum, String autoTarget1, String autoTarget2,
			String autoTarget3, String autoTarget4, String autoTarget5) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			employee.setAutoTarget1(autoTarget1);
			employee.setAutoTarget2(autoTarget2);
			employee.setAutoTarget3(autoTarget3);
			employee.setAutoTarget4(autoTarget4);
			employee.setAutoTarget5(autoTarget5);
			updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modDriverFeeType(int employeeNum, String feeType) {
		int resultCode = 9999;
		String resultMessage = "";
		int updatedRow = 0;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			Calendar toCal = Calendar.getInstance();
			toCal.add(Calendar.MONTH, -1);
			if (employee.getFeeTypeModifyDate() == null) {
				employee.setFeeType(feeType);
				employee.setFeeTypeModifyDate(new Date());
				updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
			} else {
				int compare = employee.getFeeTypeModifyDate().compareTo(toCal.getTime());
				if (compare > 0) {
					resultCode = 3001;
					resultMessage = "요금 변경 후 한달이 지나지 않았습니다.\n한달이 지난후 다시 시도해주세요.";
				} else {
					employee.setFeeType(feeType);
					employee.setFeeTypeModifyDate(new Date());
					updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
				}
			}
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
			resultMessage = "요금제가 정상적으로 변경되었습니다.\n이후 한 달간 변경이 불가능합니다.";
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modAccount(int targetNum, String autoSendMoneyYn, String withholdingYn, String bankCode,
			String account, String accountName) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Employee employee = employeeMapper.selectByPrimaryKey(targetNum);
		
		BankExample bankExample = new BankExample();
		bankExample.createCriteria().andBankCodeEqualTo(bankCode);
		List<Bank> bankList = bankMapper.selectByExample(bankExample);
		Bank bank = new Bank();
		if (bankList.size() > 0) {
			bank = bankList.get(0);
		}
		
		if (employee != null) {
			employee.setOutChargeAccountNum(1);
			employee.setAutoSendMoneyYn(autoSendMoneyYn);
			employee.setWithholdingYn(withholdingYn);
			employee.setBankCode(bankCode);
			employee.setNewBankCode(bank.getNewBankCode());
			employee.setAccount(account);
			employee.setAccountName(accountName);
			updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
		} else {
			resultCode = 3001;
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modEmployeeCompanyCertYn(int num) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		EmployeeCompany employeeCompany = employeeCompanyMapper.selectByPrimaryKey(num);
		Employee employee = employeeMapper.selectByPrimaryKey(employeeCompany.getEmployeeNum());
		
		if (employeeCompany != null && employee != null) {
			employeeCompany.setCertifyYn("Y");
			employee.setCompanyYn("Y");
			
			updatedRow = employeeCompanyMapper.updateByPrimaryKey(employeeCompany);
			updatedRow += employeeMapper.updateByPrimaryKey(employee);
		} else {
			resultCode = 3001;
		}
		
		if (updatedRow == 2) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modEmployeeCompanyYn(int num) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		EmployeeCompany employeeCompany = employeeCompanyMapper.selectByPrimaryKey(num);
		Employee employee = employeeMapper.selectByPrimaryKey(employeeCompany.getEmployeeNum());
		
		if (employeeCompany != null && employee != null) {
			employeeCompany.setDel(1);
			employee.setCompanyYn("N");
			
			updatedRow = employeeCompanyMapper.updateByPrimaryKey(employeeCompany);
			updatedRow += employeeMapper.updateByPrimaryKey(employee);
		} else {
			resultCode = 3001;
		}
		
		if (updatedRow == 2) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> requestChargeOut(int employeeNum, int reqAmount, String reqMemo) {
		int resultCode = 9999;
		String resultMessage = "";
		
		MoneyAutoSendConfig moneyAutoSendConfig = moneyAutoSendConfigMapper.selectByPrimaryKey(1);
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		
		Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
		
		BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(branch.getGroupNum());
		
		SimpleDateFormat inSdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar inCal = Calendar.getInstance();
		try {
			inCal.setTime(inSdf.parse(employee.getInDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar toCal = Calendar.getInstance();
		toCal.add(Calendar.MONTH, -1);
		
		Calendar firstDayCal = Calendar.getInstance();
		firstDayCal.set(firstDayCal.get(Calendar.YEAR), firstDayCal.get(Calendar.MONTH), 1, 0, 0, 0);
		
		EmployeeChargeOutReqExample employeeChargeOutReqExample = new EmployeeChargeOutReqExample();
		employeeChargeOutReqExample.createCriteria().andEmployeeNumEqualTo(employeeNum).andConfResultNotLike("에러%")
				.andReqDatetimeGreaterThan(firstDayCal.getTime());
		employeeChargeOutReqExample.setOrderByClause("num DESC");
		List<EmployeeChargeOutReq> employeeChargeOutReqList = employeeChargeOutReqMapper
				.selectByExample(employeeChargeOutReqExample);
		
		if (inCal.getTimeInMillis() > toCal.getTimeInMillis()) {
			resultCode = 3001;
			resultMessage = "가입 후 한달 이내의 출금은 불가능 합니다.";
		} else if (StringUtils.isBlank(employee.getBankCode()) || StringUtils.isBlank(employee.getAccount())) {
			resultCode = 3002;
			resultMessage = "출금계좌 정보가 없습니다.";
		} else if ("N".equals(employee.getAutoSendMoneyYn())) {
			resultCode = 3003;
			resultMessage = "자동 송금 대상 기사가 아닙니다.";
		} else if (reqAmount + employee.getDefaultCharge() + moneyAutoSendConfig.getMoneyTranCommission() > employee
				.getCharge()) {
			resultCode = 3004;
			resultMessage = "기사 충전금이 부족합니다.";
		} else if (reqAmount + moneyAutoSendConfig.getMoneyTranCommission() > branch.getCharge()) {
			resultCode = 3005;
			resultMessage = "퀵사 충전금이 부족합니다. 퀵사에 문의하세요.";
		} else if (branchGroup != null && branchGroup.getNum() != 3
				&& reqAmount + moneyAutoSendConfig.getMoneyTranCommission() > branchGroup.getCharge()) {
			// 3번 그룹(통합콜 그룹)의 그룹 충전금은 무한대
			resultCode = 3006;
			resultMessage = "퀵사 충전금이 부족합니다. 퀵사에 문의하세요.";
		} else if (reqAmount > moneyAutoSendConfig.getEmpLimitPerOne()) {
			resultCode = 3007;
			resultMessage = "1회 요청 한도를 초과 했습니다.";
		} else if (employeeChargeOutReqList.size() > 0) {
			resultCode = 3008;
			resultMessage = "이번달의 출금 요청이 있습니다. " + inSdf.format(employeeChargeOutReqList.get(0).getReqDatetime());
		} else {
			EmployeeChargeOutReq employeeChargeOutReq = new EmployeeChargeOutReq();
			employeeChargeOutReq.setEmployeeNum(employeeNum);
			employeeChargeOutReq.setReqAmount(reqAmount);
			employeeChargeOutReq.setReqMemo(reqMemo);
			employeeChargeOutReq.setReqDatetime(new Date());
			employeeChargeOutReq.setConfResult("접수-자동송금");
			employeeChargeOutReqMapper.insertSelective(employeeChargeOutReq);
			
			String gbn = "E";
			String totalcallVaccount = "02031091"; // 통합콜 가상계좌
			SimpleDateFormat reqSdf = new SimpleDateFormat("yyyyMMdd");
			String reqDt = reqSdf.format(new Date());
			MoneyOutReqCronExample moneyOutReqCronExample = new MoneyOutReqCronExample();
			moneyOutReqCronExample.createCriteria().andReqDtEqualTo(reqDt);
			int count = moneyOutReqCronMapper.countByExample(moneyOutReqCronExample);
			count++;
			String jmSeqNo = "jm" + String.format("%010d", count);
			
			MoneyOutReqCron moneyOutReqCron = new MoneyOutReqCron();
			moneyOutReqCron.setReqDt(reqDt);
			moneyOutReqCron.setJmSeqNo(jmSeqNo);
			moneyOutReqCron.setOutReqNum(employeeChargeOutReq.getNum());
			moneyOutReqCron.setReqGbn(gbn);
			moneyOutReqCron.setReqNum(employeeNum);
			moneyOutReqCron.setOutChargeAccountNum(totalcallVaccount);
			moneyOutReqCron.setBankCode(employee.getBankCode());
			moneyOutReqCron.setNewBankCode(employee.getNewBankCode());
			moneyOutReqCron.setAccount(employee.getAccount());
			moneyOutReqCron.setAmt(reqAmount);
			moneyOutReqCron.setResult("0"); // 결과 - 0 접수
			moneyOutReqCron.setErrMsg("");
			moneyOutReqCron.setUpdateDttm(new Date());
			moneyOutReqCron.setAdminCharge(0);
			moneyOutReqCronMapper.insertSelective(moneyOutReqCron);
			
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	public Map<String, Object> getEmployeeLogin(String userId, String userPw) {
		Map<String, Object> resultMap = employeeBranchMapper.selectEmployeeBranchCallgroup(userId, userPw);
		if (resultMap != null) {
			int callgroupNum = (Integer) resultMap.get("callgroupNum");
			if (callgroupNum != 0) {
				BranchExample branchExample = new BranchExample();
				branchExample.createCriteria().andDelEqualTo(0).andCallgroupNumEqualTo(callgroupNum);
				List<Branch> branchList = branchMapper.selectByExample(branchExample);
				List<Map<String, Object>> branchMapList = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < branchList.size(); i++) {
					Map<String, Object> branchMap = new HashMap<String, Object>();
					Branch branch = branchList.get(i);
					branchMap.put("num", branch.getNum());
					branchMap.put("name", branch.getName());
					branchMapList.add(branchMap);
				}
				resultMap.put("groupList", branchMapList);
			} else {
				resultMap.put("groupList", new ArrayList<Map<String, Object>>());
			}
		}
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> login(String userId, String userPw) {
		
		Date loginDate = new Date();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		EmployeeExample employeeExample = new EmployeeExample();
		employeeExample.createCriteria().andIdEqualTo(userId).andPasswordEqualTo(userPw).andWorkTypeNotEqualTo("기사")
				.andStopChkEqualTo(0).andOutChkEqualTo(0).andDelEqualTo(0);
		List<Employee> employeeList = employeeMapper.selectByExample(employeeExample);
		
		if (employeeList.size() > 0) {
			Employee employee = employeeList.get(0);
			Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
			BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(branch.getGroupNum());
			// Branch ownerBranch = branchMapper.selectByPrimaryKey(branchGroup.getGroupOwnerNum());
			BranchCallGroup branchCallGroup = null;
			if (branch.getCallgroupNum() != 0) {
				branchCallGroup = branchCallGroupMapper.selectByPrimaryKey(branch.getCallgroupNum());
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			resultMap.put("loginDatetime", sdf.format(loginDate));
			resultMap.put("num", employee.getNum());
			resultMap.put("name", employee.getName());
			resultMap.put("login", employee.getLogin());
			resultMap.put("workType", employee.getWorkType());
			resultMap.put("keyphone", employee.getKeyphone());
			resultMap.put("branchNum", branch.getNum());
			resultMap.put("branchName", branch.getName());
			resultMap.put("smsTel", branch.getSmsTel());
			resultMap.put("accountInfo", branch.getAccountInfo());
			resultMap.put("corpNum", branch.getCorpNum());
			resultMap.put("calllinkerIp", branch.getCalllinkerIp());
			resultMap.put("branchCharge", branch.getCharge());
			resultMap.put("internetYn", branch.getInternetYn());
			resultMap.put("branchGroupNum", branchGroup.getNum());
			resultMap.put("branchGroupName", branchGroup.getName());
			resultMap.put("branchGroupOwnerNum", branchGroup.getGroupOwnerNum());
			resultMap.put("branchGroupOwnerPhone", branch.getPhone());
			if (branchCallGroup == null) {
				resultMap.put("branchCallGroupNum", -1);
				resultMap.put("branchCallGroupOwnerNum", -1);
			} else {
				resultMap.put("branchCallGroupNum", branch.getCallgroupNum());
				resultMap.put("branchCallGroupOwnerNum", branchCallGroup.getGroupOwnerNum());
			}
			
			employee.setLogin(1);
			employee.setLoginDatetime(loginDate);
			employee.setRefreshDatetime(loginDate);
			employeeMapper.updateByPrimaryKeySelective(employee);
		}
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> getConnection(int employeeNum, String internetRecvNoti, String internetCardNoti) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null && employee.getDel() != 1) {
			
			Date refreshDatetime = new Date();
			employee.setRefreshDatetime(refreshDatetime);
			employeeMapper.updateByPrimaryKeySelective(employee);
			
			List<Map<String, Object>> messageList = messageExMapper.selectMessage(employee.getId(), -1);
			
			NoticeExample noticeExample = new NoticeExample();
			noticeExample.createCriteria().andTypeEqualTo(1).andNumGreaterThan(employee.getNoticeNum())
					.andBranchNumEqualTo(employee.getBranchNum()).andDelEqualTo(0);
			List<Notice> noticeList = noticeMapper.selectByExample(noticeExample);
			
			Options options = optionsMapper.selectByPrimaryKey(employee.getBranchNum());
			Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
			
			Calendar fromCal = Calendar.getInstance();
			Calendar toCal = Calendar.getInstance();
			fromCal.set(fromCal.get(Calendar.YEAR), fromCal.get(Calendar.MONTH), fromCal.get(Calendar.DAY_OF_MONTH), 0,
					0, 0);
			
			List<Map<String, Object>> internetRecvNotiList = new ArrayList<Map<String, Object>>();
			if ("Y".equals(internetRecvNoti)) {
				ReceiveExample receiveExample = new ReceiveExample();
				receiveExample.createCriteria().andBranchNumEqualTo(employee.getBranchNum()).andStatusEqualTo("인터넷")
						.andPaymentNotEqualTo("카드").andDatetimeBetween(fromCal.getTime(), toCal.getTime());
				List<Receive> receiveList = receiveMapper.selectByExample(receiveExample);
				Map<String, Object> map = new HashMap<String, Object>();
				for (Receive receive : receiveList) {
					map = new HashMap<String, Object>();
					map.put("num", receive.getNum());
					internetRecvNotiList.add(map);
				}
			}
			
			List<Map<String, Object>> internetCardNotiList = new ArrayList<Map<String, Object>>();
			if ("Y".equals(internetCardNoti)) {
				ReceiveExample receiveExample = new ReceiveExample();
				receiveExample.createCriteria().andBranchNumEqualTo(employee.getBranchNum()).andStatusEqualTo("인터넷")
						.andPaymentEqualTo("카드").andDatetimeGreaterThan(fromCal.getTime());
				List<Receive> receiveList = receiveMapper.selectByExample(receiveExample);
				Map<String, Object> map = new HashMap<String, Object>();
				for (Receive receive : receiveList) {
					map = new HashMap<String, Object>();
					map.put("num", receive.getNum());
					internetCardNotiList.add(map);
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			resultMap.put("login", employee.getLogin());
			resultMap.put("loginDatetime", sdf.format(employee.getLoginDatetime()));
			resultMap.put("messageChk", employee.getMessageChk());
			resultMap.put("chkInternet", employee.getChkInternet());
			resultMap.put("keyphone", employee.getKeyphone());
			resultMap.put("charge", employee.getCharge());
			resultMap.put("messageCnt", messageList.size());
			resultMap.put("noticeCnt", noticeList.size());
			resultMap.put("internetYn", branch.getInternetYn());
			resultMap.put("fareChkNight", options.getFareChkNight());
			resultMap.put("fareChkRain", options.getFareChkRain());
			resultMap.put("fareChkHoliday", options.getFareChkHoliday());
			resultMap.put("employeeDistance", options.getEmployeeDistance());
			resultMap.put("employeeDistance2", options.getEmployeeDistance2());
			resultMap.put("employeeCount", options.getEmployeeCount());
			resultMap.put("employeeFare", options.getEmployeeFare());
			resultMap.put("internetRecvNoti", internetRecvNotiList);
			resultMap.put("internetCardNoti", internetCardNotiList);
		}
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> logout(int employeeNum) {
		int resultCode = 9999;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			employee.setLogin(0); // login 1, logout 1
			employee.setLoginDatetime(new Date());
			int updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
			if (updatedRow == 1) {
				resultCode = 1000;
			}
			
			// 해당 사원이 수정 중으로 마킹된 오더를 해제한다
			ReceiveExample receiveExample = new ReceiveExample();
			receiveExample.createCriteria().andBranchNumEqualTo(employee.getBranchNum())
					.andModifyIdEqualTo(employee.getId());
			List<Receive> receiveList = receiveMapper.selectByExample(receiveExample);
			for (Receive receive : receiveList) {
				receive.setModifyId("");
				receive.setModify("");
				receiveMapper.updateByPrimaryKeySelective(receive);
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> delEmployee(int employeeNum) {
		int resultCode = 9999;
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null) {
			if ("최고관리자".equals(employee.getWorkType())) {
				// 최고 관리자는 삭제 할 수 없음
				resultCode = 3001;
			} else {
				employee.setVnum("");
				employee.setDel(1);
				int updatedRow = employeeMapper.updateByPrimaryKeySelective(employee);
				if (updatedRow == 1) {
					resultCode = 1000;
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> delEmployeeCompany(int num) {
		int resultCode = 9999;
		int deletedRow = 0;
		
		EmployeeCompany employeeCompany = employeeCompanyMapper.selectByPrimaryKey(num);
		if (employeeCompany != null) {
			deletedRow = employeeCompanyMapper.deleteByPrimaryKey(num);
		}
		
		if (deletedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public void dailyDeduct(EmpOptions empOptions) {
		Employee employee = employeeMapper.selectByPrimaryKey(empOptions.getEmpNum());
		
		if (employee.getStopChk() == 0 && employee.getStopChk() == 0 && employee.getDel() == 0
				&& StringUtils.isNotBlank(employee.getId())) {
			
			Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
			
			employee.setCharge(employee.getCharge() - empOptions.getDateSub());
			employeeMapper.updateByPrimaryKeySelective(employee);
			
			EmployeeCharge employeeCharge = new EmployeeCharge();
			employeeCharge.setEmployeeNum(employee.getNum());
			employeeCharge.setDatetime(new Date());
			employeeCharge.setMemo(empOptions.getDateSubDesc());
			employeeCharge.setConsign(0);
			employeeCharge.setIn(0);
			employeeCharge.setOut(empOptions.getDateSub());
			employeeCharge.setBalance(employee.getCharge());
			employeeCharge.setReceiveNum(0);
			employeeCharge.setDel(0);
			employeeCharge.setBak(0);
			employeeChargeMapper.insert(employeeCharge);
			
			BranchCharge branchCharge = new BranchCharge();
			branchCharge.setBranchNum(employee.getBranchNum());
			branchCharge.setBranchName(employee.getBranchName()); // TODO : delete
			branchCharge.setDatetime(new Date());
			branchCharge.setMemo("기사 일 차감 내역 : (" + employee.getVnum() + ")" + employee.getName() + "-"
					+ empOptions.getDateSub());
			branchCharge.setConsign(0);
			branchCharge.setIn(0);
			branchCharge.setOut(0);
			branchCharge.setBalance(branch.getCharge());
			branchCharge.setReceiveNum(0);
			branchCharge.setDel(0);
			branchCharge.setBak(0);
			branchChargeMapper.insert(branchCharge);
		}
	}
	
	@Transactional(value = "transactionManagerTotal")
	public void monthlyDeduct(EmpOptions empOptions) {
		Employee employee = employeeMapper.selectByPrimaryKey(empOptions.getEmpNum());
		
		if (employee.getStopChk() == 0 && employee.getStopChk() == 0 && employee.getDel() == 0
				&& StringUtils.isNotBlank(employee.getId())) {
			
			Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
			
			employee.setCharge(employee.getCharge() - empOptions.getMonSub());
			employeeMapper.updateByPrimaryKeySelective(employee);
			
			EmployeeCharge employeeCharge = new EmployeeCharge();
			employeeCharge.setEmployeeNum(employee.getNum());
			employeeCharge.setEmployeeName(employee.getName());
			employeeCharge.setDatetime(new Date());
			employeeCharge.setMemo(empOptions.getMonSubDesc());
			employeeCharge.setConsign(0);
			employeeCharge.setIn(0);
			employeeCharge.setOut(empOptions.getMonSub());
			employeeCharge.setBalance(employee.getCharge());
			employeeCharge.setReceiveNum(0);
			employeeCharge.setDel(0);
			employeeCharge.setBak(0);
			employeeChargeMapper.insert(employeeCharge);
			
			BranchCharge branchCharge = new BranchCharge();
			branchCharge.setBranchNum(employee.getBranchNum());
			branchCharge.setBranchName(employee.getBranchName()); // TODO : delete
			branchCharge.setDatetime(new Date());
			branchCharge.setMemo("기사 월 차감 내역 : (" + employee.getVnum() + ")" + employee.getName() + "-"
					+ empOptions.getMonSub());
			branchCharge.setConsign(0);
			branchCharge.setIn(0);
			branchCharge.setOut(0);
			branchCharge.setBalance(branch.getCharge());
			branchCharge.setReceiveNum(0);
			branchCharge.setDel(0);
			branchCharge.setBak(0);
			branchChargeMapper.insert(branchCharge);
		}
	}
	
	public List<Map<String, Object>> getWithholdingHistory(int branchCallGroupNum, int branchNum, String startMonth,
			String endMonth) {
		List<Map<String, Object>> withholdingList = employeeBranchMapper.selectEmployeeWithholding(branchCallGroupNum,
				branchNum, startMonth, endMonth);
		return withholdingList;
	}
	
	public Workbook getWithholdingHistoryExcel(int branchCallGroupNum, int branchNum, String startMonth, String endMonth) {
		
		List<Map<String, Object>> withholdingList = employeeBranchMapper.selectEmployeeWithholding(branchCallGroupNum,
				branchNum, startMonth, endMonth);
		
		int rowCount = 0;
		int cellCount = 0;
		int maxCellCount = 0;
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(startMonth + "~" + endMonth);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.GREEN.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLUE.index);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		
		// 가로 정렬
		// style.setAlignment((short) 1); // 가로 정렬 왼쪽
		style.setAlignment((short) 2); // 가로 정렬 중간
		// style.setAlignment((short) 3); // 가로 정렬 오른쪽
		
		// 세로 정렬
		// style.setVerticalAlignment((short) 0); // 세로 정렬 상단
		style.setVerticalAlignment((short) 1); // 세로 정렬 중단
		// style.setVerticalAlignment((short) 2); // 세로 정렬 하단
		
		HSSFFont font = wb.createFont();
		font.setFontName("맑은 고딕"); // 폰트 이름
		font.setFontHeightInPoints((short) 11); // 폰트 크기
		// font.setColor(IndexedColors.RED.getIndex()); // 폰트 컬러
		// font.setStrikeout(true); // 글자 가운데 라인
		// font.setItalic(true); // 이탤릭체
		// font.setUnderline(HSSFFont.U_SINGLE); // 밑줄
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 볼드체
		style.setFont(font);
		
		HSSFRow row = sheet.createRow(rowCount++);
		
		HSSFCell cell;
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("지사이름");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("기사번호");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("기사이름");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("주민번호");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("전화번호");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("입사일");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("퇴사일");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("메모");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("은행이름");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("계좌번호");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("출금요청금액");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("원천징수액");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("송금액");
		cell = row.createCell(cellCount++);
		cell.setCellStyle(style);
		cell.setCellValue("송금날짜");
		
		maxCellCount = cellCount;
		cellCount = 0;
		
		DecimalFormat df = new DecimalFormat("#,##0");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String, Object> map : withholdingList) {
			row = sheet.createRow(rowCount++);
			row.createCell(cellCount++).setCellValue((String) map.get("branchName"));
			row.createCell(cellCount++).setCellValue((String) map.get("employeeVnum"));
			row.createCell(cellCount++).setCellValue((String) map.get("employeeName"));
			row.createCell(cellCount++).setCellValue((String) map.get("employeeJumin"));
			row.createCell(cellCount++).setCellValue((String) map.get("employeeId"));
			row.createCell(cellCount++).setCellValue((String) map.get("inDate"));
			row.createCell(cellCount++).setCellValue((String) map.get("outDate"));
			row.createCell(cellCount++).setCellValue((String) map.get("employeeMemo"));
			row.createCell(cellCount++).setCellValue((String) map.get("bankName"));
			row.createCell(cellCount++).setCellValue((String) map.get("account"));
			row.createCell(cellCount++).setCellValue(df.format(map.get("reqAmt")));
			row.createCell(cellCount++).setCellValue(df.format(map.get("withholding")));
			row.createCell(cellCount++).setCellValue(df.format(map.get("sendAmt")));
			row.createCell(cellCount++).setCellValue(sdf.format((Date) map.get("updateDttm")));
			
			cellCount = 0;
		}
		for (int i = 0; i < maxCellCount; i++) {
			sheet.autoSizeColumn(i);
		}
		return wb;
	}
	
	public void employeeLoginCheck() {
		employeeBranchMapper.updateEmployeeLoginCheck();
	}
	
}
