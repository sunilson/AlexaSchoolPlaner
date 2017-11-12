package com.sunilson.bachelorthesis.presentation.baseClasses;

import android.arch.lifecycle.ViewModel;

/**
 * Created by linus_000 on 12.11.2017.
 */

public interface HasViewModel {
    <T extends ViewModel> T getViewModel();
}
