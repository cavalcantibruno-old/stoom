package br.com.stoom.api.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.stoom.api.response.Response;
import br.com.stoom.domain.model.Endereco;
import br.com.stoom.domain.model.geocode.GeocodeResult;
import br.com.stoom.domain.service.EnderecoService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

@RestController
@RequestMapping("/endereco")
@CrossOrigin(origins = "*")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;
	
	@GetMapping
	public List<Endereco> findAll() {
		return this.enderecoService.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response<Endereco>> create(@Valid @RequestBody Endereco endereco) {
		
		Response<Endereco> response = new Response<Endereco>();
		
		if(endereco.getLongitude() != null) {
			
			Endereco enderecoPersistedthis = (Endereco) this.enderecoService.createOrUpdate(endereco);	
			response.setData(enderecoPersistedthis);
			
		} else { 
			String addressForQuery = endereco.getNumber() + " " + endereco.getStreetName() + ", " + 
										endereco.getNeighbourhood() + ", " + endereco.getState();
			try {
				
				GeocodeResult geocodeResult = getGeocode(addressForQuery);
				
				String latitude = geocodeResult.getResults().stream()
						.map(info -> info.getGeometry().getGeocodeLocation().getLatitude())
						.collect(Collectors.joining());
				
				String longitude = 	geocodeResult.getResults().stream()
						.map(info -> info.getGeometry().getGeocodeLocation().getLongitude())
						.collect(Collectors.joining());
				
				endereco.setLatitude(latitude);
				endereco.setLongitude(longitude);
				
				Endereco enderecoPersistedthis = (Endereco) this.enderecoService.createOrUpdate(endereco);	
				response.setData(enderecoPersistedthis);
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.badRequest().body(response);
			}
		}
		
		return ResponseEntity.ok(response);
		
	}
	
	@PutMapping("/{id}")
	public Endereco update(@Valid @RequestBody Endereco endereco) {
		return this.enderecoService.createOrUpdate(endereco);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		this.enderecoService.delete(id);
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
