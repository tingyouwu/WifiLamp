package com.wty.app.wifilamp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.wty.app.wifilamp.R;
import com.wty.app.wifilamp.eventbus.WifiEvent;
import com.wty.app.wifilamp.wifi.LightCode;

import org.greenrobot.eventbus.EventBus;

/**
 * 描述：亮度控制
 */
public class BrightFragment extends Fragment {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_always_bright, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seekBar_bright);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int progress = seekBar.getProgress();
                int state;
                if (progress < 25) {
                    seekBar.setProgress(0);
                    state = LightCode.Bright_Dark;
                } else if (progress < 75) {
                    seekBar.setProgress(50);
                    state = LightCode.Bright_Normal;
                } else {
                    seekBar.setProgress(100);
                    state = LightCode.Bright_Bright;
                }
                WifiEvent event = new WifiEvent(LightCode.Type_Bright);
                event.appendHashParam(LightCode.Bright,state);
                EventBus.getDefault().post(event);
            }
        });
    }
}
