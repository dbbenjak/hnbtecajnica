package com.example.hnb;

import java.io.IOException;

import com.example.hnb.POJO.Exchange;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;


//custom deserializer for JSON parsing
@SuppressWarnings("serial")
public class CustomDeserializer extends StdDeserializer<Exchange> {

	//constructor initialization for custom deserializer 
	
	public CustomDeserializer() { 
        this(null); 
    } 

    public CustomDeserializer(Class<?> vc) { 
        super(vc); 
    }

    //deserialization method which returns an Exchange object that corresponds to one currency
    @Override
    public Exchange deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp); //JSON nodes used to easily deserialize input JSON for use with custom variables

        //custom variables that correspond to naming conventions of the input JSON
        String exchangeRate = node.get("Broj tečajnice").asText(); 
        String date = node.get("Datum primjene").asText();
        String country = node.get("Država").asText();
        String currencyCode = node.get("Šifra valute").asText();
        String currencyName = node.get("Valuta").asText();
        String unit = node.get("Jedinica").asText();
        String buyValue = node.get("Kupovni za devize").asText();
        String meanValue = node.get("Srednji za devize").asText();
        String sellValue = node.get("Prodajni za devize").asText();
        
        return new Exchange(exchangeRate, date, country, currencyCode, currencyName, unit, buyValue, meanValue, sellValue);
    }
	
}
