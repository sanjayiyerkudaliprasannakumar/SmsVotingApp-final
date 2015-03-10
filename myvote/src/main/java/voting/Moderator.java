package voting;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.*;


/**
 * Created by Sanjay Iyer on 2/23/2015.
 */

public class Moderator {
    private Integer id;

    @NotBlank(groups={ModeratorValidator.class},message="The name field must not be empty!!")
    private String name;

    @NotBlank(groups={EmailValidator.class, ModeratorValidator.class},message="The email field cannot be empty!!")
    private String email;

    @NotBlank(groups={EmailValidator.class, ModeratorValidator.class},message="The password field cannot be empty!!")
    private String password;
    private String created_at;



    public Moderator() {}

    public Integer getId() { return this.id; }

    public String getName(){ return this.name; }

    public String getEmail() { return this.email; }

    public String getPassword() { return this.password; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name){ this.name = name; }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreated_at(String created_at)

    {
        this.created_at=created_at;
    }

    public String getCreated_at(){ return this.created_at;}

    public interface ModeratorValidator{};
    public interface EmailValidator{};

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
