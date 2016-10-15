package main.model.messages;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.NONE)
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private Types type;

    private List<Extra> extra;

    public Request() {
        extra = new ArrayList<>();
    }

    public Request(Types type, Extra ...extras) {
        this();
        this.type = type;
        Collections.addAll(this.extra, extras);
    }

    @XmlElement(name = "request-type")
    public Types getType() {
        return type;
    }

    @XmlElement(name = "extra")
    public List<Extra> getExtra() {
        return extra;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public void setExtra(List<Extra> extra) {
        this.extra = extra;
    }

    public String getValue(Extra.Attr name) {
        for (Extra e : extra)
            if (e.getName().equals(name))
                return e.getValue();
        return null;
    }

    @XmlEnum
    public enum Types {
        @XmlEnumValue("CREATE-AGT")CREATE_AGT,
        @XmlEnumValue("GET-BALANCE")GET_BALANCE;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Request{");
        sb.append("type=").append(type);
        sb.append(", extra=").append(extra);
        sb.append('}');
        return sb.toString();
    }
}
