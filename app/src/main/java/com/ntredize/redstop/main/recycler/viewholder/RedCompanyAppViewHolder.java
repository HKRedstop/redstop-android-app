package com.ntredize.redstop.main.recycler.viewholder;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;

import androidx.recyclerview.widget.RecyclerView;

public class RedCompanyAppViewHolder extends RecyclerView.ViewHolder {
    
    // data
    private final int imageBorderColor;
    private final int imageBackgroundColor;

    // view
    private final RelativeLayout logoImageContainer;
    private final ImageView logoImage;
    private final TextView nameText;
    private final TextView androidPackageText;

    public RedCompanyAppViewHolder(View itemView, int imageBorderColor, int imageBackgroundColor) {
        super(itemView);
        this.imageBorderColor = imageBorderColor;
        this.imageBackgroundColor = imageBackgroundColor;

        // view
        logoImageContainer = itemView.findViewById(R.id.red_company_app_row_logo_image_container);
        logoImage = itemView.findViewById(R.id.red_company_app_row_logo_image);
        nameText = itemView.findViewById(R.id.red_company_app_row_name_text);
        androidPackageText = itemView.findViewById(R.id.red_company_app_row_android_package_text);
    }

    public void updateLogoImage(Drawable imageDrawable) {
        resetLogoImage();
        if (imageDrawable != null) setLogoImage(imageDrawable);
    }
    
    private void resetLogoImage() {
        // remove background color of container and image
        // remove image drawable
        logoImageContainer.setBackground(null);
        logoImage.setBackground(null);
        logoImage.setImageDrawable(null);
    }
    
    private void setLogoImage(Drawable imageDrawable) {
        logoImageContainer.setBackgroundColor(imageBorderColor);
        logoImage.setBackgroundColor(imageBackgroundColor);
        logoImage.setImageDrawable(imageDrawable);
    }

    public void updateName(String name) {
        nameText.setText(name);
    }
    
    public void updateAndroidPackage(String androidPackage) {
        androidPackageText.setText(androidPackage);
    }
    
}
