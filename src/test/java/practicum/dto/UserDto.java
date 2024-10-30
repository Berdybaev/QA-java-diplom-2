package practicum.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private String email;
    private String password;
    private String name;

    public UserDto(String email, String password){
        this.email = email;
        this.password = password;
    }


}
