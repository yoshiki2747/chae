package kr.totalcall.task;

import kr.totalcall.util.LoggerHelper;

import org.apache.log4j.Logger;

public abstract class CommonWorker {
	
	protected static final Logger logger = LoggerHelper.getLogger();
	
	abstract public void doWork();
	
}
