package com.ntredize.redstop.main.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ntredize.redstop.R;
import com.ntredize.redstop.main.activity.ActivityBase;
import com.ntredize.redstop.support.service.DownloadImageService;
import com.ntredize.redstop.support.service.RedCompanyService;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RedCompanyViewHolder extends RecyclerView.ViewHolder {

    // listener
    public MyClickListener myClickListener;
    
    // data
    private final int imageBorderColor;
    private final int imageBackgroundColor;
    private String rowCompanyCode;

    // view
    private final RelativeLayout logoImageContainer;
    private final ImageView logoImage;
    private final TextView displayNameText;
    private final TextView displaySubNameText;
    private final ImageView redStarImage1;
    private final ImageView redStarImage2;
    private final ImageView redStarImage3;
    private final ImageView redStarImage4;
    private final ImageView redStarImage5;

    public RedCompanyViewHolder(View itemView, int imageBorderColor, int imageBackgroundColor, MyClickListener myClickListener) {
        super(itemView);
        this.imageBorderColor = imageBorderColor;
        this.imageBackgroundColor = imageBackgroundColor;
        this.myClickListener = myClickListener;

        // view
        RelativeLayout clickCover = itemView.findViewById(R.id.red_company_row_click_cover);
        logoImageContainer = itemView.findViewById(R.id.red_company_row_logo_image_container);
        logoImage = itemView.findViewById(R.id.red_company_row_logo_image);
        displayNameText = itemView.findViewById(R.id.red_company_row_display_name_text);
        displaySubNameText = itemView.findViewById(R.id.red_company_row_display_sub_name_text);
        redStarImage1 = itemView.findViewById(R.id.red_company_row_red_star_image_1);
        redStarImage2 = itemView.findViewById(R.id.red_company_row_red_star_image_2);
        redStarImage3 = itemView.findViewById(R.id.red_company_row_red_star_image_3);
        redStarImage4 = itemView.findViewById(R.id.red_company_row_red_star_image_4);
        redStarImage5 = itemView.findViewById(R.id.red_company_row_red_star_image_5);

        // listener
        clickCover.setOnClickListener(myClickListener);
    }
    
    public void setRowCompanyCode(String rowCompanyCode) {
        this.rowCompanyCode = rowCompanyCode;
    }

    public void updateLogoImage(ActivityBase activity, DownloadImageService downloadImageService,
                                List<String> displayedCompanyCodeList, String groupCode, String companyCode) {
        resetLogoImage();
        Bitmap cacheImage = downloadImageService.getCacheRedCompanyImage(groupCode, companyCode);
        if (cacheImage != null) setLogoImage(cacheImage);
        else {
            // download from server
            // (do not get image again if it has already displayed)
            if (!displayedCompanyCodeList.contains(companyCode)) {
                new Thread(() -> {
                    String imageUrl = downloadImageService.getRedCompanyThumbnailImageUrl(groupCode, companyCode);
                    Bitmap downloadImage = downloadImageService.getImageByUrl(imageUrl);
                    if (downloadImage != null) {
                        // cache image
                        downloadImageService.saveRedCompanyImageAsCache(groupCode, companyCode, downloadImage);

                        // set image (only if company code is valid)
                        if (rowCompanyCode.equals(companyCode)) {
                            activity.runOnUiThread(() -> setLogoImage(downloadImage));
                        }
                    }
                }).start();
            }
        }
    }
    
    private void resetLogoImage() {
        // remove background color of container and image
        // remove image bitmap
        logoImageContainer.setBackground(null);
        logoImage.setBackground(null);
        logoImage.setImageBitmap(null);
    }
    
    private void setLogoImage(Bitmap imageBitmap) {
        logoImageContainer.setBackgroundColor(imageBorderColor);
        logoImage.setBackgroundColor(imageBackgroundColor);
        logoImage.setImageBitmap(imageBitmap);
    }

    public void updateDisplayName(String displayName) {
        displayNameText.setText(displayName);
    }

    public void updateDisplaySubName(String displaySubName) {
        if (displaySubName != null && !displaySubName.isEmpty()) {
            displaySubNameText.setVisibility(View.VISIBLE);
            displaySubNameText.setText(displaySubName);
        }
        else {
            displaySubNameText.setVisibility(View.GONE);
            displaySubNameText.setText("");
        }
    }

    public void updateRedStar(RedCompanyService redCompanyService, int redStar) {
        List<Drawable> redStarDrawables = redCompanyService.getRedStarDrawables(redStar);
        if (!redStarDrawables.isEmpty()) {
            redStarImage1.setImageDrawable(redStarDrawables.get(0));
            redStarImage2.setImageDrawable(redStarDrawables.get(1));
            redStarImage3.setImageDrawable(redStarDrawables.get(2));
            redStarImage4.setImageDrawable(redStarDrawables.get(3));
            redStarImage5.setImageDrawable(redStarDrawables.get(4));
        }
        
        int color = redCompanyService.getRedStarColor(redStar);
        redStarImage1.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        redStarImage2.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        redStarImage3.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        redStarImage4.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        redStarImage5.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        
        String contentDesc = redCompanyService.getRedStarContentDescription(redStar);
        redStarImage1.setContentDescription(contentDesc);
        redStarImage2.setContentDescription(contentDesc);
        redStarImage3.setContentDescription(contentDesc);
        redStarImage4.setContentDescription(contentDesc);
        redStarImage5.setContentDescription(contentDesc);
    }


    /* Click Listener (Red Company) */
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
