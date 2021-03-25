package com.psl.java_training.tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.psl.java_training.bean.Contact;
import com.psl.java_training.exception.ContactNotFoundException;
import com.psl.java_training.service.ContactService;

public class ContactTester {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		ContactService cs=new ContactService();
		List<Contact>contacts=new ArrayList<>();
		cs.readContactsFromFile(contacts,"Contact.txt" );
		String myNo[]= {"2345","4567","76859","435345"};
		Contact contact=new Contact(4, "aayush", "aayush@gmail.com", Arrays.asList(myNo));
		//cs.addContact(contact, contacts);
	/*	try {
			cs.removeContact(contact, contacts);
		} catch (ContactNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			e.getMessage();
		}*/
		/*try {
			Contact con=cs.searchContactByName("Rohan", contacts);
			System.out.println("search successfull..............");
			System.out.println(con);
		}
		catch (ContactNotFoundException e) {
			// TODO: handle exception
			e.getMessage();
		}*/
		List<Contact> list=null;
		try {
			list = cs.searchContactByNumber("9424", contacts);
			System.out.println("Here are the search results..............");
			for (Contact contact2 : list) {
				System.out.println(contact2);
			}
		} catch (ContactNotFoundException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
				
				/*cs.addContactNumber(4, "9424", contacts);
				for (Contact con : contacts) {
					System.out.println(con);
				}*/
		//cs.sortContactsByName(contacts);
		//cs.serializeContactDetails(contacts, "SerializeContacts.ser");
		/*List<Contact>list=cs.deserializeContact("SerializeContacts.ser");
		for (Contact contact2 : list) {
			System.out.println(contact2);
		}*/

	/*Set<Contact>cSet=cs.populateContactFromDb();
	for (Contact contact2 : cSet) {
		System.out.println(contact2);
	}*/
	}
		

}
