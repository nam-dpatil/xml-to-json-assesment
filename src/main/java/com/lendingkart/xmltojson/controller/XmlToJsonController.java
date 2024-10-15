package com.lendingkart.xmltojson.controller;

import com.lendingkart.xmltojson.service.XmlToJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class XmlToJsonController {
    @Autowired
    private XmlToJsonService xmlToJsonService;

    @PostMapping("/convert")
    public String convertXmlToJson(@RequestBody String xml) {
        return xmlToJsonService.convertXmlToJson(xml);
    }
}
