package se.embargo.core;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Activities {
	private static final String AUDIO_PLAYER_URI = "https://play.google.com/store/apps/details?id=com.google.android.music";

	public static void playAudio(final Context context, String uri, String mimetype) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(uri), mimetype);

        try {
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.dialog_missing_audio_player_title);
			builder.setMessage(R.string.dialog_missing_audio_player_message);
			
			builder.setPositiveButton(R.string.btn_install, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AUDIO_PLAYER_URI));
					try {
						context.startActivity(intent);
					}
					catch (ActivityNotFoundException e) {
						Toast.makeText(context, R.string.dialog_failed_to_open_url, Toast.LENGTH_LONG).show();
					}
				}
			});
			
			builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			
			builder.create().show();
		}
	}
}
