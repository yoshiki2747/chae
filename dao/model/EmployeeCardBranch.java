package kr.totalcall.dao.model;

import java.io.Serializable;
import java.util.Date;

public class EmployeeCardBranch implements Serializable {
	
	private static final long serialVersionUID = 1209417238490689052L;
	
	private int num;
	private int branchNum;
	private String branchName;
	private String vnum;
	private String name;
	private String id;
	private String password;
	private String workType;
	private String vehicle;
	private String vehicleNum;
	private String phone;
	private String tel;
	private String jumin;
	private String address;
	private String memo;
	private String inDate;
	private String outDate;
	private String reworkDate;
	private String stopDatetime;
	private int stopChk;
	private int outChk;
	private String license;
	private String licenseNum;
	private Double latitude;
	private Double longitude;
	private String location;
	private String state;
	private String county;
	private String town;
	private Date loginDatetime;
	private Date refreshDatetime;
	private Date gpsDatetime;
	private String autoTarget1;
	private String autoTarget2;
	private String autoTarget3;
	private String autoTarget4;
	private String autoTarget5;
	private int login;
	private int noticeNum;
	private int chkInternet;
	private int messageChk;
	private String keyphone;
	private int charge;
	private int defaultCharge;
	private String autoSendMoneyYn;
	private String withholdingYn;
	private String feeType;
	private Date feeTypeModifyDate;
	private String bankCode;
	private String account;
	private String accountName;
	private int outChargeAccountNum;
	private String vbankCode;
	private String vaccount;
	private String cardNum;
	private String conaCardNum;
	private String companyYn;
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
	}
	
	public int getBranchNum() {
		return branchNum;
	}
	
	public void setBranchNum(int branchNum) {
		this.branchNum = branchNum;
	}
	
	public String getBranchName() {
		return branchName;
	}
	
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	public String getVnum() {
		return vnum;
	}
	
	public void setVnum(String vnum) {
		this.vnum = vnum;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getWorkType() {
		return workType;
	}
	
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	
	public String getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	
	public String getVehicleNum() {
		return vehicleNum;
	}
	
	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getTel() {
		return tel;
	}
	
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getJumin() {
		return jumin;
	}
	
	public void setJumin(String jumin) {
		this.jumin = jumin;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public String getInDate() {
		return inDate;
	}
	
	public void setInDate(String inDate) {
		this.inDate = inDate;
	}
	
	public String getOutDate() {
		return outDate;
	}
	
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	
	public String getReworkDate() {
		return reworkDate;
	}
	
	public void setReworkDate(String reworkDate) {
		this.reworkDate = reworkDate;
	}
	
	public String getStopDatetime() {
		return stopDatetime;
	}
	
	public void setStopDatetime(String stopDatetime) {
		this.stopDatetime = stopDatetime;
	}
	
	public int getStopChk() {
		return stopChk;
	}
	
	public void setStopChk(int stopChk) {
		this.stopChk = stopChk;
	}
	
	public int getOutChk() {
		return outChk;
	}
	
	public void setOutChk(int outChk) {
		this.outChk = outChk;
	}
	
	public String getLicense() {
		return license;
	}
	
	public void setLicense(String license) {
		this.license = license;
	}
	
	public String getLicenseNum() {
		return licenseNum;
	}
	
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCounty() {
		return county;
	}
	
	public void setCounty(String county) {
		this.county = county;
	}
	
	public String getTown() {
		return town;
	}
	
	public void setTown(String town) {
		this.town = town;
	}
	
	public Date getLoginDatetime() {
		return loginDatetime;
	}
	
	public void setLoginDatetime(Date loginDatetime) {
		this.loginDatetime = loginDatetime;
	}
	
	public Date getRefreshDatetime() {
		return refreshDatetime;
	}
	
	public void setRefreshDatetime(Date refreshDatetime) {
		this.refreshDatetime = refreshDatetime;
	}
	
	public Date getGpsDatetime() {
		return gpsDatetime;
	}
	
	public void setGpsDatetime(Date gpsDatetime) {
		this.gpsDatetime = gpsDatetime;
	}
	
	public String getAutoTarget1() {
		return autoTarget1;
	}
	
	public void setAutoTarget1(String autoTarget1) {
		this.autoTarget1 = autoTarget1;
	}
	
	public String getAutoTarget2() {
		return autoTarget2;
	}
	
	public void setAutoTarget2(String autoTarget2) {
		this.autoTarget2 = autoTarget2;
	}
	
	public String getAutoTarget3() {
		return autoTarget3;
	}
	
	public void setAutoTarget3(String autoTarget3) {
		this.autoTarget3 = autoTarget3;
	}
	
	public String getAutoTarget4() {
		return autoTarget4;
	}
	
	public void setAutoTarget4(String autoTarget4) {
		this.autoTarget4 = autoTarget4;
	}
	
	public String getAutoTarget5() {
		return autoTarget5;
	}
	
	public void setAutoTarget5(String autoTarget5) {
		this.autoTarget5 = autoTarget5;
	}
	
	public int getLogin() {
		return login;
	}
	
	public void setLogin(int login) {
		this.login = login;
	}
	
	public int getNoticeNum() {
		return noticeNum;
	}
	
	public void setNoticeNum(int noticeNum) {
		this.noticeNum = noticeNum;
	}
	
	public int getChkInternet() {
		return chkInternet;
	}
	
	public void setChkInternet(int chkInternet) {
		this.chkInternet = chkInternet;
	}
	
	public int getMessageChk() {
		return messageChk;
	}
	
	public void setMessageChk(int messageChk) {
		this.messageChk = messageChk;
	}
	
	public String getKeyphone() {
		return keyphone;
	}
	
	public void setKeyphone(String keyphone) {
		this.keyphone = keyphone;
	}
	
	public int getCharge() {
		return charge;
	}
	
	public void setCharge(int charge) {
		this.charge = charge;
	}
	
	public int getDefaultCharge() {
		return defaultCharge;
	}
	
	public void setDefaultCharge(int defaultCharge) {
		this.defaultCharge = defaultCharge;
	}
	
	public String getAutoSendMoneyYn() {
		return autoSendMoneyYn;
	}
	
	public void setAutoSendMoneyYn(String autoSendMoneyYn) {
		this.autoSendMoneyYn = autoSendMoneyYn;
	}
	
	public String getWithholdingYn() {
		return withholdingYn;
	}
	
	public void setWithholdingYn(String withholdingYn) {
		this.withholdingYn = withholdingYn;
	}
	
	public String getFeeType() {
		return feeType;
	}
	
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	public Date getFeeTypeModifyDate() {
		return feeTypeModifyDate;
	}
	
	public void setFeeTypeModifyDate(Date feeTypeModifyDate) {
		this.feeTypeModifyDate = feeTypeModifyDate;
	}
	
	public String getBankCode() {
		return bankCode;
	}
	
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	public String getAccount() {
		return account;
	}
	
	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public int getOutChargeAccountNum() {
		return outChargeAccountNum;
	}
	
	public void setOutChargeAccountNum(int outChargeAccountNum) {
		this.outChargeAccountNum = outChargeAccountNum;
	}
	
	public String getVbankCode() {
		return vbankCode;
	}
	
	public void setVbankCode(String vbankCode) {
		this.vbankCode = vbankCode;
	}
	
	public String getVaccount() {
		return vaccount;
	}
	
	public void setVaccount(String vaccount) {
		this.vaccount = vaccount;
	}
	
	public String getCardNum() {
		return cardNum;
	}
	
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	public String getConaCardNum() {
		return conaCardNum;
	}
	
	public void setConaCardNum(String conaCardNum) {
		this.conaCardNum = conaCardNum;
	}
	
	public String getCompanyYn() {
		return companyYn;
	}
	
	public void setCompanyYn(String companyYn) {
		this.companyYn = companyYn;
	}
	
}
