package shok.contactmanager;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsAdapter extends ArrayAdapter<Contact> {

	Context context;
	List<Contact> data=null;
	int resource;
	public ContactsAdapter(Context context, int resource, List<Contact> objects) {
		super(context, resource, objects);
		this.context=context;
		this.data=objects;
		this.resource=resource;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Contact contact = getItem(position);
		LayoutInflater inflater = (LayoutInflater)context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resource, parent, false);
		TextView name = (TextView)view.findViewById(R.id.textView1);
		ImageView image = (ImageView)view.findViewById(R.id.imageView1);
		if(contact.getImage()!="null"){
			image.setImageURI(Uri.parse(contact.getImage()));
		}else{
			image.setImageResource(R.drawable.contact_image);
		}
		name.setText(contact.getName());
		return view;
	}
	
	

}
