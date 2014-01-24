package com.gmind7.bakery.signup;


import java.io.Serializable;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.gmind7.bakery.security.SocialMedia;
import com.gmind7.bakery.user.validation.PasswordsNotEmpty;
import com.gmind7.bakery.user.validation.PasswordsNotEqual;

@PasswordsNotEmpty(
        triggerFieldName = "signInProvider",
        passwordFieldName = "password",
        passwordVerificationFieldName = "passwordVerification"
)
@PasswordsNotEqual(
        passwordFieldName = "password",
        passwordVerificationFieldName = "passwordVerification"
)
public class SignUp implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIELD_NAME_EMAIL = "email";

    @Email
    @NotEmpty
    @Size(max = 100)
    private String email;

    @NotEmpty
    @Size(max = 100)
    private String firstName;

    @NotEmpty
    @Size(max = 100)
    private String lastName;

    private String password;

    private String passwordVerification;

    private SocialMedia signInProvider;

    public SignUp() {

    }

    public boolean isNormalRegistration() {
        return signInProvider == null;
    }

    public boolean isSocialSignIn() {
        return signInProvider != null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerification() {
        return passwordVerification;
    }

    public void setPasswordVerification(String passwordVerification) {
        this.passwordVerification = passwordVerification;
    }

    public SocialMedia getSignInProvider() {
        return signInProvider;
    }

    public void setSignInProvider(SocialMedia signInProvider) {
        this.signInProvider = signInProvider;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("email", email)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("signInProvider", signInProvider)
                .toString();
    }
}
