package com.wty.app.wifilamp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcolorpicker.android.ColorPicker;
import com.wty.app.wifilamp.R;
import com.wty.app.wifilamp.eventbus.WifiEvent;
import com.wty.app.wifilamp.wifi.LightCode;

import org.greenrobot.eventbus.EventBus;

/**
 * 描述：颜色
 */
public class ColorFragment extends Fragment implements ColorPicker.OnColorSelectListener {
    private View rootView;
    ColorPicker colorPicker;
    TextView tv_color;
    TextView tv_data;
    View view_color;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_color, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        colorPicker = (ColorPicker) rootView.findViewById(R.id.color_picker);
        colorPicker.setOnColorSelectListener(this);
        view_color = rootView.findViewById(R.id.color_view);
        tv_color = (TextView) rootView.findViewById(R.id.color_tv);
        tv_data = (TextView) rootView.findViewById(R.id.tv_send);
    }

    @Override
    public void onColorSelect(int color) {
        int red, green, blue;
        red= Color.red(color);
        green=Color.green(color);
        blue=Color.blue(color);
        view_color.setBackgroundColor(color);
        tv_color.setText("R:"+red+",G:"+green+",B:"+blue);
        WifiEvent event = new WifiEvent(LightCode.Type_Color);
        event.appendHashParam(LightCode.Color_Red,red);
        event.appendHashParam(LightCode.Color_Green,green);
        event.appendHashParam(LightCode.Color_Blue,blue);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!colorPicker.isRecycled()) {
            colorPicker.recycle();
        }
    }
}
