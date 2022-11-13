package app.trip.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import app.trip.exceptions.BusException;
import app.trip.exceptions.InvalidRouteException;
import app.trip.exceptions.TravelException;
import app.trip.models.Bus;
import app.trip.services.BusService;

@RestController
@CrossOrigin(origins = "*")
public class BusController {
	
	@Autowired
	private BusService bService;
	
	@GetMapping("/busesAll")
	public ResponseEntity<List<Bus>> serchTravle() throws TravelException, BusException{
		
		List<Bus> busList=bService.getAllBusFromAllAgency();
	
		return new ResponseEntity<>(busList,HttpStatus.CREATED);
	
	}
	
	@PostMapping("/busesAdd/{tid}/{key}")
	public ResponseEntity<Bus> addTravle(@RequestBody @Valid Bus bus,@PathVariable("tid") Integer travelId, @PathVariable("key") String authKey) throws TravelException, InvalidRouteException{
		
		Bus buses=bService.addBus(bus, travelId,authKey);
	
		return new ResponseEntity<>(buses,HttpStatus.CREATED);
	
	}

}
