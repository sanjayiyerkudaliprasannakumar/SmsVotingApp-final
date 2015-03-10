package voting;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import voting.ModPollStorage;
import voting.Moderator;
import voting.Poll;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
/**
 * Created by Sanjay Iyer on 2/26/2015.
 */
@RestController
@RequestMapping("/api/v1")



public class PollingRestController {
    private static long pollSeqId = 345546880;

    @JsonView(Poll.ViewPoll.class)
    @RequestMapping(method=RequestMethod.POST, value="/moderators/{moderator_id}/polls",  produces="application/json")
    public ResponseEntity<String> createPoll(@Valid @RequestBody Poll poll, BindingResult result, @PathVariable("moderator_id") int moderator_id) {
	if(result.hasErrors())
	{
		return new ResponseEntity<String>(callError(result),HttpStatus.BAD_REQUEST);
	}
	else
	{	
        	if(ModPollStorage.moderators.containsKey(moderator_id)) {
            		poll.setPoll_id(Long.toString(pollSeqId, 36).toUpperCase());
            		Integer[] results = new Integer[poll.getChoice().length];
            		for (int i = 0; i < results.length; i++) {
				results[i] = 0;
            		}		
            		poll.setResults(results);
            		ModPollStorage.polls.put(poll.getPoll_id(), poll);

            		if (!ModPollStorage.modPolls.containsKey(moderator_id)) {
                		ArrayList<String> pollList = new ArrayList<String>();
                		ModPollStorage.modPolls.put(moderator_id, pollList);
            		}		
                	ModPollStorage.modPolls.get(moderator_id).add(poll.getPoll_id());
			pollSeqId++;
			return new ResponseEntity<String>(poll.toString(),HttpStatus.CREATED);
        	}	
        	else{
            		return new ResponseEntity<String>("Moderator ID not found!!",HttpStatus.BAD_REQUEST);
        	}
        }
    }

   
    @JsonView(Poll.ViewPoll.class)
    @RequestMapping(method=RequestMethod.GET, value="/polls/{poll_id}")
    public ResponseEntity<Poll> viewPollWithoutResult(@PathVariable("poll_id") String poll_id){
        if(ModPollStorage.polls.containsKey(poll_id))
        {
                return new ResponseEntity<Poll>(ModPollStorage.polls.get(poll_id),HttpStatus.OK);
        }
        else
            return new ResponseEntity<Poll>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(method=RequestMethod.GET, value="/moderators/{moderator_id}/polls/{poll_id}")
    public ResponseEntity<Poll> viewPollWithResult(@PathVariable("poll_id") String poll_id, @PathVariable("moderator_id") int moderator_id){
       if(ModPollStorage.polls.containsKey(poll_id) && ModPollStorage.moderators.containsKey(moderator_id))
       {
           if(checkModeratorToPoll(moderator_id,poll_id))
               return new ResponseEntity<Poll>(ModPollStorage.polls.get(poll_id),HttpStatus.OK);
           else
               return new ResponseEntity<Poll>(HttpStatus.BAD_REQUEST);
       }
       else
           return new ResponseEntity<Poll>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(method=RequestMethod.GET, value="/moderators/{moderator_id}/polls")
    public ResponseEntity<ArrayList<Poll>> listAllPolls(@PathVariable("moderator_id") int moderator_id)
    {
        ArrayList<Poll> pollList = new ArrayList<Poll>();
        if(ModPollStorage.moderators.containsKey(moderator_id) && ModPollStorage.modPolls.containsKey(moderator_id)){
            ArrayList<String> pollIdList = ModPollStorage.modPolls.get(moderator_id);
            for(String poll_id : pollIdList)
                pollList.add(ModPollStorage.polls.get(poll_id));
            return new ResponseEntity<ArrayList<Poll>>(pollList,HttpStatus.OK);
        }
        else
            return new ResponseEntity<ArrayList<Poll>>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(method=RequestMethod.DELETE, value="/moderators/{moderator_id}/polls/{poll_id}")
    public ResponseEntity<Poll> deletePoll(@PathVariable("moderator_id") int moderator_id, @PathVariable("poll_id") String poll_id)
    {
        if(ModPollStorage.moderators.containsKey(moderator_id) && ModPollStorage.polls.containsKey(poll_id)){
            if(checkModeratorToPoll(moderator_id,poll_id)) {
                ModPollStorage.polls.remove(poll_id);
                ModPollStorage.modPolls.get(moderator_id).remove(poll_id);
                if(ModPollStorage.modPolls.get(moderator_id).isEmpty()){
                    ModPollStorage.modPolls.remove(moderator_id);
                }
            }
            else
                return new ResponseEntity<Poll>(HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity<Poll>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<Poll>(HttpStatus.NO_CONTENT);
    }



    @RequestMapping(method=RequestMethod.PUT, value="/polls/{poll_id}", produces="application/json")
    public ResponseEntity<String> Vote(@PathVariable("poll_id") String poll_id, @RequestParam("choice") int choice){
        if(ModPollStorage.polls.containsKey(poll_id)){
            Integer[] result=ModPollStorage.polls.get(poll_id).getResults();
            if(choice<result.length) {
                result[choice]++;
                ModPollStorage.polls.get(poll_id).setResults(result);
                return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity<String>("Choice index is invalid!!",HttpStatus.BAD_REQUEST);
            }
        }
        else
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
	
    private String callError(BindingResult result) {
	StringBuilder errorMsg = new StringBuilder();
	for (ObjectError err: result.getAllErrors()){
		errorMsg.append(err.getDefaultMessage());
	}
	return errorMsg.toString();

    }

    private boolean checkModeratorToPoll(Integer mod_id, String poll_id){
        return ModPollStorage.modPolls.get(mod_id).contains(poll_id);
    }

}
