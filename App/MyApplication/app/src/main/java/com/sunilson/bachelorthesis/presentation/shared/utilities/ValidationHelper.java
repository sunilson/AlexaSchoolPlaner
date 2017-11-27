package com.sunilson.bachelorthesis.presentation.shared.utilities;

import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Linus Weiss
 */

@Singleton
public class ValidationHelper {

    @Inject
    public ValidationHelper() {

    }

    public boolean validateString(boolean canBeEmpty, @Nullable String string, @Nullable Integer min, @Nullable Integer max) {

        if(string == null) return false;

        if(string.isEmpty()) return canBeEmpty;

        if(min != null) {
            if(string.length() < min) return false;
        }

        if(max != null) {
            if(string.length() > max) return false;
        }

        return true;
    }

    public boolean validateFromToDates(@Nullable DateTime from, @Nullable DateTime to) {
        if(from == null || to == null) return false;

        if(to.isBefore(from) || to.isEqual(from)) return false;

        return true;
    }

}
