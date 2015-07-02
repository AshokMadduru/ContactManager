package shok.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String name = intent.getStringExtra("contact_name");
		String number = intent.getStringExtra("contact_number");
		String image = intent.getStringExtra("contact_image");
		String mail = intent.getStringExtra("contact_mail");
		
		setContentView(R.layout.contact_dialog);
		//TextView cName = (TextView)findViewById(R.id.contactName);
		TextView cNumber = (TextView)findViewById(R.id.contactNumber);
		TextView cMail = (TextView)findViewById(R.id.contactMail);
		ImageView cImage = (ImageView) findViewById(R.id.contactImage);
		cNumber.setText(number);
		//cName.setText(name);
		cMail.setText(mail);
		if(!image.equals("null")){
			cImage.setImageURI(Uri.parse(image));
		}
	}

	
}
