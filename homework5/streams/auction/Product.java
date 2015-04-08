package streams.auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {

    public int id;
    public String title;
    public String thumb;
    public String description;
    public int quantity;   // How many items the seller has
    public LocalDateTime auctionEndTime;
    public int watchers;
    public BigDecimal minimalPrice;     // Don't sell unless the bid is more than min price
    public BigDecimal reservedPrice;   // If a bidder offers reserved price, the auction is closed

    public int hashCode() {
        return id;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Product))
            return false;

        if (obj == this)
            return true;

        Product rhs = (Product) obj;
        return id == rhs.id;
    }
}
