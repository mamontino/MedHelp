package com.medhelp2.mhchat.ui.doctor;

import com.medhelp2.mhchat.data.model.CenterResponse;
import com.medhelp2.mhchat.data.model.Doctor;
import com.medhelp2.mhchat.ui.base.MvpView;

import java.util.List;

public interface DoctorsViewHelper extends MvpView
{

    void lockDrawer();

    void unlockDrawer();

    void closeNavigationDrawer();

    void showProfileActivity();

    void showSearchActivity();

    void showContactsActivity();

    void showSettingsActivity();

    void showAboutFragment();

    void showLoginActivity();

    void updateHeader(CenterResponse response);

    void updateView(List<Doctor> response);
}