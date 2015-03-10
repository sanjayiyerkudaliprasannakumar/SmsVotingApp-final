package voting;

/**
 * Created by Sanjay Iyer on 2/25/2015.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.ApplicationContextException;
import voting.ModPollStorage;
import voting.Moderator;


@RestController
@RequestMapping("/api/v1")


public class VotingController {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static int moderatorSeqId = 10000;


    @RequestMapping(method=RequestMethod.POST, value="/moderators", produces="application/json")
    public ResponseEntity<String> createModerator(@Validated({Moderator.ModeratorValidator.class}) @RequestBody Moderator moderator, BindingResult result ) {
        if(result.hasErrors())
        {
            return new ResponseEntity<String>(callError(result),HttpStatus.BAD_REQUEST);
        }
        moderator.setId(moderatorSeqId);
        moderator.setCreated_at(dateFormatter.format(new Date()));
        ModPollStorage.moderators.put(moderatorSeqId, moderator);
	moderatorSeqId++;
        return new ResponseEntity<String>(moderator.toString(),HttpStatus.CREATED);
    }
    


	
    @RequestMapping(method=RequestMethod.GET, value="/moderators/{moderator_id}")
    public ResponseEntity<Moderator> viewModerator(@PathVariable("moderator_id") Integer moderator_id){
        if(ModPollStorage.moderators.containsKey(moderator_id)) {
            return new ResponseEntity<Moderator>(ModPollStorage.moderators.get(moderator_id),HttpStatus.OK);
        }
        else
            return new ResponseEntity<Moderator>( HttpStatus.NOT_FOUND);
    }




    @RequestMapping(method=RequestMethod.PUT, value="/moderators/{moderator_id}",  produces="application/json")
    public ResponseEntity<String> updateModerator(@Validated({Moderator.EmailValidator.class}) @PathVariable("moderator_id") Integer moderator_id, @RequestBody Moderator moderator, BindingResult result){
        if(result.hasErrors())
        {
            return new ResponseEntity<String>(callError(result),HttpStatus.BAD_REQUEST);
        }
	else
	{
        	if(ModPollStorage.moderators.containsKey(moderator_id)) {
            		Moderator mod = ModPollStorage.moderators.get(moderator_id);
	    		mod.setEmail(moderator.getEmail());
	    		mod.setPassword(moderator.getPassword());
            		return new ResponseEntity<String>(mod.toString(),HttpStatus.OK);
        	}
        	else
            		return new ResponseEntity<String>("Moderator ID not found!!",HttpStatus.NOT_FOUND);
	}
    }



    private String callError(BindingResult result) {
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError err: result.getAllErrors()){
            errorMsg.append(err.getDefaultMessage());
        }
        return errorMsg.toString();

    }
}
