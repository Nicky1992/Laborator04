package ro.pub.cs.systems.pdsd.lab04.contactsmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BasicDetailsFragment extends Fragment {
	
	private class MyButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			
			switch(id) {
			case R.id.button1:
				Button button = (Button)v;
				FragmentManager fragMng = getActivity().getFragmentManager();
				FragmentTransaction transcation = fragMng.beginTransaction();
				Fragment fragTop = fragMng.findFragmentById(R.id.frame1);
				Fragment fragBottom = fragMng.findFragmentById(R.id.frame2);
				AdditionalDetailsFragment bottom = null;
				if (fragBottom instanceof AdditionalDetailsFragment) {
					bottom = (AdditionalDetailsFragment)fragBottom;
				}
				
				if (bottom == null) {
					transcation.add(R.id.frame2, new AdditionalDetailsFragment());
					String hideText = getActivity().getResources().getString(R.string.hide_additional_fields);
					button.setText(hideText);
					transcation.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
				} else {
					transcation.remove(bottom);
					String showText = getActivity().getResources().getString(R.string.show_additional_fields);
					button.setText(showText);
					transcation.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
				}
				
				transcation.commit();
				break;
			
			case R.id.save:
				// Basic
				EditText nameEditText = (EditText)getActivity().findViewById(R.id.name);
				EditText phoneEditText = (EditText)getActivity().findViewById(R.id.tel);
				EditText emailEditText = (EditText)getActivity().findViewById(R.id.email);
				EditText addressEditText = (EditText)getActivity().findViewById(R.id.address);
				// Additional
				EditText jobEditText = (EditText)getActivity().findViewById(R.id.job);
				EditText companyEditText = (EditText)getActivity().findViewById(R.id.company);
				EditText websiteEditText = (EditText)getActivity().findViewById(R.id.website);
				EditText imEditText = (EditText)getActivity().findViewById(R.id.im);
				
				String name = nameEditText.getText().toString();
				String phone = phoneEditText.getText().toString();
				String email = emailEditText.getText().toString();
				String address = addressEditText.getText().toString();
				
				String job = null;
				String company = null;
				String website = null;
				String im = null;
				
				if (jobEditText != null) {
					job = jobEditText.getText().toString();
				}
				
				if (companyEditText != null) {
					company = companyEditText.getText().toString();
				}
				
				if (websiteEditText != null) {
					job = websiteEditText.getText().toString();
				}
				
				if (imEditText != null) {
					job = imEditText.getText().toString();
				}
				
				Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
				intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
				
				if (name != null) {
				  intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
				}
				if (phone != null) {
				  intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
				}
				if (email != null) {
				  intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
				}
				if (address != null) {
				  intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
				}
				if (job != null) {
				  intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, job);
				}
				if (company != null) {
				  intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
				}
				
				ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
				if (website != null) {
				  ContentValues websiteRow = new ContentValues();
				  websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
				  websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
				  contactData.add(websiteRow);
				}
				if (im != null) {
				  ContentValues imRow = new ContentValues();
				  imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
				  imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
				  contactData.add(imRow);
				}
				intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
				getActivity().startActivity(intent);				
				break;
				
			case R.id.cancel:
				getActivity().setResult(Activity.RESULT_CANCELED, new Intent());
				getActivity().finish();
				break;				
			}
		}
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle state) {
		return layoutInflater.inflate(R.layout.fragment_basic_details, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		MyButtonListener buttonOnClickListener = new MyButtonListener();
		super.onActivityCreated(savedInstanceState);
		Button additionalDetailsButton = (Button)getActivity().findViewById(R.id.button1);
		additionalDetailsButton.setOnClickListener(buttonOnClickListener);
		Button saveButton = (Button)getActivity().findViewById(R.id.save);
		saveButton.setOnClickListener(buttonOnClickListener);
		Button cancelButton = (Button)getActivity().findViewById(R.id.cancel);
		cancelButton.setOnClickListener(buttonOnClickListener);
		EditText phoneEditText = (EditText)getActivity().findViewById(R.id.tel);
		Intent intent = getActivity().getIntent();
		if (intent != null) {
			String phone = intent.getStringExtra(Constants.PHONE_NUMBER_KEY);
			if (phone != null) {
				phoneEditText.setText(phone);
			} else {
				Activity activity = getActivity();
				Toast.makeText(activity, activity.getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
			}
		}
	}
}
