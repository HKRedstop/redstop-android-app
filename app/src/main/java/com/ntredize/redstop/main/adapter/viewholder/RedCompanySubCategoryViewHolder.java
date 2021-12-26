package com.ntredize.redstop.main.adapter.viewholder;

import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;

import androidx.recyclerview.widget.RecyclerView;

public class RedCompanySubCategoryViewHolder extends RecyclerView.ViewHolder {

    // listener
    public MyClickListener myClickListener;

    // view
    private final TextView subCategoryNameText;

    public RedCompanySubCategoryViewHolder(View itemView, MyClickListener myClickListener) {
        super(itemView);
        this.myClickListener = myClickListener;

        // view
        RelativeLayout clickCover = itemView.findViewById(R.id.red_company_sub_category_row_click_cover);
        subCategoryNameText = itemView.findViewById(R.id.red_company_sub_category_row_name_text);

        // listener
        clickCover.setOnClickListener(myClickListener);
    }

    public void updateSubCategoryName(String name) {
        subCategoryNameText.setText(name);
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
