package com.jemi.vehicleinfosystem.repository;

import com.jemi.vehicleinfosystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}

