package com.aivarsda.keygentool;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

public class AboutDialogFragment extends DialogFragment
{
	private TextView	_github_link_tv;
	private TextView	_developer_name_tv;

	public static AboutDialogFragment newInstance()
	{
		AboutDialogFragment f = new AboutDialogFragment();
		return f;
	}

	private void sendEmail()
	{
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "aivars.dalderis@gmail.com", null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "RE: KeyGenTool - ");
		startActivity(Intent.createChooser(emailIntent, "Send Email"));
	}

	private void openGitHubLink()
	{
		try
		{
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
			startActivity(myIntent);
		}
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(getActivity(), "No application can handle this request," + " Please install a web browser", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (getDialog() != null)
		{
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		}

		View root = inflater.inflate(R.layout.about_fragment, container, false);

		_github_link_tv = (TextView) root.findViewById(R.id.github_link_tv);
		_github_link_tv.setPaintFlags(_github_link_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		_github_link_tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				openGitHubLink();
			}
		});
		_developer_name_tv = (TextView) root.findViewById(R.id.developer_name_tv);
		_developer_name_tv.setPaintFlags(_developer_name_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		_developer_name_tv.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sendEmail();
			}
		});

		return root;
	}

	public void onResume()
	{
		super.onResume();
		Window window = getDialog().getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		window.setWindowAnimations(R.style.ZoomAnim_Window);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart()
	{
		super.onStart();
		if (getDialog() != null)
		{
			int fullWidth = getDialog().getWindow().getAttributes().width;
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			fullWidth = display.getWidth();

			final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
			int w = fullWidth - padding;
			int h = getDialog().getWindow().getAttributes().height;
			getDialog().getWindow().setLayout(w, h);
		}
	}
}
