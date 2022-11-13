package app.trip.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity @Table(name = "Packages")
public class Packages {
	
	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer packageId;
	
	@Size(min = 5, message = "Package name shouldn't be less than 5 characters") @NotNull @NotBlank @NotEmpty
	private String packageName;
	
	@Size(min = 5,message = "Description shouldn't be less than 5 characters") @NotNull @NotBlank @NotEmpty
	private String packageDescription;
	
	@Enumerated(EnumType.ORDINAL)
	private PackageType packageType;
	
	@NotNull @Min(0)
	private Integer packageCost;
	
	@NotNull @NotBlank @NotEmpty @Size(min = 3,message = "Payment Details shouldn't be less than 3 characteres")
	private String paymentDetails;
	
	@JsonIgnore
	@OneToMany(mappedBy = "packages" ,cascade = CascadeType.ALL)
	private List<Ticket> ticketDetails;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "packages")
	private List<Hotel> hotelDetails;

}
