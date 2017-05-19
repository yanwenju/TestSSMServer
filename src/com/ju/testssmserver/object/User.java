/**
 * 
 */
package com.ju.testssmserver.object;

/**
 * @author Yan Wenju
 *
 */
public class User {

	private int userID;
	private String userName;
	private String password;
	
	public int getUserID(){
		return userID;
	}
	public void setUserID(int id){
		this.userID = id;
	}
	
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
}
