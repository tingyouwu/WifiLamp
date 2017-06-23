package com.wty.app.wifilamp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.wty.app.wifilamp.R;

import java.util.List;

/**
 * 功能描述：wifi列表
 * @author wty
 */
public class WifiListAdapter extends BaseViewCommonAdapter<ScanResult> {

    public WifiListAdapter(Context context, List<ScanResult> data){
        super(context, R.layout.item_wifi_list,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ScanResult scanResult, int position) {
        TextView tv_name = holder.getView(R.id.wifi_name);
        tv_name.setText(scanResult.SSID);

        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                mContext.startActivity(intent);
            }
        });
    }
}
