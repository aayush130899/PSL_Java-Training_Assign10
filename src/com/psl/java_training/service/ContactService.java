package com.psl.java_training.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.psl.java_training.bean.Contact;
import com.psl.java_training.exception.ContactNotFoundException;
import com.psl.java_training.jdbcutil.JDBCUtil;

public class ContactService {
	public void addContact(Contact contact,List<Contact>contacts) {
		contacts.add(contact);
		System.out.println("contact details successfully added to list.............");
	}
	public void removeContact(Contact contact,List<Contact>contacts) throws ContactNotFoundException {
		if(contacts.contains(contact)) {
			contacts.remove(contact);
			System.out.println("contact removed successfully from list........");
		}
		else {
			throw new ContactNotFoundException("contact not present in list.........");
		}
	}
	public Contact searchContactByName(String name,List<Contact>contacts) throws ContactNotFoundException {
		Contact c=new Contact();
		for (Contact con : contacts) {
			if(con.getContactName().equals(name)) {
				c.setContactName(con.getContactName());
				c.setContactId(con.getContactId());
				c.setContactNumber(con.getContactNumber());
				c.setEmail(con.getEmail());
				break;
			}
		}
		if(c.getContactName()==null) throw new ContactNotFoundException("contact not present in list.........search unsuccessfull!!");
		return c;
	}
	public List<Contact> searchContactByNumber(String number, List<Contact> contact) throws ContactNotFoundException{
		List<Contact>contactList=new ArrayList<>();
		for (Contact con : contact) {
			List<String>conNos=con.getContactNumber();
			for(String no:conNos) {
				if(no.equals(number) || no.contains(number)) {
					contactList.add(con);
					break;
				}
			}
		}
		if(contactList.size()==0) throw new ContactNotFoundException("contact not present in list.........search by contact number unsuccessfull!!");
		return contactList;
	}
	
	public void addContactNumber(int contactId,String contactNo,List<Contact>contacts) {
		boolean isPresent=false;
		for (Contact contact : contacts) {
			if(contact.getContactId()==contactId) {
				contact.getContactNumber().add(contactNo);
				isPresent=true;
				break;
			}
		}
		if(isPresent==true) System.out.println("contact updated successfully........");
		else System.out.println("no contact found for given id...............");
	}
	public void sortContactsByName(List<Contact>contacts) {
		Collections.sort(contacts);
		for (Contact contact : contacts) {
			System.out.println(contact);
		}
	}
	public void readContactsFromFile(List<Contact>contacts,String fileName) {
		File file=new File("C:\\Users\\HP\\Documents\\workspace-sts-3.9.10.RELEASE\\Assign10\\src\\com\\psl\\java_training\\utility"+"\\"+fileName);
		try {
			FileReader reader=new FileReader(file);
			BufferedReader br=new BufferedReader(reader);
			String str=br.readLine();
			while(str!=null) {
				String arr[]=str.split(",");
				Contact con=new Contact();
				con.setContactId(Integer.parseInt(arr[0]));
				con.setContactName(arr[1]);
				con.setEmail(arr[2]);
					List<String>list=new ArrayList<>();
					for(int i=3;i<arr.length;i++) {
						list.add(arr[i]);
					}
					con.setContactNumber(list);
					contacts.add(con);
					str=br.readLine();
			}
			System.out.println("successfully read contacts from file to list...............");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void serializeContactDetails(List<Contact>contacts,String fileName) {
		File file=new File("C:\\Users\\HP\\Documents\\workspace-sts-3.9.10.RELEASE\\Assign10\\src\\com\\psl\\java_training\\utility"+"\\"+fileName);
		boolean done=false;
		try (FileOutputStream fos=new FileOutputStream(file);
				ObjectOutputStream oos=new ObjectOutputStream(fos);//this is try with resource
				){
			oos.writeObject(contacts);
			done=true;
			//fos.close(); if not want to do explicitly then we can use ARM(Automatic Resource Management) using try with resources
			//oos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			done=false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			done=false;
			e.printStackTrace();
		}
		if(done==true) System.out.println("Serialization done successfully..........");
		else System.out.println("something went wrong.............");
	}
	public List<Contact>deserializeContact(String fileName){
		File file=new File("C:\\Users\\HP\\Documents\\workspace-sts-3.9.10.RELEASE\\Assign10\\src\\com\\psl\\java_training\\utility"+"\\"+fileName);
		List<Contact>list=new ArrayList<>();

		try {
			FileInputStream fis=new FileInputStream(file);
			ObjectInputStream ois=new ObjectInputStream(fis);
			list=(List<Contact>) ois.readObject();
			fis.close();
			ois.close();
			System.out.println("deserialization done successfully....");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("something went wrong......");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("something went wrong......");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("something went wrong......");
		}
		return list;
	}
	public Set<Contact>populateContactFromDb(){
		Set<Contact>contactSet=new HashSet<Contact>();
		Connection con=JDBCUtil.getConnection();
		Contact contact=null;
		try {
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select * from contact_tbl");
			while(rs.next()) {
				contact=new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), Arrays.asList(rs.getString(4)));
				contactSet.add(contact);
			}
			System.out.println("sucessfully populated data from db to set of contact...............");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("something went wrong.............");
		}
		finally {
			JDBCUtil.closeConnection(con);
		}
		return contactSet;
	}
}
