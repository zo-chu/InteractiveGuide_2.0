package com.kitanasoftware.interactiveguide.information;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kitanasoftware.interactiveguide.DrawerAppCompatActivity;
import com.kitanasoftware.interactiveguide.R;
import com.kitanasoftware.interactiveguide.db.WorkWithDb;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InformatoonScreen_9 extends DrawerAppCompatActivity {
    private View edit;
    private View save;
    private String guideName;
    private String guidePhone;
    private String tour;
    private String goal;
    private String company;
    private EditText etGuideName;
    private EditText etGuidePhone;
    private EditText etTour;
    private EditText etGoal;
    private TextView tvCompany;
    private boolean isEdit;
    //    String inf_id;
//    InformationAdapter adapter;
//    ListView listView;
    ArrayList<String> informList;

    @Override
    protected void onStart() {
        super.onStart();
        if(WorkWithDb.getWorkWithDb().getInformList() != null) {
            informList = WorkWithDb.getWorkWithDb().getInformList();
            etGuideName.setText(informList.get(0));
            etGuidePhone.setText(informList.get(1));
            etTour.setText(informList.get(2));
            etGoal.setText(informList.get(3));
            tvCompany.setText(informList.get(4));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#fdc68a"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        informList = WorkWithDb.getWorkWithDb().getInformList();
        etGuideName = (EditText) findViewById(R.id.etFullName);
        etGuidePhone = (EditText) findViewById(R.id.etPhone);
        etTour = (EditText) findViewById(R.id.etTour);
        etGoal = (EditText) findViewById(R.id.etTourGoal);
        tvCompany =(TextView) findViewById(R.id.tvCompanyName1);

        if(WorkWithDb.getWorkWithDb().getInformList().size() != 0) {
            informList = WorkWithDb.getWorkWithDb().getInformList();
            etGuideName.setText(informList.get(0));
            etGuidePhone.setText(informList.get(1));
            etTour.setText(informList.get(2));
            etGoal.setText(informList.get(3));
            tvCompany.setText(informList.get(4));
        }
//
//
//        adapter = new InformationAdapter(getApplicationContext(), informList);
//        listView = (ListView) findViewById(R.id.lvInform);
//        listView.setAdapter(adapter);

    }

    @Override
    public View getContentView() {
        return getLayoutInflater().inflate(R.layout.informatoon_screen_9, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_for_inf, menu);
        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu != null) {
            menu.clear();
        }
        if(isEdit==false){
            menu.setGroupVisible(R.menu.main_for_inf, false);
            getMenuInflater().inflate(R.menu.main_for_inf, menu);

        }
        else if(isEdit==true) {
            menu.setGroupVisible(R.menu.edit_for_inform, false);
            getMenuInflater().inflate(R.menu.edit_for_inform, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        edit = findViewById(R.id.item1);
        save = findViewById(R.id.item2);
        switch (item.getItemId()) {
            case R.id.item1:
                isEdit=true;
                invalidateOptionsMenu();
                etGuideName.setEnabled(true);
                etGuidePhone.setEnabled(true);
                etTour.setEnabled(true);
                etGoal.setEnabled(true);
                getContentView().invalidate();
                break;
            case R.id.item2 :
                isEdit=false;
                invalidateOptionsMenu();
                etGuideName.setEnabled(false);
                etGuidePhone.setEnabled(false);
                etTour.setEnabled(false);
                etGoal.setEnabled(false);
                guideName = etGuideName.getText().toString();
                guidePhone = etGuidePhone.getText().toString();
                tour = etTour.getText().toString();
                goal = etGoal.getText().toString();
                saveToPhone();
                getContentView().invalidate();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void saveToPhone() {
        WorkWithDb.getWorkWithDb().updateInformationByIndex(guideName, guidePhone, tour, goal);
    }

}




