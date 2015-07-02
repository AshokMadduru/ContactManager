package shok.contactmanager;

public class Contact {

	String name;
	String image;
	String number;
	String mail;
	
	public Contact(String name){
		this.name=name;
	}
	public Contact(String name,String image){
		this.name=name;
		this.image=image;
	}
	public Contact(String name,String image,String number){
		this.name=name;
		this.image=image;
		this.number=number;
	}
	public Contact(String name,String image,String number,String mail){
		this.name=name;
		this.image=image;
		this.number=number;
		this.mail=mail;
	}
	public String getName(){
		return name;
	}
	public String getImage(){
		return image;
	}
	public String getNumber(){
		return number;
	}
}
