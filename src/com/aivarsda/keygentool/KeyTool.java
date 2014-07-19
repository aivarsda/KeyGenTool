package com.aivarsda.keygentool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class KeyTool extends FragmentActivity
{
	private EditText	_hashKey_et;
	private EditText	_pkgName_et;
	private Spinner		_pkg_opt_spinner;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		_hashKey_et = (EditText) findViewById(R.id.hash_et);
		_pkgName_et = (EditText) findViewById(R.id.packageName_et);
		_pkg_opt_spinner = (Spinner) findViewById(R.id.pkg_opt_spinner);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, getInstalledAppsPkg());
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_pkg_opt_spinner.setAdapter(spinnerArrayAdapter);

		_pkg_opt_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				_pkgName_et.setText(((TextView) view).getText());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});

		final EditText packageName = (EditText) findViewById(R.id.packageName_et);
		Button getHashBtn = (Button) findViewById(R.id.getHashKey_btn);
		getHashBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				String pkgName = packageName.getText().toString();
				if (pkgName != null && !pkgName.equals(""))
				{
					getHashKey(pkgName);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Enter the Package Name", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		Button shareHashBtn = (Button) findViewById(R.id.shareHashKey_btn);
		shareHashBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (_hashKey_et.getText() != null && _hashKey_et.getText().length()>0 && _pkgName_et.getText() !=null && _pkgName_et.getText().length()>0)
				{
					onShareClicked();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please get the HashKey first", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		ImageView aboutBtn = (ImageView) findViewById(R.id.about);
		aboutBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				onAboutClicked();
			}
		});
	}

	private ArrayList<String> getInstalledAppsPkg()
	{
		ArrayList<String> pkgList = new ArrayList<String>();
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++)
		{
			PackageInfo pinfo = packs.get(i);
			if (pinfo.versionName != null)
			{
				pkgList.add(pinfo.packageName);
			}
		}
		return pkgList;
	}

	private void getHashKey(String pkgName)
	{
		try
		{
			PackageInfo info = getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures)
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String hashKey = Base64.encodeBytes(md.digest());
				_hashKey_et.setText(hashKey);
				Log.i("KeyTool", pkgName + " -> hashKey = " + hashKey);
			}
		}
		catch (NameNotFoundException e)
		{
			_hashKey_et.setText(e.toString());
		}
		catch (NoSuchAlgorithmException e)
		{
			_hashKey_et.setText(e.toString());
		}
	}
	
	private void onShareClicked()
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "KeyGenTool");
		intent.putExtra(Intent.EXTRA_TEXT, "App Package Name: "+_pkgName_et.getText().toString()+"\nHashKey: "+_hashKey_et.getText().toString());
		startActivity(Intent.createChooser(intent, "Share With"));
	}
	
	private void onAboutClicked()
	{
		AboutDialogFragment dialog = new AboutDialogFragment();
		dialog.show(getSupportFragmentManager(), "AboutDialogFragment");
	}
}