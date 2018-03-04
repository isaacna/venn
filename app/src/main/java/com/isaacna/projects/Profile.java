package com.isaacna.projects;
import android.graphics.Bitmap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Chauncey on 2/18/18.
 */

public class Profile implements Person{

    private String firstName;
    private String lastName;
    private String bioInfo;
    private Bitmap profilePic;
    private String whichCommunity;

    private Set<Community> communities;


    public Profile(){

    }
    public Profile(String first, String last, String bio, String community){
        firstName = first;
        lastName = last;
        bioInfo = bio;
        whichCommunity= community;

        communities = new HashSet<Community>();

        /*
        Add in code here that queries for all of the communities
         */
    }
    
    public Profile(String first, String last, String bio, Bitmap pic, String community){
        firstName = first;
        lastName = last;
        bioInfo = bio;
        profilePic = pic;
        whichCommunity = community;

        communities = new HashSet<Community>();

        /*
        Add in code here that queries for all of the communities
         */
    }

    public Queue<Candidate> getCandidates(){
        Queue<Candidate> candidates = new LinkedList<Candidate>();

        for(Community c: communities){
            candidates.addAll(c.getMembers());
        }

        return candidates;
    }

    public Queue<Candidate> getCandidates(Community onlyThisOne){
        Queue<Candidate> candidates = new LinkedList<Candidate>();
        candidates.addAll(onlyThisOne.getMembers());
        return candidates;
    }

    public Set<Candidate> getMatches(){
        Set<Candidate> matches = new HashSet<Candidate>();
        for(Community c: communities){
            matches.addAll(c.getMatches());
        }
        return matches;
    }

    public Set<Candidate> getMatches(Community onlyThisOne){
        Set<Candidate> matches = new HashSet<Candidate>();
        matches.addAll(onlyThisOne.getMatches());
        return matches;
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
    public String getWhichCommunity() { return whichCommunity;}
    public Bitmap getProfilePic() {return profilePic;}

}
