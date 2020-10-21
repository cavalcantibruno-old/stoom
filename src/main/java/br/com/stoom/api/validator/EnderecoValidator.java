package br.com.stoom.api.validator;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.stoom.domain.model.Endereco;
import br.com.stoom.domain.model.geocode.GeocodeResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class EnderecoValidator {

	public String getLatitude(GeocodeResult geocodeResult) {
		String latitude = geocodeResult.getResults().stream()
			.map(info -> info.getGeometry().getGeocodeLocation().getLatitude())
			.collect(Collectors.joining());
		return latitude;
	}

	public String getLongitude(GeocodeResult geocodeResult) {

		String longitude = 	geocodeResult.getResults().stream()
				.map(info -> info.getGeometry().getGeocodeLocation().getLongitude())
				.collect(Collectors.joining());
		
		return longitude;
	}
	
	public void validateUpdateAddress(@Valid Endereco endereco, BindingResult result) {
		if(endereco.getId() == null) {
			result.addError(new ObjectError("Address", "Id no information"));
		}
	}
	
	public GeocodeResult getGeocode(String address) throws IOException {
		OkHttpClient client = new OkHttpClient();
	    String encodedAddress = URLEncoder.encode(address, "UTF-8");
	    Request request = new Request.Builder()
	    		.url("https://maps.googleapis.com/maps/api/geocode/json?address=" + 
	    				encodedAddress + 
	    				"&key=AIzaSyDTK0igIQTCi5EYKL9tzOIJ9N6FUASGZos")
	            .get()
	            .addHeader("x-rapidapi-host", "maps.googleapis.com")
	            .build();
	    ResponseBody responseBody = client.newCall(request).execute().body();	 

	    ObjectMapper objectMapper = new ObjectMapper();
	    GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);
	    
	    return result;
	}
}
