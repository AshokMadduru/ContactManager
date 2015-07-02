package shok.contactmanager;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
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

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String[][] totalContacts=getContacts();
		List<Contact> list = new ArrayList<Contact>();
		for(int i=0;i<totalContacts.length;i++){
			list.add(new Contact(totalContacts[i][0],totalContacts[i][1],
					totalContacts[i][2],totalContacts[i][3]));
		}
		ArrayAdapter<Contact> adapter = new ContactsAdapter(this,R.layout.contact, list);
		ListView listView = (ListView)findViewById(R.id.listView1);
		listView.setAdapter(adapter);
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
	
	public String[][] getContacts(){
		ContentResolver cr =  getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);
		String[][] contacts = new String[cursor.getCount()][4];
		int count = 0;
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
				contacts[count][0]=contactName;
				contacts[count][1]=contactImage;
				contacts[count][2]=contactNumber;
				contacts[count][3]=contactEmail;
				count++;
			}
		}
		cursor.close();
		return contacts;
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
}
