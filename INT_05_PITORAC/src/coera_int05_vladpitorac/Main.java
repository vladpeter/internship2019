package coera_int05_vladpitorac;

import java.util.*;

public class Main {
	
	//additional method to update an account in a list of accounts
	static List<Account> update(List<Account> accounts, Account account) {
		
		for(int i = 0; i < accounts.size(); i++)
			if(accounts.get(i).name == account.name) {
				accounts.get(i).setData(account);
				break;
			}
		return accounts;
	}
	
	static public List<Account> redistributeMoney(List<Account> accounts) {
        
		//get the total sum of the accounts for distribution
		int sum = accounts.stream().map(Account::getSum).mapToInt(Integer::intValue).sum(); 

		//set the accounts to 0 money
		for(int i = 0; i < accounts.size(); i++)
			accounts.get(i).amount = 0;
		
		//sort the accounts by the biggest first rate for the distribution of 500's
		accounts.sort(Comparator.comparing(Account::getFirstRate).reversed());
		
		//duplicate of the sorted list
		List<Account> accounts2 = new ArrayList<Account>();
		accounts2 = accounts;
		
		//distributed 500's in the accounts
		for(int i = 0; i < accounts.size() && sum > 0; i++) {
			if(sum >= 500){
				accounts.get(i).amount +=500;
				sum -= 500;
			}
			else {
				accounts.get(i).amount += sum;
				sum = 0;
			}		
		}
		
		//sort the accounts by the second rate
		accounts.sort(Comparator.comparing(Account::getSecondRate).reversed());
		//distribute as much as 4500 RON per account
		for(int i = 0; i < accounts.size() && sum > 0; i++) {
			
			if(sum > 4500){
				accounts.get(i).amount += 4500;
				sum -= 4500;
			}
			else {
				accounts.get(i).amount += sum;
				sum = 0;
			}		
		}
		
		//if there's still money to distributed, put it in random account
		//because both intervals are full for every account 
		if(sum > 0){
			accounts.get(0).amount += sum;
			return accounts;
		}
		//else there's still 2 distributions to do
		else {
		
		accounts2.sort(Comparator.comparing(Account::getFirstRate));
		//first: if there are accounts with second rate bigger than first rates of some accounts
		//redistribute to the bigger rate
		for(int i = 0; i < accounts2.size(); i++)
			for(int j = 0; j < accounts.size(); j++) {
				
				int moveSum = Math.min(5000-accounts.get(j).amount,(accounts2.get(i).amount));
				
				if(accounts2.get(i).firstRate < accounts.get(j).secondRate && accounts.get(j).amount + moveSum <= 5000) {
					
					accounts.get(j).amount += moveSum;
					accounts2.get(i).amount -= moveSum;	
					Account a1 = accounts.get(j);
					Account a2 = accounts.get(i);
					accounts2 = update(accounts2, a1);
					accounts = update(accounts, a2);			
				}
				if(accounts2.get(i).amount == 0)
					break;	
			}
		//second: after the distribution from the previous case
		//there might be money in accounts with the second rate smaller that other accounts which are not full
		accounts2.sort(Comparator.comparing(Account::getSecondRate));
		for(int i = 0; i < accounts2.size(); i++)
			for(int j = 0; j < accounts.size(); j++) {
				
				if(accounts2.get(i).amount <= 500)
					break;
				int moveSum = Math.min(5000-accounts.get(j).amount,(accounts2.get(i).amount - 500));
				
				if(accounts2.get(i).secondRate < accounts.get(j).secondRate && accounts.get(j).amount + moveSum <= 5000) {
					
					accounts.get(j).amount += moveSum;
					accounts2.get(i).amount -= moveSum;	
					Account a1 = accounts.get(j);
					Account a2 = accounts.get(i);
					accounts2 = update(accounts2, a1);
					accounts = update(accounts, a2);			
				}
				if(accounts2.get(i).amount == 500)
					break;	
			}
		
		}
		
		return accounts;
    }
	
	
	public static void main (String []Args) {
		
		//instantiating accounts
		//Account bronze = new Account("Bronze", 0.002, 0.001, 800);
		Account silver = new Account("Silver", 0.003, 0.002, 5000);
		Account gold = new Account("Gold", 0.006, 0.004, 700);
		Account platinum = new Account("Platinum", 0.009, 0.005, 300);
		//Account diamond = new Account("Diamond", 0.01, 0.007, 300);
		
		//adding the accounts to a list
		List<Account> accounts = new ArrayList<Account>();
		//accounts.add(bronze);
		accounts.add(silver);
		accounts.add(gold);
		accounts.add(platinum);
		//accounts.add(diamond);
		
		
		//the initial sum of money in all accounts
		int initialSum = accounts.stream().map(Account::getSum).mapToInt(Integer::intValue).sum(); 
		
		
		//distribute the money
		accounts = redistributeMoney(accounts);
		
		//
		System.out.println("The initial sum of money is : " + initialSum + " RON\n");
		System.out.println("THE INITIAL ACCOUNTS (after distribution):");
		
		for(int i = 0; i < accounts.size(); i++)
			accounts.get(i).printData();
		
		//the current date
		Calendar date = Calendar.getInstance();
		date.set(2019, 3, 19, 11, 30);
		
		//get current month
		int month = date.get(Calendar.MONTH);
		
		//months to pass
		int months = 39;
		
		for(int i = 1; i <= months; i++) {
			//a month is passing
			date.add(Calendar.MONTH, 1);
			//check if a year has passed and apply the interest rate if so
			if(date.get(Calendar.MONTH) == month) {
				for(int j = 0; j < accounts.size(); j++)
					accounts.get(j).applyRate();
			}
		}
		
		//the final sum of money in all accounts
		int finalSum = accounts.stream().map(Account::getSum).mapToInt(Integer::intValue).sum(); 
		System.out.println("The final sum of money is : " + finalSum + " RON\n");
		
		//print the final accounts
		System.out.println("THE FINAL ACCOUNTS :");
		for(int i = 0; i < accounts.size(); i++)
			accounts.get(i).printData();
		
		}
}
