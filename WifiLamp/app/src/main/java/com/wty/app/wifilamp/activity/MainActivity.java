package com.wty.app.wifilamp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.wty.app.wifilamp.R;
import com.wty.app.wifilamp.adapter.MyPagerAdapter;
import com.wty.app.wifilamp.fragment.BrightFragment;
import com.wty.app.wifilamp.widget.CustomViewpager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.SimpleViewPagerDelegate;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

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
    @BindView(R.id.main_setting) ImageView setImg;
    @BindView(R.id.back_wifi_list) ImageView backWifiList;
    @BindView(R.id.indicator) MagicIndicator magicIndicator;
    @BindView(R.id.viewpager) CustomViewpager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        lightOff.setOnClickListener(this);
        lightOn.setOnClickListener(this);
        setImg.setOnClickListener(this);
        backWifiList.setOnClickListener(this);

        final List<String> tabNames = new ArrayList<>();
        final List<Fragment> fragments = new ArrayList<>();

        tabNames.add(getString(R.string.tab_bright));
        tabNames.add(getString(R.string.tab_color));
        tabNames.add(getString(R.string.tab_gradient));

        fragments.add(new BrightFragment());
        fragments.add(new BrightFragment());
        fragments.add(new BrightFragment());

        /**
         * 禁止viewpager 滑动
         */
        viewPager.setPagingEnabled(false);

        CommonNavigator commonNavigator = new CommonNavigator(this);
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
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(context.getResources().getDimension(R.dimen.indicator_line_width));
                indicator.setLineHeight(context.getResources().getDimension(R.dimen.indicator_line_height));
                indicator.setColors(context.getResources().getColor(R.color.white));
                return indicator;
            }
        });
        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        SimpleViewPagerDelegate.with(magicIndicator, viewPager).delegate();

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), tabNames, fragments);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                break;
            case R.id.light_on:
                /*** 灯总开关：开启*/
                lightOff.setVisibility(View.VISIBLE);
                lightOn.setVisibility(View.INVISIBLE);
                /**
                 * 先开启灯，再获取当前的模式
                 */
                lightOff.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 1000);

                break;
            case R.id.main_setting: {
                /**
                 * 跳转至设置界面
                 */
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.back_wifi_list:
                /**
                 * 跳转到wifi列表
                 */
                finish();
                break;
        }
    }
}
