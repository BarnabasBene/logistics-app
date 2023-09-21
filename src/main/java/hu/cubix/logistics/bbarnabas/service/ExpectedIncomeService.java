package hu.cubix.logistics.bbarnabas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExpectedIncomeService {
	
	@Value("${logistics.expectedIncome.incomeLossForDelayLessThan30M}")
	private double incomeLossForDelayLessThan30M;
	@Value("${logistics.expectedIncome.incomeLossForDelayMoreThan30M}")
	private double incomeLossForDelayMoreThan30M;
	@Value("${logistics.expectedIncome.incomeLossForDelayMoreThan60M}")
	private double incomeLossForDelayMoreThan60M;
	@Value("${logistics.expectedIncome.incomeLossForDelayMoreThan120M}")
	private double incomeLossForDelayMoreThan120M;
	
	public double expectedIncomeByDelay (int delayInMinutes, double expectedIncome) {
		
		double newExpectedIncome;
		
		if (delayInMinutes <= 30) {
			newExpectedIncome = expectedIncome * (100-incomeLossForDelayLessThan30M)/100; // Decrease income by 2.5%
        } else if (delayInMinutes <= 60) {
        	newExpectedIncome = expectedIncome * (100-incomeLossForDelayMoreThan30M)/100; // Decrease income by 5%
        } else if (delayInMinutes <= 120) {
        	newExpectedIncome = expectedIncome * (100-incomeLossForDelayMoreThan60M)/100; // Decrease income by 10%
        } else {
        	newExpectedIncome = expectedIncome * (100-incomeLossForDelayMoreThan120M)/100; // Decrease income by 15%
        }
		
		return newExpectedIncome;
		
	}
}
