package app.trip.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;


@Entity
@Table(name="Bus")
@Data
@ToString
public class Bus {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer busId;
	
	@Enumerated(EnumType.STRING)
	private BusType busType;
	
	private Integer busNo;
	
	private Integer capacity;
	
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "travel_id")
	private Travel tDetails;

}
