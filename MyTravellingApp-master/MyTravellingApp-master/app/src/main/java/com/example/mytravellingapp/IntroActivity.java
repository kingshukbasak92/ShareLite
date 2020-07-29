package com.example.mytravellingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext, btnGetStarted,saveUserData;
    ImageView avatarImage;
    //RelativeLayout linearLayoutNext, linearLayoutGetStarted;
    Dialog signupDialog;
    String userName="";
    Dialog showAvatarDialog;
    GridView showAvatarGridView;
    int[] avatarImagesInGridView = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        signupDialog = new Dialog(this);
        showAvatarDialog = new Dialog(signupDialog.getContext());

        if (restorePreData()){
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_intro);

        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        btnGetStarted.setVisibility(View.INVISIBLE);
        tabIndicator = findViewById(R.id.tab_indicator);


        //Data
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Plan Your Trip", "Choose your destination, plan your trip.\nPick the best place to your holiday", R.drawable.travel_page_one));
        mList.add(new ScreenItem("Select the Date", "Select the day, book your ticket. We give\nthe best price for you", R.drawable.travel_page_two));
        mList.add(new ScreenItem("Enjoy Your Trip", "Enjoy your holiday! Take a photo, share to\nthe world and tag me", R.drawable.travel_page_three));

        //Setup viewPager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //Setup tab indicator
        tabIndicator.setupWithViewPager(screenPager);

        //Button Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenPager.setCurrentItem(screenPager.getCurrentItem()+1, true);
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Button Get Started
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp();

            }
        });

    }

    private void showPopUp() {
        TextView closepopup;
        Button savebutton;
        EditText inputUserName;
        signupDialog.setContentView(R.layout.custom_popup);
        closepopup = (TextView) signupDialog.findViewById(R.id.close);
        savebutton = (Button) signupDialog.findViewById(R.id.savedatabutton);
        inputUserName = (EditText) signupDialog.findViewById(R.id.inputName);
        userName = inputUserName.getText().toString();
        closepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupDialog.dismiss();
            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                savePrefsData();
                finish();
            }
        });
        avatarImage = (ImageView) signupDialog.findViewById(R.id.imageView);
        avatarImage.setImageResource(R.drawable.d);
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvatarImages(avatarImage);
            }
        });
        signupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        signupDialog.show();

    }

    private void showAvatarImages(final ImageView avatarImage) {
        showAvatarDialog.setContentView(R.layout.show_avatars);
        showAvatarGridView = (GridView)showAvatarDialog.findViewById(R.id.gridview_avatar);
        CustomAdapter customAdapter = new CustomAdapter();
        showAvatarGridView.setAdapter(customAdapter);
        showAvatarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("avatarId", avatarImagesInGridView[i]);
                editor.apply();
                avatarImage.setImageResource(avatarImagesInGridView[i]);
                showAvatarDialog.dismiss();
            }
        });
        showAvatarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showAvatarDialog.show();
    }

    private boolean restorePreData(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = preferences.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.putString("userName", userName);
        editor.apply();
    }

    private void loadLastScreen(){
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return avatarImagesInGridView.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data,null);
            //getting view in row_data
            ImageView image = view1.findViewById(R.id.imageView2);
            image.setImageResource(avatarImagesInGridView[i]);
            return view1;
        }

    }
}
