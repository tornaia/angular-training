package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.entity.Bid;
import com.farata.course.mwd.auction.entity.Product;

import javax.inject.Inject;
import javax.json.Json;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@Path("/bid")
@Produces("application/json")
public class BidService {

    private static final Logger LOG = Logger.getLogger(BidService.class.getName());

    // Set up all the default values
    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/test";
    private static final String DEFAULT_MESSAGE_COUNT = "57";
    private static final String DEFAULT_USERNAME = "quickstartUser";
    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";

    @Inject
    private DataEngine dataEngine;

    // tornaia: Error injecting resource into CDI managed bean. Can't find a resource
    // @Resource(lookup ="java:/ConnectionFactory")
    // ConnectionFactory connectionFactory;

    // tornaia: Error injecting resource into CDI managed bean. Can't find a resource named queue/test defined on javax.jms.Queue com.farata.course.mwd.auction.service.BidService.testQueue
    // @Resource(lookup = "queue/test")
    // Queue testQueue;

    // FIXME this implementation is not thread safe (using not thread-safe collections, etc) but in this pilot project maybe we just dont care...
    @POST
    public Response placeBid(@Valid Bid bid) {
        // tornaia: at the moment there is no securityContext or anything like that so just inject a randomly picked up user
        bid.setUser(dataEngine.getRandomUser());
        // tornaia: user might send some hacked data...
        bid.setProduct(dataEngine.findProductById(bid.getProduct().getId()));

        LOG.info("New Bid from: " + bid.getUser().getName() + ", product: " + bid.getProduct().getTitle() + ", amount: " + bid.getAmount().intValue() + ", minimalPrice: " + bid.getProduct().getMinimalPrice() + ", reservedPrice: " + bid.getProduct().getReservedPrice());

        if (isPriceBelowMinimal(bid)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(Json.createObjectBuilder().add("errorMsg", "Amount is below minimal price. Bid is rejected").build()).build();
        } else if (isPriceHigherThanReservedPrice(bid)) {
            return Response.ok().entity(Json.createObjectBuilder().add("purchased", "true").build()).build();
        }

        dataEngine.placeBid(bid);
        List<Bid> productRelatedBidsSortedByAmount = dataEngine.findBidsByProductSortedByAmount(bid.getProduct());
        Bid topBidByProduct = dataEngine.findTopBidByProduct(bid.getProduct()).get();

        return Response.ok(topBidByProduct.getProduct().getJsonObject(productRelatedBidsSortedByAmount)).build();
    }

    @GET
    @Path("/{id}/")
    public Bid getBid(@PathParam("id") int id, @Context HttpHeaders headers) {
        return new Bid(id, new BigDecimal(42));
    }

    private boolean isPriceBelowMinimal(Bid bid) {
        Product product = dataEngine.findProductById(bid.getProduct().getId());
        BigDecimal amount = bid.getAmount();
        BigDecimal minimalPrice = product.getMinimalPrice();
        return amount.compareTo(minimalPrice) < 0;
    }

    private boolean isPriceHigherThanReservedPrice(Bid bid) {
        Product product = dataEngine.findProductById(bid.getProduct().getId());
        BigDecimal amount = bid.getAmount();
        BigDecimal minimalPrice = product.getReservedPrice();
        return amount.compareTo(minimalPrice) > 0;
    }
}
