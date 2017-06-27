package com.wty.app.wifilamp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wty.app.wifilamp.R;
import com.wty.app.wifilamp.util.PreferenceUtil;

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
    @BindView(R.id.tv_about)TextView tv_about;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //只能输入数字和小数点
        change_ip.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        change_port.setInputType(InputType.TYPE_CLASS_NUMBER);

        change_ip.setText(PreferenceUtil.getInstance().getIP());
        change_port.setText(PreferenceUtil.getInstance().getPort()+"");

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
                String result = saveCode();
                if(!TextUtils.isEmpty(result)){
                    Toast.makeText(SettingActivity.this, result, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingActivity.this, WifiConnectActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * @Decription 保存编码
     **/
    private void saveToPreference(String ip, String port){
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IP,ip);
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.PORT,Integer.valueOf(port));
    }

    /**
     * @Decription 判断一下编码是否符合规则
     **/
    private String saveCode(){
        //判断是否存在空值
        String ip = change_ip.getText().toString();
        String port = change_port.getText().toString();

        if(TextUtils.isEmpty(ip)){
            return "IP不能为空";
        }

        if(TextUtils.isEmpty(port)){
            return "端口不能为空";
        }

        saveToPreference(ip,port);
        return "";
    }

}
