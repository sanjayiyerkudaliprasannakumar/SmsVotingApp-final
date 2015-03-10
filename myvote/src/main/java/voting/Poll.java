package voting;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import java.util.*;


/**
 * Created by Sanjay Iyer on 2/25/2015.
 */

    public class Poll {

        @JsonView(ViewPoll.class)
        private String poll_id;

        @JsonView(ViewPoll.class)
	@NotBlank(message="question field must not be null or empty!!")
        private String question;

        @JsonView(ViewPoll.class)
	@NotBlank(message="started_at field must not be null or empty!!")
        private String started_at;

        @JsonView(ViewPoll.class)
	@NotBlank(message="expired_at field must not be null or empty!!")
        private String expired_at;

        @JsonView(ViewPoll.class)
	@NotNull(message="choice field must not be null or empty!!")
	@Size(min=2, message="choice field must have atleast 2 values!!")
        private String[] choice;

        private Integer[] results;

        public Poll(){}

        public String getPoll_id()
        {
            return poll_id;
        }

        public String getQuestion()

        {
            return question;
        }

        public String getstarted_at()
        {
            return this.started_at;
        }

        public String getexpired_at()
        {
            return this.expired_at;
        }

        public void setPoll_id(String poll_id)
        {
            this.poll_id=poll_id;
        }

        public void setQuestion(String question)
        {
            this.question=question;
        }

        public void setPoll_started_at(String started_at)
        {
            this.started_at=started_at;
        }

        public void setPoll_expired_at(String expired_at)

        {
            this.expired_at=expired_at;
        }

        public Integer[] getResults(){ return this.results; }

        public void setResults(Integer[] results){ this.results=results; }

        public void setChoice(String[] choice){ this.choice=choice;}

        public String[] getChoice(){ return this.choice;}

        public interface ViewPoll{};

        @Override
        public String toString()
        {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                 e.printStackTrace();
            }
        return null;
        }
    }
