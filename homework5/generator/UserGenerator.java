package generator;

import streams.auction.User;

import java.util.Random;

public final class UserGenerator {

    private UserGenerator() {
    }

    public static User generateUser() {
        User user = new User();
        user.id = -1;
        user.name = "name" + user.id;
        user.email = user.name + "@fakemail.hu";
        user.getOverbidNotifications = new Random().nextBoolean();
        return user;
    }
}
