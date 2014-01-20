package com.gmind7.bakery.signup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gmind7.bakery.user.DuplicateEmailException;
import com.gmind7.bakery.user.User;
import com.gmind7.bakery.user.UserRepository;

@Service
public class SignUpServiceImpl implements SignUpService {

    private static final Logger log = LoggerFactory.getLogger(SignUpServiceImpl.class);

    private PasswordEncoder passwordEncoder;

    private UserRepository repository;

    @Autowired
    public SignUpServiceImpl(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    @Transactional
    @Override
    public User registerNewUserAccount(SignUp signUp) throws DuplicateEmailException {
    	
    	log.debug("Registering new user account with information: {}", signUp);

        if (emailExist(signUp.getEmail())) {
        	log.debug("Email: {} exists. Throwing exception.", signUp.getEmail());
            throw new DuplicateEmailException("The email address: " + signUp.getEmail() + " is already in use.");
        }

        log.debug("Email: {} does not exist. Continuing registration.", signUp.getEmail());

        String encodedPassword = encodePassword(signUp);

        User.Builder user = User.getBuilder()
                .email(signUp.getEmail())
                .firstName(signUp.getFirstName())
                .lastName(signUp.getLastName())
                .password(encodedPassword);

        if (signUp.isSocialSignIn()) {
            user.signInProvider(signUp.getSignInProvider());
        }

        User registered = user.build();

        log.debug("Persisting new user with information: {}", registered);

        return repository.save(registered);
    }

    private boolean emailExist(String email) {
    	log.debug("Checking if email {} is already found from the database.", email);

        User user = repository.findByEmail(email);

        if (user != null) {
            log.debug("User account: {} found with email: {}. Returning true.", user, email);
            return true;
        }

        log.debug("No user account found with email: {}. Returning false.", email);

        return false;
    }

    private String encodePassword(SignUp signUp) {
        String encodedPassword = null;

        if (signUp.isNormalRegistration()) {
        	log.debug("Registration is normal registration. Encoding password.");
            encodedPassword = passwordEncoder.encode(signUp.getPassword());
        }

        return encodedPassword;
    }
    
}
