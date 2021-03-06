package com.example.ble_gatt_example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ble_gatt_example.Common.FACNROLL_Dialog;
import com.example.ble_gatt_example.Common.FACNROLL_HttpSocket;
import com.example.ble_gatt_example.Common.FACNROLL_Top_Actionbar;

public class SignUp extends AppCompatActivity implements FACNROLL_Top_Actionbar.IViewListener, View.OnFocusChangeListener {
    private ActionBar actionBar;
    private FACNROLL_Top_Actionbar F_actionbar;
    private FACNROLL_HttpSocket socket;
    private EditText text_user_id, text_user_pw, text_user_pw_r, text_user_name, text_device_sn,text_device_mac;
    private Button sign_up;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        init();
    }

    private void init() {
        actionBar = getSupportActionBar();
        F_actionbar = new FACNROLL_Top_Actionbar(this,this.actionBar,R.layout.actionbar);
        F_actionbar.setIViewListener(this);
        F_actionbar.init();
        text_user_id = findViewById(R.id.text_user_id);
        text_user_pw = findViewById(R.id.text_user_pw);
        text_user_pw_r = findViewById(R.id.text_user_pw_r);
        text_user_name = findViewById(R.id.text_user_name);
        text_device_sn = findViewById(R.id.text_device_sn);
        text_device_mac = findViewById(R.id.text_device_mac);
        sign_up = findViewById(R.id.sign_up);

        text_user_id.setOnFocusChangeListener(this);
        text_user_pw.setOnFocusChangeListener(this);
        text_user_pw_r.setOnFocusChangeListener(this);
        text_user_name.setOnFocusChangeListener(this);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FACNROLL_Dialog dialog = new FACNROLL_Dialog();
                dialog.show(SignUp.this, "??? ????????? ???????????? ???????????????????", null, null, new FACNROLL_Dialog.IDialogAction() {
                    @Override
                    public void okAction(Object data) {
                        Sign_up();
                    }

                    @Override
                    public void cancelAction(Object data) {

                    }
                });
            }
        });

    }

    /* actionbar.xml */
    private ImageView left_menu,bluetooth;
    private TextView title;
    @Override
    public void onView(View view) {
        //FACNROLL_Top_Actionbar ??? ?????? ??????
        left_menu = view.findViewById(R.id.left_menu);
        title = view.findViewById(R.id.title);
        bluetooth = view.findViewById(R.id.bluetooth);
        left_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        title.setText("????????????");
        bluetooth.setVisibility(View.GONE);
    }

    /* ????????? ???????????? */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus && v == text_user_pw_r)
        {
            if(!text_user_pw.getText().toString().trim().equals(text_user_pw_r.getText().toString().trim())){
                Toast.makeText(this, "??????????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                text_user_pw_r.setText("");
                text_user_pw_r.setBackgroundResource(R.drawable.error_border);
            }else if(text_user_pw.getText().toString().equals("") || text_user_pw.getText().toString().isEmpty()){
                Toast.makeText(this, "??????????????? ???????????? ???????????????.\n???????????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                text_user_pw.setBackgroundResource(R.drawable.error_border);
            }else{
                text_user_pw_r.setBackgroundResource(R.drawable.text_border);
            }
        }
        else if(!hasFocus && v == text_user_pw)
        {
            if(text_user_pw.getText().toString().equals("") || text_user_pw.getText().toString().isEmpty()){
                Toast.makeText(this, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                text_user_pw.setBackgroundResource(R.drawable.error_border);
            }else{
                text_user_pw.setBackgroundResource(R.drawable.text_border);
            }
        }
        else if(!hasFocus && v == text_user_id)
        {
            if(text_user_id.getText().toString().equals("") || text_user_id.getText().toString().isEmpty()){
                Toast.makeText(this, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                text_user_id.setBackgroundResource(R.drawable.error_border);
            }else{
                socket = new FACNROLL_HttpSocket();
                socket.setURL("http://192.168.0.52/user/id_check");
                socket.addText("user_id",text_user_id.getText().toString().trim());
                try{
                    socket.start();
                    socket.join();
                    String result = socket.response_data;
                    if(result.equals("available")){
                        Toast.makeText(this, "??????????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                        text_user_id.setBackgroundResource(R.drawable.text_border);
                    }else{
                        Toast.makeText(this, "?????? ???????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                        text_user_id.setBackgroundResource(R.drawable.error_border);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(!hasFocus && v == text_user_name){
            if(text_user_name.getText().toString().equals("") || text_user_name.getText().toString().isEmpty()){
                Toast.makeText(this, "????????? ?????? ??????????????????.", Toast.LENGTH_SHORT).show();
                text_user_name.setBackgroundResource(R.drawable.error_border);
            }else{
                text_user_name.setBackgroundResource(R.drawable.text_border);
            }
        }
    }

    private void Sign_up(){
        socket = new FACNROLL_HttpSocket();
        socket.setURL("http://192.168.0.52/user/sign_up");
        socket.addText("user_id",text_user_id.getText().toString().trim());
        socket.addText("user_pw",text_user_pw.getText().toString().trim());
        socket.addText("name",text_user_name.getText().toString().trim());
        socket.addText("device_seri",text_device_sn.getText().toString().trim());
        socket.addText("device_mac",text_device_mac.getText().toString().trim());
        try{
            socket.start();
            socket.join();
            String result = socket.response_data;
            System.out.println(result);
            if(result.equals("true")){
                Toast.makeText(SignUp.this, "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }else{
                Toast.makeText(SignUp.this, "????????? ?????????????????????.\n ????????? ??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ?????? ???????????? ?????? ??????????????? */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
