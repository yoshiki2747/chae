package kr.totalcall.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.logisoft.dao.model.CoaliOrder;
import kr.logisoft.service.CoaliOrderService;
import kr.totalcall.controller.model.CustomerModel;
import kr.totalcall.controller.model.ReceiveModel;
import kr.totalcall.controller.model.ReceiveUpdateModel;
import kr.totalcall.dao.mapper.CustomerReceiveMapper;
import kr.totalcall.dao.mapper.DriverReceiveMapper;
import kr.totalcall.dao.mapper.EmployeeBranchMapper;
import kr.totalcall.dao.mapper.JusoExMapper;
import kr.totalcall.dao.mapper.ReceiveExMapper;
import kr.totalcall.dao.mapper.ReceiveShareOrderMapper;
import kr.totalcall.dao.model.ReceiveCustomerDriverModel;
import kr.totalcall.dao.model.ReceiveShareOrder;
import kr.totalcall.generateddao.mapper.BranchGroupMapper;
import kr.totalcall.generateddao.mapper.BranchMapper;
import kr.totalcall.generateddao.mapper.CustomerMapper;
import kr.totalcall.generateddao.mapper.EmployeeMapper;
import kr.totalcall.generateddao.mapper.JusoMapper;
import kr.totalcall.generateddao.mapper.ReceiveCustomerMapper;
import kr.totalcall.generateddao.mapper.ReceiveMapper;
import kr.totalcall.generateddao.mapper.ReceiveSignMapper;
import kr.totalcall.generateddao.mapper.ReceiveTempMapper;
import kr.totalcall.generateddao.mapper.ShareOrderMapper;
import kr.totalcall.generateddao.model.Branch;
import kr.totalcall.generateddao.model.BranchGroup;
import kr.totalcall.generateddao.model.Customer;
import kr.totalcall.generateddao.model.Employee;
import kr.totalcall.generateddao.model.Juso;
import kr.totalcall.generateddao.model.JusoExample;
import kr.totalcall.generateddao.model.Receive;
import kr.totalcall.generateddao.model.ReceiveCustomer;
import kr.totalcall.generateddao.model.ReceiveCustomerExample;
import kr.totalcall.generateddao.model.ReceiveExample;
import kr.totalcall.generateddao.model.ReceiveSign;
import kr.totalcall.generateddao.model.ReceiveSignExample;
import kr.totalcall.generateddao.model.ReceiveSignKey;
import kr.totalcall.generateddao.model.ReceiveTempExample;
import kr.totalcall.generateddao.model.ReceiveTempWithBLOBs;
import kr.totalcall.service.ext.ExtCargoReceiveService;
import kr.totalcall.util.DistanceUtil;
import kr.totalcall.util.ObjectUtil;

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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceiveService extends CommonService {
	
	@Autowired
	private BranchService branchService;
	
	@Autowired
	private ShareOrderService shareOrderService;
	
	@Autowired
	private CoaliOrderService coaliOrderService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerGroupService customerGroupService;
	
	@Autowired
	private FareService fareService;
	
	@Autowired
	private ChargeService chargeService;
	
	@Autowired
	private SmsService smsService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private CommonCodeService commonCodeService;
	
	@Autowired
	private ExtCargoReceiveService extCargoReceiveService;
	
	@Autowired
	private ReceiveMapper receiveMapper;
	
	@Autowired
	private ReceiveCustomerMapper receiveCustomerMapper;
	
	@Autowired
	private ReceiveTempMapper receiveTempMapper;
	
	@Autowired
	private ReceiveSignMapper receiveSignMapper;
	
	@Autowired
	private BranchMapper branchMapper;
	
	@Autowired
	private BranchGroupMapper branchGroupMapper;
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private JusoMapper jusoMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private EmployeeBranchMapper employeeBranchMapper;
	
	@Autowired
	private CustomerReceiveMapper customerReceiveMapper;
	
	@Autowired
	private DriverReceiveMapper driverReceiveMapper;
	
	@Autowired
	private ReceiveExMapper receiveExMapper;
	
	@Autowired
	private ReceiveShareOrderMapper receiveShareOrderMapper;
	
	@Autowired
	private ShareOrderMapper shareOrderMapper;
	
	@Autowired
	private JusoExMapper jusoExMapper;
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> setReceive(ReceiveModel receiveModel) throws ParseException {
		
		int resultCode = 9999;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int insertedRow = 0;
		
		// 요금 계산
		receiveModel.setFare(receiveModel.getFareOrigin() + receiveModel.getFareWeight() + receiveModel.getFareEtc()
				+ receiveModel.getFareNight() + receiveModel.getFareRain() + receiveModel.getFareHoliday());
		receiveModel
				.setFare_(receiveModel.getFare() - receiveModel.getFareDiscount() - receiveModel.getFareDiscount2());
		
		if ("외상".equals(receiveModel.getPayment())) {
			String reportType = customerService.getReportType(receiveModel.getClient().getCnum());
			
			// 접수 가능 시간 조사
			// if ("SSS".equals(reportType)) {
			// GregorianCalendar gc = new GregorianCalendar();
			// int limithours = 19;
			// if (gc.get(Calendar.HOUR_OF_DAY) >= limithours) {
			// resultCode = 3001;
			// resultMap.put("resultCode", resultCode);
			// resultMap.put("resultMessage", limithours + "시 이후로는 외상 접수가 불가능합니다.");
			// return resultMap;
			// }
			// }
			
			Map<String, Object> limitConditionMap = null;
			
			// 고객 사용 한도금 초과 여부 조사
			if (!"SSS".equals(reportType)) {
				int customerGroupNum = customerService.checkGroupLimit(receiveModel.getClient().getCnum());
				if (customerGroupNum > 0) {
					List<Map<String, Object>> limitConditionList = customerGroupService.getLimitCondition(0, 0,
							customerGroupNum, 0);
					if (limitConditionList.size() == 1) {
						limitConditionMap = limitConditionList.get(0);
						int restAmount = Integer.parseInt(String.valueOf(limitConditionMap.get("rest_amount")));
						if (restAmount - receiveModel.getFare() < 0) {
							resultCode = 3002;
							resultMap.put("resultCode", resultCode);
							resultMap.put("limitAmount", limitConditionMap.get("limit_amount"));
							resultMap.put("usedAmount", limitConditionMap.get("used_amount"));
							resultMap.put("restAmount", limitConditionMap.get("rest_amount"));
							return resultMap;
						}
					}
				}
			}
		}
		
		// 지사 충전금에 따른 지사간 공유 여부 판단
		if (receiveModel.getShare() == 1 && !"선불".equals(receiveModel.getPayment())
				&& !"착불".equals(receiveModel.getPayment())) {
			Branch branch = branchMapper.selectByPrimaryKey(receiveModel.getBranchNum());
			if (branch != null) {
				if (branch.getCharge() < 100000) {
					receiveModel.setShare(0);
				} else {
					BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(branch.getGroupNum());
					if (branchGroup != null && branchGroup.getCharge() < 300000) {
						receiveModel.setShare(0);
					}
				}
			}
		}
		
		// 고객 인터넷 주문
		// 선불 또는 착불인 경우 그룹에 속하지 않은 고객 이면 접수로 바꾼다
		String internetReqYn = "N";
		if ("인터넷".equals(receiveModel.getStatus())) {
			internetReqYn = "Y";
			if ("선불".equals(receiveModel.getPayment()) || "착불".equals(receiveModel.getPayment())) {
				Customer customer = customerMapper.selectByPrimaryKey(receiveModel.getClient().getCnum());
				if (customer == null || customer.getGroupNum() == 0) {
					receiveModel.setStatus("접수");
				}
			}
		}
		
		// 의뢰자가 신규일 경우 고객 등록
		if (receiveModel.getClient().getCnum() == 0) {
			String clientpw = "";
			String clientMemo = receiveModel.getClient().getMemo();
			if (StringUtils.isNotBlank(receiveModel.getNonMemPw())) {
				clientpw = receiveModel.getNonMemPw();
				clientMemo = "- 인터넷 비회원 접수 고객";
			}
			
			CustomerModel customerModel = new CustomerModel();
			customerModel.setBranchNum(receiveModel.getBranchNum());
			customerModel.setGroupNum(0);
			customerModel.setCname(receiveModel.getClient().getCname());
			customerModel.setDepartment(receiveModel.getClient().getDepartment());
			customerModel.setPerson(receiveModel.getClient().getPerson());
			customerModel.setTel(receiveModel.getClient().getTel());
			customerModel.setPhone(receiveModel.getClient().getPhone());
			customerModel.setId("");
			customerModel.setPw(clientpw);
			customerModel.setJusoNum(receiveModel.getClient().getJusoNum());
			customerModel.setSido(receiveModel.getClient().getSido());
			customerModel.setGugun(receiveModel.getClient().getGugun());
			customerModel.setDong(receiveModel.getClient().getDong());
			customerModel.setDongNum(receiveModel.getClient().getDongNum());
			customerModel.setPosition(receiveModel.getClient().getPosition());
			customerModel.setBuilding(receiveModel.getClient().getBuilding());
			customerModel.setFloor(receiveModel.getClient().getFloor());
			customerModel.setEtc(receiveModel.getClient().getEtc());
			customerModel.setDetail(receiveModel.getClient().getDetail());
			customerModel.setMemo(clientMemo);
			Map<String, Object> setCustomerResultMap = customerService.setCustomer(customerModel);
			receiveModel.getClient().setCnum((Integer) setCustomerResultMap.get("customerNum"));
		}
		
		// 기사 확인
		/*
		Employee employee = new Employee();
		if (receiveModel.getDriverNum() > 0) {
			employee = employeeMapper.selectByPrimaryKey(receiveModel.getDriverNum());
		}
		*/
		
		String reserveYear = "";
		String reserveMonth = "";
		String reserveDayOfWeek = "";
		String reserveDayOfMonth = "";
		String reserveDate = "";
		String reserveHour = "";
		String reserveMinute = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfDt = new SimpleDateFormat("yyyyMMddHHmmss");
		Date datetime = null;
		
		Receive receive = null;
		ReceiveCustomer clientRc = null;
		ReceiveCustomer startRc = null;
		ReceiveCustomer endRc = null;
		
		int clientGroupNum = 0;
		int startGroupNum = 0;
		int endGroupNum = 0;
		
		Customer clientCustomer = new Customer();
		Customer startCustomer = new Customer();
		Customer endCustomer = new Customer();
		
		if (receiveModel.getClient().getCnum() != null && receiveModel.getClient().getCnum() > 0) {
			clientCustomer = customerMapper.selectByPrimaryKey(receiveModel.getClient().getCnum());
			clientGroupNum = clientCustomer.getGroupNum();
		}
		if (receiveModel.getStart().getCnum() != null && receiveModel.getStart().getCnum() > 0) {
			startCustomer = customerMapper.selectByPrimaryKey(receiveModel.getStart().getCnum());
			startGroupNum = startCustomer.getGroupNum();
		}
		if (receiveModel.getEnd().getCnum() != null && receiveModel.getEnd().getCnum() > 0) {
			endCustomer = customerMapper.selectByPrimaryKey(receiveModel.getEnd().getCnum());
			endGroupNum = endCustomer.getGroupNum();
		}
		
		// 반복 예약
		if (receiveModel.getReserve() == 1 && receiveModel.getReserveDv() > 0) {
			Date date1_time = sdf.parse(receiveModel.getSdtRepeat());
			Date date2_time = sdf.parse(receiveModel.getEdtRepeat());
			
			String keyReserveRpeat = sdf.format(new Date()) + System.currentTimeMillis(); // keyReserveRpeat
			
			Calendar start = Calendar.getInstance();
			start.setTime(date1_time);
			Calendar end = Calendar.getInstance();
			end.setTime(date2_time);
			
			GregorianCalendar gc = new GregorianCalendar();
			boolean isReserveDate = false;
			for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
				gc.setTime(date);
				reserveYear = String.valueOf(gc.get(Calendar.YEAR));
				reserveMonth = String.format("%02d", gc.get(Calendar.MONTH) + 1);
				// php의 요일 값 기준으로 db에 입력이 되어 -1을해준다
				reserveDayOfWeek = String.valueOf(gc.get(Calendar.DAY_OF_WEEK) - 1);
				reserveDayOfMonth = String.format("%02d", gc.get(Calendar.DAY_OF_MONTH));
				reserveDate = reserveMonth + "/" + reserveDayOfMonth + "/" + reserveYear;
				reserveHour = String.format("%02d", receiveModel.getReserveHour());
				reserveMinute = String.format("%02d", receiveModel.getReserveMinute());
				datetime = sdfDt.parse(reserveYear + reserveMonth + reserveDayOfMonth + reserveHour + reserveMinute
						+ "00");
				
				isReserveDate = false;
				if (receiveModel.getReserveDv() == 1) {
					// 요일반복 예약인 경우
					if (receiveModel.getSelYoilRepeat().indexOf(reserveDayOfWeek) > -1) {
						isReserveDate = true;
					}
				} else if (receiveModel.getReserveDv() == 2) {
					// 월반복 예약인 경우
					if (receiveModel.getSelDayRepeat().indexOf(reserveDayOfMonth) > -1) {
						isReserveDate = true;
					}
				}
				
				if (isReserveDate == true) {
					// 접수
					receive = new Receive();
					receive.setBranchNum(receiveModel.getBranchNum());
					receive.setBranchName(""); // TODO : delete
					receive.setStatus(receiveModel.getStatus());
					
					receive.setCnum1(receiveModel.getClient().getCnum());
					receive.setCnum2(receiveModel.getStart().getCnum());
					receive.setCnum3(receiveModel.getEnd().getCnum());
					
					receive.setStartJusoNum(receiveModel.getStart().getJusoNum()); // TODO : delete
					receive.setStartSido(receiveModel.getStart().getSido()); // TODO : delete
					receive.setStartGugun(receiveModel.getStart().getGugun()); // TODO : delete
					receive.setStartDong(receiveModel.getStart().getDong()); // TODO : delete
					receive.setStartEtc(receiveModel.getStart().getEtc()); // TODO : delete
					receive.setStartPosition(receiveModel.getStart().getPosition()); // TODO : delete
					receive.setEndJusoNum(receiveModel.getEnd().getJusoNum()); // TODO : delete
					receive.setEndSido(receiveModel.getEnd().getSido()); // TODO : delete
					receive.setEndGugun(receiveModel.getEnd().getGugun()); // TODO : delete
					receive.setEndDong(receiveModel.getEnd().getDong()); // TODO : delete
					receive.setEndEtc(receiveModel.getEnd().getEtc()); // TODO : delete
					receive.setEndPosition(receiveModel.getEnd().getPosition()); // TODO : delete
					
					receive.setDatetime(datetime);
					receive.setShuttle(receiveModel.getShuttle());
					receive.setQuick(receiveModel.getQuick());
					receive.setAuto(receiveModel.getAuto());
					receive.setWeight(receiveModel.getWeight());
					receive.setPayment(receiveModel.getPayment());
					receive.setDriverNum(receiveModel.getDriverNum());
					receive.setDriverName(""); // TODO : delete
					receive.setDriverNum1(0); // TODO : delete
					receive.setDriverNum2(0); // TODO : delete
					receive.setDriverNum3(0); // TODO : delete
					receive.setDriverDatetime(new Date()); // TODO : delete
					receive.setExtDriver(receiveModel.getExtDriver());
					receive.setFareType(receiveModel.getFareType());
					receive.setFare(receiveModel.getFare());
					receive.setFareOrigin(receiveModel.getFareOrigin());
					receive.setFareWeight(receiveModel.getFareWeight());
					receive.setFareEtc(receiveModel.getFareEtc());
					receive.setFareNight(receiveModel.getFareNight());
					receive.setFareRain(receiveModel.getFareRain());
					receive.setFareHoliday(receiveModel.getFareHoliday());
					receive.setFareDiscount(receiveModel.getFareDiscount());
					receive.setFareDiscount2(receiveModel.getFareDiscount2());
					receive.setFare_(receiveModel.getFare_());
					receive.setConsign_(receiveModel.getConsign_());
					receive.setCallpassFare(receiveModel.getCallpassFare());
					receive.setIndividual("00:00:00"); // TODO : delete
					receive.setStart("00:00:00"); // TODO : delete
					receive.setPickupTime("00:00:00"); // TODO : delete
					receive.setEnd("00:00:00"); // TODO : delete
					receive.setItem(receiveModel.getItem());
					receive.setSerialNum(receiveModel.getSerialNum());
					receive.setComment(receiveModel.getComment());
					receive.setShare(receiveModel.getShare());
					receive.setInternetReqYn(internetReqYn);
					receive.setReserve(receiveModel.getReserve());
					receive.setReserveDate(reserveDate);
					receive.setReserveHour(receiveModel.getReserveHour());
					receive.setReserveMinute(receiveModel.getReserveMinute());
					receive.setSelyoilrepeat(receiveModel.getSelYoilRepeat());
					receive.setSeldayrepeat(receiveModel.getSelDayRepeat());
					receive.setSdtrepeat(receiveModel.getSdtRepeat());
					receive.setEdtrepeat(receiveModel.getEdtRepeat());
					receive.setReservedv(Integer.toString(receiveModel.getReserveDv()));
					receive.setKeyreserverpeat(keyReserveRpeat);
					receive.setMemberId(receiveModel.getMemberId());
					receive.setMember(""); // TODO : delete
					receive.setModifyId(""); // TODO : delete
					receive.setModify(""); // TODO : delete
					receive.setCargoShareYn(receiveModel.getCargoShareYn());
					receive.setCargoNum(receiveModel.getCargoNum());
					receive.setCargoConsign(receiveModel.getCargoConsign());
					receive.setCallDatetime(receiveModel.getCallDatetime());
					receive.setCallExt(receiveModel.getCallExt());
					receive.setCallClipNum(receiveModel.getCallClipNum());
					insertedRow = receiveMapper.insertSelective(receive);
					
					// 의뢰자
					clientRc = new ReceiveCustomer();
					clientRc.setBranchNum(receiveModel.getBranchNum());
					clientRc.setBranchName(""); // TODO : delete
					clientRc.setReceiveNum(receive.getNum());
					clientRc.setRcvCustType("client");
					clientRc.setCnum(receiveModel.getClient().getCnum());
					clientRc.setCname(receiveModel.getClient().getCname());
					clientRc.setDepartment(receiveModel.getClient().getDepartment());
					clientRc.setPerson(receiveModel.getClient().getPerson());
					clientRc.setTel(receiveModel.getClient().getTel());
					clientRc.setPhone(receiveModel.getClient().getPhone());
					clientRc.setLocJusoNum(receiveModel.getClient().getJusoNum());
					clientRc.setLocSido(receiveModel.getClient().getSido());
					clientRc.setLocGugun(receiveModel.getClient().getGugun());
					clientRc.setLocDong(receiveModel.getClient().getDong());
					clientRc.setLocDongNum(receiveModel.getClient().getDongNum());
					clientRc.setLocPosition(receiveModel.getClient().getPosition());
					clientRc.setLocBuilding(receiveModel.getClient().getBuilding());
					clientRc.setLocFloor(receiveModel.getClient().getFloor());
					clientRc.setLocEtc(receiveModel.getClient().getEtc());
					clientRc.setLocDetail(receiveModel.getClient().getDetail());
					clientRc.setMemo(receiveModel.getClient().getMemo());
					clientRc.setGroupNum(String.valueOf(clientGroupNum));
					insertedRow += receiveCustomerMapper.insertSelective(clientRc);
					
					// 출발지
					startRc = new ReceiveCustomer();
					startRc.setBranchNum(receiveModel.getBranchNum());
					startRc.setBranchName(""); // TODO : delete
					startRc.setReceiveNum(receive.getNum());
					startRc.setRcvCustType("start");
					startRc.setCnum(receiveModel.getStart().getCnum());
					startRc.setCname(receiveModel.getStart().getCname());
					startRc.setDepartment(receiveModel.getStart().getDepartment());
					startRc.setPerson(receiveModel.getStart().getPerson());
					startRc.setTel(receiveModel.getStart().getTel());
					startRc.setPhone(receiveModel.getStart().getPhone());
					startRc.setLocJusoNum(receiveModel.getStart().getJusoNum());
					startRc.setLocSido(receiveModel.getStart().getSido());
					startRc.setLocGugun(receiveModel.getStart().getGugun());
					startRc.setLocDong(receiveModel.getStart().getDong());
					startRc.setLocDongNum(receiveModel.getStart().getDongNum());
					startRc.setLocPosition(receiveModel.getStart().getPosition());
					startRc.setLocBuilding(receiveModel.getStart().getBuilding());
					startRc.setLocFloor(receiveModel.getStart().getFloor());
					startRc.setLocEtc(receiveModel.getStart().getEtc());
					startRc.setLocDetail(receiveModel.getStart().getDetail());
					startRc.setMemo(receiveModel.getStart().getMemo());
					startRc.setGroupNum(String.valueOf(startGroupNum));
					insertedRow += receiveCustomerMapper.insertSelective(startRc);
					
					// 도착지
					endRc = new ReceiveCustomer();
					endRc.setBranchNum(receiveModel.getBranchNum());
					endRc.setBranchName(""); // TODO : delete
					endRc.setReceiveNum(receive.getNum());
					endRc.setRcvCustType("end");
					endRc.setCnum(receiveModel.getEnd().getCnum());
					endRc.setCname(receiveModel.getEnd().getCname());
					endRc.setDepartment(receiveModel.getEnd().getDepartment());
					endRc.setPerson(receiveModel.getEnd().getPerson());
					endRc.setTel(receiveModel.getEnd().getTel());
					endRc.setPhone(receiveModel.getEnd().getPhone());
					endRc.setLocJusoNum(receiveModel.getEnd().getJusoNum());
					endRc.setLocSido(receiveModel.getEnd().getSido());
					endRc.setLocGugun(receiveModel.getEnd().getGugun());
					endRc.setLocDong(receiveModel.getEnd().getDong());
					endRc.setLocDongNum(receiveModel.getEnd().getDongNum());
					endRc.setLocPosition(receiveModel.getEnd().getPosition());
					endRc.setLocBuilding(receiveModel.getEnd().getBuilding());
					endRc.setLocFloor(receiveModel.getEnd().getFloor());
					endRc.setLocEtc(receiveModel.getEnd().getEtc());
					endRc.setLocDetail(receiveModel.getEnd().getDetail());
					endRc.setMemo(receiveModel.getEnd().getMemo());
					endRc.setGroupNum(String.valueOf(endGroupNum));
					insertedRow += receiveCustomerMapper.insertSelective(endRc);
					
					// receiveTemp
					setReceiveHistory(receive, clientRc, startRc, endRc);
				}
			}
		} else {
			if (receiveModel.getReserve() == 1 && receiveModel.getReserveDv() == 0) {
				reserveDate = receiveModel.getReserveDate();
				reserveYear = reserveDate.substring(6);
				reserveMonth = reserveDate.substring(0, 2);
				reserveDayOfMonth = reserveDate.substring(3, 5);
				reserveHour = String.format("%02d", receiveModel.getReserveHour());
				reserveMinute = String.format("%02d", receiveModel.getReserveMinute());
				datetime = sdfDt.parse(reserveYear + reserveMonth + reserveDayOfMonth + reserveHour + reserveMinute
						+ "00");
				
				// 예약시간이 현재시간보다 한시간 이전이면 바로 접수
				if (-1000 * 60 * 60 < datetime.getTime() - new Date().getTime()
						&& datetime.getTime() - new Date().getTime() < 0) {
					receiveModel.setReserve(0);
					datetime = new Date();
				}
			} else {
				datetime = new Date();
			}
			// 접수
			receive = new Receive();
			receive.setBranchNum(receiveModel.getBranchNum());
			receive.setBranchName(""); // TODO : delete
			receive.setStatus(receiveModel.getStatus());
			
			receive.setCnum1(receiveModel.getClient().getCnum());
			receive.setCnum2(receiveModel.getStart().getCnum());
			receive.setCnum3(receiveModel.getEnd().getCnum());
			
			receive.setStartJusoNum(receiveModel.getStart().getJusoNum());
			receive.setStartSido(receiveModel.getStart().getSido());
			receive.setStartGugun(receiveModel.getStart().getGugun());
			receive.setStartDong(receiveModel.getStart().getDong());
			receive.setStartEtc(receiveModel.getStart().getEtc());
			receive.setStartPosition(receiveModel.getStart().getPosition());
			receive.setEndJusoNum(receiveModel.getEnd().getJusoNum());
			receive.setEndSido(receiveModel.getEnd().getSido());
			receive.setEndGugun(receiveModel.getEnd().getGugun());
			receive.setEndDong(receiveModel.getEnd().getDong());
			receive.setEndEtc(receiveModel.getEnd().getEtc());
			receive.setEndPosition(receiveModel.getEnd().getPosition());
			
			receive.setDatetime(datetime);
			receive.setShuttle(receiveModel.getShuttle());
			receive.setQuick(receiveModel.getQuick());
			receive.setAuto(receiveModel.getAuto());
			receive.setWeight(receiveModel.getWeight());
			receive.setPayment(receiveModel.getPayment());
			receive.setDriverNum(receiveModel.getDriverNum());
			receive.setDriverName(""); // TODO : delete
			receive.setDriverNum1(0); // TODO : delete
			receive.setDriverNum2(0); // TODO : delete
			receive.setDriverNum3(0); // TODO : delete
			receive.setDriverDatetime(new Date()); // TODO : delete
			receive.setExtDriver(receiveModel.getExtDriver());
			receive.setFareType(receiveModel.getFareType());
			receive.setFare(receiveModel.getFare());
			receive.setFareOrigin(receiveModel.getFareOrigin());
			receive.setFareWeight(receiveModel.getFareWeight());
			receive.setFareEtc(receiveModel.getFareEtc());
			receive.setFareNight(receiveModel.getFareNight());
			receive.setFareRain(receiveModel.getFareRain());
			receive.setFareHoliday(receiveModel.getFareHoliday());
			receive.setFareDiscount(receiveModel.getFareDiscount());
			receive.setFareDiscount2(receiveModel.getFareDiscount2());
			receive.setFare_(receiveModel.getFare_());
			receive.setConsign_(receiveModel.getConsign_());
			receive.setCallpassFare(receiveModel.getCallpassFare());
			receive.setIndividual("00:00:00"); // TODO : delete
			receive.setStart("00:00:00"); // TODO : delete
			receive.setPickupTime("00:00:00"); // TODO : delete
			receive.setEnd("00:00:00"); // TODO : delete
			receive.setItem(receiveModel.getItem());
			receive.setSerialNum(receiveModel.getSerialNum());
			receive.setComment(receiveModel.getComment());
			receive.setShare(receiveModel.getShare());
			receive.setInternetReqYn(internetReqYn);
			receive.setReserve(receiveModel.getReserve());
			receive.setReserveDate(receiveModel.getReserveDate());
			receive.setReserveHour(receiveModel.getReserveHour());
			receive.setReserveMinute(receiveModel.getReserveMinute());
			receive.setSelyoilrepeat("");
			receive.setSeldayrepeat("");
			receive.setSdtrepeat("");
			receive.setEdtrepeat("");
			receive.setReservedv(Integer.toString(receiveModel.getReserveDv()));
			receive.setKeyreserverpeat("");
			receive.setMemberId(receiveModel.getMemberId());
			receive.setMember(""); // TODO : delete
			receive.setModifyId(""); // TODO : delete
			receive.setModify(""); // TODO : delete
			receive.setCargoShareYn(receiveModel.getCargoShareYn());
			receive.setCargoNum(receiveModel.getCargoNum());
			receive.setCargoConsign(receiveModel.getCargoConsign());
			receive.setCallDatetime(receiveModel.getCallDatetime());
			receive.setCallExt(receiveModel.getCallExt());
			receive.setCallClipNum(receiveModel.getCallClipNum());
			insertedRow = receiveMapper.insertSelective(receive);
			
			// 의뢰자
			clientRc = new ReceiveCustomer();
			clientRc.setBranchNum(receiveModel.getBranchNum());
			clientRc.setBranchName(""); // TODO : delete
			clientRc.setReceiveNum(receive.getNum());
			clientRc.setRcvCustType("client");
			clientRc.setCnum(receiveModel.getClient().getCnum());
			clientRc.setCname(receiveModel.getClient().getCname());
			clientRc.setDepartment(receiveModel.getClient().getDepartment());
			clientRc.setPerson(receiveModel.getClient().getPerson());
			clientRc.setTel(receiveModel.getClient().getTel());
			clientRc.setPhone(receiveModel.getClient().getPhone());
			clientRc.setLocJusoNum(receiveModel.getClient().getJusoNum());
			clientRc.setLocSido(receiveModel.getClient().getSido());
			clientRc.setLocGugun(receiveModel.getClient().getGugun());
			clientRc.setLocDong(receiveModel.getClient().getDong());
			clientRc.setLocDongNum(receiveModel.getClient().getDongNum());
			clientRc.setLocPosition(receiveModel.getClient().getPosition());
			clientRc.setLocBuilding(receiveModel.getClient().getBuilding());
			clientRc.setLocFloor(receiveModel.getClient().getFloor());
			clientRc.setLocEtc(receiveModel.getClient().getEtc());
			clientRc.setLocDetail(receiveModel.getClient().getDetail());
			clientRc.setMemo(receiveModel.getClient().getMemo());
			clientRc.setGroupNum(String.valueOf(clientGroupNum));
			insertedRow += receiveCustomerMapper.insertSelective(clientRc);
			
			// 출발지
			startRc = new ReceiveCustomer();
			startRc.setBranchNum(receiveModel.getBranchNum());
			startRc.setBranchName(""); // TODO : delete
			startRc.setReceiveNum(receive.getNum());
			startRc.setRcvCustType("start");
			startRc.setCnum(receiveModel.getStart().getCnum());
			startRc.setCname(receiveModel.getStart().getCname());
			startRc.setDepartment(receiveModel.getStart().getDepartment());
			startRc.setPerson(receiveModel.getStart().getPerson());
			startRc.setTel(receiveModel.getStart().getTel());
			startRc.setPhone(receiveModel.getStart().getPhone());
			startRc.setLocJusoNum(receiveModel.getStart().getJusoNum());
			startRc.setLocSido(receiveModel.getStart().getSido());
			startRc.setLocGugun(receiveModel.getStart().getGugun());
			startRc.setLocDong(receiveModel.getStart().getDong());
			startRc.setLocDongNum(receiveModel.getStart().getDongNum());
			startRc.setLocPosition(receiveModel.getStart().getPosition());
			startRc.setLocBuilding(receiveModel.getStart().getBuilding());
			startRc.setLocFloor(receiveModel.getStart().getFloor());
			startRc.setLocEtc(receiveModel.getStart().getEtc());
			startRc.setLocDetail(receiveModel.getStart().getDetail());
			startRc.setMemo(receiveModel.getStart().getMemo());
			startRc.setGroupNum(String.valueOf(startGroupNum));
			insertedRow += receiveCustomerMapper.insertSelective(startRc);
			
			// 도착지
			endRc = new ReceiveCustomer();
			endRc.setBranchNum(receiveModel.getBranchNum());
			endRc.setBranchName(""); // TODO : delete
			endRc.setReceiveNum(receive.getNum());
			endRc.setRcvCustType("end");
			endRc.setCnum(receiveModel.getEnd().getCnum());
			endRc.setCname(receiveModel.getEnd().getCname());
			endRc.setDepartment(receiveModel.getEnd().getDepartment());
			endRc.setPerson(receiveModel.getEnd().getPerson());
			endRc.setTel(receiveModel.getEnd().getTel());
			endRc.setPhone(receiveModel.getEnd().getPhone());
			endRc.setLocJusoNum(receiveModel.getEnd().getJusoNum());
			endRc.setLocSido(receiveModel.getEnd().getSido());
			endRc.setLocGugun(receiveModel.getEnd().getGugun());
			endRc.setLocDong(receiveModel.getEnd().getDong());
			endRc.setLocDongNum(receiveModel.getEnd().getDongNum());
			endRc.setLocPosition(receiveModel.getEnd().getPosition());
			endRc.setLocBuilding(receiveModel.getEnd().getBuilding());
			endRc.setLocFloor(receiveModel.getEnd().getFloor());
			endRc.setLocEtc(receiveModel.getEnd().getEtc());
			endRc.setLocDetail(receiveModel.getEnd().getDetail());
			endRc.setMemo(receiveModel.getEnd().getMemo());
			endRc.setGroupNum(String.valueOf(endGroupNum));
			insertedRow += receiveCustomerMapper.insertSelective(endRc);
			
			// receiveTemp
			setReceiveHistory(receive, clientRc, startRc, endRc);
		}
		
		// 접수 문자
		if ("접수".equals(receiveModel.getStatus())) {
			smsService.sendReceiveSms(receive);
		}
		
		if (insertedRow == 4) {
			resultCode = 1000;
		}
		
		resultMap.put("resultCode", resultCode);
		resultMap.put("receiveNum", receive.getNum());
		return resultMap;
	}
	
	public Map<String, Object> setReceiveExcel(int branchNum, int clientNum, String shuttle, String quick, String auto,
			String weight, String payment, String invoiceNum, String orderNum, String item, String comment,
			String startCname, String startDepartment, String startPerson, String startTel, String startPhone,
			String startSido, String startGugun, String startDong, String startDongNum, String startDetail,
			String endCname, String endDepartment, String endPerson, String endTel, String endPhone, String endSido,
			String endGugun, String endDong, String endDongNum, String endDetail) {
		int resultCode = 9999;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int insertedRow = 0;
		
		Date datetime = new Date();
		
		Receive receive = null;
		ReceiveCustomer clientRc = null;
		ReceiveCustomer startRc = null;
		ReceiveCustomer endRc = null;
		
		if (!"편도".equals(shuttle) && !"왕복".equals(shuttle) && !"경유".equals(shuttle)) {
			shuttle = "편도";
		}
		if (!"일반".equals(quick) && !"긴급".equals(quick)) {
			quick = "일반";
		}
		if (!"오토".equals(auto) && !"다마".equals(auto) && !"라보".equals(auto) && !"벤".equals(auto) && !"1t".equals(auto)
				&& !"1.4t".equals(auto) && !"2.5t".equals(auto) && !"3.5t".equals(auto) && !"5t".equals(auto)) {
			auto = "오토";
		}
		if (!"선불".equals(payment) && !"착불".equals(payment) && !"송금".equals(payment) && !"외상".equals(payment)
				&& !"카드".equals(payment)) {
			payment = "선불";
		}
		
		int startJusoNum = 0;
		int endJusoNum = 0;
		String startPosition = "";
		String endPosition = "";
		
		String serialNum = "";
		if (!"".equals(invoiceNum)) {
			serialNum = invoiceNum;
		}
		
		if (!"".equals(orderNum)) {
			item = item + "(주문번호:" + orderNum + ")";
		}
		
		// JusoExample jusoExample = new JusoExample();
		// jusoExample.createCriteria().andSidoEqualTo(clientSido).andGugunEqualTo(clientGugun).andDongEqualTo(clientDong);
		// List<Juso> clientJusoList = jusoMapper.selectByExample(jusoExample);
		//
		// if (clientJusoList.size() > 0) {
		// clientJusoNum = clientJusoList.get(0).getNum();
		// clientPosition = clientJusoList.get(0).getLatitude() + "|" + clientJusoList.get(0).getLongitude();
		// }
		
		JusoExample jusoExample = new JusoExample();
		jusoExample.createCriteria().andSidoEqualTo(startSido).andGugunEqualTo(startGugun).andDongEqualTo(startDong);
		List<Juso> startJusoList = jusoMapper.selectByExample(jusoExample);
		
		if (startJusoList.size() > 0) {
			startJusoNum = startJusoList.get(0).getNum();
			startPosition = startJusoList.get(0).getLatitude() + "|" + startJusoList.get(0).getLongitude();
		}
		jusoExample = new JusoExample();
		jusoExample.createCriteria().andSidoEqualTo(endSido).andGugunEqualTo(endGugun).andDongEqualTo(endDong);
		List<Juso> endJusoList = jusoMapper.selectByExample(jusoExample);
		
		if (endJusoList.size() > 0) {
			endJusoNum = endJusoList.get(0).getNum();
			endPosition = endJusoList.get(0).getLatitude() + "|" + endJusoList.get(0).getLongitude();
		}
		//
		// CustomerModel customerModel = new CustomerModel();
		// customerModel.setBranchNum(branchNum);
		// customerModel.setGroupNum(0);
		// customerModel.setCname(clientCname);
		// customerModel.setDepartment(clientDepartment);
		// customerModel.setPerson(clientPerson);
		// customerModel.setTel(clientTel);
		// customerModel.setPhone(clientPhone);
		// customerModel.setId("");
		// customerModel.setPw("");
		// customerModel.setJusoNum(clientJusoNum);
		// customerModel.setSido(clientSido);
		// customerModel.setGugun(clientGugun);
		// customerModel.setDong(clientDong);
		// customerModel.setDongNum(clientDongNum);
		// customerModel.setPosition(clientPosition);
		// customerModel.setBuilding("");
		// customerModel.setFloor("");
		// customerModel.setEtc(clientDong);
		// customerModel.setDetail(clientDetail);
		// customerModel.setMemo("엑셀 접수 고객");
		// Map<String, Object> setCustomerResultMap = customerService.setCustomer(customerModel);
		// int clientNum = 0;
		// if (setCustomerResultMap != null) {
		// clientNum = (Integer) setCustomerResultMap.get("customerNum");
		// }
		
		Customer customer = customerMapper.selectByPrimaryKey(clientNum);
		if (customer != null) {
			
			receive = new Receive();
			receive.setBranchNum(branchNum);
			receive.setBranchName(""); // TODO : delete
			receive.setStatus("대기");
			
			receive.setCnum1(clientNum);
			receive.setCnum2(0);
			receive.setCnum3(0);
			
			receive.setStartJusoNum(startJusoNum);
			receive.setStartSido(startSido);
			receive.setStartGugun(startGugun);
			receive.setStartDong(startDong);
			receive.setStartEtc(startDong);
			receive.setStartPosition(startPosition);
			receive.setEndJusoNum(endJusoNum);
			receive.setEndSido(endSido);
			receive.setEndGugun(endGugun);
			receive.setEndDong(endDong);
			receive.setEndEtc(endDong);
			receive.setEndPosition(endPosition);
			
			receive.setDatetime(datetime);
			receive.setShuttle(shuttle);
			receive.setQuick(quick);
			receive.setAuto(auto);
			receive.setWeight(getWeight(weight));
			receive.setPayment(payment);
			receive.setDriverNum(0);
			receive.setDriverName(""); // TODO : delete
			receive.setDriverNum1(0); // TODO : delete
			receive.setDriverNum2(0); // TODO : delete
			receive.setDriverNum3(0); // TODO : delete
			receive.setDriverDatetime(new Date()); // TODO : delete
			receive.setExtDriver("//");
			receive.setFareType(0);
			receive.setFare(0);
			receive.setFareOrigin(0);
			receive.setFareWeight(0);
			receive.setFareEtc(0);
			receive.setFareNight(0);
			receive.setFareRain(0);
			receive.setFareHoliday(0);
			receive.setFareDiscount(0);
			receive.setFareDiscount2(0);
			receive.setFare_(0);
			receive.setConsign_(0);
			receive.setCallpassFare(0);
			receive.setIndividual("00:00:00"); // TODO : delete
			receive.setStart("00:00:00"); // TODO : delete
			receive.setPickupTime("00:00:00"); // TODO : delete
			receive.setEnd("00:00:00"); // TODO : delete
			receive.setItem(item);
			receive.setSerialNum(serialNum);
			receive.setComment(comment);
			receive.setShare(1);
			receive.setInternetReqYn("N");
			receive.setReserve(0);
			receive.setReserveDate("");
			receive.setReserveHour(0);
			receive.setReserveMinute(0);
			receive.setSelyoilrepeat("");
			receive.setSeldayrepeat("");
			receive.setSdtrepeat("");
			receive.setEdtrepeat("");
			receive.setReservedv("0");
			receive.setKeyreserverpeat("");
			receive.setMemberId("엑셀등록");
			receive.setMember(""); // TODO : delete
			receive.setModifyId(""); // TODO : delete
			receive.setModify(""); // TODO : delete
			receive.setCargoShareYn("N");
			receive.setCargoNum(0);
			receive.setCargoConsign(0);
			receive.setCallDatetime("");
			receive.setCallExt("");
			receive.setCallClipNum("");
			insertedRow = receiveMapper.insertSelective(receive);
			
			// 의뢰자
			clientRc = new ReceiveCustomer();
			clientRc.setBranchNum(branchNum);
			clientRc.setBranchName(""); // TODO : delete
			clientRc.setReceiveNum(receive.getNum());
			clientRc.setRcvCustType("client");
			clientRc.setCnum(clientNum);
			clientRc.setCname(customer.getCname());
			clientRc.setDepartment(customer.getDepartment());
			clientRc.setPerson(customer.getPerson());
			clientRc.setTel(customer.getTel());
			clientRc.setPhone(customer.getPhone());
			clientRc.setLocJusoNum(customer.getStartJusoNum());
			clientRc.setLocSido(customer.getStartSido());
			clientRc.setLocGugun(customer.getStartGugun());
			clientRc.setLocDong(customer.getStartDong());
			clientRc.setLocDongNum(customer.getStartDongNum());
			clientRc.setLocPosition(customer.getStartPosition());
			clientRc.setLocBuilding(customer.getStartBuilding());
			clientRc.setLocFloor(customer.getStartFloor());
			clientRc.setLocEtc(customer.getStartEtc());
			clientRc.setLocDetail(customer.getStartDetail());
			clientRc.setMemo(customer.getMemo());
			clientRc.setGroupNum(String.valueOf(customer.getGroupNum()));
			insertedRow += receiveCustomerMapper.insertSelective(clientRc);
			
			// 출발지
			startRc = new ReceiveCustomer();
			startRc.setBranchNum(branchNum);
			startRc.setBranchName(""); // TODO : delete
			startRc.setReceiveNum(receive.getNum());
			startRc.setRcvCustType("start");
			startRc.setCnum(0);
			startRc.setCname(startCname);
			startRc.setDepartment(startDepartment);
			startRc.setPerson(startPerson);
			startRc.setTel(startTel);
			startRc.setPhone(startPhone);
			startRc.setLocJusoNum(startJusoNum);
			startRc.setLocSido(startSido);
			startRc.setLocGugun(startGugun);
			startRc.setLocDong(startDong);
			startRc.setLocDongNum(startDongNum);
			startRc.setLocPosition(startPosition);
			startRc.setLocBuilding("");
			startRc.setLocFloor("");
			startRc.setLocEtc(startDong);
			startRc.setLocDetail(startDetail);
			startRc.setMemo("");
			startRc.setGroupNum("0");
			insertedRow += receiveCustomerMapper.insertSelective(startRc);
			
			// 도착지
			endRc = new ReceiveCustomer();
			endRc.setBranchNum(branchNum);
			endRc.setBranchName(""); // TODO : delete
			endRc.setReceiveNum(receive.getNum());
			endRc.setRcvCustType("end");
			endRc.setCnum(0);
			endRc.setCname(endCname);
			endRc.setDepartment(endDepartment);
			endRc.setPerson(endPerson);
			endRc.setTel(endTel);
			endRc.setPhone(endPhone);
			endRc.setLocJusoNum(endJusoNum);
			endRc.setLocSido(endSido);
			endRc.setLocGugun(endGugun);
			endRc.setLocDong(endDong);
			endRc.setLocDongNum(endDongNum);
			endRc.setLocPosition(endPosition);
			endRc.setLocBuilding("");
			endRc.setLocFloor("");
			endRc.setLocEtc(endDong);
			endRc.setLocDetail(endDetail);
			endRc.setMemo("");
			endRc.setGroupNum("0");
			insertedRow += receiveCustomerMapper.insertSelective(endRc);
			
			// receiveTemp
			setReceiveHistory(receive, clientRc, startRc, endRc);
		}
		
		if (insertedRow == 4) {
			resultCode = 1000;
		}
		
		resultMap.put("resultCode", resultCode);
		resultMap.put("receiveNum", receive.getNum());
		return resultMap;
	}
	
	private void setReceiveHistory(Receive receive, ReceiveCustomer clientRc, ReceiveCustomer startRc,
			ReceiveCustomer endRc) {
		ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
		receiveTemp.setNum(receive.getNum());
		receiveTemp.setBranchNum(receive.getBranchNum());
		receiveTemp.setBranchName(""); // TODO : delete
		receiveTemp.setStatus(receive.getStatus());
		receiveTemp.setCnum1(clientRc.getCnum());
		receiveTemp.setCnum2(startRc.getCnum());
		receiveTemp.setCnum3(endRc.getCnum());
		receiveTemp.setStartJusoNum(startRc.getLocJusoNum());
		receiveTemp.setStartSido(startRc.getLocSido());
		receiveTemp.setStartGugun(startRc.getLocGugun());
		receiveTemp.setStartDong(startRc.getLocDong());
		receiveTemp.setStartPosition(startRc.getLocPosition());
		receiveTemp.setStartEtc(startRc.getLocEtc());
		receiveTemp.setEndJusoNum(endRc.getLocJusoNum());
		receiveTemp.setEndSido(endRc.getLocSido());
		receiveTemp.setEndGugun(endRc.getLocGugun());
		receiveTemp.setEndDong(endRc.getLocDong());
		receiveTemp.setEndPosition(endRc.getLocPosition());
		receiveTemp.setEndEtc(endRc.getLocEtc());
		receiveTemp.setShuttle(receive.getShuttle());
		receiveTemp.setQuick(receive.getQuick());
		receiveTemp.setAuto(receive.getAuto());
		receiveTemp.setWeight(receive.getWeight());
		receiveTemp.setPayment(receive.getPayment());
		receiveTemp.setFareType(receive.getFareType());
		receiveTemp.setFare(receive.getFare());
		receiveTemp.setFareOrigin(receive.getFareOrigin());
		receiveTemp.setFareWeight(receive.getFareWeight());
		receiveTemp.setFareNight(receive.getFareNight());
		receiveTemp.setFareRain(receive.getFareRain());
		receiveTemp.setFareHoliday(receive.getFareHoliday());
		receiveTemp.setFareEtc(receive.getFareEtc());
		receiveTemp.setFareDiscount(receive.getFareDiscount());
		receiveTemp.setFareDiscount2(receive.getFareDiscount2());
		receiveTemp.setFare_(receive.getFare_());
		receiveTemp.setConsign_(receive.getConsign_());
		receiveTemp.setCallpassFare(receive.getCallpassFare());
		receiveTemp.setShare(receive.getShare());
		receiveTemp.setSerialNum(receive.getSerialNum());
		receiveTemp.setMemberId(receive.getMemberId());
		receiveTemp.setModifyId(receive.getModifyId());
		receiveTemp.setDatetime(new Date());
		receiveTemp.setEditlist("");
		receiveTempMapper.insertSelective(receiveTemp);
	}
	
	public Receive getReceive(int num) {
		return receiveMapper.selectByPrimaryKey(num);
	}
	
	public List<Map<String, Object>> getReceive(String callGroupBranchNums, int branchNum, String sDate, String eDate,
			boolean chkStatusAll, boolean chkStatusReceive, boolean chkStatusIndividual, boolean chkStatusWait,
			boolean chkStatusBecha, boolean chkStatusPickup, boolean chkStatusEnd, boolean chkStatusCancel,
			boolean chkStatusReserve, boolean chkStatusInternet, boolean chkStatusCallpass, boolean callGroupAll,
			boolean chkVehicleTotal, boolean chkAuto, boolean chkDamas, boolean chkLabo, boolean chkVan,
			boolean chkTruck, String searchWord, String searchOption) {
		
		List<ReceiveCustomerDriverModel> receiveMapList = receiveExMapper.selectReceive(callGroupBranchNums, branchNum,
				sDate, eDate, chkStatusAll, chkStatusReceive, chkStatusIndividual, chkStatusWait, chkStatusBecha,
				chkStatusPickup, chkStatusEnd, chkStatusCancel, chkStatusReserve, chkStatusInternet, chkStatusCallpass,
				callGroupAll, chkVehicleTotal, chkAuto, chkDamas, chkLabo, chkVan, chkTruck, searchWord, searchOption);
		
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		if (receiveMapList.size() > 0) {
			resultMapList = ObjectUtil.getMapListToElementList(receiveMapList);
		}
		
		return resultMapList;
	}
	
	public Map<String, Object> getReceiveByNum(int receiveNum) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> receiveMap = receiveShareOrderMapper.selectReceiveByNum(receiveNum);
		
		if (receiveMap != null) {
			resultMap.put("receive", receiveMap);
			
			ReceiveCustomerExample receiveCustomerExample = new ReceiveCustomerExample();
			receiveCustomerExample.createCriteria().andReceiveNumEqualTo(receiveNum);
			List<ReceiveCustomer> receiveCustomerList = receiveCustomerMapper.selectByExample(receiveCustomerExample);
			if (receiveCustomerList.size() > 0) {
				for (ReceiveCustomer receiveCustomer : receiveCustomerList) {
					resultMap.put(receiveCustomer.getRcvCustType(), ObjectUtil.getMapToElement(receiveCustomer));
				}
			} else {
				resultMap.put("client", new HashMap<String, Object>());
				resultMap.put("start", new HashMap<String, Object>());
				resultMap.put("end", new HashMap<String, Object>());
			}
			
			Map<String, Object> driverMap = new HashMap<String, Object>();
			if ((Integer) receiveMap.get("driverNum") != 0) {
				Map<String, Object> driverTempMap = new HashMap<String, Object>();
				driverTempMap = employeeBranchMapper.selectDriverByNum((Integer) receiveMap.get("driverNum"));
				if (driverTempMap != null) {
					driverMap = driverTempMap;
				}
			}
			resultMap.put("driver", driverMap);
		}
		return resultMap;
	}
	
	public Map<String, Object> getReceiveByCallInfo(String callDatetime, String callExt, String callClipNum) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int receiveNum = 0;
		
		ReceiveExample receiveExample = new ReceiveExample();
		receiveExample.createCriteria().andCallDatetimeEqualTo(callDatetime).andCallExtEqualTo(callExt)
				.andCallClipNumEqualTo(callClipNum);
		List<Receive> receiveList = receiveMapper.selectByExample(receiveExample);
		
		if (receiveList.size() == 1) {
			Receive receive = receiveList.get(0);
			receiveNum = receive.getNum();
		}
		
		resultMap.put("receiveNum", receiveNum);
		
		return resultMap;
	}
	
	public Map<String, Object> getReceiveCustomerDriverByNum(int receiveNum) {
		ReceiveCustomerDriverModel receiveCustomerDriverModel = receiveShareOrderMapper
				.selectReceiveCustomerDriverByNum(receiveNum);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (receiveCustomerDriverModel != null) {
			resultMap = ObjectUtil.getMapToElement(receiveCustomerDriverModel);
		}
		
		return resultMap;
	}
	
	public List<Map<String, Object>> getReceiveBySerialNum(int branchNum, String memberId, String serialNum) {
		ReceiveExample receiveExample = new ReceiveExample();
		receiveExample.createCriteria().andBranchNumEqualTo(branchNum).andMemberIdEqualTo(memberId)
				.andSerialNumEqualTo(serialNum);
		List<Receive> receiveList = receiveMapper.selectByExample(receiveExample);
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (receiveList.size() > 0) {
			resultList = ObjectUtil.getMapListToElementList(receiveList);
		}
		return resultList;
	}
	
	public List<Map<String, Object>> getReceiveByDriverNum(int driverNum) {
		List<ReceiveCustomerDriverModel> receiveMapList = receiveShareOrderMapper.selectReceiveByDriverNum(driverNum);
		
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		if (receiveMapList.size() > 0) {
			resultMapList = ObjectUtil.getMapListToElementList(receiveMapList);
		}
		
		return resultMapList;
	}
	
	public Map<String, Object> getReceiveDetail(int receiveNum, int employeeNum, int tab) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int resultCode = 9999;
		String resultMessage = "";
		
		resultMap = customerReceiveMapper.selectReceiveDetail(receiveNum);
		if (resultMap == null) {
			resultCode = 3001;
			resultMessage = "오더가 확인되지 않습니다.";
		} else {
			resultCode = 1000;
			if ("기사".equals(resultMap.get("registerType")) || "픽업맨".equals(resultMap.get("registerType"))) {
				resultMap.put("branchName", "기사오더");
				resultMap.put("branchTel", resultMap.get("registerPhone"));
			} else if ((Integer) resultMap.get("nTNo") != 0 && (Long) resultMap.get("nCompany") != 1125) {
				resultMap.put("branchName", resultMap.get("sCompanyName"));
				resultMap.put("branchTel", resultMap.get("sCompanyOfficeTel"));
			}
			
			String status = (String) resultMap.get("status");
			int driverNum = (Integer) resultMap.get("driverNum");
			if (tab == 0) {
				if ("취소".equals(status)) {
					resultCode = 3002;
					resultMessage = "이미 취소된 오더입니다.";
				} else if (!"자동".equals(status) && !"개별".equals(status) && !"접수".equals(status)) {
					if (driverNum == employeeNum) {
						resultCode = 3003;
						resultMessage = "기사님께 이미 배차된 오더입니다. 배차 화면에서 확인해주세요.";
					} else {
						resultCode = 3004;
						resultMessage = "다른 기사님에게 먼저 배차된 오더입니다.";
					}
				}
			}
			if (tab == 1) {
				if (!"배차".equals(status) && !"픽업".equals(status)) {
					resultCode = 3005;
					resultMessage = "상태가 변경되었습니다.";
				}
			}
			if (tab == 2) {
				if (!"수거".equals(status) && !"종료".equals(status)) {
					resultCode = 3006;
					resultMessage = "상태가 변경되었습니다.";
				}
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Calendar cal = Calendar.getInstance();
			String start = sdf.format(resultMap.get("start"));
			
			if (tab == 1 && !"00:00".equals(start)) {
				cal.setTime((Date) resultMap.get("start"));
			}
			
			// 픽업권고시간
			cal.add(Calendar.MINUTE, 20);
			String pickupAdviceTime = sdf.format(cal.getTime());
			// 도착권고시간
			cal.add(Calendar.MINUTE, getRequiredTime((Integer) resultMap.get("fare_"), (String) resultMap.get("quick")));
			String endAdviceTime = sdf.format(cal.getTime());
			
			resultMap.put("pickupAdviceTime", pickupAdviceTime);
			resultMap.put("endAdviceTime", endAdviceTime);
		}
		
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	public List<Map<String, Object>> getReceiveList(String id, String workType, int callGroupNum, int branchNum,
			String sDate, String eDate, String searchStatus, int orderSearchIndex, String searchWord, int pageNum,
			int pageRow) {
		
		String branchNums = "";
		if (pageNum > 0) {
			pageNum = (pageNum - 1) * pageRow;
		}
		
		if (branchNum == 0 && callGroupNum > 0) {
			List<Map<String, Object>> branchList = branchService.getBranchByCallgroupNum(callGroupNum);
			for (int i = 0; i < branchList.size(); i++) {
				Map<String, Object> branch = branchList.get(i);
				int callGroupBranchNum = (Integer) branch.get("num");
				if (i == (branchList.size() - 1)) {
					branchNums += callGroupBranchNum;
				} else {
					branchNums += callGroupBranchNum + ", ";
				}
			}
		}
		
		List<Map<String, Object>> receiveList = new ArrayList<Map<String, Object>>();
		receiveList = receiveExMapper.selectReceiveList(id, workType, callGroupNum, branchNum, branchNums, sDate,
				eDate, searchStatus, orderSearchIndex, searchWord, pageNum, pageRow);
		
		return receiveList;
	}
	
	public List<Receive> getReserveReceiveList() {
		int reserve = 1; // 1 : 예약 상태
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		String reserveDate = sdf.format(date);
		
		ReceiveExample example = new ReceiveExample();
		example.createCriteria().andReserveEqualTo(reserve).andReserveDateEqualTo(reserveDate);
		example.setOrderByClause("reserve_date, reserve_hour, reserve_minute");
		return receiveMapper.selectByExample(example);
	}
	
	public List<Map<String, Object>> getReceiveHistory(int receiveNum) {
		ReceiveTempExample receiveTempExample = new ReceiveTempExample();
		receiveTempExample.createCriteria().andNumEqualTo(receiveNum);
		List<ReceiveTempWithBLOBs> receiveTempList = receiveTempMapper.selectByExampleWithBLOBs(receiveTempExample);
		
		List<Map<String, Object>> receiveHistoryList = new ArrayList<Map<String, Object>>();
		Map<String, Object> receiveHistoryMap;
		for (ReceiveTempWithBLOBs receiveTempWithBLOBs : receiveTempList) {
			receiveHistoryMap = new HashMap<String, Object>();
			receiveHistoryMap.put("status", receiveTempWithBLOBs.getStatus());
			receiveHistoryMap.put("datetime", receiveTempWithBLOBs.getDatetime());
			receiveHistoryMap.put("driverVnum", receiveTempWithBLOBs.getDriverVnum());
			receiveHistoryMap.put("driverName", receiveTempWithBLOBs.getDriverName());
			receiveHistoryMap.put("memberId", receiveTempWithBLOBs.getMemberId());
			receiveHistoryMap.put("fare", receiveTempWithBLOBs.getFare());
			receiveHistoryMap.put("editlist", receiveTempWithBLOBs.getEditlist());
			receiveHistoryList.add(receiveHistoryMap);
		}
		return receiveHistoryList;
	}
	
	public List<Map<String, Object>> getDriverDoingReceive(int branchNum, String startDate, String endDate,
			String exceptEndYn) {
		return driverReceiveMapper.selectDriverDoingReceive(branchNum, startDate, endDate, exceptEndYn);
	}
	
	public List<Map<String, Object>> getSharedReceive(int branchNum) {
		return receiveExMapper.selectSharedReceive(branchNum);
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> getDriverReceive(int employeeNum, int tab, int minFare, int maxFare,
			String vehicleTypeOption, int limitOption, double latitude, double longitude) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null && employee.getStopChk() == 0 && employee.getOutChk() == 0 && employee.getDel() == 0) {
			// 기사 위치 갱신
			if (latitude > 0 && longitude > 0) {
				Map<String, Object> jusoMap = jusoExMapper.selectNearDong(latitude, longitude);
				employeeBranchMapper.updateEmployeeLocation(employee.getNum(), latitude, longitude,
						(String) jusoMap.get("sido"), (String) jusoMap.get("gugun"), (String) jusoMap.get("dong"));
			}
			
			// 폰에서 서버 접속 딜레이값
			String connectDelay = commonCodeService.getValueByCode("app_dual_connect_delay");
			resultMap.put("connectDelay", Integer.parseInt(connectDelay));
			
			// 공지사항 확인
			int noticeCount = noticeService.getDriverNoticeCount(employee.getNum());
			resultMap.put("noticeCount", noticeCount);
			
			// 오더 확인
			List<Map<String, Object>> receiveList = new ArrayList<Map<String, Object>>();
			
			if (tab == 0) {
				// 개별오더 확인
				receiveList = driverReceiveMapper.selectIndividualReceive(employee.getNum(), employee.getWorkType());
			}
			
			if (receiveList.size() == 0) {
				String vehicleTypeIn = "";
				if (tab == 0) {
					try {
						String[] vehicleTypeOptionArr = vehicleTypeOption.split(",");
						String[] vehicleTypeArr = { "지하철", "오토", "다마", "라보", "벤", "1t", "1.4t", "2.5t", "3.5t", "5t" };
						for (int i = 0; i < vehicleTypeArr.length; i++) {
							if (!"0".equals(vehicleTypeOptionArr[i])) {
								if (vehicleTypeIn.length() > 0) {
									vehicleTypeIn += ",";
								}
								vehicleTypeIn += vehicleTypeArr[i];
							}
						}
					} catch (Exception e) {
						vehicleTypeIn = "";
					}
				}
				Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
				BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(branch.getGroupNum());
				receiveList = driverReceiveMapper.selectDriverReceive(employee.getNum(), employee.getBranchNum(),
						branchGroup.getNum(), branchGroup.getShareGroupNum(), tab, minFare, maxFare, vehicleTypeIn,
						limitOption);
			}
			resultMap.put("receiveCount", receiveList.size());
			resultMap.put("receiveList", receiveList);
		}
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> getDriverReceiveAll(int employeeNum, int tab, int minFare, int maxFare,
			String vehicleTypeOption, int limitOption, double latitude, double longitude) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null && employee.getStopChk() == 0 && employee.getOutChk() == 0 && employee.getDel() == 0) {
			// 기사 위치 갱신
			if (latitude > 0 && longitude > 0) {
				Map<String, Object> jusoMap = jusoExMapper.selectNearDong(latitude, longitude);
				employeeBranchMapper.updateEmployeeLocation(employee.getNum(), latitude, longitude,
						(String) jusoMap.get("sido"), (String) jusoMap.get("gugun"), (String) jusoMap.get("dong"));
			}
			
			// 폰에서 서버 접속 딜레이값
			String connectDelay = commonCodeService.getValueByCode("app_dual_connect_delay");
			resultMap.put("connectDelay", Integer.parseInt(connectDelay));
			
			// 공지사항 확인
			int noticeCount = noticeService.getDriverNoticeCount(employee.getNum());
			resultMap.put("noticeCount", noticeCount);
			
			// 오더 확인
			List<Map<String, Object>> receiveList = new ArrayList<Map<String, Object>>();
			
			if (tab == 0) {
				// 개별오더 확인
				receiveList = driverReceiveMapper.selectIndividualReceive(employee.getNum(), employee.getWorkType());
			}
			
			if (receiveList.size() == 0) {
				String vehicleTypeIn = "";
				if (tab == 0) {
					try {
						String[] vehicleTypeOptionArr = vehicleTypeOption.split(",");
						String[] vehicleTypeArr = { "지하철", "오토", "다마", "라보", "벤", "1t", "1.4t", "2.5t", "3.5t", "5t" };
						for (int i = 0; i < vehicleTypeArr.length; i++) {
							if (!"0".equals(vehicleTypeOptionArr[i])) {
								if (vehicleTypeIn.length() > 0) {
									vehicleTypeIn += ",";
								}
								vehicleTypeIn += vehicleTypeArr[i];
							}
						}
					} catch (Exception e) {
						vehicleTypeIn = "";
					}
				}
				receiveList = driverReceiveMapper.selectDriverReceiveAll(employee.getNum(), employee.getBranchNum(),
						tab, minFare, maxFare, vehicleTypeIn, limitOption);
			}
			resultMap.put("receiveCount", receiveList.size());
			resultMap.put("receiveList", receiveList);
		}
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> getPickupManReceive(int employeeNum, int tab, int minFare, int maxFare,
			String vehicleTypeOption, int limitOption, double latitude, double longitude) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null && employee.getStopChk() == 0 && employee.getOutChk() == 0 && employee.getDel() == 0) {
			// 기사 위치 갱신
			if (latitude > 0 && longitude > 0) {
				Map<String, Object> jusoMap = jusoExMapper.selectNearDong(latitude, longitude);
				employeeBranchMapper.updateEmployeeLocation(employee.getNum(), latitude, longitude,
						(String) jusoMap.get("sido"), (String) jusoMap.get("gugun"), (String) jusoMap.get("dong"));
			}
			
			// 폰에서 서버 접속 딜레이값
			String connectDelay = commonCodeService.getValueByCode("app_dual_connect_delay");
			resultMap.put("connectDelay", Integer.parseInt(connectDelay));
			
			// 공지사항 확인
			int noticeCount = noticeService.getDriverNoticeCount(employee.getNum());
			resultMap.put("noticeCount", noticeCount);
			
			// 오더 확인
			List<Map<String, Object>> receiveList = new ArrayList<Map<String, Object>>();
			
			if (tab == 0) {
				// 개별오더 확인
				receiveList = driverReceiveMapper.selectIndividualReceive(employee.getNum(), employee.getWorkType());
			}
			
			if (receiveList.size() == 0) {
				String vehicleTypeIn = "";
				if (tab == 0) {
					try {
						String[] vehicleTypeOptionArr = vehicleTypeOption.split(",");
						String[] vehicleTypeArr = { "지하철", "오토", "다마", "라보", "벤", "1t", "1.4t", "2.5t", "3.5t", "5t" };
						for (int i = 0; i < vehicleTypeArr.length; i++) {
							if (!"0".equals(vehicleTypeOptionArr[i])) {
								if (vehicleTypeIn.length() > 0) {
									vehicleTypeIn += ",";
								}
								vehicleTypeIn += vehicleTypeArr[i];
							}
						}
					} catch (Exception e) {
						vehicleTypeIn = "";
					}
				}
				Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
				BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(branch.getGroupNum());
				receiveList = driverReceiveMapper.selectPickupManReceive(employee.getNum(), employee.getBranchNum(),
						branchGroup.getNum(), branchGroup.getShareGroupNum(), tab, minFare, maxFare, vehicleTypeIn,
						limitOption);
			}
			resultMap.put("receiveCount", receiveList.size());
			resultMap.put("receiveList", receiveList);
		}
		
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> getPickupManReceiveAll(int employeeNum, int tab, int minFare, int maxFare,
			String vehicleTypeOption, int limitOption, double latitude, double longitude) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
		if (employee != null && employee.getStopChk() == 0 && employee.getOutChk() == 0 && employee.getDel() == 0) {
			// 기사 위치 갱신
			if (latitude > 0 && longitude > 0) {
				Map<String, Object> jusoMap = jusoExMapper.selectNearDong(latitude, longitude);
				employeeBranchMapper.updateEmployeeLocation(employee.getNum(), latitude, longitude,
						(String) jusoMap.get("sido"), (String) jusoMap.get("gugun"), (String) jusoMap.get("dong"));
			}
			
			// 폰에서 서버 접속 딜레이값
			String connectDelay = commonCodeService.getValueByCode("app_dual_connect_delay");
			resultMap.put("connectDelay", Integer.parseInt(connectDelay));
			
			// 공지사항 확인
			int noticeCount = noticeService.getDriverNoticeCount(employee.getNum());
			resultMap.put("noticeCount", noticeCount);
			
			// 오더 확인
			List<Map<String, Object>> receiveList = new ArrayList<Map<String, Object>>();
			
			if (tab == 0) {
				// 개별오더 확인
				receiveList = driverReceiveMapper.selectIndividualReceive(employee.getNum(), employee.getWorkType());
			}
			
			if (receiveList.size() == 0) {
				String vehicleTypeIn = "";
				if (tab == 0) {
					try {
						String[] vehicleTypeOptionArr = vehicleTypeOption.split(",");
						String[] vehicleTypeArr = { "지하철", "오토", "다마", "라보", "벤", "1t", "1.4t", "2.5t", "3.5t", "5t" };
						for (int i = 0; i < vehicleTypeArr.length; i++) {
							if (!"0".equals(vehicleTypeOptionArr[i])) {
								if (vehicleTypeIn.length() > 0) {
									vehicleTypeIn += ",";
								}
								vehicleTypeIn += vehicleTypeArr[i];
							}
						}
					} catch (Exception e) {
						vehicleTypeIn = "";
					}
				}
				receiveList = driverReceiveMapper.selectPickupManReceiveAll(employee.getNum(), employee.getBranchNum(),
						tab, minFare, maxFare, vehicleTypeIn, limitOption);
			}
			resultMap.put("receiveCount", receiveList.size());
			resultMap.put("receiveList", receiveList);
		}
		
		return resultMap;
	}
	
	public List<Map<String, Object>> getDriverDoneReceive(int employeeNum, String startDate, String endDate) {
		return driverReceiveMapper.selectDriverDoneReceive(employeeNum, startDate, endDate);
	}
	
	public List<Map<String, Object>> getReceiveSign(int receiveNum) {
		ReceiveSignExample receiveSignExample = new ReceiveSignExample();
		receiveSignExample.createCriteria().andRecvNumEqualTo(receiveNum);
		List<ReceiveSign> receiveSignList = receiveSignMapper.selectByExampleWithBLOBs(receiveSignExample);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (ReceiveSign receiveSign : receiveSignList) {
			map = new HashMap<String, Object>();
			map.put("receiveNum", receiveSign.getRecvNum());
			map.put("type", receiveSign.getType());
			map.put("sign", receiveSign.getSign());
			map.put("datetime", receiveSign.getDatetime());
			mapList.add(map);
		}
		
		return mapList;
	}
	
	public List<Map<String, Object>> getCustomerReceive(int loginType, String startDate, String endDate,
			String reportType, int customerNum, int branchNum, String patType, String searchWord) {
		List<Map<String, Object>> resultMapList = customerReceiveMapper.selectCustomerReceive(loginType, startDate,
				endDate, reportType, customerNum, branchNum, patType, searchWord);
		if (resultMapList.size() == 0) {
			resultMapList = new ArrayList<Map<String, Object>>();
		}
		return resultMapList;
	}
	
	public List<Map<String, Object>> getCustomerGroupReceive(int loginType, String startDate, String endDate,
			String reportType, int customerGroupNum, int branchNum, String patType, String searchWord) {
		List<Map<String, Object>> resultMapList = customerReceiveMapper.selectCustomerGroupReceive(loginType,
				startDate, endDate, reportType, customerGroupNum, branchNum, patType, searchWord);
		if (resultMapList.size() == 0) {
			resultMapList = new ArrayList<Map<String, Object>>();
		}
		return resultMapList;
	}
	
	public List<Map<String, Object>> getCustomerAdminReceive(int loginType, String startDate, String endDate,
			String reportType, int customerAdminNum, int branchNum, String patType, String searchWord) {
		List<Map<String, Object>> resultMapList = customerReceiveMapper.selectCustomerAdminReceive(loginType,
				startDate, endDate, reportType, customerAdminNum, branchNum, patType, searchWord);
		if (resultMapList.size() == 0) {
			resultMapList = new ArrayList<Map<String, Object>>();
		}
		return resultMapList;
	}
	
	public List<Map<String, Object>> getCustomerMasterReceive(int loginType, String startDate, String endDate,
			String reportType, int customerMasterNum, int branchNum, String patType, String searchWord) {
		List<Map<String, Object>> resultMapList = customerReceiveMapper.selectCustomerMasterReceive(loginType,
				startDate, endDate, reportType, customerMasterNum, branchNum, patType, searchWord);
		if (resultMapList.size() == 0) {
			resultMapList = new ArrayList<Map<String, Object>>();
		}
		return resultMapList;
	}
	
	public List<Map<String, Object>> searchCustomerReceive(String startDate, String endDate, int branchNum,
			String payType, String searchWord) {
		List<Map<String, Object>> resultMapList = customerReceiveMapper.searchCustomerReceive(startDate, endDate,
				branchNum, payType, searchWord);
		if (resultMapList.size() == 0) {
			resultMapList = new ArrayList<Map<String, Object>>();
		}
		return resultMapList;
	}
	
	public List<Map<String, Object>> getNonMemberReceive(String startDate, String endDate, int branchNum,
			String telNum, String pass, String searchWord) {
		telNum = telNum.replaceAll("-", "");
		
		List<Map<String, Object>> resultMapList = customerReceiveMapper.selectNonMemberReceive(startDate, endDate,
				branchNum, telNum, pass, searchWord);
		if (resultMapList.size() == 0) {
			resultMapList = new ArrayList<Map<String, Object>>();
		}
		return resultMapList;
	}
	
	public Workbook getCustomerReceiveExcel(int loginType, String startDate, String endDate, String reportType,
			int customerNum, int customerGroupNum, int customerAdminNum, int customerMasterNum, int branchNum,
			String payType, String searchWord, String field) {
		
		List<Map<String, Object>> receiveList = new ArrayList<Map<String, Object>>();
		if (loginType == 0) {
			receiveList = getCustomerReceive(loginType, startDate, endDate, reportType, customerNum, branchNum,
					payType, searchWord);
		} else if (loginType == 1) {
			receiveList = getCustomerGroupReceive(loginType, startDate, endDate, reportType, customerGroupNum,
					branchNum, payType, searchWord);
		} else if (loginType == 2) {
			receiveList = getCustomerAdminReceive(loginType, startDate, endDate, reportType, customerAdminNum,
					branchNum, payType, searchWord);
		} else if (loginType == 3) {
			receiveList = getCustomerMasterReceive(loginType, startDate, endDate, reportType, customerMasterNum,
					branchNum, payType, searchWord);
		}
		
		int rowCount = 0;
		int cellCount = 0;
		int maxCellCount = 0;
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(startDate + "~" + endDate);
		
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
		if ("SS".equals(reportType)) {
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("접수번호");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("상 태");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("접 수 시 간");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("배 차");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("종 료");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("상 호 명");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("부 서");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("담 당 자");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("출 발 지 명");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("출 발 동");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("도 착 지 명");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("도 착 동");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("왕복");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("완급");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("차량");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("지불");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("기사정보");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("접 수 자");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("탁 송");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("요 금");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("발송번호");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("적요");
		} else if ("SSS".equals(reportType)) {
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("접수번호");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("지사명");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("지사코드");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("신청점명");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("신청점코드");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("신청자");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("신청일자");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("접수시간");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("배차시간");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("픽업시간");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("완료시간");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("품목");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("품목코드");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("차종");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("차종코드");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("단가Type");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("출발지점");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("출발지코드");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("도착지점");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("도착지코드");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("출발구");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("출발동");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("출발참고");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("출발코드");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("도착구");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("도착동");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("도착참고");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("도착코드");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("거리(Km)");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("일반/왕복");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("일반/과적");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("일반/급송");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("지연배송");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("기타요금");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("청구금액");
			cell = row.createCell(cellCount++);
			cell.setCellStyle(style);
			cell.setCellValue("비고");
		} else {
			if (field.indexOf("*") > -1 || field.indexOf("num") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("접수번호");
			}
			if (field.indexOf("*") > -1 || field.indexOf("status") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("상 태");
			}
			if (field.indexOf("*") > -1 || field.indexOf("datetime") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("접 수 시 간");
			}
			if (field.indexOf("*") > -1 || field.indexOf("start") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("배 차");
			}
			if (field.indexOf("*") > -1 || field.indexOf("pickup_time") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("픽 업");
			}
			if (field.indexOf("*") > -1 || field.indexOf("end") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("종 료");
			}
			if (field.indexOf("*") > -1 || field.indexOf("cname1") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("상 호 명");
			}
			if (field.indexOf("*") > -1 || field.indexOf("department") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("부 서");
			}
			if (field.indexOf("*") > -1 || field.indexOf("person1") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("담 당 자");
			}
			if (field.indexOf("*") > -1 || field.indexOf("tel1") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("전 화 번 호");
			}
			if (field.indexOf("*") > -1 || field.indexOf("phone1") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("핸 드 폰");
			}
			if (field.indexOf("*") > -1 || field.indexOf("id") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("아 이 디");
			}
			if (field.indexOf("*") > -1 || field.indexOf("cname2") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("출 발 지 명");
			}
			if (field.indexOf("*") > -1 || field.indexOf("start_etc") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("출 발 동");
			}
			if (field.indexOf("*") > -1 || field.indexOf("person3") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("수 신 자");
			}
			if (field.indexOf("*") > -1 || field.indexOf("cname3") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("도 착 지 명");
			}
			if (field.indexOf("*") > -1 || field.indexOf("end_etc") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("도 착 동");
			}
			if (field.indexOf("*") > -1 || field.indexOf("end_detail") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("도 착 상 세");
			}
			if (field.indexOf("*") > -1 || field.indexOf("distance") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("거리(Km)");
			}
			if (field.indexOf("*") > -1 || field.indexOf("shuttle") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("왕복");
			}
			if (field.indexOf("*") > -1 || field.indexOf("quick") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("완급");
			}
			if (field.indexOf("*") > -1 || field.indexOf("auto") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("차량");
			}
			if (field.indexOf("*") > -1 || field.indexOf("payment") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("지불");
			}
			if (field.indexOf("*") > -1 || field.indexOf("driver_num") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("기사번호");
			}
			if (field.indexOf("*") > -1 || field.indexOf("driver_name") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("기 사 명");
			}
			if (field.indexOf("*") > -1 || field.indexOf("member_id") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("접 수 자");
			}
			if (field.indexOf("*") > -1 || field.indexOf("consign_") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("탁 송");
			}
			if (field.indexOf("*") > -1 || field.indexOf("fare_") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("요 금");
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("기본요금");
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("과적할증");
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("기타할증");
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("야간할증");
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("우천할증");
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("휴일할증");
			}
			if (field.indexOf("*") > -1 || field.indexOf("serial_num") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("일련번호");
			}
			if (field.indexOf("*") > -1 || field.indexOf("comment") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("적요");
			}
			if (field.indexOf("*") > -1 || field.indexOf("item") > -1) {
				cell = row.createCell(cellCount++);
				cell.setCellStyle(style);
				cell.setCellValue("품목");
			}
		}
		cellCount = 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
		String startSido = "";
		String startEtc = "";
		String endSido = "";
		String endEtc = "";
		String endDetail = "";
		String startPosition = "";
		String endPosition = "";
		double distance = 0;
		double sssDistance = 0;
		String extDriver = "";
		String driverVnum = "";
		String driverName = "";
		String driverPhone = "";
		String driverInfo = "";
		String item = "";
		String itemCode = "";
		String vehicle = "";
		String vehicleName = "";
		String sssVehicleCode = "";
		String sssDangaRype = "";
		String sssStartGuCode = "";
		String sssEndGuCode = "";
		for (Map<String, Object> map : receiveList) {
			row = sheet.createRow(rowCount++);
			startSido = (String) map.get("start_sido");
			startEtc = (String) map.get("start_etc");
			endSido = (String) map.get("end_sido");
			endEtc = (String) map.get("end_etc");
			endDetail = (String) map.get("end_detail");
			startPosition = (String) map.get("start_position");
			endPosition = (String) map.get("end_position");
			distance = getDistance(startPosition, endPosition, reportType);
			sssDistance = getSssDistance(startSido, endSido, distance);
			extDriver = (String) map.get("ext_driver");
			if (extDriver != null && !"".equals(extDriver) && !"//".equals(extDriver)) {
				String[] extDriverArr = extDriver.split("/");
				if (extDriverArr.length > 0) {
					driverVnum = extDriverArr[0];
				}
				if (extDriverArr.length > 1) {
					driverName = extDriverArr[1];
				}
				if (extDriverArr.length > 2) {
					driverPhone = extDriverArr[2];
				}
			} else {
				driverVnum = (String) map.get("driver_vnum");
				driverName = (String) map.get("driver_name");
				driverPhone = (String) map.get("driver_phone");
			}
			driverInfo = "(" + driverVnum + ")" + driverName + " " + driverPhone;
			item = (String) map.get("item");
			itemCode = getItemCode(item);
			vehicle = (String) map.get("auto");
			vehicleName = getVehicleName(vehicle);
			sssVehicleCode = getSssVehicleCode(vehicle);
			if ("서울".equals((String) map.get("start_sido")) && "서울".equals((String) map.get("end_sido"))) {
				sssDangaRype = "A";
			} else {
				sssDangaRype = "B";
			}
			sssStartGuCode = getSssGuCode((String) map.get("start_gugun"));
			sssEndGuCode = getSssGuCode((String) map.get("end_gugun"));
			if ("SS".equals(reportType)) {
				row.createCell(cellCount++).setCellValue((Long) map.get("num"));
				row.createCell(cellCount++).setCellValue((String) map.get("status"));
				row.createCell(cellCount++).setCellValue(sdf.format(map.get("datetime")));
				row.createCell(cellCount++).setCellValue(sdf2.format(map.get("start")));
				row.createCell(cellCount++).setCellValue(sdf2.format(map.get("end")));
				row.createCell(cellCount++).setCellValue((String) map.get("cname1"));
				row.createCell(cellCount++).setCellValue((String) map.get("department"));
				row.createCell(cellCount++).setCellValue((String) map.get("person1"));
				row.createCell(cellCount++).setCellValue((String) map.get("cname2"));
				if ("".equals(startEtc)) {
					row.createCell(cellCount++).setCellValue((String) map.get("start_dong"));
				} else {
					row.createCell(cellCount++).setCellValue(startEtc);
				}
				row.createCell(cellCount++).setCellValue((String) map.get("cname3"));
				if ("".equals(endEtc)) {
					row.createCell(cellCount++).setCellValue((String) map.get("end_dong"));
				} else {
					row.createCell(cellCount++).setCellValue(endEtc);
				}
				row.createCell(cellCount++).setCellValue((String) map.get("shuttle"));
				row.createCell(cellCount++).setCellValue((String) map.get("quick"));
				row.createCell(cellCount++).setCellValue(vehicle);
				row.createCell(cellCount++).setCellValue((String) map.get("payment"));
				row.createCell(cellCount++).setCellValue(driverInfo);
				row.createCell(cellCount++).setCellValue((Integer) map.get("consign_"));
				row.createCell(cellCount++).setCellValue((Integer) map.get("fare"));
				row.createCell(cellCount++).setCellValue((String) map.get("serial_num"));
				row.createCell(cellCount++).setCellValue((String) map.get("comment"));
			} else if ("SSS".equals(reportType)) {
				row.createCell(cellCount++).setCellValue((Long) map.get("num"));
				row.createCell(cellCount++).setCellValue((String) map.get(""));
				row.createCell(cellCount++).setCellValue((String) map.get("admin_id"));
				row.createCell(cellCount++).setCellValue((String) map.get("admin_code"));
				row.createCell(cellCount++).setCellValue((String) map.get("group1_id"));
				row.createCell(cellCount++).setCellValue((String) map.get("group1_code"));
				row.createCell(cellCount++).setCellValue((String) map.get("person1"));
				row.createCell(cellCount++).setCellValue(sdf1.format(map.get("datetime")));
				row.createCell(cellCount++).setCellValue(sdf2.format(map.get("datetime")));
				row.createCell(cellCount++).setCellValue(sdf2.format(map.get("start")));
				row.createCell(cellCount++).setCellValue(sdf2.format(map.get("pickup_time")));
				row.createCell(cellCount++).setCellValue(sdf2.format(map.get("end")));
				row.createCell(cellCount++).setCellValue(item);
				row.createCell(cellCount++).setCellValue(itemCode);
				row.createCell(cellCount++).setCellValue(vehicleName);
				row.createCell(cellCount++).setCellValue(sssVehicleCode);
				row.createCell(cellCount++).setCellValue(sssDangaRype);
				row.createCell(cellCount++).setCellValue((String) map.get("group2_id"));
				row.createCell(cellCount++).setCellValue((String) map.get("group2_code"));
				row.createCell(cellCount++).setCellValue((String) map.get("group3_id"));
				row.createCell(cellCount++).setCellValue((String) map.get("group3_code"));
				row.createCell(cellCount++).setCellValue((String) map.get("start_gugun"));
				row.createCell(cellCount++).setCellValue((String) map.get("start_dong"));
				row.createCell(cellCount++).setCellValue((String) map.get("start_etc"));
				row.createCell(cellCount++).setCellValue(sssStartGuCode);
				row.createCell(cellCount++).setCellValue((String) map.get("end_gugun"));
				row.createCell(cellCount++).setCellValue((String) map.get("end_dong"));
				row.createCell(cellCount++).setCellValue((String) map.get("end_etc"));
				row.createCell(cellCount++).setCellValue(sssEndGuCode);
				row.createCell(cellCount++).setCellValue(sssDistance);
				row.createCell(cellCount++).setCellValue((String) map.get("shuttle"));
				row.createCell(cellCount++).setCellValue((Integer) map.get("fare_weight"));
				row.createCell(cellCount++).setCellValue((String) map.get("quick"));
				row.createCell(cellCount++).setCellValue("");
				row.createCell(cellCount++).setCellValue((Integer) map.get("consign_"));
				row.createCell(cellCount++).setCellValue((Integer) map.get("fare"));
				row.createCell(cellCount++).setCellValue((String) map.get("comment"));
			} else {
				if (field.indexOf("*") > -1 || field.indexOf("num") > -1) {
					row.createCell(cellCount++).setCellValue((Long) map.get("num"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("status") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("status"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("datetime") > -1) {
					row.createCell(cellCount++).setCellValue(sdf.format(map.get("datetime")));
				}
				if (field.indexOf("*") > -1 || field.indexOf("start") > -1) {
					row.createCell(cellCount++).setCellValue(sdf2.format(map.get("start")));
				}
				if (field.indexOf("*") > -1 || field.indexOf("pickup_time") > -1) {
					row.createCell(cellCount++).setCellValue(sdf2.format(map.get("pickup_time")));
				}
				if (field.indexOf("*") > -1 || field.indexOf("end") > -1) {
					row.createCell(cellCount++).setCellValue(sdf2.format(map.get("end")));
				}
				if (field.indexOf("*") > -1 || field.indexOf("cname1") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("cname1"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("department") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("department"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("person1") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("person1"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("tel1") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("tel1"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("phone1") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("phone1"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("id") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("id"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("cname2") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("cname2"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("start_etc") > -1) {
					if ("".equals(startEtc)) {
						row.createCell(cellCount++).setCellValue((String) map.get("start_dong"));
					} else {
						row.createCell(cellCount++).setCellValue(startEtc);
					}
				}
				if (field.indexOf("*") > -1 || field.indexOf("person3") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("person3"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("cname3") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("cname3"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("end_etc") > -1) {
					if ("".equals(endEtc)) {
						row.createCell(cellCount++).setCellValue((String) map.get("end_dong"));
					} else {
						row.createCell(cellCount++).setCellValue(endEtc);
					}
				}
				if (field.indexOf("*") > -1 || field.indexOf("end_detail") > -1) {
					row.createCell(cellCount++).setCellValue(endDetail);
				}
				if (field.indexOf("*") > -1 || field.indexOf("distance") > -1) {
					row.createCell(cellCount++).setCellValue(distance);
				}
				if (field.indexOf("*") > -1 || field.indexOf("shuttle") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("shuttle"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("quick") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("quick"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("auto") > -1) {
					row.createCell(cellCount++).setCellValue(vehicle);
				}
				if (field.indexOf("*") > -1 || field.indexOf("payment") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("payment"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("driver_num") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("driver_vnum"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("driver_name") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("driver_name"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("member_id") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("member_id"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("consign_") > -1) {
					row.createCell(cellCount++).setCellValue((Integer) map.get("consign_"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("fare_") > -1) {
					row.createCell(cellCount++).setCellValue((Integer) map.get("fare"));
					row.createCell(cellCount++).setCellValue((Integer) map.get("fare_origin"));
					row.createCell(cellCount++).setCellValue((Integer) map.get("fare_weight"));
					row.createCell(cellCount++).setCellValue((Integer) map.get("fare_etc"));
					row.createCell(cellCount++).setCellValue((Integer) map.get("fare_night"));
					row.createCell(cellCount++).setCellValue((Integer) map.get("fare_rain"));
					row.createCell(cellCount++).setCellValue((Integer) map.get("fare_holiday"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("serial_num") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("serial_num"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("comment") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("comment"));
				}
				if (field.indexOf("*") > -1 || field.indexOf("item") > -1) {
					row.createCell(cellCount++).setCellValue((String) map.get("item"));
				}
			}
			maxCellCount = cellCount;
			cellCount = 0;
		}
		for (int i = 0; i < maxCellCount; i++) {
			sheet.autoSizeColumn(i);
		}
		return wb;
	}
	
	private double getDistance(String startPosition, String endPosition, String reportType) {
		double distance = 0;
		
		try {
			String[] startArr = startPosition.split("\\|");
			String[] endArr = endPosition.split("\\|");
			distance = DistanceUtil.getDistance(Double.parseDouble(startArr[0]), Double.parseDouble(startArr[1]),
					Double.parseDouble(endArr[0]), Double.parseDouble(endArr[1]));
			distance = Math.round(distance / 10.0) / 100.0;
			
			// fake //
			distance += 2.0;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return distance;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> callpass(int receiveNum, String callpassYn, int callpassBranchNum, int callpassFare) {
		int resultCode = 9999;
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		receive.setCallpassYn(callpassYn);
		if ("Y".equals(callpassYn)) {
			receive.setStatus("콜패스");
			receive.setCallpassBranchNum(receive.getBranchNum());
			receive.setCallpassBranchName("");
			receive.setBranchNum(callpassBranchNum);
		} else {
			receive.setStatus("콜복귀");
			receive.setBranchNum(receive.getCallpassBranchNum());
			receive.setCallpassBranchNum(0);
			receive.setCallpassBranchName("");
		}
		receive.setCallpassFare(callpassFare);
		int updatedRow = receiveMapper.updateByPrimaryKeySelective(receive);
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> confirmCallpass(int receiveNum) {
		int resultCode = 9999;
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		receive.setCallpassConfirmYn("Y");
		int updatedRow = receiveMapper.updateByPrimaryKeySelective(receive);
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("updatedRow", updatedRow);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> lockReceive(int receiveNum, String modifyId) {
		int resultCode = 9999;
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if (receive != null) {
			receive.setModifyId(modifyId);
			int updatedRow = receiveMapper.updateByPrimaryKeySelective(receive);
			if (updatedRow == 1) {
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modReceive(ReceiveUpdateModel receiveUpdateModel) {
		int resultCode = 9999;
		String resultMessage = "";
		
		if ("Y".equals(receiveUpdateModel.getCallpassBtnClick())) {
			int tempBranchNum = 0;
			String tempBranchName = "";
			
			tempBranchNum = receiveUpdateModel.getBranchNum();
			tempBranchName = receiveUpdateModel.getBranchName();
			receiveUpdateModel.setBranchNum(receiveUpdateModel.getCallpassBranchNum());
			receiveUpdateModel.setBranchName(receiveUpdateModel.getCallpassBranchName());
			receiveUpdateModel.setCallpassBranchNum(tempBranchNum);
			receiveUpdateModel.setCallpassBranchName(tempBranchName);
		}
		
		if ("콜복귀".equals(receiveUpdateModel.getStatus())) {
			receiveUpdateModel.setCallpassBranchNum(0);
			receiveUpdateModel.setCallpassBranchName("");
		}
		
		Branch branch = branchMapper.selectByPrimaryKey(receiveUpdateModel.getBranchNum());
		
		// 지사충전금이 없을때는 공유하지 못한다
		if (branch != null) {
			if (branch.getCharge() <= 0) {
				receiveUpdateModel.setShare(0);
			} else {
				BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(branch.getGroupNum());
				if (branchGroup != null) {
					if (branchGroup.getCharge() <= 0) {
						receiveUpdateModel.setShare(0);
					}
				}
			}
			
			Map<String, Object> receiveMap = receiveShareOrderMapper.selectReceiveByNum(receiveUpdateModel
					.getReceiveNum());
			ReceiveShareOrder receiveShareOrder = setReceiveShare(receiveMap);
			
			if (!receiveUpdateModel.getOrgStatus().equals(receiveShareOrder.getStatus())
					&& !"콜복귀".equals(receiveUpdateModel.getStatus()) && !"콜패스".equals(receiveUpdateModel.getStatus())
					&& !"인터넷".equals(receiveShareOrder.getStatus()) && !"대기".equals(receiveShareOrder.getStatus())
					&& receiveShareOrder.getReserve() != 1) {
				resultCode = 3001;
				resultMessage = "외부에서 상태가 변경되었습니다. 창을 닫고 다시 시도해 주십시오.";
			} else {
				// 건당 프로그램 사용료 수수료 율
				int programFeeRate = 2;
				// 기사오더등록 프로그램 사용료 수수료 율
				int registerProgramPeeRate = 1;
				// 기사오더등록 오더의 퀵사 수수료율
				int registerBranchRate = 2;
				
				// 기사 정보
				Employee employee = employeeMapper.selectByPrimaryKey(receiveUpdateModel.getDriverNum());
				
				int orgFare = receiveShareOrder.getFare_();
				String memo = "";
				if (!"".equals(receiveShareOrder.getStartEtc())) {
					memo = receiveShareOrder.getStartEtc();
				} else {
					memo = receiveShareOrder.getStartDong();
				}
				memo += " => ";
				if (!"".equals(receiveShareOrder.getEndEtc())) {
					memo = receiveShareOrder.getEndEtc();
				} else {
					memo = receiveShareOrder.getEndDong();
				}
				memo += " : " + orgFare + "(" + receiveShareOrder.getPayment() + ")";
				
				// 공유서버 전송
				if (receiveShareOrder.getnTNo() > 0 && receiveShareOrder.getnState() != 600) {
					CoaliOrder coaliOrder = coaliOrderService.selectCoaliOrderDetail(receiveShareOrder.getnTNo(),
							receiveShareOrder.getBranchNum(), 0, 0);
				}
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> modReceiveStatus(int receiveNum, String fromStatus, String toStatus, int driverNum,
			String memberId) {
		int resultCode = 9999;
		String resultMessage = "";
		
		// 오더 확인
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if (receive != null) {
			if (!receive.getStatus().equals(fromStatus)) {
				resultCode = 3001; // 상태 일치 하지 않음
				resultMessage = "외부에서 상태가 변경되었습니다.\n새로고침 후에 다시작업 하세요";
			} else if (StringUtils.isNotBlank(receive.getModifyId()) && !receive.getModifyId().equals(memberId)) {
				resultCode = 3002; // 다른 사람 수정중
				resultMessage = "현재 " + receive.getModifyId() + "님이 수정중입니다.";
			} else {
				// 메모 설정
				String receiveMemo = getReceiveMemo(receive);
				receiveMemo += " : " + receive.getFare_() + "(" + receive.getPayment() + ")";
				
				// 종료 시간
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				Date date = new Date();
				
				// 오더의 지사 정보 가져오기
				Branch branch = branchMapper.selectByPrimaryKey(receive.getBranchNum());
				
				Employee employee = null;
				if (!"접수".equals(toStatus) && driverNum > 0) {
					employee = employeeMapper.selectByPrimaryKey(driverNum);
				}
				
				// 종료된 오더를 다른 상태로 바꿀때
				if ("종료".equals(receive.getStatus()) && !"종료".equals(toStatus)) {
					// 고객 마일리지 취소
					chargeService.cancelChargeCustomerMileage(receive);
					
					// 오더 등록한 기사 정산 취소
					chargeService.cancelChargeRegisterDriver(receive, date);
					
					// 오더 처리한 기사 정산 취소
					chargeService.cancelChargeCompleteDriver(receive, date);
					
					// 콜패스 정산 취소
					chargeService.cancelChargeCallpass(receive, date);
					
					// 콜그룹 정산 취소
					chargeService.cancelChargeCallGroup(receive, date);
				} else if (!"종료".equals(receive.getStatus()) && "종료".equals(toStatus)) {
					// 고객 마일리지 적립
					chargeService.chargeCustomerMileage(receive);
					
					// 오더 등록한 기사 정산
					chargeService.chargeRegisterDriver(receive, branch, receiveMemo, date);
					
					// 오더 처리한 기사 정산
					chargeService.chargeCompleteDriver(receive, branch, receiveMemo, date);
					
					// 콜패스일 경우 오더 받은 지사와 오더 처리한 지사간 정산
					chargeService.chargeCallpass(receive, receiveMemo, date);
					
					// 콜그룹인 경우 그룹장에게 수수료
					chargeService.chargeCallGroup(receive, branch, receiveMemo, date);
				}
				
				// 배차취소 공지
				if ("배차".equals(receive.getStatus())) {
					if (receive.getDriverNum() > 0 && !"배차".equals(toStatus) && !"픽업".equals(toStatus)
							&& !"종료".equals(toStatus)) {
						Employee exDriver = employeeMapper.selectByPrimaryKey(receive.getDriverNum());
						String title = "[알림] 배차취소 : " + getReceiveMemo(receive);
						noticeService.setNotice(3, title, receiveMemo, memberId, "", exDriver.getNum(), "", "", "",
								receive.getBranchNum(), 0);
					}
				}
				
				// update
				if (receive.getReserve() == 1
						&& ("대기".equals(toStatus) || "개별".equals(toStatus) || "배차".equals(toStatus))) {
					receive.setDatetime(date);
				}
				if ("취소".equals(receive.getStatus()) && "개별".equals(toStatus)) {
					receive.setDatetime(date);
				}
				
				receive.setReserve(0);
				receive.setStatus(toStatus);
				
				if (employee != null) {
					receive.setDriverNum(employee.getNum());
					receive.setDriverVnum(employee.getVnum());
					receive.setDriverName(employee.getName());
				} else {
					receive.setDriverNum(0);
					receive.setDriverVnum("");
					receive.setDriverName("");
				}
				if ("접수".equals(toStatus)) {
					receive.setDatetime(date);
					receive.setExtDriver("//");
					smsService.sendReceiveSms(receive);
				}
				if ("개별".equals(receive.getStatus())) {
					receive.setIndividual(sdf.format(date));
				} else if ("배차".equals(receive.getStatus())) {
					receive.setStart(sdf.format(date));
				} else if ("픽업".equals(receive.getStatus())) {
					receive.setPickupTime(sdf.format(date));
					smsService.sendReceiveSms(receive);
				} else if ("종료".equals(receive.getStatus())) {
					receive.setEnd(sdf.format(date));
					smsService.sendReceiveSms(receive);
				}
				receiveMapper.updateByPrimaryKeySelective(receive);
				
				// 이력 남기기
				ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
				receiveTemp.setNum(receive.getNum());
				receiveTemp.setBranchNum(receive.getBranchNum());
				receiveTemp.setBranchName(receive.getBranchName());
				receiveTemp.setStatus(receive.getStatus());
				receiveTemp.setCnum1(receive.getCnum1());
				receiveTemp.setCnum2(receive.getCnum2());
				receiveTemp.setCnum3(receive.getCnum3());
				receiveTemp.setStartJusoNum(receive.getStartJusoNum());
				receiveTemp.setStartSido(receive.getStartSido());
				receiveTemp.setStartGugun(receive.getStartGugun());
				receiveTemp.setStartDong(receive.getStartDong());
				receiveTemp.setStartEtc(receive.getStartEtc());
				receiveTemp.setEndJusoNum(receive.getEndJusoNum());
				receiveTemp.setEndSido(receive.getEndSido());
				receiveTemp.setEndGugun(receive.getEndGugun());
				receiveTemp.setEndDong(receive.getEndDong());
				receiveTemp.setEndEtc(receive.getEndEtc());
				receiveTemp.setDatetime(date);
				receiveTemp.setDriverNum(receive.getDriverNum());
				receiveTemp.setDriverVnum(receive.getDriverVnum());
				receiveTemp.setDriverName(receive.getDriverName());
				receiveTemp.setMemberId(memberId);
				receiveTemp.setFare(receive.getFare());
				receiveTemp.setShuttle(receive.getShuttle());
				receiveTemp.setQuick(receive.getQuick());
				receiveTemp.setAuto(receive.getAuto());
				receiveTemp.setWeight(receive.getWeight());
				receiveTemp.setPayment(receive.getPayment());
				receiveTemp.setFareType(receive.getFareType());
				receiveTemp.setFare(receive.getFare());
				receiveTemp.setFareOrigin(receive.getFareOrigin());
				receiveTemp.setFareWeight(receive.getFareWeight());
				receiveTemp.setFareNight(receive.getFareNight());
				receiveTemp.setFareRain(receive.getFareRain());
				receiveTemp.setFareHoliday(receive.getFareHoliday());
				receiveTemp.setFareEtc(receive.getFareEtc());
				receiveTemp.setFareDiscount(receive.getFareDiscount());
				receiveTemp.setFareDiscount2(receive.getFareDiscount2());
				receiveTemp.setFare_(receive.getFare_());
				receiveTemp.setConsign_(receive.getConsign_());
				receiveTemp.setCallpassFare(receive.getCallpassFare());
				receiveTemp.setIndividual(receive.getIndividual());
				receiveTemp.setStart(receive.getStart());
				receiveTemp.setPickupTime(receive.getPickupTime());
				receiveTemp.setEnd(receive.getEnd());
				receiveTemp.setEditlist("");
				receiveTempMapper.insertSelective(receiveTemp);
				
				resultCode = 1000;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	public Map<String, Object> modReceiveEndTime(int receiveNum, String endTime) {
		int resultCode = 9999;
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if (receive != null) {
			receive.setEnd(endTime);
			int updatedRow = receiveMapper.updateByPrimaryKeySelective(receive);
			
			if (updatedRow == 1) {
				resultCode = 1000;
			}
		} else {
			resultCode = 2000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	public Map<String, Object> modReceiveSign(int receiveNum, String type, String sign) {
		int resultCode = 9999;
		int updatedRow = 0;
		
		ReceiveSignKey receiveSignKey = new ReceiveSignKey();
		receiveSignKey.setRecvNum(receiveNum);
		receiveSignKey.setType(type);
		ReceiveSign receiveSign = receiveSignMapper.selectByPrimaryKey(receiveSignKey);
		
		Date datetime = new Date();
		if (receiveSign == null) {
			receiveSign = new ReceiveSign();
			receiveSign.setRecvNum(receiveNum);
			receiveSign.setType(type);
			receiveSign.setSign(sign);
			receiveSign.setDatetime(datetime);
			updatedRow = receiveSignMapper.insert(receiveSign);
		} else {
			receiveSign.setSign(sign);
			receiveSign.setDatetime(datetime);
			updatedRow = receiveSignMapper.updateByPrimaryKeyWithBLOBs(receiveSign);
		}
		
		if (updatedRow == 1) {
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public void modReceiveToNormal(Receive receive) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HHmmss");
		Date reserveDate = new Date();
		Date nowDate = new Date();
		int compare = 0;
		int result = 0;
		
		if ("오후".equals(receive.getReserveAmpm()) && receive.getReserveHour() < 12) {
			receive.setReserveHour(receive.getReserveHour() + 12);
		}
		
		try {
			reserveDate = sdf.parse(receive.getReserveDate() + " " + String.format("%02d", receive.getReserveHour())
					+ String.format("%02d", receive.getReserveMinute()) + "00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		compare = reserveDate.compareTo(nowDate);
		if (compare < 0) {
			logger.info("num=" + receive.getNum() + ", " + "reserveDate=" + receive.getReserveDate() + ", " + "ampm="
					+ receive.getReserveAmpm() + ", " + "hour=" + receive.getReserveHour() + ", " + "min="
					+ receive.getReserveMinute());
			
			receive.setReserve(0); // 0 : 예약이 아닌 일반 상태
			receive.setDatetime(nowDate);
			if (receive.getDriverDatetime() == null) {
				receive.setDriverDatetime(new Date(0));
			}
			result = receiveMapper.updateByPrimaryKey(receive);
			
			// 이력 남기기
			ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
			receiveTemp.setNum(receive.getNum());
			receiveTemp.setStatus(receive.getStatus());
			receiveTemp.setReserve(receive.getReserve());
			receiveTemp.setDatetime(receive.getDatetime());
			receiveTemp.setDriverNum(receive.getDriverNum());
			receiveTemp.setDriverVnum(receive.getDriverVnum());
			receiveTemp.setDriverName(receive.getDriverName());
			receiveTemp.setMemberId("예약시스템");
			receiveTemp.setFare(receive.getFare());
			receiveTemp.setBranchNum(receive.getBranchNum());
			receiveTemp.setBranchName(receive.getBranchName());
			receiveTemp.setStartSido(receive.getStartSido());
			receiveTemp.setStartGugun(receive.getStartGugun());
			receiveTemp.setStartDong(receive.getStartDong());
			receiveTemp.setEndSido(receive.getEndSido());
			receiveTemp.setEndGugun(receive.getEndGugun());
			receiveTemp.setEndDong(receive.getEndDong());
			receiveTemp.setEditlist("[예약] -> [접수]");
			receiveTempMapper.insertSelective(receiveTemp);
			
			logger.info("Update !!! " + receive.getNum() + " = " + result);
		}
	}
	
	public Map<String, Object> delRepeatReserve(int branchNum, String keyReserveRpeat) {
		int resultCode = 9999;
		
		Receive receive = new Receive();
		receive.setStatus("취소");
		receive.setDel(1);
		ReceiveExample receiveExample = new ReceiveExample();
		receiveExample.createCriteria().andBranchNumEqualTo(branchNum).andKeyreserverpeatEqualTo(keyReserveRpeat)
				.andStatusNotEqualTo("종료").andStatusNotEqualTo("픽업").andStatusNotEqualTo("배차");
		int updatedRow = receiveMapper.updateByExampleSelective(receive, receiveExample);
		
		// TODO : 이력 남겨야 하는가?
		
		if (updatedRow >= 0) {
			// 미처리 예약건이 한건도 없을때도 이 액션을 취할 수 있다
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		return resultMap;
	}
	
	// @Transactional(value = "transactionManagerTotal")
	@Transactional(value = "transactionManagerTotal", isolation = Isolation.REPEATABLE_READ)
	// @Transactional(isolation = Isolation.READ_COMMITTED)
	public Map<String, Object> allocateByDriver(int receiveNum, int employeeNum) {
		int resultCode = 9999;
		String resultMessage = "";
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if (receive != null) {
			if ("종료".equals(receive.getStatus())) {
				resultCode = 3001; // 종료된 오더
				resultMessage = "종료된 오더입니다.";
			} else if ("취소".equals(receive.getStatus())) {
				resultCode = 3002; // 취소된 오더
				resultMessage = "취소된 오더입니다.";
			} else if (receive.getReserve() == 1) {
				resultCode = 3007; // 예약 오더
				resultMessage = "예약 오더입니다.";
			} else if (!"접수".equals(receive.getStatus())
					&& !(("개별".equals(receive.getStatus()) || "자동".equals(receive.getStatus())) && receive
							.getDriverNum() == employeeNum)) {
				resultCode = 3003; // 기사가 픽업 할 수 없는 오더
				resultMessage = "다른 기사님에게 먼저 배차된 오더이거나, 취소된 오더입니다.";
			} else {
				Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
				if (employee == null) {
					resultCode = 3005; // 기사 확인 안됨
					resultMessage = "기사가 확인 되지 않습니다.";
				} else {
					if (("선불".equals(receive.getPayment()) || "착불".equals(receive.getPayment()))
							&& employee.getCharge() < getCommission(receive.getFare_(), receive.getAuto(),
									employee.getBranchNum())) {
						resultCode = 3006; // 잔액 부족
						resultMessage = "적립잔액 부족으로 배차 받을 수 없습니다. 충전 후 이용해 주세요.(적립잔액 " + employee.getCharge() + "원)";
					} else {
						Branch branch = branchMapper.selectByPrimaryKey(employee.getBranchNum());
						BranchGroup branchGroup = branchGroupMapper.selectByPrimaryKey(branch.getGroupNum());
						
						if (("선불".equals(receive.getPayment()) || "착불".equals(receive.getPayment()))
								&& branch.getNum() != receive.getBranchNum() && branch.getCharge() < 100000) {
							resultCode = 3007; // 지사 충전금 부족 10만
							resultMessage = "퀵사 적립잔액 부족으로 배차 받을 수 없습니다. (" + branch.getTel() + ")로 연락바랍니다.";
						} else if (("선불".equals(receive.getPayment()) || "착불".equals(receive.getPayment()))
								&& branch.getNum() != receive.getBranchNum() && branchGroup.getCharge() < 300000) {
							resultCode = 3008; // 지사 그룹 충전금 부족 30만
							resultMessage = "퀵그룹사 적립잔액 부족으로 배차 받을 수 없습니다. (" + branch.getTel() + ")로 연락바랍니다.";
						} else {
							// 제휴오더의 경우 제휴공유 서버에 오더 배차 전달
							// CoaliOrder coaliOrder = shareOrderService.allocateShareOrder(receive, branch.getNum(),
							// branch.getName(), branch.getTel(), branch.getTel(), employee.getNum(),
							// employee.getVnum(), employee.getName(), employee.getId(), employee.getPhone(),
							// getShareCarType(employee.getVehicle()));
							// if (coaliOrder.getnReturn() != 1) {
							// resultCode = coaliOrder.getnReturn();
							// resultMessage = coaliOrder.getsError();
							// } else {
							// Boolean result = true;
							// int cargoResultMessage = 0;
							//
							// // 화물 공유된 오더일 경우 화물 쪽에 배차 여부 조회 후 처리
							// if ("Y".equals(receive.getCargoShareYn()) && receive.getCargoShareBranch() == 274) {
							// Map<String, Object> cargoResultMap = extCargoReceiveService.cargoSendAllocate(
							// receive.getNum(), receive.getCargoNum(), employee.getVehicleNum(),
							// employee.getName(), employee.getPhone(),
							// CargoShareCodeUtil.getTonTypesJsonValue(employee.getVehicle()), 1);
							//
							// result = (Boolean) cargoResultMap.get("cargoResult");
							// cargoResultMessage = (Integer) cargoResultMap.get("cargoResultMessage");
							// }
							
							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
							Date datetime = new Date();
							
							// Receive newReceive = receiveMapper.selectByPrimaryKey(receiveNum);
							// if (!"접수".equals(newReceive.getStatus())
							// && !(("개별".equals(newReceive.getStatus()) || "자동".equals(newReceive.getStatus())) &&
							// newReceive
							// .getDriverNum() == employeeNum)) {
							// resultCode = 3003; // 기사가 픽업 할 수 없는 오더
							// resultMessage = "다른 기사님에게 먼저 배차된 오더이거나, 취소된 오더입니다.";
							// } else {
							Receive checkReceive = receiveMapper.selectByPrimaryKey(receiveNum);
							if (!"배차".equals(checkReceive.getStatus())) {
								receive.setStatus("배차");
								receive.setDriverNum(employee.getNum());
								receive.setDriverVnum(employee.getVnum());
								receive.setDriverName(employee.getName());
								receive.setStart(sdf.format(datetime));
								receiveMapper.updateByPrimaryKeySelective(receive);
								
								// 이력 남기기
								ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
								receiveTemp.setNum(receive.getNum());
								receiveTemp.setStatus(receive.getStatus());
								receiveTemp.setDatetime(datetime);
								receiveTemp.setDriverNum(receive.getDriverNum());
								receiveTemp.setDriverVnum(receive.getDriverVnum());
								receiveTemp.setDriverName(receive.getDriverName());
								receiveTemp.setMemberId(employee.getId());
								receiveTemp.setFare(receive.getFare());
								receiveTemp.setBranchNum(receive.getBranchNum());
								receiveTemp.setBranchName(receive.getBranchName());
								receiveTemp.setStartSido(receive.getStartSido());
								receiveTemp.setStartGugun(receive.getStartGugun());
								receiveTemp.setStartDong(receive.getStartDong());
								receiveTemp.setEndSido(receive.getEndSido());
								receiveTemp.setEndGugun(receive.getEndGugun());
								receiveTemp.setEndDong(receive.getEndDong());
								receiveTemp.setEditlist("");
								receiveTempMapper.insertSelective(receiveTemp);
								
								resultCode = 1000;
								resultMessage = "배차가 완료되었습니다.";
							} else {
								resultCode = 3003;
								resultMessage = "다른 기사님에게 먼저 배차된 오더이거나, 취소된 오더입니다.";
							}
							// }
						}
					}
				}
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		// 픽업권고시간
		cal.add(Calendar.MINUTE, 20);
		String pickupAdviceTime = sdf.format(cal.getTime());
		
		// 도착권고시간
		cal.add(Calendar.MINUTE, getRequiredTime(receive.getFare_(), receive.getQuick()));
		String endAdviceTime = sdf.format(cal.getTime());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		resultMap.put("pickupAdviceTime", pickupAdviceTime);
		resultMap.put("endAdviceTime", endAdviceTime);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> cancelAllocationByDriver(int receiveNum, int employeeNum) {
		int resultCode = 9999;
		String resultMessage = "";
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if ("종료".equals(receive.getStatus())) {
			resultCode = 3001; // 종료된 오더
			resultMessage = "종료된 오더입니다.";
		} else if ("취소".equals(receive.getStatus())) {
			resultCode = 3002; // 취소된 오더
			resultMessage = "취소된 오더입니다.";
		} else {
			Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
			if (employee == null) {
				resultCode = 3005; // 기사 확인 안됨
				resultMessage = "기사가 확인 되지 않습니다.";
			} else {
				String[] startArr = receive.getStart().split(":");
				Calendar startCal = Calendar.getInstance();
				Calendar nowCal = Calendar.getInstance();
				startCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startArr[0]));
				startCal.set(Calendar.MINUTE, Integer.parseInt(startArr[1]));
				startCal.set(Calendar.SECOND, Integer.parseInt(startArr[2]));
				startCal.add(Calendar.MINUTE, 1);
				if (startCal.getTimeInMillis() < nowCal.getTimeInMillis()) {
					resultCode = 3006; // 취소 불가
					resultMessage = "배차 후 1분이상 경과되었습니다. 퀵사로 전화하세요.";
				} else {
					// 제휴오더의 경우 제휴공유 서버에 오더 배차취소 전달
					// CoaliOrder coaliOrder = shareOrderService.cancelShareOrder(receive, employeeNum, "");
					// if (coaliOrder.getnReturn() != 1) {
					// resultCode = coaliOrder.getnReturn();
					// resultMessage = coaliOrder.getsError();
					// } else {
					// Boolean result = true;
					// int cargoResultMessage = 0;
					
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					Date datetime = new Date();
					
					// 화물 공유된 오더일 경우 화물 쪽에 배차 취소 여부 조회 후 처리
					// if ("Y".equals(receive.getCargoShareYn()) && receive.getCargoShareBranch() == 274) {
					// Map<String, Object> cargoResultMap = extCargoReceiveService.cargoSendAllocateCancel(
					// receive.getNum(), receive.getCargoNum());
					// result = (Boolean) cargoResultMap.get("cargoResult");
					// cargoResultMessage = (Integer) cargoResultMap.get("cargoResultMessage");
					// }
					
					// if (result) {
					receive.setStatus("접수");
					receive.setDriverNum(0);
					receive.setDriverVnum("");
					receive.setDriverName("");
					receive.setStart(sdf.format(datetime));
					receiveMapper.updateByPrimaryKeySelective(receive);
					
					// 이력 남기기
					ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
					receiveTemp.setNum(receive.getNum());
					receiveTemp.setStatus(receive.getStatus());
					receiveTemp.setDatetime(datetime);
					receiveTemp.setDriverNum(0);
					receiveTemp.setDriverVnum("");
					receiveTemp.setDriverName("");
					receiveTemp.setMemberId(employee.getId());
					receiveTemp.setFare(receive.getFare());
					receiveTemp.setBranchNum(receive.getBranchNum());
					receiveTemp.setBranchName(receive.getBranchName());
					receiveTemp.setStartSido(receive.getStartSido());
					receiveTemp.setStartGugun(receive.getStartGugun());
					receiveTemp.setStartDong(receive.getStartDong());
					receiveTemp.setEndSido(receive.getEndSido());
					receiveTemp.setEndGugun(receive.getEndGugun());
					receiveTemp.setEndDong(receive.getEndDong());
					receiveTemp.setEditlist("배차거부");
					receiveTempMapper.insertSelective(receiveTemp);
					
					resultCode = 1000;
					resultMessage = "배차 취소가 완료되었습니다.";
					// } else {
					// resultCode = 2001;
					// resultMessage = "배차 취소를 할 수 없습니다. 문의바랍니다.  " + cargoResultMessage;
					// }
					// }
				}
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> takeByDriver(int receiveNum, int employeeNum) {
		int resultCode = 9999;
		String resultMessage = "";
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if ("종료".equals(receive.getStatus())) {
			resultCode = 3001; // 종료된 오더
			resultMessage = "종료된 오더입니다.";
		} else if ("취소".equals(receive.getStatus())) {
			resultCode = 3002; // 취소된 오더
			resultMessage = "취소된 오더입니다.";
		} else if (receive.getReserve() == 1) {
			resultCode = 3007; // 예약 오더
			resultMessage = "예약 오더입니다.";
		} else {
			Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
			if (employee == null) {
				resultCode = 3005; // 기사 확인 안됨
				resultMessage = "기사가 확인 되지 않습니다.";
			} else {
				// 제휴공유 한 오더에대해서 채크 하지 않아 문제가 될 가능성 있음
				
				receive.setStatus("수거");
				receiveMapper.updateByPrimaryKeySelective(receive);
				
				// 이력 남기기
				ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
				receiveTemp.setNum(receive.getNum());
				receiveTemp.setStatus(receive.getStatus());
				receiveTemp.setDatetime(new Date());
				receiveTemp.setDriverNum(employeeNum);
				receiveTemp.setDriverVnum(employee.getVnum());
				receiveTemp.setDriverName(employee.getName());
				receiveTemp.setMemberId(employee.getId());
				receiveTemp.setFare(receive.getFare());
				receiveTemp.setBranchNum(receive.getBranchNum());
				receiveTemp.setBranchName(receive.getBranchName());
				receiveTemp.setStartSido(receive.getStartSido());
				receiveTemp.setStartGugun(receive.getStartGugun());
				receiveTemp.setStartDong(receive.getStartDong());
				receiveTemp.setEndSido(receive.getEndSido());
				receiveTemp.setEndGugun(receive.getEndGugun());
				receiveTemp.setEndDong(receive.getEndDong());
				receiveTemp.setEditlist("");
				receiveTempMapper.insertSelective(receiveTemp);
				
				resultCode = 1000;
				resultMessage = "수거가 완료되었습니다.";
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> cancelTakeByDriver(int receiveNum, int employeeNum) {
		int resultCode = 9999;
		String resultMessage = "";
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if ("종료".equals(receive.getStatus())) {
			resultCode = 3001; // 종료된 오더
			resultMessage = "종료된 오더입니다.";
		} else if ("취소".equals(receive.getStatus())) {
			resultCode = 3002; // 취소된 오더
			resultMessage = "취소된 오더입니다.";
		} else {
			Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
			if (employee == null) {
				resultCode = 3005; // 기사 확인 안됨
				resultMessage = "기사가 확인 되지 않습니다.";
			} else {
				// 제휴공유 한 오더에대해서 채크 하지 않아 문제가 될 가능성 있음
				
				receive.setStatus("배차");
				receiveMapper.updateByPrimaryKeySelective(receive);
				
				// 이력 남기기
				ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
				receiveTemp.setNum(receive.getNum());
				receiveTemp.setStatus(receive.getStatus());
				receiveTemp.setDatetime(new Date());
				receiveTemp.setDriverNum(0);
				receiveTemp.setDriverVnum("");
				receiveTemp.setDriverName("");
				receiveTemp.setMemberId(employee.getId());
				receiveTemp.setFare(receive.getFare());
				receiveTemp.setBranchNum(receive.getBranchNum());
				receiveTemp.setBranchName(receive.getBranchName());
				receiveTemp.setStartSido(receive.getStartSido());
				receiveTemp.setStartGugun(receive.getStartGugun());
				receiveTemp.setStartDong(receive.getStartDong());
				receiveTemp.setEndSido(receive.getEndSido());
				receiveTemp.setEndGugun(receive.getEndGugun());
				receiveTemp.setEndDong(receive.getEndDong());
				receiveTemp.setEditlist("");
				receiveTempMapper.insertSelective(receiveTemp);
				
				resultCode = 1000;
				resultMessage = "수거 취소가 완료되었습니다.";
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> pickupByDriver(int receiveNum, int employeeNum, String sign) {
		int resultCode = 9999;
		String resultMessage = "";
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if (receive != null) {
			if ("종료".equals(receive.getStatus())) {
				resultCode = 3001; // 종료된 오더
				resultMessage = "종료된 오더입니다.";
			} else if ("취소".equals(receive.getStatus())) {
				resultCode = 3002; // 취소된 오더
				resultMessage = "취소된 오더입니다.";
			} else if (!"배차".equals(receive.getStatus())) {
				resultCode = 3003; // 기사가 픽업 할 수 없는 오더
				resultMessage = "배차 오더가 아닙니다.";
			} else if (receive.getDriverNum() != employeeNum) {
				resultCode = 3004; // 해당 오더의 배차기사가 아님
				resultMessage = "해당 오더의 배차 기사가 아닙니다.";
			} else {
				Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
				if (employee == null) {
					resultCode = 3005; // 기사 확인 안됨
					resultMessage = "기사가 확인 되지 않습니다.";
				} else {
					// 제휴오더의 경우 제휴공유 서버에 오더 픽업 전달
					CoaliOrder coaliOrder = shareOrderService.pickupShareOrder(receive.getNtno(),
							employee.getBranchNum(), employee.getNum());
					if (coaliOrder.getnReturn() != 1) {
						resultCode = coaliOrder.getnReturn();
						resultMessage = coaliOrder.getsError();
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						Date datetime = new Date();
						
						setReceiveSign(receive.getNum(), "start", sign, datetime);
						
						receive.setStatus("픽업");
						receive.setPickupTime(sdf.format(datetime));
						receiveMapper.updateByPrimaryKeySelective(receive);
						
						// 이력 남기기
						ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
						receiveTemp.setNum(receive.getNum());
						receiveTemp.setBranchNum(receive.getBranchNum());
						receiveTemp.setStatus(receive.getStatus());
						receiveTemp.setDatetime(datetime);
						receiveTemp.setDriverNum(receive.getDriverNum());
						receiveTemp.setDriverVnum(receive.getDriverVnum());
						receiveTemp.setDriverName(receive.getDriverName());
						receiveTemp.setMemberId(employee.getId());
						receiveTemp.setFare(receive.getFare());
						receiveTemp.setBranchNum(receive.getBranchNum());
						receiveTemp.setBranchName(receive.getBranchName());
						receiveTemp.setStartSido(receive.getStartSido());
						receiveTemp.setStartGugun(receive.getStartGugun());
						receiveTemp.setStartDong(receive.getStartDong());
						receiveTemp.setEndSido(receive.getEndSido());
						receiveTemp.setEndGugun(receive.getEndGugun());
						receiveTemp.setEndDong(receive.getEndDong());
						receiveTemp.setEditlist("");
						receiveTempMapper.insertSelective(receiveTemp);
						
						// 픽업 문자
						smsService.sendReceiveSms(receive);
						
						resultCode = 1000;
						resultMessage = "출발지 서명 전송이 완료되었습니다.";
					}
				}
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	@Transactional(value = "transactionManagerTotal")
	public Map<String, Object> completeByDriver(int receiveNum, int employeeNum, String sign) {
		int resultCode = 9999;
		String resultMessage = "";
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if (receive != null) {
			if ("종료".equals(receive.getStatus())) {
				resultCode = 3001; // 종료된 오더
				resultMessage = "종료된 오더입니다.";
			} else if ("취소".equals(receive.getStatus())) {
				resultCode = 3002; // 취소된 오더
				resultMessage = "취소된 오더입니다.";
			} else if (!"배차".equals(receive.getStatus()) && !"픽업".equals(receive.getStatus())) {
				resultCode = 3003; // 기사가 종료 할 수 없는 오더
				resultMessage = "배차 또는 픽업 오더가 아닙니다.";
			} else if (receive.getDriverNum() != employeeNum) {
				resultCode = 3004; // 해당 오더의 배차기사가 아님
				resultMessage = "해당 오더의 배차 기사가 아닙니다.";
			} else {
				Employee employee = employeeMapper.selectByPrimaryKey(employeeNum);
				if (employee == null) {
					resultCode = 3005; // 기사 확인 안됨
					resultMessage = "기사가 확인 되지 않습니다.";
				} else {
					// 제휴오더의 경우 제휴공유 서버에 오더 종료 전달
					// CoaliOrder coaliOrder = shareOrderService.completeShareOrder(receive.getNtno(),
					// employee.getBranchNum(), employee.getNum());
					// if (coaliOrder.getnReturn() != 1) {
					// resultCode = coaliOrder.getnReturn();
					// resultMessage = coaliOrder.getsError();
					// } else {
					// 오더의 지사 정보 가져오기
					Branch branch = branchMapper.selectByPrimaryKey(receive.getBranchNum());
					
					// 메모 설정
					String receiveMemo = getReceiveMemo(receive);
					
					// 고객 마일리지 적립
					chargeService.chargeCustomerMileage(receive);
					
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					Date completeDate = new Date();
					
					// 오더 등록한 기사 정산
					chargeService.chargeRegisterDriver(receive, branch, receiveMemo, completeDate);
					
					// 오더 처리한 기사 정산
					chargeService.chargeCompleteDriver(receive, branch, receiveMemo, completeDate);
					
					// 콜패스일 경우 오더 받은 지사와 오더 처리한 지사간 정산
					chargeService.chargeCallpass(receive, receiveMemo, completeDate);
					
					// 콜그룹인 경우 그룹장에게 수수료
					chargeService.chargeCallGroup(receive, branch, receiveMemo, completeDate);
					
					setReceiveSign(receive.getNum(), "end", sign, completeDate);
					
					receive.setStatus("종료");
					receive.setEnd(sdf.format(completeDate));
					receiveMapper.updateByPrimaryKeySelective(receive);
					
					// 이력 남기기
					ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
					receiveTemp.setNum(receive.getNum());
					receiveTemp.setStatus(receive.getStatus());
					receiveTemp.setDatetime(completeDate);
					receiveTemp.setBranchNum(receive.getBranchNum());
					receiveTemp.setBranchName(receive.getBranchName());
					receiveTemp.setStartJusoNum(receive.getStartJusoNum());
					receiveTemp.setStartSido(receive.getStartSido());
					receiveTemp.setStartGugun(receive.getStartGugun());
					receiveTemp.setStartDong(receive.getStartDong());
					receiveTemp.setEndJusoNum(receive.getEndJusoNum());
					receiveTemp.setEndSido(receive.getEndSido());
					receiveTemp.setEndGugun(receive.getEndGugun());
					receiveTemp.setEndDong(receive.getEndDong());
					receiveTemp.setShuttle(receive.getShuttle());
					receiveTemp.setQuick(receive.getQuick());
					receiveTemp.setAuto(receive.getAuto());
					receiveTemp.setWeight(receive.getWeight());
					receiveTemp.setPayment(receive.getPayment());
					receiveTemp.setDriverNum(receive.getDriverNum());
					receiveTemp.setDriverVnum(receive.getDriverVnum());
					receiveTemp.setDriverName(receive.getDriverName());
					receiveTemp.setFare(receive.getFare());
					receiveTemp.setMemberId(employee.getId());
					receiveTemp.setEditlist("");
					receiveTempMapper.insertSelective(receiveTemp);
					
					// 완료 문자
					smsService.sendReceiveSms(receive);
					
					resultCode = 1000;
					resultMessage = "도착지 서명 전송이 완료되었습니다.";
					// }
				}
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	public Map<String, Object> cancelReceive(int receiveNum) {
		int resultCode = 9999;
		int updatedRow = 0;
		String resultMessage = "";
		
		Receive receive = receiveMapper.selectByPrimaryKey(receiveNum);
		if (receive != null) {
			if ("접수".equals(receive.getStatus()) || "대기".equals(receive.getStatus())
					|| "인터넷".equals(receive.getStatus())) {
				if (receive.getNtno() == 0) {
					receive.setReserve(0);
					receive.setStatus("취소");
					updatedRow = receiveMapper.updateByPrimaryKeySelective(receive);
				} else {
					int delShareOrderResultCode = 0;
					
					Map<String, Object> delShareOrderMap = shareOrderService.delShareOrder(receiveNum, "시스템",
							receive.getStatus(), "");
					delShareOrderResultCode = (Integer) delShareOrderMap.get("resultCode");
					
					if (delShareOrderResultCode == 1000) {
						receive.setReserve(0);
						receive.setStatus("취소");
						updatedRow = receiveMapper.updateByPrimaryKeySelective(receive);
					} else {
						resultCode = 3003;
						resultMessage = "취소 불가. 퀵사로 연락주세요.";
					}
				}
			} else {
				resultCode = 3002;
				resultMessage = "접수, 대기 상태일 때만 취소가 가능합니다.";
			}
		} else {
			resultCode = 3001;
			resultMessage = "오더 조회가 되지 않습니다.";
		}
		
		if (updatedRow == 1) {
			// 이력 남기기
			ReceiveTempWithBLOBs receiveTemp = new ReceiveTempWithBLOBs();
			receiveTemp.setNum(receive.getNum());
			receiveTemp.setBranchNum(receive.getBranchNum());
			receiveTemp.setBranchName(receive.getBranchName());
			receiveTemp.setStatus(receive.getStatus());
			receiveTemp.setCnum1(receive.getCnum1());
			receiveTemp.setCnum2(receive.getCnum2());
			receiveTemp.setCnum3(receive.getCnum3());
			receiveTemp.setStartJusoNum(receive.getStartJusoNum());
			receiveTemp.setStartSido(receive.getStartSido());
			receiveTemp.setStartGugun(receive.getStartGugun());
			receiveTemp.setStartDong(receive.getStartDong());
			receiveTemp.setStartEtc(receive.getStartEtc());
			receiveTemp.setEndJusoNum(receive.getEndJusoNum());
			receiveTemp.setEndSido(receive.getEndSido());
			receiveTemp.setEndGugun(receive.getEndGugun());
			receiveTemp.setEndDong(receive.getEndDong());
			receiveTemp.setEndEtc(receive.getEndEtc());
			receiveTemp.setDatetime(new Date());
			receiveTemp.setDriverNum(receive.getDriverNum());
			receiveTemp.setDriverName(receive.getDriverName());
			receiveTemp.setMemberId("외부");
			receiveTemp.setFare(receive.getFare());
			receiveTemp.setShuttle(receive.getShuttle());
			receiveTemp.setQuick(receive.getQuick());
			receiveTemp.setAuto(receive.getAuto());
			receiveTemp.setWeight(receive.getWeight());
			receiveTemp.setPayment(receive.getPayment());
			receiveTemp.setFareType(receive.getFareType());
			receiveTemp.setFare(receive.getFare());
			receiveTemp.setFareOrigin(receive.getFareOrigin());
			receiveTemp.setFareWeight(receive.getFareWeight());
			receiveTemp.setFareNight(receive.getFareNight());
			receiveTemp.setFareRain(receive.getFareRain());
			receiveTemp.setFareHoliday(receive.getFareHoliday());
			receiveTemp.setFareEtc(receive.getFareEtc());
			receiveTemp.setFareDiscount(receive.getFareDiscount());
			receiveTemp.setFareDiscount2(receive.getFareDiscount2());
			receiveTemp.setFare_(receive.getFare_());
			receiveTemp.setConsign_(receive.getConsign_());
			receiveTemp.setCallpassFare(receive.getCallpassFare());
			receiveTemp.setIndividual(receive.getIndividual());
			receiveTemp.setStart(receive.getStart());
			receiveTemp.setPickupTime(receive.getPickupTime());
			receiveTemp.setEnd(receive.getEnd());
			receiveTemp.setEditlist("");
			receiveTempMapper.insertSelective(receiveTemp);
			
			resultCode = 1000;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resultCode", resultCode);
		resultMap.put("resultMessage", resultMessage);
		return resultMap;
	}
	
	private int getRequiredTime(int fare, String quickType) {
		int requiredTime = 0;
		if ("긴급".equals(quickType)) {
			if (fare < 7000) {
				requiredTime = 30;
			} else if (fare < 51000) {
				requiredTime = (30 + (fare - 10000) / 1000 * 2);
			} else {
				requiredTime = (111 + (fare - 51000) / 1000);
			}
		} else { // 일반
			if (fare < 7000) {
				requiredTime = 60;
			} else if (fare < 41000) {
				requiredTime = (60 + (fare - 7000) / 1000 * 3);
			} else {
				requiredTime = (160 + (fare - 41000) / 1000);
			}
		}
		return requiredTime;
	}
	
	private String getReceiveMemo(Receive receive) {
		StringBuffer memoSb = new StringBuffer();
		if (StringUtils.isNotBlank(receive.getStartEtc())) {
			memoSb.append(receive.getStartEtc());
		} else {
			memoSb.append(receive.getStartDong());
		}
		memoSb.append(" => ");
		if (StringUtils.isNotBlank(receive.getEndEtc())) {
			memoSb.append(receive.getEndEtc());
		} else {
			memoSb.append(receive.getEndDong());
		}
		return memoSb.toString();
	}
	
	private void setReceiveSign(int receiveNum, String type, String sign, Date datetime) {
		ReceiveSignKey receiveSignKey = new ReceiveSignKey();
		receiveSignKey.setRecvNum(receiveNum);
		receiveSignKey.setType(type);
		ReceiveSign receiveSign = receiveSignMapper.selectByPrimaryKey(receiveSignKey);
		if (receiveSign == null) {
			receiveSign = new ReceiveSign();
			receiveSign.setRecvNum(receiveNum);
			receiveSign.setType(type);
			receiveSign.setSign(sign);
			receiveSign.setDatetime(datetime);
			receiveSignMapper.insert(receiveSign);
		} else {
			receiveSign.setSign(sign);
			receiveSign.setDatetime(datetime);
			receiveSignMapper.updateByPrimaryKeyWithBLOBs(receiveSign);
		}
	}
	
	private double getSssDistance(String startSido, String endSido, double distance) {
		if (("서울".equals(startSido) && "경기".equals(endSido)) || ("서울".equals(startSido) && "인천".equals(endSido))
				|| ("경기".equals(startSido) && "서울".equals(endSido)) || ("경기".equals(startSido) && "인천".equals(endSido))
				|| ("경기".equals(startSido) && "경기".equals(endSido)) || ("인천".equals(startSido) && "서울".equals(endSido))
				|| ("인천".equals(startSido) && "경기".equals(endSido)) || ("인천".equals(startSido) && "인천".equals(endSido))) {
			// fake //
			distance += 1;
		}
		return distance;
	}
	
	private String getItemCode(String item) {
		String itemCode = "";
		if ("TV".equals(item)) {
			itemCode = "P1";
		} else if ("냉장고(일반)".equals(item)) {
			itemCode = "P2";
		} else if ("냉장고(지펠)".equals(item)) {
			itemCode = "P3";
		} else if ("김치냉장고(뚜껑)".equals(item)) {
			itemCode = "P4";
		} else if ("김치냉장고(스텐드)".equals(item)) {
			itemCode = "P5";
		} else if ("세탁기(일반)".equals(item)) {
			itemCode = "P6";
		} else if ("세탁기(드럼)".equals(item)) {
			itemCode = "P7";
		} else if ("에어컨(PAC)".equals(item)) {
			itemCode = "P8";
		} else if ("에어컨(RAC)".equals(item)) {
			itemCode = "P9";
		} else if ("PC(데스크)".equals(item)) {
			itemCode = "P10";
		} else if ("PC(노트)".equals(item)) {
			itemCode = "P11";
		} else if ("모니터".equals(item)) {
			itemCode = "P12";
		} else if ("프린터".equals(item)) {
			itemCode = "P13";
		} else if ("청소기".equals(item)) {
			itemCode = "P14";
		} else if ("스마트오븐".equals(item)) {
			itemCode = "P15";
		} else if ("전자레인지".equals(item)) {
			itemCode = "P16";
		} else if ("밥솥".equals(item)) {
			itemCode = "P17";
		} else if ("가스렌지".equals(item)) {
			itemCode = "P18";
		} else if ("선풍기".equals(item)) {
			itemCode = "P19";
		} else if ("제습기".equals(item)) {
			itemCode = "P20";
		} else if ("전기히터".equals(item)) {
			itemCode = "P21";
		} else if ("석유히터".equals(item)) {
			itemCode = "P22";
		} else if ("휴대폰".equals(item)) {
			itemCode = "P25";
		} else if ("기타(소물)".equals(item)) {
			itemCode = "P31";
		} else if ("기타(대물)".equals(item)) {
			itemCode = "P41";
		}
		return itemCode;
	}
	
	private String getSssVehicleCode(String vehicle) {
		String vehicleCode = "";
		if ("오토".equals(vehicle)) {
			vehicleCode = "C1";
		} else if ("다마".equals(vehicle)) {
			vehicleCode = "C2";
		} else if ("라보".equals(vehicle)) {
			vehicleCode = "C3";
		} else if ("벤".equals(vehicle)) {
			vehicleCode = "C4";
		} else if ("1t".equals(vehicle) || "라보1t".equals(vehicle)) {
			vehicleCode = "C5";
		} else if ("1.4t".equals(vehicle)) {
			vehicleCode = "C6";
		} else if ("2.5t".equals(vehicle)) {
			vehicleCode = "C7";
		} else if ("3.5t".equals(vehicle)) {
			vehicleCode = "C8";
		} else if ("5t".equals(vehicle)) {
			vehicleCode = "C9";
		}
		return vehicleCode;
	}
	
	private String getVehicleName(String vehicle) {
		String vehicleName = "";
		if ("오토".equals("vehicle")) {
			vehicleName = "오토바이";
		} else if ("다마".equals("vehicle")) {
			vehicleName = "다마스";
		} else if ("라보".equals("vehicle")) {
			vehicleName = "라보";
		} else if ("벤".equals("vehicle")) {
			vehicleName = "승합벤";
		} else if ("1t".equals("vehicle") || "라보1t".equals("vehicle")) {
			vehicleName = "트럭1T";
		} else if ("1.4t".equals("vehicle")) {
			vehicleName = "트럭1.4T";
		} else if ("2.5t".equals("vehicle")) {
			vehicleName = "트럭2.5T";
		} else if ("3.5t".equals("vehicle")) {
			vehicleName = "트럭3.5T";
		} else if ("5t".equals("vehicle")) {
			vehicleName = "트럭5T";
		}
		return vehicleName;
	}
	
	private String getSssGuCode(String gu) {
		String guCode = "";
		if ("강남구".equals(gu)) {
			guCode = "A1";
		} else if ("강동구".equals(gu)) {
			guCode = "A2";
		} else if ("강북구".equals(gu)) {
			guCode = "A3";
		} else if ("강서구".equals(gu)) {
			guCode = "A4";
		} else if ("구로구".equals(gu)) {
			guCode = "A5";
		} else if ("금천구".equals(gu)) {
			guCode = "A6";
		} else if ("관악구".equals(gu)) {
			guCode = "A7";
		} else if ("광진구".equals(gu)) {
			guCode = "A8";
		} else if ("노원구".equals(gu)) {
			guCode = "A9";
		} else if ("도봉구".equals(gu)) {
			guCode = "A10";
		} else if ("동대문구".equals(gu)) {
			guCode = "A11";
		} else if ("동작구".equals(gu)) {
			guCode = "A12";
		} else if ("마포구".equals(gu)) {
			guCode = "A13";
		} else if ("서대문구".equals(gu)) {
			guCode = "A14";
		} else if ("성동구".equals(gu)) {
			guCode = "A15";
		} else if ("성북구".equals(gu)) {
			guCode = "A16";
		} else if ("송파구".equals(gu)) {
			guCode = "A17";
		} else if ("양천구".equals(gu)) {
			guCode = "A18";
		} else if ("영등포구".equals(gu)) {
			guCode = "A19";
		} else if ("용산구".equals(gu)) {
			guCode = "A20";
		} else if ("은평구".equals(gu)) {
			guCode = "A21";
		} else if ("중랑구".equals(gu)) {
			guCode = "A22";
		} else if ("종로구".equals(gu)) {
			guCode = "A23";
		} else if ("중구".equals(gu)) {
			guCode = "A24";
		} else if ("서초구".equals(gu)) {
			guCode = "A25";
		}
		return guCode;
	}
	
	private ReceiveCustomer getReceiveCustomer(Map<String, Object> receiveMap, String customerDiv) {
		ReceiveCustomer receiveCustomer = new ReceiveCustomer();
		receiveCustomer.setNum((Integer) receiveMap.get(customerDiv + "num"));
		receiveCustomer.setBranchNum((Integer) receiveMap.get(customerDiv + "branchNum"));
		receiveCustomer.setBranchName((String) receiveMap.get(customerDiv + "branchName"));
		receiveCustomer.setReceiveNum((Integer) receiveMap.get(customerDiv + "receiveNum"));
		receiveCustomer.setRcvCustType((String) receiveMap.get(customerDiv + "rcvCustType"));
		receiveCustomer.setCnum((Integer) receiveMap.get(customerDiv + "cnum"));
		receiveCustomer.setCname((String) receiveMap.get(customerDiv + "cname"));
		receiveCustomer.setDepartment((String) receiveMap.get(customerDiv + "department"));
		receiveCustomer.setPerson((String) receiveMap.get(customerDiv + "person"));
		receiveCustomer.setTel((String) receiveMap.get(customerDiv + "tel"));
		receiveCustomer.setPhone((String) receiveMap.get(customerDiv + "phone"));
		receiveCustomer.setTelephone((String) receiveMap.get(customerDiv + "telephone"));
		receiveCustomer.setLocJusoNum((Integer) receiveMap.get(customerDiv + "locJusoNum"));
		receiveCustomer.setLocSido((String) receiveMap.get(customerDiv + "locSido"));
		receiveCustomer.setLocGugun((String) receiveMap.get(customerDiv + "locGugun"));
		receiveCustomer.setLocDong((String) receiveMap.get(customerDiv + "locDong"));
		receiveCustomer.setLocDongNum((String) receiveMap.get(customerDiv + "locDongNum"));
		receiveCustomer.setLocPosition((String) receiveMap.get(customerDiv + "locPosition"));
		receiveCustomer.setLocBuilding((String) receiveMap.get(customerDiv + "locBuilding"));
		receiveCustomer.setLocFloor((String) receiveMap.get(customerDiv + "locFloor"));
		receiveCustomer.setLocEtc((String) receiveMap.get(customerDiv + "locEtc"));
		receiveCustomer.setLocDetail((String) receiveMap.get(customerDiv + "locDetail"));
		receiveCustomer.setCredit((Integer) receiveMap.get(customerDiv + "credit"));
		receiveCustomer.setAddition((Integer) receiveMap.get(customerDiv + "addition"));
		receiveCustomer.setExtra((Integer) receiveMap.get(customerDiv + "extra"));
		receiveCustomer.setExtraName((String) receiveMap.get(customerDiv + "extraName"));
		receiveCustomer.setCarGroup((String) receiveMap.get(customerDiv + "carGroup"));
		receiveCustomer.setMemo((String) receiveMap.get(customerDiv + "memo"));
		receiveCustomer.setSalesTeam((Integer) receiveMap.get(customerDiv + "salesTeam"));
		receiveCustomer.setGroupNum((String) receiveMap.get(customerDiv + "groupNum"));
		receiveCustomer.setBranch((String) receiveMap.get(customerDiv + "branch"));
		receiveCustomer.setTempNum((Integer) receiveMap.get(customerDiv + "tempNum"));
		receiveCustomer.setDriverMemo((String) receiveMap.get(customerDiv + "driverMemo"));
		receiveCustomer.setAccount((String) receiveMap.get(customerDiv + "account"));
		receiveCustomer.setSaveOver((Integer) receiveMap.get(customerDiv + "saveOver"));
		receiveCustomer.setSave((Integer) receiveMap.get(customerDiv + "save"));
		receiveCustomer.setSaveName((String) receiveMap.get(customerDiv + "saveName"));
		receiveCustomer.setSms1((Integer) receiveMap.get(customerDiv + "sms1"));
		receiveCustomer.setSms2((Integer) receiveMap.get(customerDiv + "sms2"));
		receiveCustomer.setSms3((Integer) receiveMap.get(customerDiv + "sms3"));
		receiveCustomer.setSms4((Integer) receiveMap.get(customerDiv + "sms4"));
		receiveCustomer.setSms5((Integer) receiveMap.get(customerDiv + "sms5"));
		receiveCustomer.setSms6((Integer) receiveMap.get(customerDiv + "sms6"));
		receiveCustomer.setSms6((Integer) receiveMap.get(customerDiv + "sms7"));
		receiveCustomer.setSms7((Integer) receiveMap.get(customerDiv + "sms8"));
		receiveCustomer.setSms8((Integer) receiveMap.get(customerDiv + "sms9"));
		receiveCustomer.setId((String) receiveMap.get(customerDiv + "id"));
		receiveCustomer.setGroupId((String) receiveMap.get(customerDiv + "groupId"));
		receiveCustomer.setDel((Integer) receiveMap.get(customerDiv + "del"));
		receiveCustomer.setBak((Integer) receiveMap.get(customerDiv + "bak"));
		
		return receiveCustomer;
	}
	
	private Map<String, Object> getReceiveDriverMap(Map<String, Object> receiveMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("num", (Integer) receiveMap.get("driver_num"));
		returnMap.put("vnum", (String) receiveMap.get("driver_vnum"));
		returnMap.put("name", (String) receiveMap.get("driver_name"));
		returnMap.put("id", (String) receiveMap.get("driver_id"));
		returnMap.put("password", (String) receiveMap.get("driver_password"));
		returnMap.put("workType", (String) receiveMap.get("driver_workType"));
		returnMap.put("vehicle", (String) receiveMap.get("driver_vehicle"));
		returnMap.put("vehicleNum", (String) receiveMap.get("driver_vehicleNum"));
		returnMap.put("phone", (String) receiveMap.get("driver_phone"));
		returnMap.put("tel", (String) receiveMap.get("driver_tel"));
		returnMap.put("jumin", (String) receiveMap.get("driver_jumin"));
		returnMap.put("address", (String) receiveMap.get("driver_address"));
		returnMap.put("memo", (String) receiveMap.get("driver_memo"));
		returnMap.put("inDate", (String) receiveMap.get("driver_inDate"));
		returnMap.put("outDate", (String) receiveMap.get("driver_outDate"));
		returnMap.put("reworkDate", (String) receiveMap.get("driver_reworkDate"));
		returnMap.put("stopDatetime", (String) receiveMap.get("driver_stopDatetime"));
		returnMap.put("stopChk", (Integer) receiveMap.get("driver_stopChk"));
		returnMap.put("outChk", (Integer) receiveMap.get("driver_outChk"));
		returnMap.put("license", (String) receiveMap.get("driver_license"));
		returnMap.put("licenseNum", (String) receiveMap.get("driver_licenseNum"));
		returnMap.put("latitude", (Double) receiveMap.get("driver_latitude"));
		returnMap.put("longitude", (Double) receiveMap.get("driver_longitude"));
		returnMap.put("location", (String) receiveMap.get("driver_location"));
		returnMap.put("state", (String) receiveMap.get("driver_state"));
		returnMap.put("county", (String) receiveMap.get("driver_county"));
		returnMap.put("town", (String) receiveMap.get("driver_town"));
		returnMap.put("loginDatetime", (Date) receiveMap.get("driver_loginDatetime"));
		returnMap.put("refreshDatetime", (Date) receiveMap.get("driver_refreshDatetime"));
		returnMap.put("gpsDatetime", (Date) receiveMap.get("driver_gpsDatetime"));
		returnMap.put("autoTarget1", (String) receiveMap.get("driver_autoTarget1"));
		returnMap.put("autoTarget2", (String) receiveMap.get("driver_autoTarget2"));
		returnMap.put("autoTarget3", (String) receiveMap.get("driver_autoTarget3"));
		returnMap.put("autoTarget4", (String) receiveMap.get("driver_autoTarget4"));
		returnMap.put("autoTarget5", (String) receiveMap.get("driver_autoTarget5"));
		returnMap.put("login", (Integer) receiveMap.get("driver_login"));
		returnMap.put("noticeNum", (Integer) receiveMap.get("driver_noticeNum"));
		returnMap.put("chkInternet", (Integer) receiveMap.get("driver_chkInternet"));
		returnMap.put("messageChk", (Integer) receiveMap.get("driver_messageChk"));
		returnMap.put("keyphone", (String) receiveMap.get("driver_keyphone"));
		returnMap.put("charge", (Integer) receiveMap.get("driver_charge"));
		returnMap.put("defaultCharge", (Integer) receiveMap.get("driver_defaultCharge"));
		returnMap.put("autoSendMoneyYn", (String) receiveMap.get("driver_autoSendMoneyYn"));
		returnMap.put("withholdingYn", (String) receiveMap.get("driver_withholdingYn"));
		returnMap.put("feeType", (String) receiveMap.get("driver_feeType"));
		returnMap.put("feeTypeModifyDate", (Date) receiveMap.get("driver_feeTypeModifyDate"));
		returnMap.put("bankCode", (String) receiveMap.get("driver_bankCode"));
		returnMap.put("account", (String) receiveMap.get("driver_account"));
		returnMap.put("accountName", (String) receiveMap.get("driver_accountName"));
		returnMap.put("outChargeAccountNum", (Integer) receiveMap.get("driver_outChargeAccountNum"));
		returnMap.put("vbankCode", (String) receiveMap.get("driver_vbankCode"));
		returnMap.put("vaccount", (String) receiveMap.get("driver_vaccount"));
		returnMap.put("bankName", (String) receiveMap.get("driver_bankName"));
		returnMap.put("vbankName", (String) receiveMap.get("driver_vbankName"));
		returnMap.put("branchNum", (Integer) receiveMap.get("driver_branchNum"));
		returnMap.put("branchName", (String) receiveMap.get("driver_branchName"));
		returnMap.put("branchTel", (String) receiveMap.get("driver_branchTel"));
		return returnMap;
	}
	
	private Map<String, Object> getReceiveMap(Map<String, Object> receiveMap) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("num", (Integer) receiveMap.get("receive_num"));
		returnMap.put("status", (String) receiveMap.get("receive_status"));
		returnMap.put("datetime", (Date) receiveMap.get("receive_datetime"));
		returnMap.put("cnum1", (Integer) receiveMap.get("receive_cnum1"));
		returnMap.put("cnum2", (Integer) receiveMap.get("receive_cnum2"));
		returnMap.put("cnum3", (Integer) receiveMap.get("receive_cnum3"));
		returnMap.put("reserve", (Integer) receiveMap.get("receive_reserve"));
		returnMap.put("reserveDate", (String) receiveMap.get("receive_reserveDate"));
		returnMap.put("reserveHour", (Integer) receiveMap.get("receive_reserveHour"));
		returnMap.put("reserveMinute", (Integer) receiveMap.get("receive_reserveMinute"));
		returnMap.put("selYoilRepeat", (String) receiveMap.get("receive_selYoilRepeat"));
		returnMap.put("selDayRepeat", (String) receiveMap.get("receive_selDayRepeat"));
		returnMap.put("reserveDv", (String) receiveMap.get("receive_reserveDv"));
		returnMap.put("sdtRepeat", (String) receiveMap.get("receive_sdtRepeat"));
		returnMap.put("edtRepeat", (String) receiveMap.get("receive_edtRepeat"));
		returnMap.put("keyReserveRpeat", (String) receiveMap.get("receive_keyReserveRpeat"));
		returnMap.put("shuttle", (String) receiveMap.get("receive_shuttle"));
		returnMap.put("quick", (String) receiveMap.get("receive_quick"));
		returnMap.put("auto", (String) receiveMap.get("receive_auto"));
		returnMap.put("weight", (Integer) receiveMap.get("receive_weight"));
		returnMap.put("payment", (String) receiveMap.get("receive_payment"));
		returnMap.put("driverNum", (Integer) receiveMap.get("receive_driverNum"));
		returnMap.put("driverVnum", (String) receiveMap.get("receive_driverVnum"));
		returnMap.put("fareType", (Integer) receiveMap.get("receive_fareType"));
		returnMap.put("fare", (Integer) receiveMap.get("receive_fare"));
		returnMap.put("fareOrigin", (Integer) receiveMap.get("receive_fareOrigin"));
		returnMap.put("fareWeight", (Integer) receiveMap.get("receive_fareWeight"));
		returnMap.put("fareEtc", (Integer) receiveMap.get("receive_fareEtc"));
		returnMap.put("fareNight", (Integer) receiveMap.get("receive_fareNight"));
		returnMap.put("fareRain", (Integer) receiveMap.get("receive_fareRain"));
		returnMap.put("fareHoliday", (Integer) receiveMap.get("receive_fareHoliday"));
		returnMap.put("fareDiscount", (Integer) receiveMap.get("receive_fareDiscount"));
		returnMap.put("fareDiscount2", (Integer) receiveMap.get("receive_fareDiscount2"));
		returnMap.put("fare_", (Integer) receiveMap.get("receive_fare_"));
		returnMap.put("consign_", (Integer) receiveMap.get("receive_consign_"));
		returnMap.put("item", (String) receiveMap.get("receive_item"));
		returnMap.put("serialNum", (String) receiveMap.get("receive_serialNum"));
		returnMap.put("comment", (String) receiveMap.get("receive_comment"));
		returnMap.put("extDriver", (String) receiveMap.get("receive_extDriver"));
		returnMap.put("individual", (Date) receiveMap.get("receive_individual"));
		returnMap.put("start", (Date) receiveMap.get("receive_start"));
		returnMap.put("pickupTime", (Date) receiveMap.get("receive_pickupTime"));
		returnMap.put("end", (Date) receiveMap.get("receive_end"));
		returnMap.put("memberId", (String) receiveMap.get("receive_memberId"));
		returnMap.put("member", (String) receiveMap.get("receive_member"));
		returnMap.put("modifyId", (String) receiveMap.get("receive_modifyId"));
		returnMap.put("modify", (String) receiveMap.get("receive_modify"));
		returnMap.put("share", (Integer) receiveMap.get("receive_share"));
		returnMap.put("callpassYn", (String) receiveMap.get("receive_callpassYn"));
		returnMap.put("callpassConfirmYn", (String) receiveMap.get("receive_callpassConfirmYn"));
		returnMap.put("callpassBranchNum", (Integer) receiveMap.get("receive_callpassBranchNum"));
		returnMap.put("callpassBranchName", (String) receiveMap.get("receive_callpassBranchName"));
		returnMap.put("callpassFare", (Integer) receiveMap.get("receive_callpassFare"));
		returnMap.put("internetReqYn", (String) receiveMap.get("receive_internetReqYn"));
		returnMap.put("nTNo", (Integer) receiveMap.get("receive_nTNo"));
		returnMap.put("elapse", (Long) receiveMap.get("receive_elapse"));
		returnMap.put("nState", (Integer) receiveMap.get("receive_nState"));
		returnMap.put("nCompany", (Integer) receiveMap.get("receive_nCompany"));
		returnMap.put("sRCompanyName", (String) receiveMap.get("receive_sRCompanyName"));
		returnMap.put("sRCompanyTel", (String) receiveMap.get("receive_sRCompanyTel"));
		returnMap.put("sRCompanyOfficeTel", (String) receiveMap.get("receive_sRCompanyOfficeTel"));
		returnMap.put("branchNum", (Integer) receiveMap.get("receive_branchNum"));
		returnMap.put("branchName", (String) receiveMap.get("receive_branchName"));
		return returnMap;
	}
	
	private int getShareCarType(String vehicle) {
		int shareCarType = 0;
		
		// 공유서버 차량 타입 (0: 오토바이, 1: 다마스, 2: 라보, 3: 밴, 4:지하철, 5: 트럭)
		
		if ("오토".equals(vehicle)) {
			shareCarType = 0;
		} else if ("다마".equals(vehicle)) {
			shareCarType = 1;
		} else if ("라보".equals(vehicle)) {
			shareCarType = 2;
		} else if ("벤".equals(vehicle)) {
			shareCarType = 3;
		} else if ("지하철".equals(vehicle)) {
			shareCarType = 4;
		} else {
			shareCarType = 5;
		}
		
		return shareCarType;
	}
	
	private ReceiveShareOrder setReceiveShare(Map<String, Object> receiveMap) {
		ReceiveShareOrder receiveShareOrder = new ReceiveShareOrder();
		receiveShareOrder.setReceiveNum((Integer) receiveMap.get("num"));
		receiveShareOrder.setStatus((String) receiveMap.get("status"));
		receiveShareOrder.setDateTime((Date) receiveMap.get("dateTime"));
		receiveShareOrder.setCnum1((Integer) receiveMap.get("cnum1"));
		receiveShareOrder.setCnum2((Integer) receiveMap.get("cnum2"));
		receiveShareOrder.setCnum3((Integer) receiveMap.get("cnum3"));
		receiveShareOrder.setReserve((Integer) receiveMap.get("reserve"));
		receiveShareOrder.setReserveDate((String) receiveMap.get("reserveDate"));
		receiveShareOrder.setReserveHour((Integer) receiveMap.get("reserveHour"));
		receiveShareOrder.setReserveMinute((Integer) receiveMap.get("reserveMinute"));
		receiveShareOrder.setSelYoilRepeat((String) receiveMap.get("selYoilRepeat"));
		receiveShareOrder.setSelDayRepeat((String) receiveMap.get("selDayRepeat"));
		receiveShareOrder.setReserveDv((String) receiveMap.get("reserveDv"));
		receiveShareOrder.setSdtRepeat((String) receiveMap.get("sdtRepeat"));
		receiveShareOrder.setEdtRepeat((String) receiveMap.get("edtRepeat"));
		receiveShareOrder.setKeyReserveRpeat((String) receiveMap.get("keyReserveRpeat"));
		receiveShareOrder.setAuto((String) receiveMap.get("auto"));
		receiveShareOrder.setShuttle((String) receiveMap.get("shuttle"));
		receiveShareOrder.setQuick((String) receiveMap.get("quick"));
		receiveShareOrder.setWeight((Integer) receiveMap.get("weight"));
		receiveShareOrder.setPayment((String) receiveMap.get("payment"));
		receiveShareOrder.setStartDong((String) receiveMap.get("startDong"));
		receiveShareOrder.setStartEtc((String) receiveMap.get("startEtc"));
		receiveShareOrder.setEndDong((String) receiveMap.get("endDong"));
		receiveShareOrder.setEndEtc((String) receiveMap.get("endEtc"));
		receiveShareOrder.setDriverNum((Integer) receiveMap.get("driverNum"));
		receiveShareOrder.setDriverVnum((String) receiveMap.get("driverVnum"));
		receiveShareOrder.setFareType((Integer) receiveMap.get("fareType"));
		receiveShareOrder.setFare((Integer) receiveMap.get("fare"));
		receiveShareOrder.setFareOrigin((Integer) receiveMap.get("fareOrigin"));
		receiveShareOrder.setFareWeight((Integer) receiveMap.get("fareWeight"));
		receiveShareOrder.setFareEtc((Integer) receiveMap.get("fareEtc"));
		receiveShareOrder.setFareNight((Integer) receiveMap.get("fareNight"));
		receiveShareOrder.setFareRain((Integer) receiveMap.get("fareRain"));
		receiveShareOrder.setFareHoliday((Integer) receiveMap.get("fareHoliday"));
		receiveShareOrder.setFareDiscount((Integer) receiveMap.get("fareDiscount"));
		receiveShareOrder.setFareDiscount2((Integer) receiveMap.get("fareDiscount2"));
		receiveShareOrder.setFare_((Integer) receiveMap.get("fare_"));
		receiveShareOrder.setConsign_((Integer) receiveMap.get("consign_"));
		receiveShareOrder.setItem((String) receiveMap.get("item"));
		receiveShareOrder.setSerialNum((String) receiveMap.get("serialNum"));
		receiveShareOrder.setComment((String) receiveMap.get("comment"));
		receiveShareOrder.setExtDriver((String) receiveMap.get("extDriver"));
		receiveShareOrder.setIndividual((String) receiveMap.get("individual"));
		receiveShareOrder.setStart((String) receiveMap.get("start"));
		receiveShareOrder.setPickupTime((String) receiveMap.get("pickupTime"));
		receiveShareOrder.setEnd((String) receiveMap.get("end"));
		receiveShareOrder.setMember((String) receiveMap.get("member"));
		receiveShareOrder.setMemberId((String) receiveMap.get("memberId"));
		receiveShareOrder.setModify((String) receiveMap.get("modify"));
		receiveShareOrder.setModifyId((String) receiveMap.get("modifyId"));
		receiveShareOrder.setShare((Integer) receiveMap.get("share"));
		receiveShareOrder.setCallpassYn((String) receiveMap.get("callpassYn"));
		receiveShareOrder.setCallpassConfirmYn((String) receiveMap.get("callpassConfirmYn"));
		receiveShareOrder.setCallpassBranchNum((Integer) receiveMap.get("callpassBranchNum"));
		receiveShareOrder.setCallpassBranchName((String) receiveMap.get("callpassBranchName"));
		receiveShareOrder.setCallpassFare((Integer) receiveMap.get("callpassFare"));
		receiveShareOrder.setInternetReqYn((String) receiveMap.get("internetReqYn"));
		receiveShareOrder.setnTNo((Integer) receiveMap.get("nTNo"));
		receiveShareOrder.setCargoShareYn((String) receiveMap.get("cargoShareYn"));
		receiveShareOrder.setCargoNum((Integer) receiveMap.get("cargoNum"));
		receiveShareOrder.setCargoConsign((Integer) receiveMap.get("cargoConsign"));
		receiveShareOrder.setElapse((String) receiveMap.get("elapse"));
		receiveShareOrder.setnState((Integer) receiveMap.get("nState"));
		receiveShareOrder.setnCompany((Integer) receiveMap.get("nCompany"));
		receiveShareOrder.setsRCompanyName((String) receiveMap.get("sRCompanyName"));
		receiveShareOrder.setsRCompanyTel((String) receiveMap.get("sRCompanyTel"));
		receiveShareOrder.setsRCompanyOfficeTel((String) receiveMap.get("sRCompanyOfficeTel"));
		receiveShareOrder.setBranchNum((Integer) receiveMap.get("branchNum"));
		receiveShareOrder.setBranchName((String) receiveMap.get("branchName"));
		
		return receiveShareOrder;
	}
	
	private int getCommission(int fare_, String auto, int branchNum) {
		Branch branch = branchMapper.selectByPrimaryKey(branchNum);
		
		int commission = 0;
		int commissionRate = getRiderCommissionRate(auto, branch);
		
		commission = fare_ * commissionRate / 100;
		
		return commission;
	}
	
	private int getRiderCommissionRate(String vehicleType, Branch branch) {
		int riderCommissionRate = 0;
		if ("오토".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission1();
		} else if ("다마".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission2();
		} else if ("라보".equals(vehicleType) || "라보1t".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission3();
		} else if ("벤".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission4();
		} else if ("1t".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission5();
		} else if ("1.4t".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission6();
		} else if ("2.5t".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission7();
		} else if ("3.5t".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission8();
		} else if ("5t".equals(vehicleType)) {
			riderCommissionRate = branch.getCommission9();
		}
		return riderCommissionRate;
	}
	
	private int getWeight(String weight) {
		int weightCode = 0;
		if ("0".equals(weight) || "1".equals(weight) || "2".equals(weight) || "3".equals(weight) || "4".equals(weight)
				|| "5".equals(weight) || "6".equals(weight) || "7".equals(weight) || "8".equals(weight)
				|| "9".equals(weight) || "10".equals(weight)) {
			weightCode = Integer.parseInt(weight);
		}
		return weightCode;
	}
	
}
