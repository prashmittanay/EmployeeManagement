package org.learn.employeemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;

public class ListSettingsActivity extends AppCompatActivity {
    private static final String TAG = "ListSettingsActivity";
    private RadioGroup mOrientationRadioGroup;
    private RadioGroup mDividerColorRadioGroup;
    private RadioGroup mDividerHeightRadioGroup;
    private RadioGroup mBackgroundRadioGroup;
    private RadioGroup mListTypeRadioGroup;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mPreferencesEditor;
    private Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_settings);
        mOrientationRadioGroup = findViewById(R.id.rg_text_orientation);
        mDividerColorRadioGroup = findViewById(R.id.rg_divider_color);
        mDividerHeightRadioGroup = findViewById(R.id.rg_divider_height);
        mBackgroundRadioGroup = findViewById(R.id.rg_background_color);
        mListTypeRadioGroup = findViewById(R.id.rg_list_type);
        mBackButton = findViewById(R.id.button_pref_back);
        initSettings();
        onChangeListeners();
        onClickListeners();
    }

    private void onClickListeners() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void onChangeListeners() {
        mOrientationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int direction = 3;
                switch (checkedId) {
                    case R.id.rb_ori_left:
                        direction = View.TEXT_DIRECTION_LTR;
                        break;
                    case R.id.rb_ori_right:
                        direction = View.TEXT_DIRECTION_RTL;
                        break;
                }
                mPreferencesEditor.putInt(getString(R.string.list_text_orientation), direction);
                mPreferencesEditor.apply();
            }
        });
        mDividerColorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String color = "#000000";
                switch (checkedId) {
                    case R.id.rb_black:
                        color = "#000000";
                        break;
                    case R.id.rb_blue:
                        color = "#0026FA";
                        break;
                    case R.id.rb_red:
                        color = "#FB0404";
                        break;
                    case R.id.rb_green:
                        color = "#30FF02";
                        break;
                }
                mPreferencesEditor.putString(getString(R.string.list_divider_color), color);
                mPreferencesEditor.apply();
            }
        });
        mDividerHeightRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int height = 1;
                switch (checkedId) {
                    case R.id.rb_height_one:
                        height = 1;
                        break;
                    case R.id.rb_height_three:
                        height = 3;
                        break;
                    case R.id.rb_height_five:
                        height = 5;
                        break;
                }
                mPreferencesEditor.putInt(getString(R.string.list_divider_height), height);
                mPreferencesEditor.apply();
            }
        });
        mBackgroundRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String color = "#eeeeee";
                switch (checkedId) {
                    case R.id.rb_light:
                        color = "#eeeeee";
                        break;
                    case R.id.rb_dark:
                        color = "#888888";
                        break;
                }
                mPreferencesEditor.putString(getString(R.string.list_background_color), color);
                mPreferencesEditor.apply();
            }
        });
        mListTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int listType = 0;
                switch (checkedId) {
                    case R.id.rb_ltype_condensed:
                        listType = 0;
                        break;
                    case R.id.rb_ltype_expanded:
                        listType = 1;
                        break;
                }
                mPreferencesEditor.putInt(getString(R.string.list_view_type), listType);
                mPreferencesEditor.apply();
            }
        });
    }

    private void initSettings() {
        mSharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int textDirection = mSharedPreferences.getInt(getString(R.string.list_text_orientation), 3);
        String dividerColor = mSharedPreferences.getString(getString(R.string.list_divider_color), "#000000");
        int dividerHeight = mSharedPreferences.getInt(getString(R.string.list_divider_height), 1);
        String bgColor = mSharedPreferences.getString(getString(R.string.list_background_color), "#eeeeee");
        int listType = mSharedPreferences.getInt(getString(R.string.list_view_type), 0);
        mPreferencesEditor = mSharedPreferences.edit();

        if (textDirection == View.TEXT_DIRECTION_LTR)
            mOrientationRadioGroup.check(R.id.rb_ori_left);
        else
            mOrientationRadioGroup.check(R.id.rb_ori_right);

        switch (dividerColor) {
            case "#000000":
                mDividerColorRadioGroup.check(R.id.rb_black);
                break;
            case "#0026FA":
                mDividerColorRadioGroup.check(R.id.rb_blue);
                break;
            case "#FB0404":
                mDividerColorRadioGroup.check(R.id.rb_red);
                break;
            case "#30FF02":
                mDividerColorRadioGroup.check(R.id.rb_green);
                break;
        }

        switch (dividerHeight) {
            case 1:
                mDividerHeightRadioGroup.check(R.id.rb_height_one);
                break;
            case 3:
                mDividerHeightRadioGroup.check(R.id.rb_height_three);
                break;
            case 5:
                mDividerHeightRadioGroup.check(R.id.rb_height_five);
                break;
        }

        if (bgColor.equals("#eeeeee"))
            mBackgroundRadioGroup.check(R.id.rb_light);
        else
            mBackgroundRadioGroup.check(R.id.rb_dark);

        if (listType == 0)
            mListTypeRadioGroup.check(R.id.rb_ltype_condensed);
        else
            mListTypeRadioGroup.check(R.id.rb_ltype_expanded);
    }
}