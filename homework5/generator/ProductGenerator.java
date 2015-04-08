package generator;

import streams.auction.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

public final class ProductGenerator {

    public static final int MINIMAL_PRICE_TOP_LIMIT = 1000;
    public static final int RESERVED_PRICE_OFFSET = 1000;
    public static final int MAX_QUANTITY_PER_ITEM = 10;

    private ProductGenerator() {
    }

    public static Product generateProduct() {
        Product product = new Product();
        product.id = -1;
        product.title = "title" + product.id;
        product.thumb = null;
        product.description = null;
        product.quantity = -1;
        product.auctionEndTime = LocalDateTime.MAX;
        product.watchers = -1;
        product.minimalPrice = new BigDecimal(new Random().nextInt(MINIMAL_PRICE_TOP_LIMIT) + 1);
        product.reservedPrice = new BigDecimal(new Random().nextInt(RESERVED_PRICE_OFFSET + 1)).add(product.minimalPrice);
        return product;
    }
}
