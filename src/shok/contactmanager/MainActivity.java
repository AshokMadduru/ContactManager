package shok.contactmanager;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ArrayAdapter<Contact> adapter = new ContactsAdapter(this,R.layout.contact, new ArrayList<Contact>());
		listView = (ListView)findViewById(R.id.listView1);
		listView.setAdapter(adapter);
		new ContactsTask().execute();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int arg2,
					long arg3) {
				Contact contact = (Contact) parent.getItemAtPosition(arg2);
				String name = contact.getName();
				String mail = contact.getMail();
				String number = contact.getNumber();
				String image = contact.getImage();
				Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
				intent.putExtra("contact_name",name );
				intent.putExtra("contact_mail", mail );
				intent.putExtra("contact_number", number);
				intent.putExtra("contact_image", image);
				startActivity(intent);
				
			}
		});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	class ContactsTask extends AsyncTask<Void, String, Void>{
		
		ArrayAdapter<Contact> contact;
		
		@Override
		protected void onPostExecute(Void result) {
			Toast.makeText(MainActivity.this, "Loading completed", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			contact = (ArrayAdapter<Contact>) listView.getAdapter();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			contact.add(new Contact(values[0],values[1],values[2],values[3]));
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			ContentResolver cr =  getContentResolver();
			Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
					null, null, null, null);
			if(cursor.getCount()>0){
				while(cursor.moveToNext()){
					String contactName = "";
					String contactNumber = "";
					String contactImage = "";
					String contactEmail = "";
					String id = cursor.getString(
							cursor.getColumnIndex(ContactsContract.Contacts._ID));
					contactName = cursor.getString(
							cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					
					String image_uri = cursor.getString(cursor.getColumnIndex(
							ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
		 
					if(image_uri==null){
						contactImage = "null";
					}else{
						contactImage=image_uri;
					}	            
		  
					Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
							null, ContactsContract.CommonDataKinds.Email.CONTACT_ID+" = ?",
							new String[] {id}, null);
					while(emails.moveToNext()){
						String email_id = emails.getString(emails.getColumnIndex(ContactsContract.
								CommonDataKinds.Email.ADDRESS));
						if(email_id==null){
							contactEmail = "null";
						}else{
							contactEmail = email_id;
						}
					}
					
					int has_phone_num = Integer.parseInt(cursor.getString(
							cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
					if(has_phone_num>0){
						Cursor cur= cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",
								new String[] {id},null);
						while(cur.moveToNext()){
							contactNumber = cur.getString(cur.getColumnIndex(
									ContactsContract.CommonDataKinds.Phone.NUMBER));
						}
						cur.close();
					}
					publishProgress(contactName,contactImage,contactNumber,contactEmail);
				}
			}
			cursor.close();

			return null;
		}
		
	}
}
