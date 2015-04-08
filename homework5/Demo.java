import generator.BidGenerator;
import streams.auction.BidRequest;
import streams.auction.Product;
import streams.auction.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class Demo {

    public static void main(String[] args) throws Exception {
        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new SubmitRandomBid(), 0L, 5000L);

        Thread.sleep(60000L);
    }

    private static User getRandomUser() {
        ArrayList<User> users = new ArrayList<>(BidEngine.getUsers());
        Collections.shuffle(users);
        return users.isEmpty() ? null : users.get(0);
    }

    private static Product getRandomProduct() {
        ArrayList<Product> products = new ArrayList<>(BidEngine.getProducts());
        Collections.shuffle(products);
        return products.isEmpty() ? null : products.get(0);
    }

    private static class SubmitRandomBid extends TimerTask {

        @Override
        public void run() {
            User randomUser = getRandomUser();
            Product randomProduct = getRandomProduct();
            BidRequest bidRequest = BidGenerator.generateBidRequest(randomUser, randomProduct);
            BidEngine.bid(bidRequest);
        }
    }
}
