package com.jemi.vehicleinfosystem;

import com.jemi.vehicleinfosystem.model.Vehicle;
import com.jemi.vehicleinfosystem.repository.VehicleRepository;
import com.jemi.vehicleinfosystem.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle vehicle1;
    private Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        vehicle1 = new Vehicle();
        vehicle1.setId(1L);
        vehicle1.setMake("Toyota");
        vehicle1.setModel("Camry");
        vehicle1.setLaunchYear(2020);
        vehicle1.setVin("12345");

        vehicle2 = new Vehicle();
        vehicle2.setId(2L);
        vehicle2.setMake("Honda");
        vehicle2.setModel("Civic");
        vehicle2.setLaunchYear(2019);
        vehicle2.setVin("67890");
    }

    @Test
    void testFindAll() {
        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(vehicle1, vehicle2));

        List<Vehicle> vehicles = vehicleService.findAll();

        assertEquals(2, vehicles.size());
        assertEquals("Toyota", vehicles.get(0).getMake());
        assertEquals("Honda", vehicles.get(1).getMake());
    }

    @Test
    void testFindById() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle1));

        Optional<Vehicle> foundVehicle = vehicleService.findById(1L);

        assertTrue(foundVehicle.isPresent());
        assertEquals("Toyota", foundVehicle.get().getMake());
    }

    @Test
    void testSave() {
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle1);

        Vehicle savedVehicle = vehicleService.save(vehicle1);

        assertEquals("Toyota", savedVehicle.getMake());
        assertEquals("Camry", savedVehicle.getModel());
    }

    @Test
    void testDeleteById() {
        doNothing().when(vehicleRepository).deleteById(1L);

        vehicleService.deleteById(1L);

        verify(vehicleRepository, times(1)).deleteById(1L);
    }
}
