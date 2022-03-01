package com.ntredize.redstop.main.recycler.viewholder;

import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;

import androidx.recyclerview.widget.RecyclerView;

public class SettingViewHolder extends RecyclerView.ViewHolder {

    // listener
    public MyClickListener myClickListener;

    // view
    private final TextView nameText;
    private final TextView valueText;

    public SettingViewHolder(View itemView, MyClickListener myClickListener) {
        super(itemView);
        this.myClickListener = myClickListener;

        // view
        RelativeLayout clickCover = itemView.findViewById(R.id.setting_row_click_cover);
        nameText = itemView.findViewById(R.id.setting_row_name);
        valueText = itemView.findViewById(R.id.setting_row_value);

        // listener
        clickCover.setOnClickListener(myClickListener);
    }

    public void updateText(Integer labelId, String valueStr) {
        if (labelId != null) nameText.setText(labelId);
        else nameText.setText("");
        
        if (valueStr != null) valueText.setText(valueStr);
        else valueText.setText("");
    }


    /* Click Listener */
    public static class MyClickListener implements View.OnClickListener {

        private Runnable action;

        public void updateAction(Runnable action) {
            this.action = action;
        }

        @Override
        public void onClick(View v) {
            new Handler().post(action);
        }

    }

}
