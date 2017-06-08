package com.iotaddon.goout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMemo extends AppCompatActivity implements View.OnClickListener {

    private TextView txtLength;
    private EditText editContent;
    private RelativeLayout btnSet;
    private String strContent;
    private final int MAX_CONTENT_LENGTH = 50;
    private DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        getSupportActionBar().setElevation(0);

        txtLength = (TextView) findViewById(R.id.activity_memo_txt_length);
        editContent = (EditText) findViewById(R.id.activity_memo_edit_content);
        btnSet = (RelativeLayout) findViewById(R.id.activity_memo_btn_set);
        btnSet.setOnClickListener(this);

        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtLength.setText(s.length() + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editContent.setText(dataManager.getSavedMemo());
        editContent.setSelection(editContent.getText().length());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_memo_btn_set:
                if (editContent.getText().toString().length() < MAX_CONTENT_LENGTH) {
                    strContent = editContent.getText().toString();
                    dataManager.setSavedMemo(strContent);
                    if(strContent.equals("")){
                        Toast.makeText(this, "메모 정보를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "메모 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else {
                    Toast.makeText(this, "등록할 수 있는 문자를 초과했습니다. 내용을 수정해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
