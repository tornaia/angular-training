package streams.auction;

import java.math.BigDecimal;

public final class BidRequest {

    public final Product product;
    public final BigDecimal amount;
    public final int desiredQuantity;
    public final User user;

    public BidRequest(User user, Product product, BigDecimal amount, int desiredQuantity) {
        this.user = user;
        this.product = product;
        this.amount = amount;
        this.desiredQuantity = desiredQuantity;
    }
}
