package kr.totalcall.controller.pc;

import java.util.List;
import java.util.Map;

import kr.totalcall.service.BranchService;

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
@RequestMapping("/pc/branch")
public class PcBranchController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BranchService branchService;
	
	@RequestMapping(value = "setBranch", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setBranch(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int groupNum = (Integer) paramMap.get("groupNum");
		String corpNum = (String) paramMap.get("corpNum");
		String branchName = (String) paramMap.get("branchName");
		String representative = (String) paramMap.get("representative");
		String tel = (String) paramMap.get("tel");
		String phone = (String) paramMap.get("phone");
		String smsTel = (String) paramMap.get("smsTel");
		String locDo = (String) paramMap.get("locDo");
		String locSi = (String) paramMap.get("locSi");
		String accountInfo = (String) paramMap.get("accountInfo");
		String memo = (String) paramMap.get("memo");
		String creditCardApiKey = (String) paramMap.get("creditCardApiKey");
		int shareTime = (Integer) paramMap.get("shareTime");
		String partnerId = (String) paramMap.get("partnerId");
		String rechargeId = (String) paramMap.get("rechargeId");
		int commission1 = (Integer) paramMap.get("commission1");
		int commission2 = (Integer) paramMap.get("commission2");
		int commission3 = (Integer) paramMap.get("commission3");
		int commission4 = (Integer) paramMap.get("commission4");
		int commission5 = (Integer) paramMap.get("commission5");
		int commission6 = (Integer) paramMap.get("commission6");
		int commission7 = (Integer) paramMap.get("commission7");
		int commission8 = (Integer) paramMap.get("commission8");
		int commission9 = (Integer) paramMap.get("commission9");
		String memberId = (String) paramMap.get("memberId");
		
		Map<String, Object> resultMap = branchService.setBranch(groupNum, corpNum, branchName, representative, tel,
				phone, smsTel, locDo, locSi, accountInfo, memo, creditCardApiKey, shareTime, partnerId, rechargeId,
				commission1, commission2, commission3, commission4, commission5, commission6, commission7, commission8,
				commission9, memberId);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "setGroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setGroup(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		// int branchNumArr[] = paramMap.get("branchNumArr");
		List<Integer> branchNumList = (List<Integer>) paramMap.get("branchNumArr");
		int groupNum = (Integer) paramMap.get("groupNum");
		
		Map<String, Object> resultMap = branchService.setGroup(branchNumList, groupNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getBranchAll", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getBranchAll() throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		List<Map<String, Object>> branchList = branchService.getBranchAll();
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				branchList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getBranchSido", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getBranchSido() throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		List<Map<String, Object>> branchList = branchService.getBranchSido();
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				branchList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getBranchTree", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getBranchTree() throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		List<Map<String, Object>> contactList = branchService.getBranchTree();
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				contactList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getContact", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getContact() throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		List<Map<String, Object>> contactList = branchService.getContact();
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				contactList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getBranchTel", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getTel(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		int branchNum = (Integer) paramMap.get("branchNum");
		
		Map<String, Object> resultMap = branchService.getBranchTel(branchNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getBranchByBranchNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getBranchByBranchNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		
		Map<String, Object> branchMap = branchService.getBranchByBranchNum(branchNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(branchMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getBranchByGroupNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getBranchByGroupNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int groupNum = (Integer) paramMap.get("groupNum");
		
		List<Map<String, Object>> branchList = branchService.getBranchByGroupNum(groupNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				branchList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getBranchByCallgroupNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getBranchByCallgroupNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int callgroupNum = (Integer) paramMap.get("callgroupNum");
		
		List<Map<String, Object>> branchList = branchService.getBranchByCallgroupNum(callgroupNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				branchList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getVaccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getVaccountInfo(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		int branchNum = (Integer) paramMap.get("branchNum");
		
		Map<String, Object> map = branchService.getVaccountInfo(branchNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 지사가 지사그룹장에게 출금요청
	 * 
	 * @param paramMap
	 *            branchNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "setBranchChargeOutReq", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> setBranchChargeOutReq(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		int reqAmount = (Integer) paramMap.get("reqAmount");
		String reqMemo = (String) paramMap.get("reqMemo");
		
		Map<String, Object> resultMap = branchService.setBranchChargeOutReq(branchNum, reqAmount, reqMemo);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 지사가 지사그룹장에게 출금요청한 내용을 조회
	 * 
	 * @param paramMap
	 *            branchNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBranchChargeOutReq", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getBranchChargeOutReq(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		
		List<Map<String, Object>> resultList = branchService.getBranchChargeOutReq(branchNum);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	/**
	 * 지사그룹장이 그룹원의 출금요청을 조회
	 * 
	 * @param paramMap
	 *            branchGroupNum, startDate, endDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBranchChargeRequest", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getBranchChargeRequest(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchGroupNum = (Integer) paramMap.get("branchGroupNum");
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		
		List<Map<String, Object>> resultList = branchService.getBranchChargeRequest(branchGroupNum, startDate, endDate);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "sendAutoBranchChargeRequest", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sendAutoBranchChargeRequest(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		int requestNum = (Integer) paramMap.get("requestNum");
		int confAmount = (Integer) paramMap.get("confAmount");
		String confMemo = (String) paramMap.get("confMemo");
		
		Map<String, Object> map = branchService.sendAutoBranchChargeRequest(requestNum, confAmount, confMemo);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getBranchChargeHistory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getBranchChargeHistory(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String startDate = (String) paramMap.get("startDate");
		String endDate = (String) paramMap.get("endDate");
		
		List<Map<String, Object>> resultList = branchService.getBranchChargeHistory(branchNum, startDate, endDate);
		ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<List<Map<String, Object>>>(
				resultList, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "getArsUseYnByBranchNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getArsUseYn(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		int branchNum = (Integer) paramMap.get("branchNum");
		
		Map<String, Object> map = branchService.getArsUseYnByBranchNum(branchNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modBranch", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modBranch(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		int groupNum = (Integer) paramMap.get("groupNum");
		String corpNum = (String) paramMap.get("corpNum");
		String branchName = (String) paramMap.get("branchName");
		String representative = (String) paramMap.get("representative");
		String tel = (String) paramMap.get("tel");
		String phone = (String) paramMap.get("phone");
		String smsTel = (String) paramMap.get("smsTel");
		String locDo = (String) paramMap.get("locDo");
		String locSi = (String) paramMap.get("locSi");
		String accountInfo = (String) paramMap.get("accountInfo");
		String memo = (String) paramMap.get("memo");
		String creditCardApiKey = (String) paramMap.get("creditCardApiKey");
		int shareTime = (Integer) paramMap.get("shareTime");
		String partnerId = (String) paramMap.get("partnerId");
		String rechargeId = (String) paramMap.get("rechargeId");
		int commission1 = (Integer) paramMap.get("commission1");
		int commission2 = (Integer) paramMap.get("commission2");
		int commission3 = (Integer) paramMap.get("commission3");
		int commission4 = (Integer) paramMap.get("commission4");
		int commission5 = (Integer) paramMap.get("commission5");
		int commission6 = (Integer) paramMap.get("commission6");
		int commission7 = (Integer) paramMap.get("commission7");
		int commission8 = (Integer) paramMap.get("commission8");
		int commission9 = (Integer) paramMap.get("commission9");
		
		Map<String, Object> resultMap = branchService.modBranch(branchNum, groupNum, corpNum, branchName,
				representative, tel, phone, smsTel, locDo, locSi, accountInfo, memo, creditCardApiKey, shareTime,
				partnerId, rechargeId, commission1, commission2, commission3, commission4, commission5, commission6,
				commission7, commission8, commission9);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modBranchInternetYn", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modBranchInternetYn(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String internetYn = (String) paramMap.get("internetYn");
		
		Map<String, Object> resultMap = branchService.modBranchInternetYn(branchNum, internetYn);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modCharge", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modCharge(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		int deposit = (Integer) paramMap.get("deposit");
		int withdraw = (Integer) paramMap.get("withdraw");
		String memo = (String) paramMap.get("memo");
		
		Map<String, Object> resultMap = branchService.modCharge(branchNum, deposit, withdraw, memo);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "confirmBranchChargeRequest", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> confirmBranchChargeRequest(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int num = (Integer) paramMap.get("num");
		int branchNum = (Integer) paramMap.get("branchNum");
		String confMemo = (String) paramMap.get("confMemo");
		int confAmount = (Integer) paramMap.get("confAmount");
		
		Map<String, Object> resultMap = branchService.confirmBranchChargeRequest(num, branchNum, confMemo, confAmount);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "rejectBranchChargeRequest", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> rejectBranchChargeRequest(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int num = (Integer) paramMap.get("num");
		int branchNum = (Integer) paramMap.get("branchNum");
		String confMemo = (String) paramMap.get("confMemo");
		
		Map<String, Object> resultMap = branchService.rejectBranchChargeRequest(num, branchNum, confMemo);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modCallgroupCommission", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modCallgroupCommission(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		int callgroupCommission = (Integer) paramMap.get("callgroupCommission");
		
		Map<String, Object> resultMap = branchService.modCallgroupCommission(branchNum, callgroupCommission);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modArsUseYnByBranchNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modArsUseYnByBranchNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchNum = (Integer) paramMap.get("branchNum");
		String arsUseYn = (String) paramMap.get("arsUseYn");
		
		Map<String, Object> map = branchService.modArsUseYnByBranchNum(branchNum, arsUseYn);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modArsUseYnByCallgroupNum", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modArsUseYnByCallgroupNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int callgroupNum = (Integer) paramMap.get("callgroupNum");
		String arsUseYn = (String) paramMap.get("arsUseYn");
		
		Map<String, Object> map = branchService.modArsUseYnByCallgroupNum(callgroupNum, arsUseYn);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "modAccount", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> modAccount(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int targetNum = (Integer) paramMap.get("targetNum");
		String autoSendMoneyYn = (String) paramMap.get("autoSendMoneyYn");
		String bankCode = (String) paramMap.get("bankCode");
		String account = (String) paramMap.get("account");
		String accountName = (String) paramMap.get("accountName");
		
		Map<String, Object> resultMap = branchService.modAccount(targetNum, autoSendMoneyYn, bankCode, account,
				accountName);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "bindBranchCallgroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> bindBranchCallgroupNum(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		List<Integer> branchNumList = (List<Integer>) paramMap.get("branchNumArr");
		int callgroupNum = (Integer) paramMap.get("callgroupNum");
		
		Map<String, Object> resultMap = branchService.bindBranchCallgroup(branchNumList, callgroupNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "unbindBranchCallgroup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> unbindBranchCallgroup(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		List<Integer> branchNumList = (List<Integer>) paramMap.get("branchNumArr");
		
		Map<String, Object> resultMap = branchService.bindBranchCallgroup(branchNumList, 0);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
	@RequestMapping(value = "delBranchChargeOutReq", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delBranchChargeOutReq(@RequestBody Map<String, Object> paramMap)
			throws Exception {
		logger.info("start ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.info("parameter {}", paramMap);
		int branchChargeOutReqNum = (Integer) paramMap.get("branchChargeOutReqNum");
		
		Map<String, Object> resultMap = branchService.delBranchChargeOutReq(branchChargeOutReqNum);
		ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<Map<String, Object>>(resultMap,
				HttpStatus.OK);
		return responseEntity;
	}
	
}
