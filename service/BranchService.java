package kr.totalcall.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.totalcall.dao.mapper.BranchBranchGroupMapper;
import kr.totalcall.dao.mapper.BranchChargeHistoryMapper;
import kr.totalcall.dao.mapper.BranchChargeRequestMapper;
import kr.totalcall.dao.mapper.BranchLocationMapper;
import kr.totalcall.generateddao.mapper.BankMapper;
import kr.totalcall.generateddao.mapper.BranchChargeMapper;
import kr.totalcall.generateddao.mapper.BranchChargeOutReqMapper;
import kr.totalcall.generateddao.mapper.BranchGroupMapper;
import kr.totalcall.generateddao.mapper.BranchMapper;
import kr.totalcall.generateddao.mapper.BranchOptionMapper;
import kr.totalcall.generateddao.mapper.FareDefaultMapper;
import kr.totalcall.generateddao.mapper.MoneyAutoSendConfigMapper;
import kr.totalcall.generateddao.mapper.MoneyOutReqCronMapper;
import kr.totalcall.generateddao.mapper.OptionsMapper;
import kr.totalcall.generateddao.model.Bank;
import kr.totalcall.generateddao.model.BankExample;
import kr.totalcall.generateddao.model.Branch;
import kr.totalcall.generateddao.model.BranchCharge;
import kr.totalcall.generateddao.model.BranchChargeOutReq;
import kr.totalcall.generateddao.model.BranchChargeOutReqExample;
import kr.totalcall.generateddao.model.BranchExample;
import kr.totalcall.generateddao.model.BranchGroup;
import kr.totalcall.generateddao.model.FareDefault;
import kr.totalcall.generateddao.model.MoneyAutoSendConfig;
import kr.totalcall.generateddao.model.MoneyOutReqCron;
import kr.totalcall.generateddao.model.MoneyOutReqCronExample;
import kr.totalcall.generateddao.model.Options;
import kr.totalcall.util.ObjectUtil;
import kr.totalcall.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BranchService extends CommonService {
	
	@Autowired
	private ChargeService chargeService;
	
	@Autowired
	private BankMapper bankMapper;
	
	@Autowired
	private BranchMapper branchMapper;
	
	@Autowired
	private BranchBranchGroupMapper branchContactMapper;
	
	@Autowired
	private BranchOptionMapper branchOptionMapper;
	
	@Autowired
	private BranchChargeMapper branchChargeMapper;
	
	@Autowired
	private BranchGroupMapper branchGroupMapper;
	
	@Autowired
	private BranchChargeOutReqMapper branchChargeOutReqMapper;
	
	@Autowired
	private BranchChargeRequestMapper branchChargeRequestMapper;
	
	@Autowired
	private BranchChargeHistoryMapper branchChargeHistoryMapper;
	
	@Autowired
	private MoneyOutReqCronMapper moneyOutReqCronMapper;
	
	@Autowired
	private OptionsMapper optionsMapper;
	
	@Autowired
	private FareDefaultMapper fareDefaultMapper;
	
	@Autowired
	private BranchLocationMapper branchLocationMapper;
	
	@Autowired
	private MoneyAutoSendConfigMapper moneyAutoSendConfigMapper;
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> setBranch(int groupNum, String corpNum, String branchName, String representative,
			String tel, String phone, String smsTel, String locDo, String locSi, String accountInfo, String memo,
			String creditCardApiKey, int shareTime, String partnerId, String rechargeId, int commission1,
			int commission2, int commission3, int commission4, int commission5, int commission6, int commission7,
			int commission8, int commission9, String memberId) {
		int resultCode = 9999;
		int insertedRow = 0;
		
		BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(groupNum);
		
		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andNameEqualTo(branchName);
		List<Branch> branchList = branchMapper.selectByExample(branchExample);
		
		if (branchList.size() == 0) {
			Branch branch = new Branch();
			branch.setGroupNum(groupNum);
			branch.setGroupName(branchGroup.getName());
			branch.setCorpNum(corpNum);
			branch.setName(branchName);
			branch.setRepresentative(representative);
			branch.setTel(tel);
			branch.setPhone(phone);
			branch.setSmsTel(smsTel);
			branch.setCharge(0);
			branch.setLocDo(locDo);
			branch.setLocSi(locSi);
			branch.setAccountInfo(accountInfo);
			branch.setMemo(memo);
			branch.setCreditCardApiKey(creditCardApiKey);
			branch.setShareTime(shareTime);
			branch.setPartnerId(partnerId);
			branch.setRechargeId(rechargeId);
			branch.setCommission1(commission1);
			branch.setCommission2(commission2);
			branch.setCommission3(commission3);
			branch.setCommission4(commission4);
			branch.setCommission5(commission5);
			branch.setCommission6(commission6);
			branch.setCommission7(commission7);
			branch.setCommission8(commission8);
			branch.setCommission9(commission9);
			branch.setDel(0);
			branch.setBak(0);
			insertedRow += branchMapper.insertSelective(branch);
			
			Options options = new Options();
			options.setBranchNum(branch.getNum());
			options.setBranchName(branch.getName());
			options.setEmployeeDistance(1500);
			options.setEmployeeDistance2(3000);
			options.setEmployeeCount(4);
			options.setEmployeeFare(40000);
			insertedRow += optionsMapper.insertSelective(options);
			
			FareDefault fareDefault = new FareDefault();
			fareDefault.setBranchNum(branch.getNum());
			fareDefault.setAuto(7000);
			fareDefault.setDamas(20000);
			fareDefault.setLabo(30000);
			fareDefault.setVan(30000);
			fareDefault.setTruck(50000);
			fareDefault.setTruck14(60000);
			fareDefault.setTruck25(80000);
			fareDefault.setTruck35(90000);
			fareDefault.setTruck50(130000);
			fareDefault.setMember(memberId);
			fareDefault.setDatetime(new Date());
			fareDefault.setDel(0);
			fareDefault.setBak(0);
			insertedRow += fareDefaultMapper.insertSelective(fareDefault);
		} else {
			resultCode = 3001; // 이름 중복
		}
		
		if (insertedRow == 3) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("insertedRow", insertedRow);
		return resultMap;
	}
	
	public Map<String, Object> setGroup(List<Integer> branchNumList, int groupNum) {
		int resultCode = 9999;
		
		Branch branch;
		int updatedRow = 0;
		for (Integer branchNum : branchNumList) {
			branch = branchMapper.selectByPrimaryKey(branchNum);
			branch.setGroupNum(groupNum);
			updatedRow += branchMapper.updateByPrimaryKeySelective(branch);
		}
		if (branchNumList.size() == updatedRow) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	public List<Map<String, Object>> getBranchAll() {
		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andDelEqualTo(0);
		branchExample.setOrderByClause("CASE group_num WHEN 0 THEN 10000 ELSE group_num END ASC, num ASC");
		List<Branch> branchList = branchMapper.selectByExample(branchExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = ObjectUtil.getMapListToElementList(branchList);
		return mapList;
	}
	
	public List<Map<String, Object>> getBranchSido() {
		List<Map<String, Object>> resultList = branchLocationMapper.selectBranchSido();
		if (resultList.size() == 0) {
			resultList = new ArrayList<Map<String, Object>>();
		}
		return resultList;
	}
	
	public List<Map<String, Object>> getBranchTree() {
		return branchContactMapper.selectBranchWhithGroupOwnerNum();
	}
	
	public List<Map<String, Object>> getContact() {
		return branchContactMapper.selectBranchGroupOwnerContact();
	}
	
	public Map<String, Object> getBranchByBranchNum(int branchNum) {
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		Map<String, Object> resultMap = ObjectUtil.getMapToElement(branch);
		return resultMap;
	}
	
	public List<Map<String, Object>> getBranchByGroupNum(int groupNum) {
		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andDelEqualTo(0).andGroupNumEqualTo(groupNum);
		List<Branch> branchList = branchMapper.selectByExample(branchExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = ObjectUtil.getMapListToElementList(branchList);
		return mapList;
	}
	
	public List<Map<String, Object>> getBranchByCallgroupNum(int callgroupNum) {
		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andCallgroupNumEqualTo(callgroupNum).andDelEqualTo(0);
		List<Branch> branchList = branchMapper.selectByExample(branchExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = ObjectUtil.getMapListToElementList(branchList);
		return mapList;
	}
	
	public int getBranchNumByBranchTel(String branchTel) {
		
		branchTel = StringUtil.getDigitString(branchTel);
		
		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andDelEqualTo(0);
		List<Branch> branchList = branchMapper.selectByExample(branchExample);
		
		int branchNum = 0;
		for (Branch branch : branchList) {
			if (StringUtil.getDigitString(branch.getTel()).equals(branchTel)
					|| StringUtil.getDigitString(branch.getPhone()).equals(branchTel)) {
				branchNum = branch.getNum();
				break;
			}
		}
		
		return branchNum;
	}
	
	public String getTel(int branchNum) {
		String branchTel = "";
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		if (branch != null) {
			branchTel = branch.getTel();
			if (branch.getNum() == 1) {
				branchTel = "15881710";
			}
		}
		return branchTel;
	}
	
	public Map<String, Object> getBranchTel(int branchNum) {
		String branchTel = "";
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		if (branch != null) {
			branchTel = branch.getTel();
			if (branch.getNum() == 1) {
				branchTel = "15881710";
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("branchTel", branchTel);
		return resultMap;
	}
	
	public Map<String, Object> getVaccountInfo(int branchNum) {
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (branch != null) {
			resultMap.put("num", branch.getNum());
			resultMap.put("name", branch.getName());
			resultMap.put("charge", branch.getCharge());
			if (branch.getVbankCode() != null) {
				resultMap.put("vbankCode", branch.getVbankCode());
			} else {
				resultMap.put("vbankCode", "");
			}
			if (branch.getVaccount() != null) {
				resultMap.put("vaccount", branch.getVaccount());
			} else {
				resultMap.put("vaccount", "");
			}
			
			if (branch.getVbankCode() != null) {
				BankExample bankExample = new BankExample();
				bankExample.createCriteria().andBankCodeEqualTo(branch.getVbankCode()).andVbankYnEqualTo("Y");
				List<Bank> bankList = bankMapper.selectByExample(bankExample);
				
				if (bankList.size() > 0) {
					resultMap.put("bankName", bankList.get(0).getBankName());
				} else {
					resultMap.put("bankName", "");
				}
			} else {
				resultMap.put("bankName", "");
			}
		}
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> setBranchChargeOutReq(int branchNum, int reqAmount, String reqMemo) {
		int resultCode = 9999;
		String resultMessage = "";
		
		MoneyAutoSendConfig moneyAutoSendConfig = moneyAutoSendConfigMapper.selectByPrimaryKey(1);
		
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		BranchGroup branchGroup = null;
		if (branch.getGroupNum() > 0) {
			branchGroup = branchGroupMapper.selectByPrimaryKey(branch.getGroupNum());
		}
		
		String gbn = "B";
		String totalcallVaccount = "02031091"; // 통합콜 가상계좌
		SimpleDateFormat reqSdf = new SimpleDateFormat("yyyyMMdd");
		String reqDt = reqSdf.format(new Date());
		
		MoneyOutReqCronExample moneyOutReqCronExample = new MoneyOutReqCronExample();
		moneyOutReqCronExample.createCriteria().andReqDtEqualTo(reqDt).andReqGbnEqualTo(gbn)
				.andReqNumEqualTo(branchNum);
		List<MoneyOutReqCron> moneyOutReqCronList = moneyOutReqCronMapper.selectByExample(moneyOutReqCronExample);
		int reqCount = 0;
		int reqAmountTotal = 0;
		for (MoneyOutReqCron moneyOutReqCron : moneyOutReqCronList) {
			if ("0".equals(moneyOutReqCron.getResult())) {
				reqCount++;
			}
			reqAmountTotal += moneyOutReqCron.getAmt();
		}
		
		if (reqAmount + moneyAutoSendConfig.getMoneyTranCommission() > branch.getCharge()) {
			resultCode = 3001;
			resultMessage = "지사 충전금이 부족 합니다.";
		} else if (reqAmount + moneyAutoSendConfig.getMoneyTranCommission() > branchGroup.getCharge()) {
			resultCode = 3005;
			resultMessage = "지사 그룹 충전금이 부족 합니다.";
		} else {
			if ("Y".equals(branch.getAutoSendMoneyYn())) {
				if (reqAmount > moneyAutoSendConfig.getBranchLimitPerOne()) {
					resultCode = 3002;
					resultMessage = "1회 출금 한도를 초과 했습니다.\n[1회 출금한도:" + moneyAutoSendConfig.getBranchLimitPerOne() + "원]";
				} else if (reqAmount + reqAmountTotal > moneyAutoSendConfig.getBranchLimitPerDay()) {
					resultCode = 3003;
					resultMessage = "1일 출금 한도를 초과 했습니다.\n[1일 출금한도:" + moneyAutoSendConfig.getBranchLimitPerDay()
							+ "원]\n[금일 출금(신청)금액:" + reqAmountTotal + "원]";
				} else if (reqCount > 0) {
					resultCode = 3004;
					resultMessage = "이미 출금 대기 중인 항목이 있습니다.\n잠시후에 다시 신청해 주세요.";
				}
			}
		}
		
		if (resultCode == 9999) {
			String confResult = "";
			if ("Y".equals(branch.getAutoSendMoneyYn())) {
				confResult = "접수-자동송금";
			} else if ("N".equals(branch.getAutoSendMoneyYn())) {
				confResult = "접수";
			}
			BranchChargeOutReq branchChargeOutReq = new BranchChargeOutReq();
			branchChargeOutReq.setBranchNum(branchNum);
			branchChargeOutReq.setReqDatetime(new Date());
			branchChargeOutReq.setReqAmount(reqAmount);
			branchChargeOutReq.setReqMemo(reqMemo);
			branchChargeOutReq.setConfResult(confResult);
			branchChargeOutReqMapper.insertSelective(branchChargeOutReq);
			
			if ("Y".equals(branch.getAutoSendMoneyYn())) {
				moneyOutReqCronExample = new MoneyOutReqCronExample();
				moneyOutReqCronExample.createCriteria().andReqDtEqualTo(reqDt);
				int count = moneyOutReqCronMapper.countByExample(moneyOutReqCronExample);
				count++;
				String jmSeqNo = "jm" + String.format("%010d", count);
				
				MoneyOutReqCron moneyOutReqCron = new MoneyOutReqCron();
				moneyOutReqCron.setReqDt(reqDt);
				moneyOutReqCron.setJmSeqNo(jmSeqNo);
				moneyOutReqCron.setOutReqNum(branchChargeOutReq.getNum());
				moneyOutReqCron.setReqGbn(gbn);
				moneyOutReqCron.setReqNum(branchNum);
				moneyOutReqCron.setOutChargeAccountNum(totalcallVaccount);
				moneyOutReqCron.setBankCode(branch.getBankCode());
				moneyOutReqCron.setNewBankCode(branch.getNewBankCode());
				moneyOutReqCron.setAccount(branch.getAccount());
				moneyOutReqCron.setAmt(reqAmount);
				moneyOutReqCron.setResult("0"); // 결과 - 0 접수
				moneyOutReqCron.setErrMsg("");
				moneyOutReqCron.setUpdateDttm(new Date());
				moneyOutReqCron.setAdminCharge(0);
				moneyOutReqCronMapper.insertSelective(moneyOutReqCron);
			}
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	public List<Map<String, Object>> getBranchChargeOutReq(int branchNum) {
		BranchChargeOutReqExample branchChargeOutReqExample = new BranchChargeOutReqExample();
		branchChargeOutReqExample.createCriteria().andBranchNumEqualTo(branchNum).andDelEqualTo(0);
		branchChargeOutReqExample.setOrderByClause("req_datetime DESC");
		List<BranchChargeOutReq> branchChargeOutReqList = branchChargeOutReqMapper
				.selectByExample(branchChargeOutReqExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		mapList = ObjectUtil.getMapListToElementList(branchChargeOutReqList);
		return mapList;
	}
	
	public List<Map<String, Object>> getBranchChargeRequest(int branchGroupNum, String startDate, String endDate) {
		List<Map<String, Object>> mapList = branchChargeRequestMapper.selectBranchChargeRequest(branchGroupNum,
				startDate, endDate);
		return mapList;
	}
	
	/**
	 * @param requestNum
	 * @param confAmount
	 * @param confMemo
	 * @return
	 */
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> sendAutoBranchChargeRequest(int requestNum, int confAmount, String confMemo) {
		int resultCode = 9999;
		String resultMessage = "";
		
		String gbn = "B";
		String totalcallVaccount = "02031091"; // 통합콜 가상계좌
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String reqDt = sdf.format(new Date());
		
		BranchChargeOutReq branchChargeOutReq = branchChargeOutReqMapper.selectByPrimaryKey(requestNum);
		
		Map<String, Object> map = branchChargeRequestMapper.selectBranchSendInfo(branchChargeOutReq.getBranchNum(),
				reqDt);
		int branchCharge = (Integer) map.get("branchCharge");
		int branchGroupNum = (Integer) map.get("groupNum");
		String autoSendMoneyYn = (String) map.get("autoSendMoneyYn");
		String bankCode = (String) map.get("bankCode");
		String newBankCode = (String) map.get("newBankCode");
		String bankAccount = (String) map.get("bankAccount");
		int moneyTranCommission = (Integer) map.get("moneyTranCommission");
		int branchLimitPerOne = (Integer) map.get("branchLimitPerOne");
		int branchLimitPerDay = (Integer) map.get("branchLimitPerDay");
		int sumAmt = ((BigDecimal) map.get("sumAmt")).intValue();
		
		BranchGroup branchGroup = null;
		if (branchGroupNum > 0) {
			branchGroup = branchGroupMapper.selectByPrimaryKey(branchGroupNum);
		}
		
		if ("Y".equals(autoSendMoneyYn)) {
			resultCode = 3001;
			resultMessage = "자동 송금 지정된 지사입니다.";
		} else {
			if (confAmount + moneyTranCommission > branchCharge) {
				resultCode = 3002;
				resultMessage = "지사의 충전금이 부족 합니다.";
			} else if (branchGroup != null && confAmount + moneyTranCommission > branchGroup.getCharge()) {
				resultCode = 3005;
				resultMessage = "지사 그룹의 충전금이 부족 합니다.";
			} else if (confAmount > branchLimitPerOne) {
				resultCode = 3003;
				resultMessage = "1회 요청 제한 금액을 초과 하였습니다.";
			} else if (confAmount + sumAmt > branchLimitPerDay) {
				resultCode = 3004;
				resultMessage = "1일 요청 제한 금액을 초과 하였습니다.";
			} else {
				branchChargeOutReq.setConfAmount(confAmount);
				branchChargeOutReq.setConfMemo(confMemo);
				branchChargeOutReq.setConfResult("접수-자동송금");
				branchChargeOutReq.setConfDatetime(new Date());
				branchChargeOutReqMapper.updateByPrimaryKeySelective(branchChargeOutReq);
				
				MoneyOutReqCron moneyOutReqCron = new MoneyOutReqCron();
				moneyOutReqCron.setResult("2"); // 이전 출금 요청을 에러로 처리
				MoneyOutReqCronExample moneyOutReqCronExample = new MoneyOutReqCronExample();
				moneyOutReqCronExample.createCriteria().andReqGbnEqualTo(gbn).andOutReqNumEqualTo(requestNum);
				moneyOutReqCronMapper.updateByExampleSelective(moneyOutReqCron, moneyOutReqCronExample);
				
				moneyOutReqCronExample = new MoneyOutReqCronExample();
				moneyOutReqCronExample.createCriteria().andReqDtEqualTo(reqDt);
				int count = moneyOutReqCronMapper.countByExample(moneyOutReqCronExample);
				count++;
				String jmSeqNo = "jm" + String.format("%010d", count);
				
				moneyOutReqCron = new MoneyOutReqCron();
				moneyOutReqCron.setReqDt(reqDt);
				moneyOutReqCron.setJmSeqNo(jmSeqNo);
				moneyOutReqCron.setOutReqNum(requestNum);
				moneyOutReqCron.setReqGbn(gbn);
				moneyOutReqCron.setReqNum(branchChargeOutReq.getBranchNum());
				moneyOutReqCron.setOutChargeAccountNum(totalcallVaccount);
				moneyOutReqCron.setBankCode(bankCode);
				moneyOutReqCron.setNewBankCode(newBankCode);
				moneyOutReqCron.setAccount(bankAccount);
				moneyOutReqCron.setAmt(confAmount);
				moneyOutReqCron.setResult("0"); // 결과 - 0 접수
				moneyOutReqCron.setErrMsg("");
				moneyOutReqCron.setUpdateDttm(new Date());
				moneyOutReqCron.setAdminCharge(0);
				moneyOutReqCronMapper.insertSelective(moneyOutReqCron);
				
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	public List<Map<String, Object>> getBranchChargeHistory(int branchNum, String startDate, String endDate) {
		List<Map<String, Object>> mapList = branchChargeHistoryMapper.selectBranchChargeHistory(branchNum, startDate,
				endDate);
		return mapList;
	}
	
	public Map<String, Object> getArsUseYnByBranchNum(int branchNum) {
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (branch != null) {
			resultMap.put("arsUseYn", branch.getArsUseYn());
		} else {
			resultMap.put("arsUseYn", "N");
		}
		return resultMap;
	}
	
	public Map<String, Object> modBranch(int branchNum, int groupNum, String corpNum, String branchName,
			String representative, String tel, String phone, String smsTel, String locDo, String locSi,
			String accountInfo, String memo, String creditCardApiKey, int shareTime, String partnerId,
			String rechargeId, int commission1, int commission2, int commission3, int commission4, int commission5,
			int commission6, int commission7, int commission8, int commission9) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(groupNum);
		
		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andNumNotEqualTo(branchNum).andNameEqualTo(branchName);
		List<Branch> branchList = branchMapper.selectByExample(branchExample);
		
		if (branchList.size() == 0) {
			Branch branch = branchMapper.selectByPrimaryKey(branchNum);
			if (branch != null) {
				branch.setGroupNum(groupNum);
				branch.setGroupName(branchGroup.getName());
				branch.setCorpNum(corpNum);
				branch.setName(branchName);
				branch.setRepresentative(representative);
				branch.setTel(tel);
				branch.setPhone(phone);
				branch.setSmsTel(smsTel);
				branch.setLocDo(locDo);
				branch.setLocSi(locSi);
				branch.setAccountInfo(accountInfo);
				branch.setMemo(memo);
				branch.setCreditCardApiKey(creditCardApiKey);
				branch.setShareTime(shareTime);
				branch.setPartnerId(partnerId);
				branch.setRechargeId(rechargeId);
				branch.setCommission1(commission1);
				branch.setCommission2(commission2);
				branch.setCommission3(commission3);
				branch.setCommission4(commission4);
				branch.setCommission5(commission5);
				branch.setCommission6(commission6);
				branch.setCommission7(commission7);
				branch.setCommission8(commission8);
				branch.setCommission9(commission9);
				
				updatedRow = branchMapper.updateByPrimaryKeySelective(branch);
			}
		} else {
			resultCode = 3001; // 이름 중복
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	public Map<String, Object> modBranchInternetYn(int branchNum, String internetYn) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		if (branch != null) {
			branch.setInternetYn(internetYn);
			updatedRow = branchMapper.updateByPrimaryKeySelective(branch);
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	public Map<String, Object> modCharge(int branchNum, int deposit, int withdraw, String memo) {
		int resultCode = 9999;
		
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		Map<String, Integer> serviceResultMap = chargeService.chargeBranch(branch.getNum(), 0, 0, "", memo, 0, deposit,
				withdraw, new Date());
		
		int updatedRow = serviceResultMap.get("updatedRow");
		int insertedRow = serviceResultMap.get("insertedRow");
		if (updatedRow == 1 && insertedRow == 1) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		resultMap.put("insertedRow", insertedRow);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> confirmBranchChargeRequest(int num, int branchNum, String confMemo, int confAmount) {
		int resultCode = 9999;
		int charge = 0;
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		
		int updatedRow = 0;
		int insertedRow = 0;
		
		if (branch != null) {
			charge = branch.getCharge() - confAmount;
			branch.setCharge(charge);
			
			updatedRow += branchMapper.updateByPrimaryKeySelective(branch);
			
			BranchChargeOutReq branchChargeOutReq = branchChargeOutReqMapper.selectByPrimaryKey(num);
			
			if (branchChargeOutReq != null) {
				branchChargeOutReq.setConfResult("처리");
				branchChargeOutReq.setConfMemo(confMemo);
				branchChargeOutReq.setConfDatetime(new Date());
				branchChargeOutReq.setConfAmount(confAmount);
				branchChargeOutReq.setCharge(charge);
				updatedRow += branchChargeOutReqMapper.updateByPrimaryKeySelective(branchChargeOutReq);
				
				BranchCharge branchCharge = new BranchCharge();
				branchCharge.setBranchNum(branchNum);
				branchCharge.setBranchName(branch.getName()); // TODO : delete
				branchCharge.setReceiveNum(0);
				branchCharge.setType("");
				branchCharge.setMemo("출금처리-" + confMemo);
				branchCharge.setConsign(0);
				branchCharge.setIn(0);
				branchCharge.setOut(confAmount);
				branchCharge.setBalance(charge);
				branchCharge.setDatetime(new Date());
				branchCharge.setDel(0);
				branchCharge.setBak(0);
				insertedRow = branchChargeMapper.insert(branchCharge);
			} else {
				resultCode = 3001;
			}
		} else {
			resultCode = 3001;
		}
		
		if (updatedRow == 2 && insertedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		
		return resultMap;
	}
	
	public Map<String, Object> rejectBranchChargeRequest(int num, int branchNum, String confMemo) {
		int resultCode = 9999;
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		
		int updatedRow = 0;
		if (branch != null) {
			BranchChargeOutReq branchChargeOutReq = branchChargeOutReqMapper.selectByPrimaryKey(num);
			if (branchChargeOutReq != null) {
				branchChargeOutReq.setConfResult("반려");
				branchChargeOutReq.setConfMemo(confMemo);
				branchChargeOutReq.setConfDatetime(new Date());
				branchChargeOutReq.setConfAmount(0);
				branchChargeOutReq.setCharge(branch.getCharge());
				updatedRow = branchChargeOutReqMapper.updateByPrimaryKeySelective(branchChargeOutReq);
			} else {
				resultCode = 3001;
			}
		} else {
			resultCode = 3001;
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		
		return resultMap;
	}
	
	public Map<String, Object> modCallgroupCommission(int branchNum, int callgroupCommission) {
		int resultCode = 9999;
		
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		branch.setCallgroupCommission(callgroupCommission);
		int updatedRow = branchMapper.updateByPrimaryKeySelective(branch);
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	public Map<String, Object> modArsUseYnByBranchNum(int branchNum, String arsUseYn) {
		int resultCode = 9999;
		
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		
		int updatedRow = 0;
		if (branch != null) {
			branch.setArsUseYn(arsUseYn);
			updatedRow = branchMapper.updateByPrimaryKeySelective(branch);
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	public Map<String, Object> modArsUseYnByCallgroupNum(int callgroupNum, String arsUseYn) {
		int resultCode = 9999;
		
		BranchExample branchExample = new BranchExample();
		branchExample.createCriteria().andCallgroupNumEqualTo(callgroupNum);
		List<Branch> branchList = branchMapper.selectByExample(branchExample);
		
		int updatedRow = 0;
		if (branchList.size() > 0) {
			for (Branch branch : branchList) {
				branch.setArsUseYn(arsUseYn);
				updatedRow += branchMapper.updateByPrimaryKeySelective(branch);
			}
		}
		
		if (updatedRow > 0) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	public Map<String, Object> modAccount(int targetNum, String autoSendMoneyYn, String bankCode, String account,
			String accountName) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Branch branch = branchMapper.selectByPrimaryKey(targetNum);
		
		BankExample bankExample = new BankExample();
		bankExample.createCriteria().andBankCodeEqualTo(bankCode);
		List<Bank> bankList = bankMapper.selectByExample(bankExample);
		Bank bank = new Bank();
		if (bankList.size() > 0) {
			bank = bankList.get(0);
		}
		
		if (branch != null) {
			branch.setOutChargeAccountNum(1);
			branch.setAutoSendMoneyYn(autoSendMoneyYn);
			branch.setBankCode(bankCode);
			branch.setNewBankCode(bank.getNewBankCode());
			branch.setAccount(account);
			branch.setAccountName(accountName);
			updatedRow = branchMapper.updateByPrimaryKeySelective(branch);
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
	
	public Map<String, Object> bindBranchCallgroup(List<Integer> branchNumList, int callgroupNum) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		Branch branch;
		for (int branchNum : branchNumList) {
			branch = branchMapper.selectByPrimaryKey(branchNum);
			if (branch != null) {
				branch.setCallgroupNum(callgroupNum);
				updatedRow += branchMapper.updateByPrimaryKeySelective(branch);
			}
		}
		
		if (branchNumList.size() == updatedRow) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		
		return resultMap;
	}
	
	public Map<String, Object> delBranchChargeOutReq(int branchChargeOutReqNum) {
		int resultCode = 9999;
		
		BranchChargeOutReq branchChargeOutReq = branchChargeOutReqMapper.selectByPrimaryKey(branchChargeOutReqNum);
		branchChargeOutReq.setDel(1);
		int updatedRow = branchChargeOutReqMapper.updateByPrimaryKeySelective(branchChargeOutReq);
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
}
