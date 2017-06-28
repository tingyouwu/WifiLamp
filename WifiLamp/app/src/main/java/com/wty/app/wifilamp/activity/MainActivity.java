package com.wty.app.wifilamp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wty.app.wifilamp.R;
import com.wty.app.wifilamp.adapter.MyPagerAdapter;
import com.wty.app.wifilamp.eventbus.WifiEvent;
import com.wty.app.wifilamp.fragment.BrightFragment;
import com.wty.app.wifilamp.fragment.ColorFragment;
import com.wty.app.wifilamp.fragment.GradientFragment;
import com.wty.app.wifilamp.widget.CustomViewpager;
import com.wty.app.wifilamp.wifi.ControlLight;
import com.wty.app.wifilamp.wifi.LightCode;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：控制主页面
 **/
public class MainActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.light_off) ImageView lightOff;
    @BindView(R.id.light_on) ImageView lightOn;
    @BindView(R.id.back_wifi_list) ImageView backWifiList;
    @BindView(R.id.indicator) MagicIndicator magicIndicator;
    @BindView(R.id.viewpager) CustomViewpager viewPager;

    private Handler handler;
    private sendThread sendThread;
    private String sendMessage = "";
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WifiEvent event){
        switch (event.getType()){
            case LightCode.Type_Bright:
                //亮度
                int bright = (int)(event.getHashMap().get(LightCode.Bright));
                sendBrightMessage(bright);
                break;
            case LightCode.Type_Color:
                //颜色
                int red = (int) event.getHashMap().get(LightCode.Color_Red);
                int green = (int) event.getHashMap().get(LightCode.Color_Green);
                int blue = (int) event.getHashMap().get(LightCode.Color_Blue);
                sendColorMessage(red,green,blue);
                break;
            case LightCode.Type_Gradien:
                //呼吸灯
                int frequency = (int) event.getHashMap().get(LightCode.Gradien_Frequency);
                sendGradientMessage(frequency);
                break;
            default:
                break;
        }
    }

    private void initView(){
        handler = new Handler();
        if(sendThread == null){
            sendThread = new sendThread();
        }
        lightOff.setOnClickListener(this);
        lightOn.setOnClickListener(this);
        backWifiList.setOnClickListener(this);

        final List<String> tabNames = new ArrayList<>();
        final List<Fragment> fragments = new ArrayList<>();

        tabNames.add(getString(R.string.tab_bright));
        tabNames.add(getString(R.string.tab_color));
        tabNames.add(getString(R.string.tab_gradient));

        fragments.add(new BrightFragment());
        fragments.add(new ColorFragment());
        fragments.add(new GradientFragment());

        /**
         * 禁止viewpager 滑动
         */
        viewPager.setPagingEnabled(false);

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabNames.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(context.getResources().getColor(R.color.tabUnSelected));
                colorTransitionPagerTitleView.setSelectedColor(context.getResources().getColor(R.color.white));
                colorTransitionPagerTitleView.setText(tabNames.get(index));
                colorTransitionPagerTitleView.setTextSize(18);
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(context.getResources().getColor(R.color.white));
                return indicator;
            }
        });

        magicIndicator.setNavigator(commonNavigator);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabNames, fragments);
        viewPager.setAdapter(pagerAdapter);

        ViewPagerHelper.bind(magicIndicator, viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if(fragments.get(position) instanceof ColorFragment){
                    lightOff.setVisibility(View.GONE);
                    lightOn.setVisibility(View.GONE);
                }else{
                    lightOff.setVisibility(View.VISIBLE);
                    lightOn.setVisibility(View.VISIBLE);
                }
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.light_off:
                /*** 灯总开关：关闭*/
                lightOff.setVisibility(View.INVISIBLE);
                lightOn.setVisibility(View.VISIBLE);
                sendSwitchMessage(LightCode.Switch_Off);
                break;
            case R.id.light_on:
                /*** 灯总开关：开启*/
                lightOff.setVisibility(View.VISIBLE);
                lightOn.setVisibility(View.INVISIBLE);
                sendSwitchMessage(LightCode.Switch_On);
                break;
            case R.id.back_wifi_list:
                /**
                 * 跳转到wifi列表
                 */
                Intent intent = new Intent(MainActivity.this, WifiConnectActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class sendThread implements Runnable {
        @Override
        public void run() {
            sendMessage(sendMessage);
        }
    }

    /**
     * 发送数据
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        Log.d("Message:",message);
        if(!ControlLight.newInstance().isConnected()){
            if(toast == null){
                toast = Toast.makeText(getApplicationContext(), getResources().getText(R.string.no_wifi_fail), Toast.LENGTH_SHORT);
            }else{
                toast.setText(getResources().getText(R.string.no_wifi_fail));
            }
            toast.show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            byte[] send = message.getBytes();
            ControlLight.newInstance().getTransceiver().send(send);
        }
    }

    /**
     * RGB协议数据
     **/
    private void sendColorMessage(int red,int green,int blue){
        JSONObject jb = new JSONObject();
        try {
            jb.put(LightCode.Type,LightCode.Type_Color);
            jb.put(LightCode.Color_Red,red);
            jb.put(LightCode.Color_Green,green);
            jb.put(LightCode.Color_Blue,blue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage = jb.toString();
        handler.removeCallbacks(sendThread);
        handler.postDelayed(sendThread, 10);
    }

    /**
     * 亮度协议数据
     **/
    private void sendBrightMessage(int bright){
        JSONObject jb = new JSONObject();
        try {
            jb.put(LightCode.Type,LightCode.Type_Bright);
            jb.put(LightCode.Bright,bright);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage = jb.toString();
        handler.removeCallbacks(sendThread);
        handler.postDelayed(sendThread, 10);
    }

    /**
     * 呼吸灯协议数据
     **/
    private void sendGradientMessage(int frequency){
        JSONObject jb = new JSONObject();
        try {
            jb.put(LightCode.Type,LightCode.Type_Gradien);
            jb.put(LightCode.Gradien_Frequency,frequency);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage = jb.toString();
        handler.removeCallbacks(sendThread);
        handler.postDelayed(sendThread, 10);
    }

    /**
     * 开关数据
     **/
    private void sendSwitchMessage(int state){
        JSONObject jb = new JSONObject();
        try {
            jb.put(LightCode.Type,LightCode.Type_Switch);
            jb.put(LightCode.Switch,state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage = jb.toString();
        handler.removeCallbacks(sendThread);
        handler.postDelayed(sendThread, 10);
    }
}
