package com.ntredize.redstop.common.exception;

import android.content.Context;

import com.ntredize.redstop.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApplicationException extends Exception {
	
	/* Exception Code */
	public final static int SYSTEM_ERROR = R.string.msg_system_error;
	public final static int VIEW_URL_ERROR = R.string.msg_view_url_error;
	public final static int SHARE_ERROR = R.string.msg_share_error;
	public final static int SEND_EMAIL_ERROR = R.string.msg_send_email_error;
	public final static int SCAN_ERROR = R.string.msg_scan_error;
	public final static int INVALID_BARCODE_FORMAT = R.string.msg_barcode_format_invalid;
	public final static int HTTP_CLIENT_ERROR = R.string.msg_http_client_error;
	
	private final Context context;
	private final int id;
	private final List<Object> args;
	
	
	/* Init */
	public ApplicationException(Context context, int id) {
		this.context = context;
		this.id = id;
		this.args = new ArrayList<>();
	}
	
	public ApplicationException(Context context, int id, Object... args) {
		this.context = context;
		this.id = id;
		
		this.args = new ArrayList<>();
		Collections.addAll(this.args, args);
	}
	
	
	/* Get Message */
	@Override
	public String getMessage() {
		return context.getString(id, args.toArray());
	}
	
}
