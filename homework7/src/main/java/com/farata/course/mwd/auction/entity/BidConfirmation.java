package com.farata.course.mwd.auction.entity;

import java.io.Serializable;

public class BidConfirmation implements Serializable {

    public final User user;

    public final String content;

    private BidConfirmation(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public static BidConfirmation onlyToBidder(User user, String content) {
        return new BidConfirmation(user, content);
    }

    public static BidConfirmation toEveryone(String content) {
        return new BidConfirmation(null, content);
    }
}
