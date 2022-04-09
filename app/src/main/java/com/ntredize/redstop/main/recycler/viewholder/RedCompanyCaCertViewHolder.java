package com.ntredize.redstop.main.recycler.viewholder;

import android.view.View;
import android.widget.TextView;

import com.ntredize.redstop.R;

import androidx.recyclerview.widget.RecyclerView;

public class RedCompanyCaCertViewHolder extends RecyclerView.ViewHolder {

    // view
    private final TextView organizationText;
    private final TextView commonNameText;

    public RedCompanyCaCertViewHolder(View itemView) {
        super(itemView);

        // view
        organizationText = itemView.findViewById(R.id.red_company_ca_cert_row_organization_text);
        commonNameText = itemView.findViewById(R.id.red_company_ca_cert_row_common_name_text);
    }

    public void updateOrganization(String organization) {
        organizationText.setText(organization);
    }
    
    public void updateCommonName(String commonName) {
        commonNameText.setText(commonName);
    }
    
}
