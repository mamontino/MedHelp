package com.medhelp2.mhchat.ui.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.medhelp2.mhchat.R;
import com.medhelp2.mhchat.data.model.CenterResponse;
import com.medhelp2.mhchat.data.model.Doctor;
import com.medhelp2.mhchat.ui.base.BaseActivity;
import com.medhelp2.mhchat.ui.contacts.ContactsActivity;
import com.medhelp2.mhchat.ui.login.LoginActivity;
import com.medhelp2.mhchat.ui.profile.ProfileActivity;
import com.medhelp2.mhchat.ui.search.SearchActivity;
import com.medhelp2.mhchat.ui.settings.SettingsActivity;
import com.medhelp2.mhchat.utils.view.ListDivider;
import com.medhelp2.mhchat.utils.view.RecyclerViewClickListener;
import com.medhelp2.mhchat.utils.view.RecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DoctorsActivity extends BaseActivity implements DoctorsViewHelper, NavigationView.OnNavigationItemSelectedListener, Spinner.OnItemSelectedListener
{
    @Inject
    DoctorsPresenterHelper<DoctorsViewHelper> presenter;

    @BindView(R.id.toolbar_doctors)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar_doctors)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.drawer_doctors)
    DrawerLayout drawer;

    @BindView(R.id.spinner_doctors)
    Spinner spinner;

    @BindView(R.id.nav_view_doctors)
    NavigationView navView;

    @Inject
    DocListAdapter adapter;

    @Inject
    LinearLayoutManager layoutManager;

    @BindView(R.id.rv_doctors)
    RecyclerView recyclerView;

    private List<Doctor> cashList;
    private ActionBarDrawerToggle drawerToggle;

    private TextView headerTitle;
    private TextView headerDesc;
    private ImageView headerLogo;

    public static Intent getStartIntent(Context context)
    {
        return new Intent(context, DoctorsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
        setUp();
    }

    @Override
    protected void setUp()
    {
        Timber.d("setUp");
        setupToolbar();
        setupDrawer();
        cashList = new ArrayList<>();
        presenter.getCenterInfo();
        presenter.getDoctorList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void updateHeader(CenterResponse response)
    {
        View headerLayout = navView.getHeaderView(0);
        headerLogo = headerLayout.findViewById(R.id.header_logo);
        headerDesc = headerLayout.findViewById(R.id.header_tv_desc);
        headerTitle = headerLayout.findViewById(R.id.header_tv_title);

        Timber.d("updateHeader: " + response.getTitle());
        if (response.getLogo() != null)
        {
            //       headerLogo.setImageBitmap(response.getLogo());
        }
        headerTitle.setText(response.getTitle());
        headerDesc.setText(response.getInfo());
    }

    @Override
    public void updateView(List<Doctor> response)
    {
        adapter = new DocListAdapter(cashList);
        recyclerView.addItemDecoration(new ListDivider(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        List<Doctor> spinnerList = new ArrayList<>();
        spinnerList.add(0, new Doctor("Все"));
        spinnerList.addAll(response);

        DocSpinnerAdapter spinnerAdapter = new DocSpinnerAdapter(this, spinnerList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                if (position == 0)
                {
                    adapter.addItems(response);
                } else
                {
                    List<Doctor> sortList = new ArrayList<>();
                    for (Doctor doctor : response)
                    {
                        if (doctor.getIdSpec() == spinnerList.get(position).getIdSpec())
                        {
                            sortList.add(doctor);
                        }
                    }
                    adapter.addItems(sortList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this, recyclerView, new RecyclerViewClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {
//                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
//                intent.putExtra(AppConstants.ID_ROOM, cashList.get(position).getIdRoom());
//                intent.putExtra(AppConstants.ROOM_NAME, cashList.get(position).getFullName());
//                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position)
            {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                spinner.setSelection(0);
                final List<Doctor> filteredModelList = filterDoctor(cashList, newText);
                adapter.setFilter(filteredModelList);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }


    private List<Doctor> filterDoctor(List<Doctor> models, String query)
    {
        query = query.toLowerCase();
        final List<Doctor> filteredModelList = new ArrayList<>();
        for (Doctor model : models)
        {
            if (model.getFullName() != null)
            {
                final String text = model.getFullName().toLowerCase();
                if (text.contains(query))
                {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    private void setupToolbar()
    {
        Timber.d("setupToolbar");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        toolbarLayout.setTitleEnabled(false);

        AppBarLayout.LayoutParams appBarParams = (AppBarLayout.LayoutParams) toolbarLayout.getLayoutParams();
        if (actionBar != null)
        {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void lockDrawer()
    {
        Timber.d("lockDrawer");
        if (drawer != null)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void unlockDrawer()
    {
        Timber.d("unlockDrawer");
        if (drawer != null)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void closeNavigationDrawer()
    {
        Timber.d("closeNavigationDrawer");
        if (drawer != null)
        {
            drawer.closeDrawer(Gravity.START);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (drawer != null)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void setupDrawer()
    {
        Timber.d("setupDrawer");
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setupNavMenu();
    }

    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    private void setupNavMenu()
    {
        View headerLayout = navView.getHeaderView(0);
        headerLogo = headerLayout.findViewById(R.id.header_logo);
        headerTitle = headerLayout.findViewById(R.id.header_tv_title);
        headerDesc = headerLayout.findViewById(R.id.header_tv_desc);
        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void showProfileActivity()
    {
        Intent intent = ProfileActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    public void showSearchActivity()
    {
        Intent intent = SearchActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    public void showSettingsActivity()
    {
        Intent intent = SettingsActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    public void showLoginActivity()
    {
        Intent intent = LoginActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    public void showContactsActivity()
    {
        Intent intent = ContactsActivity.getStartIntent(this);
        startActivity(intent);
    }

    @Override
    public void showAboutFragment()
    {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId())
        {
            case R.id.nav_item_chat:
                showContactsActivity();
                return true;

            case R.id.nav_item_feedback:
                showAboutFragment();
                return true;

            case R.id.nav_item_logout:
                showLoginActivity();
                return true;

            case R.id.nav_item_main:
                showProfileActivity();
                return true;

            case R.id.nav_item_price:
                showSearchActivity();
                return true;

            case R.id.nav_item_record:
                showSearchActivity();
                return true;

            case R.id.nav_item_schedule:
                showSearchActivity();
                return true;

//            case R.id.nav_item_settings:
//                showSettingsActivity();
//                return true;

            case R.id.nav_item_staff:
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onDestroy()
    {
        presenter.onDetach();
        super.onDestroy();
    }
}