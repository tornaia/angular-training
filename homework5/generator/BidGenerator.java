package generator;

import streams.auction.BidRequest;
import streams.auction.Product;
import streams.auction.User;

import java.math.BigDecimal;
import java.util.Random;

public final class BidGenerator {

    private BidGenerator() {
    }

    public static BidRequest generateBidRequest(User user, Product product) {
        BigDecimal amount = new BigDecimal(new Random().nextInt(3000));
        int desiredQuantity = new Random().nextInt(ProductGenerator.MAX_QUANTITY_PER_ITEM + 5) + 1;
        return new BidRequest(user, product, amount, desiredQuantity);
    }
}
