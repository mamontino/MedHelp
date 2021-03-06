package com.medhelp2.mhchat.ui.profile;


import com.medhelp2.mhchat.data.model.CenterResponse;
import com.medhelp2.mhchat.data.model.VisitResponse;
import com.medhelp2.mhchat.ui.base.MvpView;

import java.util.List;

public interface ProfileViewHelper extends MvpView
{
    void lockDrawer();

    void unlockDrawer();

    void closeNavigationDrawer();

    void showErrorScreen();

    void swipeDismiss();

    void onRefresh();

    void showContactsActivity();

    void showAnaliseActivity();

    void showSaleActivity();

    void showDoctorsActivity();

    void showSearchActivity();

    void showRateFragment();

    void updateHeader(CenterResponse response);

    void updateData(List<VisitResponse> response, String today);

    void runSendRegistrationService(String fbToken, int idUser);
}
