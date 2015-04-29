package com.farata.course.mwd.auction.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import xml.LocalDateTimeXmlAdapter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

// tornaia: ignore unknown properties during de-serialization: on client side we have (framework related) properties on product object like 'route', 'restangularized', and 'reqParams'...
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private Integer id;
    private String title;
    @XmlElement(name = "thumb")
    private String thumbnail;
    private String url;
    private String description;
    private int quantity;   // How many items the seller has

    @XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
    private LocalDateTime auctionEndTime;
    private BigDecimal minimalPrice;     // Don't sell unless the bid is more than min price
    private BigDecimal reservedPrice;   // If a bidder offers reserved price, the auction is closed
    private String timeleft;
    private int watchers;

    public Product() {
        // jackson
    }

    public Product(Integer id, String title, String thumbnail, String url, String description,
                   int quantity,
                   LocalDateTime auctionEndTime, BigDecimal minimalPrice, BigDecimal reservedPrice,
                   String timeleft, int watchers) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.url = url;
        this.description = description;
        this.quantity = quantity;
        this.auctionEndTime = auctionEndTime;
        this.minimalPrice = minimalPrice;
        this.reservedPrice = reservedPrice;
        this.timeleft = timeleft;
        this.watchers = watchers;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", thumbnail='").append(thumbnail).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", auctionEndTime=").append(auctionEndTime);
        sb.append(", minimalPrice=").append(minimalPrice);
        sb.append(", reservedPrice=").append(reservedPrice);
        sb.append(", timeleft='").append(timeleft).append('\'');
        sb.append(", watchers=").append(watchers);
        return sb.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(String timeleft) {
        this.timeleft = timeleft;
    }

    public BigDecimal getMinimalPrice() {
        return minimalPrice;
    }

    public BigDecimal getReservedPrice() {
        return reservedPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    public JsonObject getJsonObject(List<Bid> amountSortedBidsOfThisProduct) {
        boolean hasAtLeastOneBid = !amountSortedBidsOfThisProduct.isEmpty();
        return Json.createObjectBuilder()
                .add("id", id)
                .add("title", title)
                .add("thumb", thumbnail)
                .add("url", url)
                .add("description", description)
                .add("quantity", quantity)
                .add("auctionEndTime", auctionEndTime.format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .add("minimalPrice", minimalPrice)
                .add("reservedPrice", reservedPrice)
                .add("timeleft", timeleft)
                .add("watchers", watchers)
                .add("bidStatus", hasAtLeastOneBid ? "ongoing" : "no bid yet")
                .add("topBidAmount", hasAtLeastOneBid ? amountSortedBidsOfThisProduct.get(0).getAmount().toPlainString() : "-")
                .add("topBidderName", hasAtLeastOneBid ? amountSortedBidsOfThisProduct.get(0).getUser().getName() : "")
                .add("numberOfBids", amountSortedBidsOfThisProduct.size())
                .build();
    }

    public JsonObject getJsonObject() {
        return getJsonObject(Collections.emptyList());
    }

    @XmlTransient
    public static Product fromJson(String json) {
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObject = reader.readObject();

        return fromJsonObject(jsonObject);

    }

    @XmlTransient
    public static Product fromJsonObject(JsonObject jsonObject) {
        // date format 2015-01-23T13:52:46.850 yyyy-MM-ddThh:mm:ss
        LocalDateTime endDateTime = LocalDateTime
                .parse(jsonObject.getString("auctionEndTime"),
                        DateTimeFormatter.ofPattern(DATE_PATTERN));
        return new Product(jsonObject.getInt("id"), jsonObject.getString("title"),
                jsonObject.getString("thumb"), jsonObject.getString("url"),
                jsonObject.getString("description"), jsonObject.getInt("quantity"),
                endDateTime,
                jsonObject.getJsonNumber("minimalPrice").bigDecimalValue(),
                jsonObject.getJsonNumber("reservedPrice").bigDecimalValue(),
                jsonObject.getString("timeleft"), jsonObject.getInt("watchers"));
    }


    public int hashCode() {
        return id;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Product)) {
            return false;
        }

        Product other = (Product) o;

        return Objects.equals(this.id, other.id);
    }
}
