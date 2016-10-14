package main.model.messages;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlValue;
import java.io.Serializable;

public class Extra implements Serializable {
    private String value;
    private Attr name;

    public Extra() {
    }

    public Extra(String value, Attr attr) {
        this.value = value;
        this.name = attr;
    }

    @XmlValue
    public String getValue() {
        return value;
    }

    @XmlAttribute(name = "name")
    public Attr getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(Attr name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Extra{");
        sb.append("value='").append(value).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @XmlEnum
    public enum Attr {
        @XmlEnumValue("login") LOGIN,
        @XmlEnumValue("balance") BALANCE,
        @XmlEnumValue("password") PASS;
    }
}