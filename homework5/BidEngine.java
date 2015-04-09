import generator.ProductGenerator;
import generator.UserGenerator;
import streams.auction.Bid;
import streams.auction.BidRequest;
import streams.auction.Product;
import streams.auction.User;

import java.util.*;
import java.util.stream.Collectors;

public final class BidEngine {

    private static final Set<User> users = new HashSet<>();

    private static final Map<Product, List<Bid>> productsAndBids = new HashMap<>();

    static {
        // 3 bidders
        users.add(UserGenerator.generateUser());
        users.add(UserGenerator.generateUser());
        users.add(UserGenerator.generateUser());

        // 2 products
        productsAndBids.put(ProductGenerator.generateProduct(), new ArrayList<>());
        // productsAndBids.put(ProductGenerator.generateProduct(), new ArrayList<>());
    }

    public static synchronized Set<User> getUsers() {
        return users;
    }

    public static synchronized Set<Product> getProducts() {
        return productsAndBids.keySet();
    }

    public static synchronized void bid(BidRequest bidRequest) {
        System.out.println("\n\n-------- NEW BID --------");
        System.out.println("from: " + bidRequest.user.name + ", product: " + bidRequest.product.title + ", amount: " + bidRequest.amount.intValue() + ", minimalPrice: " + bidRequest.product.minimalPrice + ", reservedPrice: " + bidRequest.product.reservedPrice);

        Bid bid = new Bid(bidRequest);

        List<Bid> productRelatedBids = productsAndBids.get(bidRequest.product);
        productRelatedBids.add(bid);

        Comparator<Bid> byAmount = (bid1, bid2) -> bid1.amount.compareTo(bid2.amount);
        productsAndBids.put(bid.product, productRelatedBids
                .stream()
                .sorted(byAmount)
                .collect(Collectors.toList()));

        // notify all bidders of the product with overbidNotification flag true
        productRelatedBids.stream()
                .filter(b -> b.user.getOverbidNotifications)
                .map(b -> b.user) // a user might have many different bids but notify only once
                .collect(Collectors.toSet())
                .forEach((user) -> {
                    System.out.println(String.format("Sending mail to user '%s' about product with title '%s' because new bid has arrived", user.name, bid.product.title));
                });

        // send winning mail to bidder if bid is greater or equal to reserved price
        if (bid.amount.compareTo(bid.product.reservedPrice) >= 0) {
            System.out.println(String.format("Sending WINNING mail to user '%s' about product with title '%s'", bid.user.name, bid.product.title));
        }

        if (bid.amount.compareTo(bid.product.minimalPrice) == -1) {
            System.out.println(String.format("Sending SORRY mail to user '%s' about product with title '%s'", bid.user.name, bid.product.title));
        }
    }
}
