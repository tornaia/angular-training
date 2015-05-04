package streams.auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid {

    public final int id;
    public final Product product;
    public final BigDecimal amount;
    public final int desiredQuantity; // How many items the user wants
    public final User user;
    public final LocalDateTime bidTime;
    public final boolean isWinning;

    public Bid(BidRequest bidRequest) {
        this.id = -1;
        this.product = bidRequest.product;
        this.amount = bidRequest.amount;
        this.desiredQuantity = bidRequest.desiredQuantity;
        this.user = bidRequest.user;
        this.bidTime = LocalDateTime.now();
        this.isWinning = false;
    }
}
