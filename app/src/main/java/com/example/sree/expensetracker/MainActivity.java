package com.example.sree.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ManageExpense {

    String months[] = {"January","February","March","April","May","June","July","August",
            "September","October","November","December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragments = getSupportFragmentManager();
        fragments.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction fragmentTransaction = fragments.beginTransaction();
        AddExpense addExpense = new AddExpense();
        fragmentTransaction.replace(R.id.fragment_container,addExpense);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add)
        {
            FragmentManager fragments = getSupportFragmentManager();
            fragments.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            AddExpense addExpense = new AddExpense();
            fragmentTransaction.replace(R.id.fragment_container,addExpense);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_view)
        {
            FragmentManager fragments = getSupportFragmentManager();
            fragments.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            ExpensesList expensesList = new ExpensesList();
            fragmentTransaction.replace(R.id.fragment_container,expensesList);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_account)
        {
            FragmentManager fragments = getSupportFragmentManager();
            fragments.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            MyAccountFragment myAccountFragment = new MyAccountFragment();
            fragmentTransaction.replace(R.id.fragment_container,myAccountFragment);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_logout)
        {
            // Create object of SharedPreferences.
            SharedPreferences sharedPref = getSharedPreferences("emailPref",0);
            //now get Editor
            SharedPreferences.Editor editor = sharedPref.edit();
            //put your value
            editor.putString("enteredEmailId", "");
            editor.putString("firstName", "");
            editor.putString("lastName", "");
            editor.commit();

            finish();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void manageExpenseListner(int position, ArrayList<String> itemList,ArrayList<String> itemInfo)
    {
        Database database = new Database(getApplicationContext());
        SharedPreferences sharedPref = getSharedPreferences("emailPref", 0);
        String savedEmailId = sharedPref.getString("enteredEmailId", "");
        if(itemInfo.get(position).equals("year"))
        {

        }
        else if(itemInfo.get(position).equals("month"))
        {

        }
        else if(itemInfo.get(position).equals("item"))
        {
            String data = itemList.get(position);
            FragmentManager fragments = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragments.beginTransaction();
            ManageExpenseFragment manageExpenseFragment = new ManageExpenseFragment();
            manageExpenseFragment.setItemData(data);
            fragmentTransaction.replace(R.id.fragment_container,manageExpenseFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
