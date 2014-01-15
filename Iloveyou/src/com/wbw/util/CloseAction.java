package com.wbw.util;


import com.wbw.iloveyou.R;
import com.wbw.inter.AllSurfaceView;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.SurfaceView;



public class CloseAction {

private NotificationManager myNotiManager;  //×´Ì¬ÌáÊ¾À¸
	
	
	public CloseAction(final Context context,final AllSurfaceView sv){
		myNotiManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(R.string.dialog_title)
		.setMessage(R.string.dialog_message)
		.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
					}
				})
				
		.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						//sv.setRun(false);
						sv.setRun(true);
						System.exit(0);
					}
				})
	
		.create().show();
	}

	
}
