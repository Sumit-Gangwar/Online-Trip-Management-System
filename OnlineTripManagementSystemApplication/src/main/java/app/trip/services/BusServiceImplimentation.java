package app.trip.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.trip.exceptions.BusException;
import app.trip.exceptions.InvalidRouteException;
import app.trip.exceptions.TravelException;
import app.trip.models.Bus;
import app.trip.models.CurrentUserLoginSession;
import app.trip.models.Route;
import app.trip.models.Travel;
import app.trip.models.User;
import app.trip.repository.BusRepository;
import app.trip.repository.RouteRepository;
import app.trip.repository.SessionRepository;
import app.trip.repository.TravelRepository;
import app.trip.repository.UserRepository;

@Service
public class BusServiceImplimentation implements BusService{

	@Autowired
	private TravelRepository tDao;
	
	@Autowired
	private SessionRepository sRepo;
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private BusRepository bRepo;
	
	@Autowired
	private RouteRepository routeRepo;


	@Override
	public List<Bus> getAllBusFromAllAgency() throws BusException {
		List<Bus> busList=new ArrayList<>();
		List<Travel> travelList=tDao.findAll();
		
		if(travelList.size()>0) {
			busList = bRepo.findAll();
			return busList;
		}
		else
			throw new BusException("Buses are not present");
	}



	@Override
	public Bus addBus(Bus bus,Integer travelId, String authKey) throws TravelException,InvalidRouteException {

		 Bus b=null;
		    Optional<CurrentUserLoginSession> opt=sRepo.findByAuthkey(authKey);
			
		    if(!opt.isPresent()) {
		 	   throw new TravelException("Please Login first");
		    }
		    else {
		 	   CurrentUserLoginSession loginSession=opt.get();
			    	 Optional<User> optUser=uRepo.findById(loginSession.getUserId());
			    	  User user=optUser.get();
			    	  
			    	  if(user.getUserType().equals("admin")) {
			    		 Optional<Travel> getTravel= tDao.findById(travelId);
			    		 if(!getTravel.isPresent()) {
			    			 throw new TravelException("Travel Id does not match");
			    		 }
                           Travel travels=getTravel.get();
                           bus.setTDetails(travels);
                           b=bRepo.save(bus);   
			    		 
			    	  }
			    	  else {
			    		  throw new TravelException("Only Admin have to Access this.");
			    	  }	  
		    }
			return b;
	}






	

}
