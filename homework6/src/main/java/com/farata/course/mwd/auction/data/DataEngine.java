package com.farata.course.mwd.auction.data;

import com.farata.course.mwd.auction.entity.Bid;
import com.farata.course.mwd.auction.entity.Product;
import com.farata.course.mwd.auction.entity.User;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public class DataEngine {

    private static final Logger LOG = Logger.getLogger(DataEngine.class.getName());

    private static final Comparator<Bid> byAmountComparator = (bid1, bid2) -> bid2.getAmount().compareTo(bid1.getAmount());

    private final List<Product> productsList = new ArrayList<>();
    private final Map<Product, List<Bid>> productsAndBids = new HashMap<>();
    private final Set<User> users = new HashSet<>();

    @PostConstruct
    void init() {
        initProducts();
        initUsers();
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
        LOG.info("Store new bid in engine");
        
        List<Bid> productRelatedBids = productsAndBids.get(bidRequest.getProduct());
        productRelatedBids.add(bidRequest);

        productsAndBids.put(bidRequest.getProduct(), productRelatedBids
                .stream()
                .sorted(byAmountComparator)
                .collect(Collectors.toList()));
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

    // tornaia: we need this since there is no security solution at the moment
    public User getRandomUser() {
        List<User> randomUserList = new ArrayList<>(this.users);
        Collections.shuffle(randomUserList);
        return randomUserList.get(0);
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

    private void initUsers() {
        users.add(new User(100, "User100Name", "user100@email.com", true));
        users.add(new User(200, "User200Name", "user200@email.com", true));
    }
}