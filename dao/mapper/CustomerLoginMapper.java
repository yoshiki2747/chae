package kr.totalcall.dao.mapper;

import kr.totalcall.dao.model.CustomerLogin;

import org.apache.ibatis.annotations.Param;

public interface CustomerLoginMapper {
	
	CustomerLogin selectCustomerLogin(@Param("loginId") String loginId, @Param("loginPw") String loginPw,
			@Param("loginType") int loginType, @Param("branchNum") int branchNum);
	
}
