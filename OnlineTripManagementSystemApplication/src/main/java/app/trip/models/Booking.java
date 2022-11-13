package app.trip.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity @Table(name = "Booking")
public class Booking {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer bookingId;
	private String bookingType;
	private String description;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status = BookingStatus.Not_Booked;
	private String bookingTitle;
	private LocalDateTime date;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id",referencedColumnName = "userId")
	private User user;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ticket_id",referencedColumnName = "ticketId")
	private Ticket ticket;
	
}
