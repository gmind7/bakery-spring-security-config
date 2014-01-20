package com.gmind7.bakery.signup;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.gmind7.bakery.security.SecurityUtils;
import com.gmind7.bakery.user.DuplicateEmailException;
import com.gmind7.bakery.user.User;
import com.gmind7.bakery.security.SocialMedia;

@Controller
public class SignUpController {

    private static final Logger log = LoggerFactory.getLogger(SignUpController.class);

    private SignUpService service;

    @Autowired
    public SignUpController(SignUpService service) {
        this.service = service;
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String showSigninPage(WebRequest request, Model model) {
    	log.debug("Rendering signup page.");
    	
        Connection<?> connection = ProviderSignInUtils.getConnection(request);
    	
        if (connection == null) return "/signup/signup";
        
        User registered = createRegistrationSignUp(connection);
        
         //If email address was already found from the database, render the form view.
        if (registered == null) {
        	log.debug("An email address was found from the database. Rendering form view.");
            return "/signup";
        }

        log.debug("Registered user account with information: {}", registered);

        //Logs the user in.
        SecurityUtils.logInUser(registered);
        log.debug("User {} has been signed in");
        //If the user is signing in by using a social provider, this method call stores
        //the connection to the UserConnection table. Otherwise, this method does not
        //do anything.
        ProviderSignInUtils.handlePostSignUp(registered.getEmail(), request);
        
    	return "redirect:/";
    }
    
    @RequestMapping(value ="/signup", method = RequestMethod.POST)
    public String registerUserAccount(@Valid @ModelAttribute("user") SignUp singUp,
                                      BindingResult result,
                                      WebRequest request) throws DuplicateEmailException {
        log.debug("Registering user account with information: {}", singUp);
        if (result.hasErrors()) {
        	log.debug("Validation errors found. Rendering form view.");
            return "/signup";
        }

        log.debug("No validation errors found. Continuing registration process.");

        User registered = createUserAccount(singUp, result);

        //If email address was already found from the database, render the form view.
        if (registered == null) {
        	log.debug("An email address was found from the database. Rendering form view.");
            return "/signup";
        }

        log.debug("Registered user account with information: {}", registered);

        //Logs the user in.
        SecurityUtils.logInUser(registered);
        log.debug("User {} has been signed in");
        //If the user is signing in by using a social provider, this method call stores
        //the connection to the UserConnection table. Otherwise, this method does not
        //do anything.
        ProviderSignInUtils.handlePostSignUp(registered.getEmail(), request);

        return "redirect:/";
    }
    
    private User createRegistrationSignUp(Connection<?> connection) {
    	
    	SignUp signUp = new SignUp();

        if (connection != null) {
            UserProfile socialMediaProfile = connection.fetchUserProfile();
            signUp.setEmail(socialMediaProfile.getEmail());
            signUp.setFirstName(socialMediaProfile.getFirstName());
            signUp.setLastName(socialMediaProfile.getLastName());

            ConnectionKey providerKey = connection.getKey();
            signUp.setSignInProvider(SocialMedia.valueOf(providerKey.getProviderId().toUpperCase()));
        }
        User registered = null;
        try {
            registered = service.registerNewUserAccount(signUp);
        }
        catch (DuplicateEmailException ex) {
        	log.debug("An email address: {} exists.", signUp.getEmail());
        }

        return registered;
    }
    
    private User createUserAccount(SignUp signUp, BindingResult result) {
        log.debug("Creating user account with information: {}", signUp);
        User registered = null;

        try {
            registered = service.registerNewUserAccount(signUp);
        }
        catch (DuplicateEmailException ex) {
        	log.debug("An email address: {} exists.", signUp.getEmail());
            addFieldError(
            		"user",
                    SignUp.FIELD_NAME_EMAIL,
                    signUp.getEmail(),
                    "NotExist.user.email",
                    result);
        }

        return registered;
    }
    
    private void addFieldError(String objectName, String fieldName, String fieldValue,  String errorCode, BindingResult result) {
        log.debug(
                "Adding field error object's: {} field: {} with error code: {}",
                objectName,
                fieldName,
                errorCode
        );
        FieldError error = new FieldError(
                objectName,
                fieldName,
                fieldValue,
                false,
                new String[]{errorCode},
                new Object[]{},
                errorCode
        );

        result.addError(error);
        log.debug("Added field error: {} to binding result: {}", error, result);
    }
}
