package com.farata.course.mwd.auction.service;

import com.farata.course.mwd.auction.data.DataEngine;
import com.farata.course.mwd.auction.entity.Bid;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/bid")
@Produces("application/json")
public class BidService {

    @Inject
    private DataEngine dataEngine;

    @Resource(lookup = "java:/ConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(lookup = "queue/incomingbids")
    private Destination incomingbidsQueue;

    @POST
    public Response placeBid(@Valid Bid bidRequest, @Context HttpHeaders headers) {
        // tornaia: its not secure i know
        int userId = Integer.parseInt(headers.getRequestHeader("userId").get(0));
        bidRequest.setUser(dataEngine.getUser(userId));

        // tornaia: user might send some hacked data... see prev comment
        bidRequest.setProduct(dataEngine.findProductById(bidRequest.getProduct().getId()));

        connectionFactory.createContext().createProducer().send(incomingbidsQueue, bidRequest);

        return Response.ok().build();
    }

    @GET
    @Path("/{id}/")
    public Bid getBid(@PathParam("id") int id, @Context HttpHeaders headers) {
        return new Bid(id, new BigDecimal(42));
    }
}
