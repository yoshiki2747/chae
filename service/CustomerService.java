package kr.totalcall.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.totalcall.controller.model.CustomerModel;
import kr.totalcall.dao.mapper.CustomerChargeReceiveMapper;
import kr.totalcall.dao.mapper.CustomerCustomerGroupMapper;
import kr.totalcall.dao.mapper.CustomerFavoriteJusoMapper;
import kr.totalcall.dao.mapper.CustomerLoginMapper;
import kr.totalcall.dao.model.CustomerChargeReceive;
import kr.totalcall.dao.model.CustomerCustomerGroup;
import kr.totalcall.dao.model.CustomerLogin;
import kr.totalcall.generateddao.mapper.CustomerChargeMapper;
import kr.totalcall.generateddao.mapper.CustomerFavoriteMapper;
import kr.totalcall.generateddao.mapper.CustomerGroupChargeMapper;
import kr.totalcall.generateddao.mapper.CustomerGroupMapper;
import kr.totalcall.generateddao.mapper.CustomerMapper;
import kr.totalcall.generateddao.mapper.JusoMapper;
import kr.totalcall.generateddao.mapper.ReceiveMapper;
import kr.totalcall.generateddao.model.Customer;
import kr.totalcall.generateddao.model.CustomerCharge;
import kr.totalcall.generateddao.model.CustomerChargeExample;
import kr.totalcall.generateddao.model.CustomerExample;
import kr.totalcall.generateddao.model.CustomerFavorite;
import kr.totalcall.generateddao.model.CustomerGroup;
import kr.totalcall.generateddao.model.CustomerGroupCharge;
import kr.totalcall.generateddao.model.CustomerGroupExample;
import kr.totalcall.generateddao.model.Juso;
import kr.totalcall.generateddao.model.JusoExample;
import kr.totalcall.util.ObjectUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService extends CommonService {
	
	@Autowired
	private CustomerGroupService customerGroupService;
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private CustomerGroupMapper customerGroupMapper;
	
	@Autowired
	private CustomerCustomerGroupMapper customerCustomerGroupMapper;
	
	@Autowired
	private CustomerChargeMapper customerChargeMapper;
	
	@Autowired
	private CustomerChargeReceiveMapper customerChargeReceiveMapper;
	
	@Autowired
	private CustomerGroupChargeMapper customerGroupChargeMapper;
	
	@Autowired
	private CustomerFavoriteMapper customerFavoriteMapper;
	
	@Autowired
	private CustomerFavoriteJusoMapper customerFavoriteJusoMapper;
	
	@Autowired
	private CustomerLoginMapper customerLoginMapper;
	
	@Autowired
	private ReceiveMapper receiveMapper;
	
	@Autowired
	private JusoMapper jusoMapper;
	
	public Map<String, Object> setCustomer(CustomerModel model) {
		int resultCode = 9999;
		int customerNum = 0;
		
		// 아이디가 공백인 경우는 같은 아이디가 있는지 검사하지 않는다.
		List<Customer> customerList = new ArrayList<Customer>();
		if (StringUtils.isNotBlank(model.getId())) {
			CustomerExample customerExample = new CustomerExample();
			customerExample.createCriteria().andIdEqualTo(model.getId()).andBranchNumEqualTo(model.getBranchNum())
					.andDelEqualTo(0);
			customerList = customerMapper.selectByExample(customerExample);
		}
		
		if (customerList.size() > 0) {
			resultCode = 3001;
		} else {
			Juso juso = null;
			if (model.getJusoNum() != null && model.getJusoNum() > 0) {
				juso = jusoMapper.selectByPrimaryKey(model.getJusoNum());
			} else {
				JusoExample jusoExample = new JusoExample();
				jusoExample.createCriteria().andSidoEqualTo(model.getSido()).andGugunEqualTo(model.getGugun())
						.andDongEqualTo(model.getDong()).andDelEqualTo(0);
				List<Juso> jusoList = jusoMapper.selectByExample(jusoExample);
				if (jusoList.size() > 0) {
					juso = jusoList.get(0);
				}
			}
			
			int groupNum = 0;
			int credit = 0;
			int approveValue = 0;
			// 기업 고객이 직접 회원가입을 할 경우 groupNum을 매칭 시켜줌
			// credit 값을 외상으로 저장하게 처리
			if ("후지제록스2016".equals(model.getCname())) {
				CustomerGroupExample customerGroupExample = new CustomerGroupExample();
				customerGroupExample.createCriteria().andNameEqualTo(model.getCname()).andDelEqualTo(0);
				List<CustomerGroup> customerGroupList = customerGroupMapper.selectByExample(customerGroupExample);
				if (customerGroupList.size() > 0) {
					CustomerGroup customerGroup = customerGroupList.get(0);
					groupNum = customerGroup.getNum();
				}
				credit = 1;
				approveValue = 1;
			} else {
				groupNum = model.getGroupNum();
				credit = model.getCredit();
			}
			
			Customer customer = new Customer();
			customer.setBranchNum(model.getBranchNum());
			customer.setGroupNum(groupNum);
			customer.setCname(model.getCname());
			customer.setDepartment(model.getDepartment());
			customer.setPerson(model.getPerson());
			customer.setTel(model.getTel());
			customer.setPhone(model.getPhone());
			customer.setLine(model.getLine());
			customer.setTelephone(model.getTelephone());
			customer.setId(model.getId());
			customer.setPw(model.getPw());
			
			// 엑셀 입력시 주소번호 확인이 불가능
			if (juso != null) {
				customer.setStartJusoNum(juso.getNum());
				customer.setStartSido(juso.getSido());
				customer.setStartGugun(juso.getGugun());
				customer.setStartDong(juso.getDong());
				if (StringUtils.isBlank(model.getPosition())) {
					customer.setStartPosition(juso.getLatitude() + "|" + juso.getLongitude());
				}
			} else {
				customer.setStartSido(model.getSido());
				customer.setStartGugun(model.getGugun());
				customer.setStartDong(model.getDong());
			}
			customer.setStartDongNum(model.getDongNum());
			if (StringUtils.isNotBlank(model.getPosition()) && model.getPosition().indexOf("|") > -1) {
				customer.setStartPosition(model.getPosition());
			}
			customer.setStartBuilding(model.getBuilding());
			customer.setStartFloor(model.getFloor());
			customer.setStartEtc(model.getEtc());
			customer.setStartDetail(model.getDetail());
			customer.setCredit(credit);
			customer.setMemo(model.getMemo());
			customer.setDriverMemo(model.getDriverMemo());
			customer.setAccount(model.getAccount());
			customer.setCardNum(model.getCardNum());
			customer.setCardNotiPhone(model.getCardNotiPhone());
			customer.setCardNotiEmail(model.getCardNotiEmail());
			if (credit == 1) {
				customer.setMileageUseYn("N");
				customer.setSaveOver(0);
				customer.setSave(0);
				customer.setSaveName("%");
			} else {
				customer.setMileageUseYn("Y");
				customer.setSaveOver(7000);
				customer.setSave(10);
				customer.setSaveName("%");
			}
			customer.setSms1(model.getSms1());
			customer.setSms2(model.getSms2());
			customer.setSms3(model.getSms3());
			customer.setSms4(model.getSms4());
			customer.setSms5(model.getSms5());
			customer.setSms6(model.getSms6());
			customer.setSms7(model.getSms7());
			customer.setSms8(model.getSms8());
			customer.setSms9(model.getSms9());
			customer.setLtnuSmsUseYn(model.getLtnuSmsUseYn());
			customer.setBak(approveValue);
			
			int insertedRow = customerMapper.insertSelective(customer);
			customerNum = customer.getNum();
			if (insertedRow > 0) {
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("customerNum", customerNum);
		
		return resultMap;
	}
	
	public Map<String, Object> setCustomerByGroup(CustomerModel model) {
		int resultCode = 9999;
		int customerNum = 0;
		
		// 아이디가 공백인 경우는 같은 아이디가 있는지 검사하지 않는다.
		List<Customer> customerList = new ArrayList<Customer>();
		if (StringUtils.isNotBlank(model.getId())) {
			CustomerExample customerExample = new CustomerExample();
			customerExample.createCriteria().andIdEqualTo(model.getId()).andBranchNumEqualTo(model.getBranchNum())
					.andDelEqualTo(0);
			customerList = customerMapper.selectByExample(customerExample);
		}
		
		if (customerList.size() > 0) {
			resultCode = 3001;
		} else {
			Juso juso = null;
			if (model.getJusoNum() != null && model.getJusoNum() > 0) {
				juso = jusoMapper.selectByPrimaryKey(model.getJusoNum());
			} else {
				JusoExample jusoExample = new JusoExample();
				jusoExample.createCriteria().andSidoEqualTo(model.getSido()).andGugunEqualTo(model.getGugun())
						.andDongEqualTo(model.getDong()).andDelEqualTo(0);
				List<Juso> jusoList = jusoMapper.selectByExample(jusoExample);
				if (jusoList.size() > 0) {
					juso = jusoList.get(0);
				}
			}
			
			CustomerExample customerExample = new CustomerExample();
			customerExample.createCriteria().andGroupNumEqualTo(model.getGroupNum()).andDelEqualTo(0);
			customerExample.setOrderByClause("num");
			List<Customer> customerListInGroup = customerMapper.selectByExample(customerExample);
			
			Customer customer = new Customer();
			customer.setBranchNum(model.getBranchNum());
			customer.setGroupNum(model.getGroupNum());
			customer.setCname(model.getCname());
			customer.setDepartment(model.getDepartment());
			customer.setPerson(model.getPerson());
			customer.setTel(model.getTel());
			customer.setPhone(model.getPhone());
			customer.setLine("");
			customer.setTelephone("");
			customer.setId(model.getId());
			customer.setPw(model.getPw());
			// 엑셀 입력시 주소번호 확인이 불가능
			if (juso != null) {
				customer.setStartJusoNum(juso.getNum());
				customer.setStartSido(juso.getSido());
				customer.setStartGugun(juso.getGugun());
				customer.setStartDong(juso.getDong());
				if (StringUtils.isBlank(model.getPosition())) {
					customer.setStartPosition(juso.getLatitude() + "|" + juso.getLongitude());
				}
			} else {
				customer.setStartSido(model.getSido());
				customer.setStartGugun(model.getGugun());
				customer.setStartDong(model.getDong());
			}
			customer.setStartDongNum("");
			if (StringUtils.isNotBlank(model.getPosition()) && model.getPosition().indexOf("|") > -1) {
				customer.setStartPosition(model.getPosition());
			}
			customer.setStartBuilding("");
			customer.setStartFloor("");
			customer.setStartEtc(model.getEtc());
			customer.setStartDetail(model.getDetail());
			customer.setMemo("");
			customer.setDriverMemo("");
			customer.setAccount("");
			customer.setCardNum(model.getCardNum());
			customer.setCardNotiPhone(model.getCardNotiPhone());
			customer.setCardNotiEmail(model.getCardNotiEmail());
			customer.setGroupMileageYn("Y");
			if (customerListInGroup.size() > 0) {
				Customer customerInGroup = customerListInGroup.get(0);
				customer.setCredit(customerInGroup.getCredit());
				customer.setMileageUseYn(customerInGroup.getMileageUseYn());
				customer.setSaveOver(customerInGroup.getSaveOver());
				customer.setSave(customerInGroup.getSave());
				customer.setSaveName(customerInGroup.getSaveName());
			} else {
				customer.setCredit(0);
				customer.setMileageUseYn("Y");
				customer.setSaveOver(7000);
				customer.setSave(10);
				customer.setSaveName("%");
			}
			customer.setSms1(0);
			customer.setSms2(0);
			customer.setSms3(0);
			customer.setSms4(0);
			customer.setSms5(0);
			customer.setSms6(0);
			customer.setSms7(0);
			customer.setSms8(0);
			customer.setSms9(0);
			customer.setLtnuSmsUseYn("N");
			int insertedRow = customerMapper.insertSelective(customer);
			customerNum = customer.getNum();
			if (insertedRow > 0) {
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("customerNum", customerNum);
		
		return resultMap;
	}
	
	public Map<String, Object> setFavorite(int customerNum, String cname, String department, String person, String tel,
			String phone, int jusoNum, String detail) {
		
		int resultCode = 9999;
		
		CustomerFavorite customerFavorite = new CustomerFavorite();
		customerFavorite.setCustomerNum(customerNum);
		customerFavorite.setCname(cname);
		customerFavorite.setDepartment(department);
		customerFavorite.setPerson(person);
		customerFavorite.setTel(tel);
		customerFavorite.setPhone(phone);
		customerFavorite.setJusoNum(jusoNum);
		customerFavorite.setDetail(detail);
		
		int insertedRow = 0;
		insertedRow = customerFavoriteMapper.insertSelective(customerFavorite);
		
		if (insertedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		
		return resultMap;
	}
	
	public Customer getCustomer(int num) {
		Customer customer = customerMapper.selectByPrimaryKey(num);
		return customer;
	}
	
	public Map<String, Object> loginCustomer(String loginId, String loginPw, int loginType, int branchNum) {
		CustomerLogin customerLogin = customerLoginMapper.selectCustomerLogin(loginId, loginPw, loginType, branchNum);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (customerLogin != null) {
			resultMap = ObjectUtil.getMapToElement(customerLogin);
		}
		return resultMap;
	}
	
	public Map<String, Object> approveCustomer(int customerNum) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		if (customer != null) {
			if (customer.getBak() == 1) {
				customer.setBak(0);
				updatedRow = customerMapper.updateByPrimaryKeySelective(customer);
			} else {
				// 이미 승인
				resultCode = 3002;
			}
		} else {
			// 고객 없음
			resultCode = 3001;
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		
		return resultMap;
	}
	
	public List<Map<String, Object>> searchCompanyCustomer(String reportType, String searchWord) {
		List<CustomerCustomerGroup> customerList = customerCustomerGroupMapper.selectCustomerByReportType(reportType,
				searchWord);
		
		List<Map<String, Object>> resultList = ObjectUtil.getMapListToElementList(customerList);
		if (resultList.size() == 0) {
			resultList = new ArrayList<Map<String, Object>>();
		}
		return resultList;
	}
	
	public Map<String, Object> getCustomerByNum(int num) {
		Customer customer = customerMapper.selectByPrimaryKey(num);
		
		Map<String, Object> resultMap = ObjectUtil.getMapToElement(customer);
		return resultMap;
	}
	
	public Customer getCustomerById(String clientId) {
		CustomerExample example = new CustomerExample();
		example.createCriteria().andIdEqualTo(clientId).andDelEqualTo(0);
		List<Customer> customerList = customerMapper.selectByExample(example);
		Customer clientCustomer = null;
		if (customerList.size() == 1) {
			clientCustomer = customerList.get(0);
		}
		return clientCustomer;
	}
	
	public List<Map<String, Object>> getCustomerByGroupNum(int groupNum) {
		List<CustomerCustomerGroup> customerCustomerGroupList = customerCustomerGroupMapper
				.selectCustomerByGroupNum(groupNum);
		
		List<Map<String, Object>> resultList = null;
		resultList = ObjectUtil.getMapListToElementList(customerCustomerGroupList);
		return resultList;
	}
	
	public List<Map<String, Object>> getCustomerByGroupId(int branchNum, String groupId) {
		CustomerGroupExample customerGroupExample = new CustomerGroupExample();
		customerGroupExample.createCriteria().andBranchNumEqualTo(branchNum).andIdEqualTo(groupId).andDelEqualTo(0);
		List<CustomerGroup> customerGroupList = customerGroupMapper.selectByExample(customerGroupExample);
		CustomerGroup customerGroup = null;
		if (customerGroupList.size() == 1) {
			customerGroup = customerGroupList.get(0);
		}
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		if (customerGroup != null) {
			CustomerExample customerExample = new CustomerExample();
			customerExample.createCriteria().andGroupNumEqualTo(customerGroup.getNum()).andDelEqualTo(0);
			List<Customer> customerList = customerMapper.selectByExample(customerExample);
			
			mapList = ObjectUtil.getMapListToElementList(customerList);
		}
		
		return mapList;
	}
	
	public List<Map<String, Object>> getCustomerByAdminNum(int adminNum) {
		List<CustomerCustomerGroup> customerCustomerGroupList = customerCustomerGroupMapper
				.selectCustomerByAdminNum(adminNum);
		
		List<Map<String, Object>> resultList = ObjectUtil.getMapListToElementList(customerCustomerGroupList);
		return resultList;
	}
	
	public List<Map<String, Object>> searchCustomerByPhone(int branchNum, String phoneNum) {
		String subPhoneNum = phoneNum;
		String locNum = phoneNum.substring(0, 2);
		int locNumResult = -1;
		if ("02".equals(locNum)) { // 서울
			locNumResult = 2;
		} else {
			locNum = phoneNum.substring(0, 3);
			if ("051".equals(locNum) // 부산
					|| "053".equals(locNum) // 대구
					|| "032".equals(locNum) // 인천
					|| "062".equals(locNum) // 광주
					|| "042".equals(locNum) // 대전
					|| "052".equals(locNum) // 울산
					|| "044".equals(locNum) // 세종
					|| "031".equals(locNum) // 경기
					|| "033".equals(locNum) // 강원
					|| "043".equals(locNum) // 충북
					|| "041".equals(locNum) // 충남
					|| "063".equals(locNum) // 전북
					|| "061".equals(locNum) // 전남
					|| "054".equals(locNum) // 경북
					|| "055".equals(locNum) // 경남
					|| "064".equals(locNum)) { // 제주
				locNumResult = 3;
			}
		}
		
		if (locNumResult != -1) {
			subPhoneNum = subPhoneNum.substring(locNumResult);
		}
		
		phoneNum = phoneNum.replace("-", "");
		subPhoneNum = subPhoneNum.replace("-", "");
		
		List<CustomerCustomerGroup> customerCustomerGroupList = customerCustomerGroupMapper.selectCustomerByPhone(
				branchNum, phoneNum, subPhoneNum);
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		resultList = ObjectUtil.getMapListToElementList(customerCustomerGroupList);
		return resultList;
	}
	
	public String getReportType(int customerNum) {
		String reportType = "";
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		if (customer != null && customer.getGroupNum() > 0) {
			reportType = customerGroupService.getReportType(customer.getGroupNum());
		}
		return reportType;
	}
	
	public Map<String, Object> getCustomerReportType(int customerNum) {
		String reportType = "";
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		if (customer != null && customer.getGroupNum() > 0) {
			reportType = customerGroupService.getReportType(customer.getGroupNum());
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("reportType", reportType);
		return resultMap;
	}
	
	public List<Map<String, Object>> searchCustomer(int branchNum, String searchWord) {
		int wordLength = searchWord.length();
		String subPhoneNum = searchWord;
		String locNum = "";
		if (wordLength > 2) {
			locNum = searchWord.substring(0, 2);
		}
		
		int locNumResult = -1;
		if ("02".equals(locNum)) { // 서울
			locNumResult = 2;
		} else {
			if (wordLength > 3) {
				locNum = searchWord.substring(0, 3);
				if ("051".equals(locNum) // 부산
						|| "053".equals(locNum) // 대구
						|| "032".equals(locNum) // 인천
						|| "062".equals(locNum) // 광주
						|| "042".equals(locNum) // 대전
						|| "052".equals(locNum) // 울산
						|| "044".equals(locNum) // 세종
						|| "031".equals(locNum) // 경기
						|| "033".equals(locNum) // 강원
						|| "043".equals(locNum) // 충북
						|| "041".equals(locNum) // 충남
						|| "063".equals(locNum) // 전북
						|| "061".equals(locNum) // 전남
						|| "054".equals(locNum) // 경북
						|| "055".equals(locNum) // 경남
						|| "064".equals(locNum)) { // 제주
					locNumResult = 3;
				}
			}
		}
		
		if (locNumResult != -1) {
			subPhoneNum = subPhoneNum.substring(locNumResult);
		}
		
		List<CustomerCustomerGroup> customerCustomerGroupList = customerCustomerGroupMapper.searchCustomer(branchNum,
				searchWord, subPhoneNum);
		
		List<Map<String, Object>> resultList = null;
		resultList = ObjectUtil.getMapListToElementList(customerCustomerGroupList);
		return resultList;
	}
	
	public List<Map<String, Object>> searchCustomerByCall(int branchNum, String call) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String, Object> getMileage(int customerNum) {
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (customer != null) {
			resultMap.put("mileage", customer.getMileage());
		} else {
			resultMap.put("mileage", 0);
		}
		return resultMap;
	}
	
	public List<Map<String, Object>> getCustomerInGroup(int masterNum, int adminNum, int groupNum, String searchWord) {
		List<Map<String, Object>> customerList = customerCustomerGroupMapper.selectCustomerInGroup(masterNum, adminNum,
				groupNum, searchWord);
		return customerList;
	}
	
	public Map<String, Object> getChargeHistory(int customerNum) {
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (customer != null) {
			resultMap.put("mileage", customer.getMileage());
			resultMap.put("account", customer.getAccount());
			
			List<CustomerChargeReceive> customerChargeList = customerChargeReceiveMapper
					.selectCustomerCharge(customerNum);
			resultMap.put("customerChargeList", customerChargeList);
		} else {
			resultMap.put("mileage", 0);
			resultMap.put("account", "");
			resultMap.put("customerChargeList", new ArrayList<Map<String, Object>>());
		}
		
		return resultMap;
	}
	
	public List<Map<String, Object>> getFavorite(int customerNum) {
		List<Map<String, Object>> customerFavoriteList = customerFavoriteJusoMapper.selectFavorite(customerNum);
		return customerFavoriteList;
	}
	
	public Map<String, Object> checkExistCustomer(String tel, String pw, int branchNum) {
		CustomerExample customerExample = new CustomerExample();
		customerExample.createCriteria().andTelEqualTo(tel).andPwEqualTo(pw).andBranchNumEqualTo(branchNum);
		customerExample.setOrderByClause("num DESC");
		List<Customer> customerList = customerMapper.selectByExample(customerExample);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (customerList.size() > 0) {
			resultMap = ObjectUtil.getMapToElement(customerList.get(0));
		}
		
		return resultMap;
	}
	
	/**
	 * 그룹 사용 한도를 사용하는지 확인
	 * 
	 * @param customerNum
	 *            고객번호
	 * @return customerGroupNum 그룹사용한도를 사용하면 그룹번호, 아니면 0
	 */
	public int checkGroupLimit(int customerNum) {
		int customerGroupNum = 0;
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		if (customer != null && customer.getGroupNum() > 0) {
			CustomerGroup customerGroup = customerGroupMapper.selectByPrimaryKey(customer.getGroupNum());
			if (customerGroup != null && "Y".equals(customerGroup.getLimitYn())) {
				customerGroupNum = customerGroup.getNum();
			}
		}
		return customerGroupNum;
	}
	
	public Map<String, Object> modCustomer(CustomerModel model) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		// 아이디가 공백인 경우는 같은 아이디가 있는지 검사하지 않는다.
		List<Customer> customerList = new ArrayList<Customer>();
		if (StringUtils.isNotBlank(model.getId())) {
			CustomerExample customerExample = new CustomerExample();
			customerExample.createCriteria().andNumNotEqualTo(model.getNum()).andIdEqualTo(model.getId())
					.andBranchNumEqualTo(model.getBranchNum()).andDelEqualTo(0);
			customerList = customerMapper.selectByExample(customerExample);
		}
		
		if (customerList.size() > 0) {
			resultCode = 3001;
		} else {
			Customer customer = customerMapper.selectByPrimaryKey(model.getNum());
			if (customer != null) {
				// customer.setBranchNum(model.getBranchNum());
				// customer.setGroupNum(model.getGroupNum());
				customer.setCname(model.getCname());
				customer.setDepartment(model.getDepartment());
				customer.setPerson(model.getPerson());
				customer.setTel(model.getTel());
				customer.setPhone(model.getPhone());
				customer.setLine(model.getLine());
				customer.setTelephone(model.getTelephone());
				customer.setId(model.getId());
				customer.setPw(model.getPw());
				customer.setStartJusoNum(model.getJusoNum());
				customer.setStartSido(model.getSido());
				customer.setStartGugun(model.getGugun());
				customer.setStartDong(model.getDong());
				customer.setStartDongNum(model.getDongNum());
				customer.setStartPosition(model.getPosition());
				customer.setStartBuilding(model.getBuilding());
				customer.setStartFloor(model.getFloor());
				customer.setStartEtc(model.getEtc());
				customer.setStartDetail(model.getDetail());
				customer.setCredit(model.getCredit());
				customer.setMemo(model.getMemo());
				customer.setDriverMemo(model.getDriverMemo());
				customer.setAccount(model.getAccount());
				customer.setCardNum(model.getCardNum());
				customer.setCardNotiPhone(model.getCardNotiPhone());
				customer.setCardNotiEmail(model.getCardNotiEmail());
				customer.setSms1(model.getSms1());
				customer.setSms2(model.getSms2());
				customer.setSms3(model.getSms3());
				customer.setSms4(model.getSms4());
				customer.setSms5(model.getSms5());
				customer.setSms6(model.getSms6());
				customer.setSms7(model.getSms7());
				customer.setSms8(model.getSms8());
				customer.setSms9(model.getSms9());
				customer.setLtnuSmsUseYn(model.getLtnuSmsUseYn());
				customer.setBlacklistYn(model.getBlacklistYn());
				updatedRow = customerMapper.updateByPrimaryKeySelective(customer);
			}
		}
		
		if (updatedRow > 0) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("customerNum", model.getNum());
		
		return resultMap;
	}
	
	public Map<String, Object> modInternetCustomer(int num, String cname, String department, String person, String tel,
			String phone, String pw, int jusoNum, String sido, String gugun, String dong, String position, String etc,
			String detail, String account, String cardNum, String cardNotiPhone, String cardNotiEmail) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Customer customer = customerMapper.selectByPrimaryKey(num);
		if (customer != null) {
			customer.setCname(cname);
			customer.setDepartment(department);
			customer.setPerson(person);
			customer.setTel(tel);
			customer.setPhone(phone);
			customer.setPw(pw);
			customer.setStartJusoNum(jusoNum);
			customer.setStartSido(sido);
			customer.setStartGugun(gugun);
			customer.setStartDong(dong);
			customer.setStartPosition(position);
			customer.setStartEtc(etc);
			customer.setStartDetail(detail);
			customer.setAccount(account);
			customer.setCardNum(cardNum);
			customer.setCardNotiPhone(cardNotiPhone);
			customer.setCardNotiEmail(cardNotiPhone);
			updatedRow = customerMapper.updateByPrimaryKeySelective(customer);
		}
		
		if (updatedRow > 0) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	public Map<String, Object> modSelectionCustomer(List<Integer> customerNumArr, String cname, String department,
			String person, String tel, int startJusoNum, String startSido, String startGugun, String startDong,
			String startDongNum, String startPosition, String startEtc, String startDetail, int credit, String memo,
			String driverMemo, String modifyField) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Customer customer = null;
		for (Integer customerNum : customerNumArr) {
			customer = customerMapper.selectByPrimaryKey(customerNum);
			if (modifyField.indexOf("cname") > -1) {
				customer.setCname(cname);
			}
			if (modifyField.indexOf("department") > -1) {
				customer.setDepartment(department);
			}
			if (modifyField.indexOf("person") > -1) {
				customer.setPerson(person);
			}
			if (modifyField.indexOf("tel") > -1) {
				customer.setTel(tel);
			}
			if (modifyField.indexOf("startJuso") > -1) {
				customer.setStartJusoNum(startJusoNum);
				customer.setStartSido(startSido);
				customer.setStartGugun(startGugun);
				customer.setStartDong(startDong);
				customer.setStartPosition(startPosition);
				customer.setStartDongNum(startDongNum);
			}
			if (modifyField.indexOf("startEtc") > -1) {
				customer.setStartEtc(startEtc);
			}
			if (modifyField.indexOf("startDetail") > -1) {
				customer.setStartDetail(startDetail);
			}
			if (modifyField.indexOf("credit") > -1) {
				customer.setCredit(credit);
			}
			if (modifyField.indexOf("memo") > -1) {
				customer.setMemo(memo);
			}
			if (modifyField.indexOf("driverMemo") > -1) {
				customer.setDriverMemo(driverMemo);
			}
			
			updatedRow += customerMapper.updateByPrimaryKeySelective(customer);
		}
		
		if (updatedRow == customerNumArr.size()) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	public Map<String, Object> bindGroup(List<Integer> customerNumList, int groupNum) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Customer customer;
		for (int customerNum : customerNumList) {
			customer = customerMapper.selectByPrimaryKey(customerNum);
			customer.setGroupNum(groupNum);
			customer.setGroupId("");
			updatedRow += customerMapper.updateByPrimaryKeySelective(customer);
		}
		
		if (customerNumList.size() == updatedRow) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	public Map<String, Object> modCardInfo(int customerNum, String cardNum, String cardPhone, String cardEmail) {
		int resultCode = 9999;
		
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		customer.setCardNum(cardNum);
		customer.setCardNotiPhone(cardPhone);
		customer.setCardNotiEmail(cardEmail);
		int updatedRow = customerMapper.updateByPrimaryKeySelective(customer);
		
		if (updatedRow > 0) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> requestMileage(int customerNum, int requestAmount, String account) {
		int resultCode = 9999;
		int updatedRow = 0;
		int insertedRow = 0;
		int resultMileage = 0;
		
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		if (customer != null) {
			if (customer.getMileage() < requestAmount) {
				resultCode = 3002; // 적립 잔액 부족
			} else {
				resultMileage = customer.getMileage() - requestAmount;
				
				CustomerCharge customerCharge = new CustomerCharge();
				customerCharge.setCustomerNum(customerNum);
				customerCharge.setReceiveNum(0);
				customerCharge.setResult(0);
				customerCharge.setStartDong("");
				customerCharge.setEndDong("");
				customerCharge.setMemo(account);
				customerCharge.setFare(0);
				customerCharge.setMileage(0);
				customerCharge.setAdd(0);
				customerCharge.setRequest(requestAmount);
				customerCharge.setIn(0);
				customerCharge.setOut(0);
				customerCharge.setBalance(resultMileage);
				customerCharge.setDatetime(new Date());
				customerCharge.setDel(0);
				customerCharge.setBak(0);
				insertedRow = customerChargeMapper.insert(customerCharge);
				
				customer.setMileage(resultMileage);
				updatedRow = customerMapper.updateByPrimaryKeySelective(customer);
			}
		} else {
			resultCode = 3001;
		}
		
		if (updatedRow > 0) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("insertedRow", insertedRow);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	public Map<String, Object> transferMileage(List<Map<String, Object>> customerList) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		for (Map<String, Object> customerObj : customerList) {
			int customerNum = (Integer) customerObj.get("customerNum");
			String groupMileageYn = (String) customerObj.get("groupMileageYn");
			updatedRow += modCustomerMileage(customerNum, groupMileageYn);
		}
		
		if (updatedRow == customerList.size()) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public int modCustomerMileage(int customerNum, String groupMileageYn) {
		int updatedRow = 0;
		Customer customer;
		customer = customerMapper.selectByPrimaryKey(customerNum);
		if (customer != null) {
			if ("N".equals(groupMileageYn)) {
				customer.setGroupMileageYn("N");
				updatedRow += customerMapper.updateByPrimaryKeySelective(customer);
			} else {
				// 마일리지 출금 요청한게 있다면 돌려줌
				CustomerChargeExample customerChargeExample = new CustomerChargeExample();
				customerChargeExample.createCriteria().andCustomerNumEqualTo(customer.getNum()).andResultEqualTo(0);
				List<CustomerCharge> customerChargeList = customerChargeMapper.selectByExample(customerChargeExample);
				
				for (CustomerCharge customerCharge : customerChargeList) {
					// customerCharge result 1로 변경
					customerCharge.setResult(1);
					customerChargeMapper.updateByPrimaryKeySelective(customerCharge);
					
					// customer에 마일리지 반환
					customer.setMileage(customer.getMileage() + customerCharge.getRequest());
					customerMapper.updateByPrimaryKeySelective(customer);
					
					// customer에 마일리지 반환 charge insert
					customerCharge = new CustomerCharge();
					customerCharge.setCustomerNum(customer.getNum());
					customerCharge.setReceiveNum(0);
					customerCharge.setResult(1);
					customerCharge.setStartDong("");
					customerCharge.setEndDong("");
					customerCharge.setMemo("그룹이관에 따른 마일리지 요청 반환");
					customerCharge.setFare(0);
					customerCharge.setMileage(0);
					customerCharge.setAdd(0);
					customerCharge.setRequest(0);
					customerCharge.setIn(customerCharge.getRequest());
					customerCharge.setOut(0);
					customerCharge.setBalance(customer.getMileage());
					customerCharge.setDatetime(new Date());
					customerCharge.setDel(0);
					customerCharge.setBak(0);
					customerChargeMapper.insert(customerCharge);
				}
				
				CustomerGroup customerGroup = customerGroupMapper.selectByPrimaryKey(customer.getGroupNum());
				// customerGroup 마일리지에 customer의 마일리지를 더함
				customerGroup.setGroupMileage(customerGroup.getGroupMileage() + customer.getMileage());
				customerGroupMapper.updateByPrimaryKeySelective(customerGroup);
				
				// customerGroupcharge insert
				CustomerGroupCharge customerGroupCharge = new CustomerGroupCharge();
				customerGroupCharge.setGroupNum(customerGroup.getNum());
				customerGroupCharge.setGroupId(customerGroup.getId());
				customerGroupCharge.setCustomerNum(customer.getNum());
				customerGroupCharge.setReceiveNum(0);
				customerGroupCharge.setResult(1);
				customerGroupCharge.setMemo("그룹마일리지로 이관");
				customerGroupCharge.setStartDong("");
				customerGroupCharge.setEndDong("");
				customerGroupCharge.setFare(0);
				customerGroupCharge.setMileage(0);
				customerGroupCharge.setAdd(0);
				customerGroupCharge.setRequest(0);
				customerGroupCharge.setIn(customer.getMileage());
				customerGroupCharge.setOut(0);
				customerGroupCharge.setBalance(customerGroup.getGroupMileage());
				customerGroupCharge.setDatetime(new Date());
				customerGroupCharge.setDel(0);
				customerGroupCharge.setBak(0);
				customerGroupChargeMapper.insert(customerGroupCharge);
				
				// customer의 마일리지를 0으로 변경
				int customerMileage = customer.getMileage();
				customer.setGroupMileageYn("Y");
				customer.setMileage(0);
				customer.setMovGroupmileage(customerMileage);
				updatedRow = customerMapper.updateByPrimaryKeySelective(customer);
				
				// customer의 마일리지 0으로 변경 charge insert
				CustomerCharge customerCharge = new CustomerCharge();
				customerCharge.setCustomerNum(customer.getNum());
				customerCharge.setReceiveNum(0);
				customerCharge.setResult(1);
				customerCharge.setStartDong("");
				customerCharge.setEndDong("");
				customerCharge.setMemo("그룹마일리지로 이관");
				customerCharge.setFare(0);
				customerCharge.setMileage(0);
				customerCharge.setAdd(0);
				customerCharge.setRequest(0);
				customerCharge.setIn(0);
				customerCharge.setOut(customerMileage);
				customerCharge.setBalance(0);
				customerCharge.setDatetime(new Date());
				customerCharge.setDel(0);
				customerCharge.setBak(0);
				customerChargeMapper.insert(customerCharge);
			}
		}
		
		return updatedRow;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modMileage(int customerNum, String mileageUseYn, String account, int saveOver, int save,
			String saveName, int deposit, int withdraw, String memberId) {
		int resultCode = 9999;
		int updatedRow = 0;
		int insertedRow = 0;
		
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		if (customer != null) {
			int mileage = 0;
			
			mileage = customer.getMileage() + deposit - withdraw;
			customer.setMileageUseYn(mileageUseYn);
			customer.setAccount(account);
			customer.setSaveOver(saveOver);
			customer.setSave(save);
			customer.setSaveName(saveName);
			customer.setMileage(mileage);
			
			updatedRow = customerMapper.updateByPrimaryKeySelective(customer);
			
			if (deposit > 0 || withdraw > 0) {
				String message = "";
				if (deposit != 0) {
					message = "충전 : " + deposit + "원 (" + memberId + ")";
				}
				if (withdraw != 0) {
					message = "차감 : " + withdraw + "원 (" + memberId + ")";
				}
				CustomerCharge customerCharge = new CustomerCharge();
				customerCharge.setCustomerNum(customerNum);
				customerCharge.setReceiveNum(0);
				customerCharge.setResult(1);
				customerCharge.setStartDong("");
				customerCharge.setEndDong("");
				customerCharge.setMemo(message);
				customerCharge.setFare(0);
				customerCharge.setMileage(0);
				customerCharge.setAdd(0);
				customerCharge.setRequest(0);
				customerCharge.setIn(deposit);
				customerCharge.setOut(withdraw);
				customerCharge.setBalance(mileage);
				customerCharge.setDatetime(new Date());
				customerCharge.setDel(0);
				customerCharge.setBak(0);
				insertedRow = customerChargeMapper.insert(customerCharge);
			}
		} else {
			resultCode = 3001;
		}
		
		if (updatedRow > 0) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		resultMap.put("insertedRow", insertedRow);
		
		return resultMap;
	}
	
	public Map<String, Object> addTelephone(int customerNum, String telephone) {
		int resultCode = 9999;
		
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		
		if (customer.getTelephone() == null || customer.getTelephone().length() == 0) {
			customer.setTelephone(telephone);
		} else {
			customer.setTelephone(customer.getTelephone() + "|" + telephone);
		}
		
		int updatedRow = customerMapper.updateByPrimaryKeyWithBLOBs(customer);
		
		if (updatedRow > 0) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	public Map<String, Object> delCustomer(int customerNum) {
		int resultCode = 9999;
		
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		customer.setDel(1);
		int updatedRow = customerMapper.updateByPrimaryKeyWithBLOBs(customer);
		
		if (updatedRow > 0) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	public Map<String, Object> removeCustomer(int customerNum) {
		int resultCode = 9999;
		int deletedRow = 0;
		
		Customer customer = customerMapper.selectByPrimaryKey(customerNum);
		
		if (customer != null) {
			deletedRow = customerMapper.deleteByPrimaryKey(customerNum);
		} else {
			// 고객 없음
			resultCode = 3001;
		}
		
		if (deletedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		
		return resultMap;
	}
	
	public Map<String, Object> delFavorite(int num) {
		int resultCode = 9999;
		
		CustomerFavorite customerFavorite = customerFavoriteMapper.selectByPrimaryKey(num);
		
		int deletedRow = 0;
		if (customerFavorite != null) {
			deletedRow = customerFavoriteMapper.deleteByPrimaryKey(num);
		}
		
		if (deletedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		
		return resultMap;
	}
	
}
