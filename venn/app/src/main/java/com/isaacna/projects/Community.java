package com.isaacna.projects;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Chauncey on 2/24/18.
 */

public class Community {

    int ID;
    Queue<Candidate> members;
    LinkedList<Candidate> matches;
    Candidate last;

    public Community (int CommID, int hostID){
        members = new LinkedList<Candidate>();
        matches = new LinkedList<Candidate>();

        /*
        A database query will be here for getting all the members of a given community
        and all the people matched with hostID
         */
    }

    public Queue<Candidate> getMembers(){
        return members;
    }

    public Candidate getNext(){
        last =  members.remove();
        return last;
    }

    public void makeDecision(boolean decide){
        if(decide && last != null){
            matches.add(last);
        }
        last = null;
    }

    public LinkedList<Candidate> getMatches(){
        return matches;
    }
}
