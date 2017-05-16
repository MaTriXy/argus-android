package com.moldedbits.argus.provider.login;

import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.moldedbits.argus.R;
import com.moldedbits.argus.logger.ArgusLogger;
import com.moldedbits.argus.provider.BaseProvider;
import com.moldedbits.argus.validations.RegexValidation;
import com.moldedbits.argus.validations.ValidationEngine;

/**
 * Allow user to login with email and password
 */
public abstract class EmailLoginProvider extends BaseProvider {

    private static final String TAG = "EmailLoginProvider";

    private EditText usernameInput;
    private EditText passwordInput;

    abstract public void doServerLogin(String username, String password);

    public EmailLoginProvider() {
        validationEngine = new ValidationEngine();
    }

    @Nullable
    @Override
    public View inflateLoginView(ViewGroup parentView) {
        if(context == null)
            return null;

        getValidationEngine().addEmailValidation(new RegexValidation(Patterns.EMAIL_ADDRESS.pattern(),
                context.getString(R.string.invalid_email)));

        if (context != null) {
            View loginView = LayoutInflater.from(context)
                    .inflate(R.layout.login_email, parentView, false);

            usernameInput = (EditText) loginView.findViewById(R.id.username);
            passwordInput = (EditText) loginView.findViewById(R.id.password);
            return loginView;
        } else {
            throw new RuntimeException("Context cannot be null");
        }
    }

    @Override
    public void performLogin() {
        if (validateInput() && resultListener != null) {
            doServerLogin(usernameInput.getText().toString(), passwordInput.getText().toString());
        }
    }

    private boolean validateInput() {
        if(validationEngine == null) {
            ArgusLogger.w(TAG, "ValidationEngine is null not validating SignUp form");
            return true;
        }

        // we want to run all validations
        boolean result1 = ValidationEngine.validateEditText(usernameInput, validationEngine);
        boolean result2 = ValidationEngine.validateEditText(passwordInput, validationEngine);

        return result1 && result2;
    }

    @Override
    public int getContainerId() {
        return R.id.container_email;
    }
}
