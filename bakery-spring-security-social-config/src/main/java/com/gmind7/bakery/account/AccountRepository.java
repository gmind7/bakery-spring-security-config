package com.gmind7.bakery.account;

public interface AccountRepository {
	
	void createAccount(Account account) throws UsernameAlreadyInUseException;

	Account findAccountByUsername(String username);
	
}
