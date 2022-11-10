package app.trip.services;

import java.util.List;

import app.trip.exceptions.BusException;
import app.trip.exceptions.InvalidRouteException;
import app.trip.exceptions.TravelException;
import app.trip.models.Bus;

public interface BusService {
	
	public Bus addBus(Bus bus,Integer travelId,String authKey) throws TravelException,InvalidRouteException;
	
	public List<Bus> getAllBusFromAllAgency() throws BusException;

}
