package com.jemi.vehicleinfosystem.controller;

import com.jemi.vehicleinfosystem.model.Vehicle;
import com.jemi.vehicleinfosystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Optional<Vehicle> vehicle = vehicleService.findById(id);
        return vehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.save(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        Optional<Vehicle> optionalVehicle = vehicleService.findById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setMake(vehicleDetails.getMake());
            vehicle.setModel(vehicleDetails.getModel());
            vehicle.setLaunchYear(vehicleDetails.getLaunchYear());
            vehicle.setVin(vehicleDetails.getVin());
            return ResponseEntity.ok(vehicleService.save(vehicle));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        if (vehicleService.findById(id).isPresent()) {
            vehicleService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

