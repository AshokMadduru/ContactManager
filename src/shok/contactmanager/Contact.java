package shok.contactmanager;

public class Contact {

	String name;
	String image;
	
	public Contact(String name){
		this.name=name;
	}
	public Contact(String name,String image){
		this.name=name;
		this.image=image;
	}
	public String getName(){
		return name;
	}
	public String getImage(){
		return image;
	}
}
