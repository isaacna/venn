package com.isaacna.projects;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private int communityId;
    private int userId;
    private int answer;

    private Set<Community> communities;


    public Profile(){

    }
    public Profile(String first, String last, String bio, String community, int community_id, int user_id){
        firstName = first;
        lastName = last;
        bioInfo = bio;
        whichCommunity= community;

        communities = new HashSet<Community>();
        communityId = community_id;
        userId= user_id;
        /*
        Add in code here that queries for all of the communities
         */
    }
    
    public Profile(String first, String last, String bio, String picSrc, String community, int community_id, int user_id, int yesOrNo){
        firstName = first;
        lastName = last;
        bioInfo = bio;
        profilePic = getBitmapFromURL(picSrc);
        whichCommunity = community;

        communities = new HashSet<Community>();
        communityId = community_id;
        userId= user_id;
        answer=yesOrNo; //what the profile answered to you
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


    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/images/" + src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
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
    public int getUserId() {return userId;}
    public int getCommunityId() {return communityId;}
    public int getAnswer() {return answer;}

}
