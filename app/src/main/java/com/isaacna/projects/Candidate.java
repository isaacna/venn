package com.isaacna.projects;
import android.graphics.Bitmap;

/**
 * Created by Chauncey on 2/24/18.
 */

public class Candidate implements Person {

    private String firstName;
    private String lastName;
    private String bioInfo;
    private Bitmap profilePic;
    private int community;

    public Candidate(){

    }
    public Candidate(String first, String last, String bio){
        firstName = first;
        lastName = last;
        bioInfo = bio;
    }

    public Candidate(String first, String last, String bio, Bitmap pic){
        firstName = first;
        lastName = last;
        bioInfo = bio;
        profilePic = pic;
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
    public Bitmap getProfilePic() {return profilePic;}

    public int getCommunity() {
        return community;
    }
    public void setCommunity(int com){
        this.community = com;
    }
}
