package praktikum.constants;

public class UserConstants {

    public static final String USER_NAME = "User";
    public static final String USER_EMAIL = "@yandex.ru";
    public static final String USER_PASSWORD = "pass";

    public static final String EMPTY_USER_NAME = "";
    public static final String EMPTY_USER_EMAIL = "";
    public static final String EMPTY_USER_PASSWORD = "";

    public static final String USER_ALREADY_EXISTS_MESSAGE = "User already exists";
    public static final String MISSING_REQUIRED_FIELDS_MESSAGE = "Email, password and name are required fields";
    public static final String INCORRECT_EMAIL_OR_PASSWORD_MESSAGE = "email or password are incorrect";
    public static final String YOU_SHOULD_BE_AUTHORIZED_MESSAGE = "You should be authorised";
    public static final String USER_WITH_SUCH_EMAIL_EXISTS_MESSAGE = "User with such email already exists";

    public static final String CREATE_USER_PATH = "/api/auth/register";
    public static final String LOGIN_USER_PATH = "/api/auth/login";
    public static final String CHANGE_USER_DATA_PATH = "/api/auth/user";
    public static final String DELETE_USER_PATH = "/api/auth/user";
}
