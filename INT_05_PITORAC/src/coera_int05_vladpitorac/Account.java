package coera_int05_vladpitorac;

public class Account {
	
	String name;
	double firstRate;
	double secondRate;
	int amount;
	
	Account(String name, double firstRate, double secondRate, int amount){
		this.name = name;
		this.firstRate = firstRate;
		this.secondRate = secondRate;
		this.amount = amount;
	}
	
	void printData() {
		System.out.println("\tThe " + this.name + " account:");
		System.out.println("-First rate is : " + this.firstRate * 100 + "%");
		System.out.println("-Second rate is : " + this.secondRate * 100 + "%");
		System.out.println("-The amount of money in the account is : " + this.amount + " RON\n");
	}
	
	void applyRate() {
		
		//round to the closest integer
		if(this.amount != 0)
		this.amount = (int) Math.round(this.amount + 500 * this.firstRate + Math.min((this.amount - 500), 4500) * this.secondRate);

	}
	void setData(Account account) {
		this.name = account.name;
		this.firstRate = account.firstRate;
		this.secondRate = account.secondRate;
		this.amount = account.amount;
		
	}
	
	int getSum() {		
		return this.amount;
	}
	
	double getFirstRate() {
		return this.firstRate;
	}
	
	double getSecondRate() {
		return this.secondRate;
	}
}
