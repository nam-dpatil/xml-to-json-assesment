package com.lendingkart.xmltojson.service;

import com.lendingkart.xmltojson.utility.XmlToJsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XmlToJsonService {
    @Autowired
    private XmlToJsonConverter xmlToJsonConverter;

    public String convertXmlToJson(String xml) {
        try {
            return xmlToJsonConverter.convert(xml);
        } catch (Exception e) {
            throw new RuntimeException("Error converting XML to JSON", e);
        }
    }
}
