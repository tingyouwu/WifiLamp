package com.wty.app.wifilamp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.wty.app.wifilamp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：设置wifi ip地址以及端口
 */
public class SettingActivity extends BaseActivity{

    @BindView(R.id.setting_back) ImageView back;
    @BindView(R.id.change_ip) EditText change_ip;
    @BindView(R.id.change_port) EditText change_port;
    @BindView(R.id.restart) Button restart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 跳转到wifi 列表
                 */
                Intent intent = new Intent(SettingActivity.this, WifiConnectActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
