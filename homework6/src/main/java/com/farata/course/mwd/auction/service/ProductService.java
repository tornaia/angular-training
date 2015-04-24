package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.entity.Bid;
import com.farata.course.mwd.auction.entity.Product;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/products")
@Produces("application/json")
public class ProductService {

    private static final Logger LOG = Logger.getLogger(ProductService.class.getName());

    @Inject
    private DataEngine dataEngine;

    @GET
    public List<Product> getAllProducts() {
        return dataEngine.findAllProducts();
    }

    @GET
    @Path("/featured")
    public Response getFeaturedProducts() {
        final JsonObjectBuilder jsonResult = itemsWithHeading("Featured products");
        return Response.ok(jsonResult.build()).build();
    }

    @GET
    @Path("/search")
    public Response getSearchResults() {
        final JsonObjectBuilder jsonResult = itemsWithHeading("Search results");
        return Response.ok(jsonResult.build()).build();
    }

    @GET
    @Path("/{id}/")
    public Response getProductById(@PathParam("id") int productId, @Context HttpHeaders headers) {
        LOG.info("Fetch product with id: " + productId);
        Product product = dataEngine.findProductById(productId);

        if (product != null) {
            List<Bid> productRelatedBidsSortedByAmount = dataEngine.findBidsByProductSortedByAmount(product);
            return Response.ok(product.getJsonObject(productRelatedBidsSortedByAmount)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private JsonObjectBuilder itemsWithHeading(String heading) {
        JsonArrayBuilder itemsBuilder = Json.createArrayBuilder();

        dataEngine.findAllFeaturedProducts()
                .forEach(product -> {
                    itemsBuilder.add(product.getJsonObject());
                });

        return Json.createObjectBuilder()
                .add("heading", heading)
                .add("items", itemsBuilder);
    }
}