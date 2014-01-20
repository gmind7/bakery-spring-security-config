package com.gmind7.bakery.signup;

import com.gmind7.bakery.user.DuplicateEmailException;
import com.gmind7.bakery.user.User;

public interface SignUpService {

    public User registerNewUserAccount(SignUp userAccountData) throws DuplicateEmailException;
}
