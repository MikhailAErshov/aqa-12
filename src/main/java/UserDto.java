public class UserDto {

    public Long id;

    public String username;

    public String firstName;

    public String lastName;

    public String email;

    public String password;

    public String phone;

    public Integer userStatus;

    public UserDto(Long id, String username, String firstName, String lastName,
                   String email, String password, String phone, Integer userStatus) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userStatus = userStatus;
    }
}
