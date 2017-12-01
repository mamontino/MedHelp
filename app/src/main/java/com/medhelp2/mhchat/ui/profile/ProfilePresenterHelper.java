package com.medhelp2.mhchat.ui.profile;


import com.medhelp2.mhchat.data.model.CenterResponse;
import com.medhelp2.mhchat.di.scope.PerActivity;
import com.medhelp2.mhchat.ui.base.MvpPresenter;

@PerActivity
public interface ProfilePresenterHelper<V extends ProfileViewHelper> extends MvpPresenter<V>
{
    void updateHeaderInfo();

    void getVisits();

    void updateToken();

    CenterResponse getCenterInfo();
}
