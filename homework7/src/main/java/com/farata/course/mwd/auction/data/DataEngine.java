package com.farata.course.mwd.auction.data;

import com.farata.course.mwd.auction.entity.Bid;
import com.farata.course.mwd.auction.entity.BidConfirmation;
import com.farata.course.mwd.auction.entity.Product;
import com.farata.course.mwd.auction.entity.User;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Singleton;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.json.Json;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public class DataEngine {

    private static final Logger LOG = Logger.getLogger(DataEngine.class.getName());

    private static final Comparator<Bid> byAmountComparator = (bid1, bid2) -> bid2.getAmount().compareTo(bid1.getAmount());

    @Resource(lookup = "java:/ConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(lookup = "queue/bidconfirmations")
    private Destination bidconfirmationsQueue;

    private final List<Product> productsList = new ArrayList<>();
    private final Map<Product, List<Bid>> productsAndBids = new HashMap<>();
    private final AtomicInteger bidderIdsequenceGenerator = new AtomicInteger();
    private final Set<User> bidders = new HashSet<>();

    @PostConstruct
    void init() {
        initProducts();
    }

    public List<Product> findAllProducts() {
        return Collections.unmodifiableList(productsList);
    }

    public List<Product> findAllFeaturedProducts() {
        return Collections.unmodifiableList(productsList);
    }

    public Product findProductById(int id) {
        Product result = null;
        for (Product product : productsList) {
            if (product.getId().compareTo(id) == 0) {
                result = product;
            }
        }
        return result;
    }

    public void placeBid(Bid bidRequest) {
        LOG.info("New Bid from: " + bidRequest.getUser().getName() + ", product: " + bidRequest.getProduct().getTitle() + ", amount: " + bidRequest.getAmount().intValue() + ", minimalPrice: " + bidRequest.getProduct().getMinimalPrice() + ", reservedPrice: " + bidRequest.getProduct().getReservedPrice());

        if (isPriceBelowMinimal(bidRequest)) {
            confirmBid(BidConfirmation.onlyToBidder(bidRequest.getUser(), Json.createObjectBuilder().add("errorMsg", "Amount is below minimal price. Bid is rejected").build().toString()));
            return;
        } else if (isPriceHigherThanReservedPrice(bidRequest)) {
            confirmBid(BidConfirmation.onlyToBidder(bidRequest.getUser(), Json.createObjectBuilder().add("purchased", "true").build().toString()));
            return;
        }

        List<Bid> productRelatedBids = productsAndBids.get(bidRequest.getProduct());
        productRelatedBids.add(bidRequest);

        productsAndBids.put(bidRequest.getProduct(), productRelatedBids
                .stream()
                .sorted(byAmountComparator)
                .collect(Collectors.toList()));

        List<Bid> productRelatedBidsSortedByAmount = findBidsByProductSortedByAmount(bidRequest.getProduct());
        Bid topBidByProduct = findTopBidByProduct(bidRequest.getProduct()).get();
        confirmBid(BidConfirmation.onlyToBidder(bidRequest.getUser(), Json.createObjectBuilder().add("bid", "added").build().toString()));

        confirmBid(BidConfirmation.toEveryone(topBidByProduct.getProduct().getJsonObject(productRelatedBidsSortedByAmount).toString()));
    }

    private void confirmBid(BidConfirmation bidConfirmationt) {
        connectionFactory.createContext().createProducer().send(bidconfirmationsQueue, bidConfirmationt);
    }

    public Optional<Bid> findTopBidByProduct(Product product) {
        Optional<Bid> topPriceBid = productsAndBids.get(product)
                .stream()
                .sorted(byAmountComparator)
                .findFirst();
        return topPriceBid;
    }

    public List<Bid> findBidsByProductSortedByAmount(Product product) {
        return productsAndBids.get(product)
                .stream()
                .sorted(byAmountComparator)
                .collect(Collectors.toList());
    }

    public User createUser() {
        int id = bidderIdsequenceGenerator.getAndIncrement();
        User bidder = new User(id, "User" + id + "Name", "user" + id + "@email.com", true);
        bidders.add(bidder);
        return bidder;
    }

    public User getUser(int userId) {
        Optional<User> user = bidders
                .stream()
                .filter(u -> Objects.equals(userId, u.getId()))
                .findFirst();

        if (!user.isPresent()) {
            throw new RuntimeException("Unknown user: " + userId);
        }
        return user.get();
    }

    private void initProducts() {
        // Populate a collection of dummy products
        Random random = new Random();
        for (int i = 1; i <= 6; i++) {
            Product product = new Product(i, "Item " + i, "images/0" + i + ".jpg",
                    "",
                    "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore adipiscing elit. Ut enim.",
                    2,
                    LocalDateTime.now().plusDays(random.nextInt(10)),
                    new BigDecimal(12),
                    new BigDecimal(35),
                    "123", 5
            );
            productsList.add(product);

            productsAndBids.put(product, new ArrayList<>());
        }
    }

    private boolean isPriceBelowMinimal(Bid bid) {
        Product product = findProductById(bid.getProduct().getId());
        BigDecimal amount = bid.getAmount();
        BigDecimal minimalPrice = product.getMinimalPrice();
        return amount.compareTo(minimalPrice) < 0;
    }

    private boolean isPriceHigherThanReservedPrice(Bid bid) {
        Product product = findProductById(bid.getProduct().getId());
        BigDecimal amount = bid.getAmount();
        BigDecimal minimalPrice = product.getReservedPrice();
        return amount.compareTo(minimalPrice) > 0;
    }
}