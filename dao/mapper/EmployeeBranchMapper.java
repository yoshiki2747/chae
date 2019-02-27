package kr.totalcall.dao.mapper;

import java.util.List;
import java.util.Map;

import kr.totalcall.dao.model.EmployeeCardBranch;

import org.apache.ibatis.annotations.Param;

public interface EmployeeBranchMapper {
	
	String selectBranchName(@Param("employeeId") String employeeId, @Param("employeeNum") int employeeNum);
	
	List<EmployeeCardBranch> selectEmployee(@Param("branchNum") int branchNum, @Param("callgroupNum") int callgroupNum,
			@Param("workType") int workType, @Param("vehicle") String vehicle,
			@Param("workStatusType") int workStatusType, @Param("searchWord") String searchWord);
	
	Map<String, Object> selectDriverByNum(int employeeNum);
	
	List<Map<String, Object>> selectEmployeeByKeyphone(@Param("keyphoneNum") String keyphoneNum,
			@Param("branchNum") int branchNum);
	
	List<Integer> selectOtherEmployeeByKeyphone(@Param("employeeNum") int employeeNum,
			@Param("keyphoneNum") String keyphoneNum, @Param("callgroupNum") int callgroupNum);
	
	List<Map<String, Object>> selectEmployeeWithholding(@Param("branchCallGroupNum") int branchCallGroupNum,
			@Param("branchNum") int branchNum, @Param("startMonth") String startMonth,
			@Param("endMonth") String endMonth);
	
	void updateEmployeeLocation(@Param("employeeNum") int employeeNum, @Param("latitude") double latitude,
			@Param("longitude") double longitude, @Param("state") String state, @Param("county") String county,
			@Param("town") String town);
	
	void updateEmployeeLoginCheck();
	
	Map<String, Object> selectEmployeeBranchCallgroup(@Param("userId") String userId, @Param("userPw") String userPw);
	
}
