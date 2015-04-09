package generator;

import streams.auction.User;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class UserGenerator {

    private static final AtomicInteger sequence = new AtomicInteger(1);

    private UserGenerator() {
    }

    public static User generateUser() {
        User user = new User();
        user.id = sequence.getAndIncrement();
        user.name = "name" + user.id;
        user.email = user.name + "@fakemail.hu";
        user.getOverbidNotifications = new Random().nextBoolean();
        return user;
    }
}
