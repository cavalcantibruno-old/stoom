package br.com.stoom.api.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

import br.com.stoom.api.response.Response;
import br.com.stoom.api.validator.EnderecoValidator;
import br.com.stoom.domain.model.Endereco;
import br.com.stoom.domain.model.geocode.GeocodeResult;
import br.com.stoom.domain.service.EnderecoService;

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
				EnderecoValidator validator = new EnderecoValidator();
				
				GeocodeResult geocodeResult = validator.getGeocode(addressForQuery);
				String latitude = validator.getLatitude(geocodeResult);
				String longitude = validator.getLongitude(geocodeResult);
								
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

	@PutMapping
	public ResponseEntity<Response<Endereco>> update(@Valid @RequestBody Endereco endereco, BindingResult result) {
		Response<Endereco> response = new Response<Endereco>();
		
		try {
			EnderecoValidator validator = new EnderecoValidator();
			validator.validateUpdateAddress(endereco, result);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Endereco addressPersisted = (Endereco) enderecoService.createOrUpdate(endereco);
			response.setData(addressPersisted);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		this.enderecoService.delete(id);
	}
			
}
