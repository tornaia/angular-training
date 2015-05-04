package xml;

import com.farata.course.mwd.auction.entity.Product;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TODO
 *
 * @since 1/21/15
 */
public class LocalDateTimeXmlAdapter extends XmlAdapter<String, LocalDateTime> {
    @Override public LocalDateTime unmarshal(String v) throws Exception {

        return LocalDateTime.parse(v, DateTimeFormatter.ofPattern(Product.DATE_PATTERN));
    }

    @Override public String marshal(LocalDateTime v) throws Exception {
        return v.format(DateTimeFormatter.ofPattern(Product.DATE_PATTERN));
    }
}
