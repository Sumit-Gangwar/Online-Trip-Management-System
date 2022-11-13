package app.trip.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.trip.exceptions.BookingException;
import app.trip.exceptions.InvalidCredentialException;
import app.trip.models.Booking;
import app.trip.models.BookingStatus;
import app.trip.models.CurrentUserLoginSession;
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

	@Override
	public Booking makeBooking(Booking bookings, Integer ticketId,String authKey) throws BookingException,InvalidCredentialException {
		Optional<CurrentUserLoginSession> session = sessionRepo.findByAuthkey(authKey);
		if(!session.isPresent())throw new InvalidCredentialException("Please Login First...");
		Optional<User> user = userRepo.findById(session.get().getUserId());
		Optional<Ticket> ticketOpt = tRepo.findById(ticketId);

		if(ticketOpt.isPresent()) {
			bookings.setUser(user.get());
			bookings.setTicket(ticketOpt.get());
			bookings.setStatus(BookingStatus.Booked);
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
			user.getBookings().remove(booking);
			booking.setStatus(BookingStatus.Cancelled);
			Ticket t = booking.getTicket();
			t.setTicketStatus(false);
			tRepo.save(t);
			return bookRepo.save(booking);
		}else {
			throw new BookingException("Booking not exists");
		}
				
	}

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
