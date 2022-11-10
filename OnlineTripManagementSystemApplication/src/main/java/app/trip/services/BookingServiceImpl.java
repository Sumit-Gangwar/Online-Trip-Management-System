package app.trip.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.trip.exceptions.BookingException;
import app.trip.exceptions.InvalidCredentialException;
import app.trip.models.Booking;
import app.trip.models.CurrentUserLoginSession;
import app.trip.models.Packages;
import app.trip.models.SessionDTO;
import app.trip.models.Ticket;
import app.trip.models.User;
import app.trip.repository.BookingRepository;
import app.trip.repository.PackageRepository;
import app.trip.repository.SessionRepository;
import app.trip.repository.TicketRepository;
import app.trip.repository.UserRepository;

@Service
public class BookingServiceImpl implements BookingService{

	@Autowired
	PackageRepository pkgRepo;
	
	@Autowired
	SessionRepository sessionRepo;
	
	@Autowired
	BookingRepository bookRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	TicketRepository tRepo;
	
	/*
	 * Need get/setBookings 
	 */
	@Override
	public Booking makeBooking(Booking bookings, Integer ticketId,String authKey) throws BookingException,InvalidCredentialException {
		Optional<CurrentUserLoginSession> session = sessionRepo.findByAuthkey(authKey);
		if(!session.isPresent())throw new InvalidCredentialException("Please Login First...");
		Optional<User> user = userRepo.findById(session.get().getUserId());
		Optional<Ticket> ticketOpt = tRepo.findById(ticketId);

		if(ticketOpt.isPresent()) {
			bookings.setUser(user.get());
			Packages pkg = ticketOpt.get().getPackages();
			if(pkg!=null)bookings.setPackages(pkg);
			return bookRepo.save(bookings);
		}else {
			throw new BookingException("Provide valid ticket id... ");
		}	
	}
	
	
	@Override
	public Booking cancelBooking(Integer bookingsId) throws BookingException {
		Booking booking = null;
		Optional<Booking> book = bookRepo.findById(bookingsId);
		if(book.isPresent()) {
			booking = book.get();
			User user = booking.getUser();
//			for(User user:users) {
//				 user.getBookings().remove(booking);
//			}
			user.getBookings().remove(booking);
			bookRepo.delete(booking);
			return booking;
		}else {
			throw new BookingException("Booking not exists");
		}
				
	}
	
	/*
	 * entityManager.remove(group)
		for (User user : group.users) {
		     user.groups.remove(group);
		}
	 */

	@Override
	public List<Booking> viewBookings(Integer userId) throws BookingException {
		User user = null;
		Optional<User> userOpt = userRepo.findById(userId);
		if(userOpt.isPresent()) {
			user = userOpt.get();
			List<Booking> bookings = user.getBookings();
			if(bookings.isEmpty()) {
				throw new BookingException("No booking exists..");
			}
			return bookings;
		}else {
			throw new BookingException("No user exists..");
		}
		
	}


	@Override
	public List<Booking> viewAllBookings(String authKey) throws BookingException {
		Optional<CurrentUserLoginSession> currUser = sessionRepo.findByAuthkey(authKey);
		String userType = userRepo.findById(currUser.get().getUserId()).get().getUserType();
		List<Booking> bookings = null;
		if(userType.equalsIgnoreCase("user")) {
			throw new BookingException("Unauthorized Request...");
		}
		else{
			bookings = bookRepo.findAll();
			if(bookings.isEmpty()) {
				throw new BookingException("No bookings available...");
			}else {
				return bookings;
			}
		}
	}
	

}
