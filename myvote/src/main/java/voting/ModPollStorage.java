package voting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sanjay Iyer on 3/2/2015.
 */
public class ModPollStorage {
    public static HashMap<Integer, Moderator> moderators = new HashMap<Integer, Moderator>();
    public static HashMap<String, Poll> polls = new HashMap<String, Poll>();
    public static HashMap<Integer,ArrayList<String>> modPolls = new HashMap<Integer, ArrayList<String>>();

}