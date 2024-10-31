package praktikum.user;

import org.apache.commons.lang3.RandomStringUtils;

import static praktikum.constants.UserConstants.*;

public class UserRandomData {

    public static String userRandomName(){
        return (USER_NAME + RandomStringUtils.randomAlphanumeric(5, 10));
    }

    public static String userRandomEmail(){
        return (RandomStringUtils.randomAlphanumeric(3, 10) + USER_EMAIL);
    }

    public static String userRandomPassword(){
        return (USER_PASSWORD + RandomStringUtils.randomAlphanumeric(2, 5));
    }
}
