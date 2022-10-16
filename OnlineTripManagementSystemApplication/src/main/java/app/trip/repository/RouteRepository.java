package app.trip.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.trip.models.Route;


public interface RouteRepository extends JpaRepository<Route, Integer> {
	
	@Query("select r from Route r where r.Routefrom=?1 and r.RouteTo=?2")
	public Optional<Route> findByRoutefromAndRouteTo(String routeFrom, String routeTo);
}
