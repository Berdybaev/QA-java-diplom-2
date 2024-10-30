package practicum.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {
    private String success;
    private UserDto user;
    private String accessToken;
    private String refreshToken;
    private String message;

    private UserAuthDto(String success, UserDto user, String accessToken, String refreshToken){
        this.success = success;
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserAuthDto(String success, String message){
        this.success = success;
        this.message = message;
    }


}
