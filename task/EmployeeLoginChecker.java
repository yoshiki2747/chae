package kr.totalcall.task;

import kr.totalcall.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("employeeLoginChecker")
public class EmployeeLoginChecker extends CommonWorker {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Override
	public void doWork() {
		logger.info("employeeLoginChecker start");
		employeeService.employeeLoginCheck();
	}
}
