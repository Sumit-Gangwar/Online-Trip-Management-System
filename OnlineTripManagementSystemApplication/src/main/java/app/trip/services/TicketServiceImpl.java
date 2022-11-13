package app.trip.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.trip.exceptions.InvalidCredentialException;
import app.trip.exceptions.InvalidRouteException;
import app.trip.exceptions.InvalidTicketException;
import app.trip.exceptions.PackageException;
import app.trip.models.Bus;
import app.trip.models.CurrentUserLoginSession;
import app.trip.models.Packages;
import app.trip.models.Ticket;
import app.trip.models.User;
import app.trip.repository.BusRepository;
import app.trip.repository.PackageRepository;
import app.trip.repository.RouteRepository;
import app.trip.repository.SessionRepository;
import app.trip.repository.TicketRepository;
import app.trip.repository.UserRepository;


@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	TicketRepository ticketRepo;

	@Autowired
	PackageRepository pkgRepo;

	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RouteRepository routeRepo;
	
	@Autowired
	BusRepository busRepo;

	@Override
	public List<Ticket> getAllTickets(Integer packageId,String authKey) throws InvalidTicketException {
		List<Ticket> tickets = null;
		
		Optional<CurrentUserLoginSession> culs = sessionRepo.findByAuthkey(authKey);
		
		String userType = userRepo.findById(culs.get().getUserId()).get().getUserType();
		System.out.println(packageId);		
		if(userType.equalsIgnoreCase("user") && packageId != null) {
			Optional<Packages> pkg = pkgRepo.findById(packageId);
			Packages packages = pkg.get();
			tickets = ticketRepo.findByPackages(packages);
		} 
		else if(userType.equalsIgnoreCase("admin") && packageId == null) {
			tickets = ticketRepo.findAll();
		}
			else if(userType.equalsIgnoreCase("admin") && packageId != null) {
			Optional<Packages> pkg = pkgRepo.findById(packageId);
			Packages packages = pkg.get();
			tickets = ticketRepo.findByPackages(packages);
		}
		if(tickets.isEmpty() || tickets == null) {
			throw new InvalidTicketException("Tickets Not Available.");
		}
		
		return tickets;
	}
	
	@Override
	public Ticket getTicket(Integer ticketId) throws InvalidTicketException {
		
		Optional<Ticket> tkt = ticketRepo.findById(ticketId);
		
		if(tkt.isPresent() == false) {
			throw new InvalidTicketException("No such ticket exists.");
		}
		
		return tkt.get();
	}
	
	@Override
	public Ticket createTicket(Ticket ticket,String authKey, Integer packageId, Integer busId) throws InvalidTicketException, PackageException, InvalidRouteException, InvalidCredentialException  {
		Optional<CurrentUserLoginSession> culs = sessionRepo.findByAuthkey(authKey);
		if(!culs.isPresent()) {
			throw new InvalidCredentialException("Please Login First...");
		}
		Optional<User> user = userRepo.findById(culs.get().getUserId());
		Optional<Packages> pkgOpt = pkgRepo.findById(packageId);
		if(pkgOpt.isPresent()) {
			ticket.setPackages(pkgOpt.get());
		}
		Optional<Bus> busOpt = busRepo.findById(busId);
		if(!busOpt.isPresent()) {
			throw new InvalidRouteException("Bus with bus id = "+busId+" does not exist.");
		}
		

		ticket.setBus(busOpt.get());
		
		Ticket ticketCreated  = ticketRepo.save(ticket);
		
		return ticketCreated;
	}

	
	@Override
	public Ticket deleteTicket(Integer ticketId) throws InvalidTicketException {
		Ticket ticket = null;
		
		Optional<Ticket> user = ticketRepo.findById(ticketId);		
		
		if(user.isPresent()) {
			ticket = user.get();
			ticket.setTicketStatus(false);
			ticketRepo.save(ticket);
		} else {
			throw new InvalidTicketException("Ticket does not exist....");			
		}
		return ticket;
	}
}
