package main.model.messages;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.NONE)
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private ResultCode resultCode;

    @XmlElement(name = "extra")
    private List<Extra> extra;

    public void setResultCodeOrdinal(int resultCode) {
        this.resultCode = ResultCode.values()[resultCode];
    }

    public void setExtra(List<Extra> extra) {
        this.extra = extra;
    }

    public Response() {
        extra = new ArrayList<>();
    }

    public Response(ResultCode resultCode, Extra ...extras) {
        this();
        this.resultCode = resultCode;
        Collections.addAll(this.extra, extras);
    }

    public Extra getByAttr(Extra.Attr name) {//TODO to map?
        for (Extra e : extra)
            if (e.getName().equals(name))
                return e;
        return null;
    }

    @XmlElement(name = "result-code")
    public int getResultCodeOrdinal() {
        return resultCode.ordinal();
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Response{");
        sb.append("resultCode=").append(resultCode);
        sb.append(", extra=").append(extra);
        sb.append('}');
        return sb.toString();
    }
}