package com.jemi.vehicleinfosystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jemi.vehicleinfosystem.controller.VehicleController;
import com.jemi.vehicleinfosystem.model.Vehicle;
import com.jemi.vehicleinfosystem.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    void testGetAllVehicles() throws Exception {
        when(vehicleService.findAll()).thenReturn(Arrays.asList(vehicle1, vehicle2));

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make").value("Toyota"))
                .andExpect(jsonPath("$[1].make").value("Honda"));
    }

    @Test
    void testGetVehicleById() throws Exception {
        when(vehicleService.findById(1L)).thenReturn(Optional.of(vehicle1));

        mockMvc.perform(get("/api/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Toyota"));
    }

    @Test
    void testGetVehicleByIdNotFound() throws Exception {
        when(vehicleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/vehicles/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateVehicle() throws Exception {
        when(vehicleService.save(any(Vehicle.class))).thenReturn(vehicle1);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(vehicle1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.make").value("Toyota"));
    }

    @Test
    void testUpdateVehicle() throws Exception {
        when(vehicleService.findById(1L)).thenReturn(Optional.of(vehicle1));
        when(vehicleService.save(any(Vehicle.class))).thenReturn(vehicle1);

        vehicle1.setModel("Corolla");

        mockMvc.perform(put("/api/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(vehicle1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Corolla"));
    }

    @Test
    void testUpdateVehicleNotFound() throws Exception {
        when(vehicleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(vehicle1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteVehicle() throws Exception {
        when(vehicleService.findById(1L)).thenReturn(Optional.of(vehicle1));
        doNothing().when(vehicleService).deleteById(1L);

        mockMvc.perform(delete("/api/vehicles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteVehicleNotFound() throws Exception {
        when(vehicleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/vehicles/1"))
                .andExpect(status().isNotFound());
    }
}

