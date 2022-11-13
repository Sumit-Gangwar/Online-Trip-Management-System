package app.trip.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;


@Entity
@Table(name="Travels")
@Data
@ToString
public class Travel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer travelId;
	
	@Size(min=3,message="Travel name shouldn't be less than 3 characters")
	private String travelName;
	
	@Size(min=3,max=10,message="Agent name shouldn't be less than 3 characters")
	private String agentName;
	
	@Embedded
	private Address addr;
	
	@Pattern(regexp = "^[789][0-9]{9}", message = "Contact should be of 10 digits and contains first digit 7 OR 8 OR 9")
	@NotNull
	private String contact;
	
    @JsonIgnore
	@OneToMany(cascade=CascadeType.ALL,mappedBy="tDetails",fetch=FetchType.EAGER)
	private Set<Bus> buses=new HashSet<>();
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "route_id", referencedColumnName = "routeId")
	private Route route;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Travel other = (Travel) obj;
		return Objects.equals(addr, other.addr) && Objects.equals(agentName, other.agentName)
				&& Objects.equals(contact, other.contact) && Objects.equals(travelId, other.travelId)
				&& Objects.equals(travelName, other.travelName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(addr, agentName, contact, travelId, travelName);
	}
	
}
