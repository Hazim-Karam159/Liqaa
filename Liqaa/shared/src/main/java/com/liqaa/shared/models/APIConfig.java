package com.liqaa.shared.models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class APIConfig {
    private String url;
    private String key;

    @XmlElement
    public String getUrl() { return url; }

    @XmlElement
    public String getKey() { return key; }

    public void setUrl(String url) { this.url = url; }
    public void setKey(String key) { this.key = key; }
}