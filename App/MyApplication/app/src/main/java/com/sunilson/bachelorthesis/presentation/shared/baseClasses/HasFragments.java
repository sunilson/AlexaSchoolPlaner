package com.sunilson.bachelorthesis.presentation.shared.baseClasses;

import android.support.v4.app.Fragment;

/**
 * @author Linus Weiss
 */

public interface HasFragments {

     void changeFragment(Fragment fragment, String tag);
     void addFragment(Fragment fragment, String tag);

}
