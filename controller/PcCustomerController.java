package kr.totalcall.controller.pc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import kr.totalcall.controller.model.CustomerModel;
import kr.totalcall.service.CustomerService;

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
@RequestMapping("/pc/customer")
public class PcCustomerController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value = "setCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setCustomer(@RequestBody @Valid CustomerModel model,
			BindingResult bindingResult) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// logger.info("parameter {}", model);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			int codeIndex = 2000;
			for (ObjectError error : bindingResult.getAllErrors()) {
				resultMap.put("resultCode", ++codeIndex);
				resultMap.put("resultMessage", error.getDefaultMessage());
			}
		} else {
			resultMap.putAll(customerService.setCustomer(model));
		}
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "setCustomerByGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setCustomerByGroup(@RequestBody @Valid CustomerModel model,
			BindingResult bindingResult) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// logger.info("parameter {}", model);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (bindingResult.hasErrors()) {
			int codeIndex = 2000;
			for (ObjectError error : bindingResult.getAllErrors()) {
				resultMap.put("resultCode", ++codeIndex);
				resultMap.put("resultMessage", error.getDefaultMessage());
			}
		} else {
			resultMap.putAll(customerService.setCustomerByGroup(model));
		}
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "setFavorite", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setFavorite(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		String cname = (String) paramMap.get("cname");
		String department = (String) paramMap.get("department");
		String person = (String) paramMap.get("person");
		String tel = (String) paramMap.get("tel");
		String phone = (String) paramMap.get("phone");
		int jusoNum = (Integer) paramMap.get("jusoNum");
		String detail = (String) paramMap.get("detail");
		
		Map<String, Object> resultMap = customerService.setFavorite(customerNum, cname, department, person, tel, phone,
				jusoNum, detail);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "loginCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> loginCustomer(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String loginId = (String) paramMap.get("loginId");
		String loginPw = (String) paramMap.get("loginPw");
		int loginType = (Integer) paramMap.get("loginType");
		int branchNum = (Integer) paramMap.get("branchNum");
		
		Map<String, Object> customer = customerService.loginCustomer(loginId, loginPw, loginType, branchNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(customer,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "approveCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> approveCustomer(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		
		Map<String, Object> customer = customerService.approveCustomer(customerNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(customer,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "searchCompanyCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> searchCompanyCustomer(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String reportType = (String) paramMap.get("reportType");
		String searchWord = (String) paramMap.get("searchWord");
		
		List<Map<String, Object>> customerList = customerService.searchCompanyCustomer(reportType, searchWord);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				customerList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getCustomerByNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getCustomerByNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int num = (Integer) paramMap.get("num");
		
		Map<String, Object> customer = customerService.getCustomerByNum(num);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(customer,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getCustomerByGroupNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getCustomerByGroupNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int groupNum = (Integer) paramMap.get("groupNum");
		
		List<Map<String, Object>> customerList = customerService.getCustomerByGroupNum(groupNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				customerList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getCustomerByGroupId", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getCustomerByGroupId(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String groupId = (String) paramMap.get("groupId");
		
		List<Map<String, Object>> customerList = customerService.getCustomerByGroupId(branchNum, groupId);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				customerList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getCustomerByAdminNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getCustomerByAdminNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int adminNum = (Integer) paramMap.get("adminNum");
		
		List<Map<String, Object>> customerList = customerService.getCustomerByAdminNum(adminNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				customerList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "searchCustomerByPhone", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> searchCustomerByPhone(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String phoneNum = (String) paramMap.get("phoneNum");
		
		List<Map<String, Object>> resultMap = customerService.searchCustomerByPhone(branchNum, phoneNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultMap, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "searchCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> searchCustomer(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String searchWord = (String) paramMap.get("searchWord");
		
		List<Map<String, Object>> customerList = customerService.searchCustomer(branchNum, searchWord);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				customerList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "searchCustomerByCall", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> searchCustomerByCall(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String call = (String) paramMap.get("call");
		
		List<Map<String, Object>> customerList = customerService.searchCustomerByCall(branchNum, call);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				customerList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getCustomerInGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getCustomerInGroup(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int masterNum = (Integer) paramMap.get("masterNum");
		int adminNum = (Integer) paramMap.get("adminNum");
		int groupNum = (Integer) paramMap.get("groupNum");
		String searchWord = (String) paramMap.get("searchWord");
		
		List<Map<String, Object>> customerList = customerService.getCustomerInGroup(masterNum, adminNum, groupNum,
				searchWord);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				customerList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getMileage", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getMileage(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		
		Map<String, Object> customerList = customerService.getMileage(customerNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(customerList,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getChargeHistory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getChargeHistory(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		
		Map<String, Object> customerList = customerService.getChargeHistory(customerNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(customerList,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "checkExistCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> checkExistCustomer(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		String tel = (String) paramMap.get("tel");
		String pw = (String) paramMap.get("pw");
		int branchNum = (Integer) paramMap.get("branchNum");
		
		Map<String, Object> customerMap = customerService.checkExistCustomer(tel, pw, branchNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(customerMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getFavorite", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getFavorite(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		
		List<Map<String, Object>> CustomerFavoriteList = customerService.getFavorite(customerNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				CustomerFavoriteList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getCustomerRepotType", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getRepotType(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		
		Map<String, Object> reportType = customerService.getCustomerReportType(customerNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(reportType,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modCustomer(@RequestBody CustomerModel model) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// logger.info("parameter {}", model);
		
		Map<String, Object> resultMap = customerService.modCustomer(model);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modInternetCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modInternetCustomer(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		
		int num = (Integer) paramMap.get("num");
		String cname = (String) paramMap.get("cname");
		String department = (String) paramMap.get("department");
		String person = (String) paramMap.get("person");
		String tel = (String) paramMap.get("tel");
		String phone = (String) paramMap.get("phone");
		String pw = (String) paramMap.get("pw");
		int jusoNum = (Integer) paramMap.get("jusoNum");
		String sido = (String) paramMap.get("sido");
		String gugun = (String) paramMap.get("gugun");
		String dong = (String) paramMap.get("dong");
		String position = (String) paramMap.get("position");
		String etc = (String) paramMap.get("etc");
		String detail = (String) paramMap.get("detail");
		String account = (String) paramMap.get("account");
		String cardNum = (String) paramMap.get("cardNum");
		String cardNotiPhone = (String) paramMap.get("cardNotiPhone");
		String cardNotiEmail = (String) paramMap.get("cardNotiEmail");
		
		Map<String, Object> resultMap = customerService.modInternetCustomer(num, cname, department, person, tel, phone,
				pw, jusoNum, sido, gugun, dong, position, etc, detail, account, cardNum, cardNotiPhone, cardNotiEmail);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modSelectionCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modSelectionCustomer(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		List<Integer> customerNumArr = (List<Integer>) paramMap.get("customerNumArr");
		String cname = (String) paramMap.get("cname");
		String department = (String) paramMap.get("department");
		String person = (String) paramMap.get("person");
		String tel = (String) paramMap.get("tel");
		int startJusoNum = (Integer) paramMap.get("startJusoNum");
		String startSido = (String) paramMap.get("startSido");
		String startGugun = (String) paramMap.get("startGugun");
		String startDong = (String) paramMap.get("startDong");
		String startDongNum = (String) paramMap.get("startDongNum");
		String startPosition = (String) paramMap.get("startPosition");
		String startEtc = (String) paramMap.get("startEtc");
		String startDetail = (String) paramMap.get("startDetail");
		int credit = (Integer) paramMap.get("credit");
		String memo = (String) paramMap.get("memo");
		String driverMemo = (String) paramMap.get("driverMemo");
		String modifyField = (String) paramMap.get("modifyField");
		
		Map<String, Object> resultMap = customerService.modSelectionCustomer(customerNumArr, cname, department, person,
				tel, startJusoNum, startSido, startGugun, startDong, startDongNum, startPosition, startEtc,
				startDetail, credit, memo, driverMemo, modifyField);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "bindGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> bindGroup(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		List<Integer> customerNumList = (List<Integer>) paramMap.get("customerNumArr");
		int groupNum = (Integer) paramMap.get("groupNum");
		
		Map<String, Object> resultMap = customerService.bindGroup(customerNumList, groupNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "unbindGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> unbindGroup(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		List<Integer> customerNumList = (List<Integer>) paramMap.get("customerNumArr");
		
		Map<String, Object> resultMap = customerService.bindGroup(customerNumList, 0);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modCardInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modCardInfo(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		String cardNum = (String) paramMap.get("cardNum");
		String cardPhone = (String) paramMap.get("cardPhone");
		String cardEmail = (String) paramMap.get("cardEmail");
		
		Map<String, Object> resultMap = customerService.modCardInfo(customerNum, cardNum, cardPhone, cardEmail);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "transferMileage", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> transferMileage(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		List<Map<String, Object>> customerList = (List<Map<String, Object>>) paramMap.get("customerArr");
		
		Map<String, Object> resultMap = customerService.transferMileage(customerList);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modMileage", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modMileage(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		String mileageUseYn = (String) paramMap.get("mileageUseYn");
		String account = (String) paramMap.get("account");
		int saveOver = (Integer) paramMap.get("saveOver");
		int save = (Integer) paramMap.get("save");
		String saveName = (String) paramMap.get("saveName");
		int deposit = (Integer) paramMap.get("deposit");
		int withdraw = (Integer) paramMap.get("withdraw");
		String memberId = (String) paramMap.get("memberId");
		
		Map<String, Object> resultMap = customerService.modMileage(customerNum, mileageUseYn, account, saveOver, save,
				saveName, deposit, withdraw, memberId);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "requestMileage", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> requestMileage(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		int requestAmount = (Integer) paramMap.get("requestAmount");
		String account = (String) paramMap.get("account");
		
		Map<String, Object> resultMap = customerService.requestMileage(customerNum, requestAmount, account);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "addTelephone", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addTelephone(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		String telephone = (String) paramMap.get("telephone");
		
		Map<String, Object> resultMap = customerService.addTelephone(customerNum, telephone);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "delCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delCustomer(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		
		Map<String, Object> resultMap = customerService.delCustomer(customerNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "removeCustomer", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> removeCustomer(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int customerNum = (Integer) paramMap.get("customerNum");
		
		Map<String, Object> customer = customerService.removeCustomer(customerNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(customer,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "delFavorite", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delFavorite(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int num = (Integer) paramMap.get("num");
		
		Map<String, Object> resultMap = customerService.delFavorite(num);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
}
