package com.isaacna.projects;

/**
 * Created by Chauncey on 2/18/18.
 */

public class Profile {

    private String firstName;
    private String lastName;

    private String bioInfo;

    public Profile(){

    }
    public Profile(String first, String last, String bio){
        firstName = first;
        lastName = last;
        bioInfo = bio;
    }

    public void setFirstName(String s){
        this.firstName = s;
    }
    public void setLastName(String s){
        this.lastName = s;
    }
    public void setBioInfo(String s){
        this.bioInfo = s;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getBioInfo(){
        return bioInfo;
    }

}
