package kr.totalcall.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CustomerReceiveMapper {
	
	List<Map<String, Object>> selectCustomerReceive(@Param("loginType") int loginType,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("reportType") String reportType, @Param("customerNum") int customerNum,
			@Param("branchNum") int branchNum, @Param("payType") String payType, @Param("searchWord") String searchWord);
	
	List<Map<String, Object>> selectCustomerGroupReceive(@Param("loginType") int loginType,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("reportType") String reportType, @Param("customerGroupNum") int customerGroupNum,
			@Param("branchNum") int branchNum, @Param("payType") String payType, @Param("searchWord") String searchWord);
	
	List<Map<String, Object>> selectCustomerAdminReceive(@Param("loginType") int loginType,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("reportType") String reportType, @Param("customerAdminNum") int customerAdminNum,
			@Param("branchNum") int branchNum, @Param("payType") String payType, @Param("searchWord") String searchWord);
	
	List<Map<String, Object>> selectCustomerMasterReceive(@Param("loginType") int loginType,
			@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("reportType") String reportType, @Param("customerMasterNum") int customerMasterNum,
			@Param("branchNum") int branchNum, @Param("payType") String payType, @Param("searchWord") String searchWord);
	
	List<Map<String, Object>> searchCustomerReceive(@Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("branchNum") int branchNum, @Param("payType") String payType,
			@Param("searchWord") String searchWord);
	
	List<Map<String, Object>> selectNonMemberReceive(@Param("startDate") String startDate,
			@Param("endDate") String endDate, @Param("branchNum") int branchNum, @Param("telNum") String telNum,
			@Param("pass") String pass, @Param("searchWord") String searchWord);
	
	Map<String, Object> selectReceiveDetail(int receiveNum);
	
}
