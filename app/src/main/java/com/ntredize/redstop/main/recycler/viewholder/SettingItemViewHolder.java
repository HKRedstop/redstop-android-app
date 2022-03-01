package com.ntredize.redstop.main.recycler.viewholder;

import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;

import androidx.recyclerview.widget.RecyclerView;

public class SettingItemViewHolder extends RecyclerView.ViewHolder {

    // listener
    public MyClickListener myClickListener;

    // view
    private final TextView textView;

    public SettingItemViewHolder(View itemView, MyClickListener myClickListener) {
        super(itemView);
        this.myClickListener = myClickListener;

        // view
        RelativeLayout clickCover = itemView.findViewById(R.id.setting_item_row_click_cover);
        textView = itemView.findViewById(R.id.setting_item_row_text);

        // listener
        clickCover.setOnClickListener(myClickListener);
    }

    public void updateText(String str) {
        if (str != null) textView.setText(str);
        else textView.setText("");
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
