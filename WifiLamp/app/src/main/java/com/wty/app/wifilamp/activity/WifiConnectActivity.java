package com.wty.app.wifilamp.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.wty.app.wifilamp.R;
import com.wty.app.wifilamp.adapter.WifiListAdapter;
import com.wty.app.wifilamp.util.GpsUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

import static android.os.Build.VERSION_CODES.M;

/**
 * 描述：Wifi连接页面
 */
public class WifiConnectActivity extends BaseActivity {
    public static final String TAG = "WifiConnectActivity";

    @BindView(R.id.wifi_connect_icon) ImageView wifiConnectIcon;
    @BindView(R.id.wifi_state_ll) LinearLayout wifiStateLL;
    @BindView(R.id.wifi_name) TextView wifiName;
    @BindView(R.id.button_enter) Button enter;
    @BindView(R.id.wifi_list) ListView listView;

    private WifiListAdapter adapter;
    private List<ScanResult> list = new ArrayList<>();
    WifiManager wifiManager;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> result = wifiManager.getScanResults();
                if(result == null || result.size()==0){
                    if (Build.VERSION.SDK_INT >= M) {
                        Toast.makeText(WifiConnectActivity.this, "未搜索到wifi信号！请确定是否打开GPS", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WifiConnectActivity.this, "未搜索到wifi信号！", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Map<String,ScanResult> wifimap = new LinkedHashMap<>();
                    for(ScanResult wifi:result){
                        if(wifimap.containsKey(wifi.SSID))continue;
                        wifimap.put(wifi.SSID,wifi);
                    }
                    List<ScanResult> list = new ArrayList<>();
                    list.addAll(wifimap.values());
                    adapter.refreshList(list);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_state);
        ButterKnife.bind(this);
        initView();

        if (Build.VERSION.SDK_INT >= M) {
            RxPermissions.getInstance(WifiConnectActivity.this)
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                initWifi();
                            }
                        }
                    });
        } else {
            initWifi();
        }

    }

    private void initWifi() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "wifi未打开！正在打开...", Toast.LENGTH_SHORT).show();
            wifiManager.setWifiEnabled(true);
        }

        adapter = new WifiListAdapter(this,list);
        listView.setAdapter(adapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(receiver, filter);
    }

    private void initView() {
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WifiConnectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiManager.startScan();
        /**
         * 在onPause()中判断wifi连接状态
         *暂时不做wifi状态监听（wifi确认链接后返回页面，或者重启app，才会生效）
         */
    }

    /**
     * 连接成功  获取当前连接的wifi
     */
    public void connectY() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        wifiConnectIcon.setImageResource(R.mipmap.wifi_connect_y);
        wifiStateLL.setVisibility(View.INVISIBLE);
        wifiName.setVisibility(View.VISIBLE);
        enter.setVisibility(View.VISIBLE);
        wifiName.setText(wifiInfo.getSSID());
    }

    /**
     *连接失败
     */
    public void connectN() {
        wifiConnectIcon.setImageResource(R.mipmap.wifi_connect_n);
        wifiStateLL.setVisibility(View.VISIBLE);
        wifiName.setVisibility(View.INVISIBLE);
        enter.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            unregisterReceiver(receiver);
        }
    }
}
