package com.lendingkart.xmltojson.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class XmlToJsonConverter {

    private static final Logger logger = LoggerFactory.getLogger(XmlToJsonConverter.class);

    public String convert(String xml) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        ObjectMapper jsonMapper = new ObjectMapper();

        JsonNode rootNode = xmlMapper.readTree(xml);

        int totalMatchScore = 0;

        JsonNode matchDetailsNode = rootNode.at("/Response/ResultBlock/MatchDetails/Match");
        ArrayNode matchDetailsArray = jsonMapper.createArrayNode();

        if (matchDetailsNode.isArray()) {
            for (JsonNode matchNode : matchDetailsNode) {
                int score = matchNode.get("Score").asInt();
                totalMatchScore += score;

                ObjectNode matchObject = jsonMapper.createObjectNode();
                matchObject.putObject("Match")
                        .put("Entity", matchNode.get("Entity").asText())
                        .put("MatchType", matchNode.get("MatchType").asText())
                        .put("Score", matchNode.get("Score").asText());

                matchDetailsArray.add(matchObject);
            }
        }

        JsonNode resultBlockNode = rootNode.at("/Response/ResultBlock");
        if (resultBlockNode.isObject()) {
            ((ObjectNode) resultBlockNode).putObject("MatchSummary")
                    .put("TotalMatchScore", String.valueOf(totalMatchScore));

            ((ObjectNode) resultBlockNode).set("MatchDetails", matchDetailsArray);
        }

        JsonNode valuesNode = rootNode.at("/Response/ResultBlock/ErrorWarnings/Warnings/Warning/Values/Value");
        if (valuesNode.isArray()) {
            ArrayNode valuesArray = jsonMapper.createArrayNode();
            for (JsonNode value : valuesNode) {
                valuesArray.add(value.asText());
            }
            JsonNode warningValuesNode = rootNode.at("/Response/ResultBlock/ErrorWarnings/Warnings/Warning/Values");
            if (warningValuesNode.isObject()) {
                ((ObjectNode) warningValuesNode).set("Value", valuesArray);
            }
        }

        logger.info("Successfully converted XML to JSON with custom structure");

        return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
