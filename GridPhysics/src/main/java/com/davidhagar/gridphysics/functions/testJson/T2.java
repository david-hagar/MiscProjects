package com.davidhagar.gridphysics.functions.testJson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class T2 {


    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Car.class, name = "car"),
            @JsonSubTypes.Type(value = Truck.class, name = "truck")
    })
    public abstract static class Vehicle {
        public String make;
        public String model;
        // Constructors, getters, setters
    }

    public static class Car extends Vehicle {
        public int seats;
        // Constructors, getters, setters
    }

    public static class Truck extends Vehicle {
        public int payloadCapacity;
        // Constructors, getters, setters
    }


    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        Car car = new Car();
        car.make = "Toyota";
        car.model = "Camry";
        car.seats = 5;

        Truck truck = new Truck();
        truck.make = "Ford";
        truck.model = "F150";
        truck.payloadCapacity = 5000;
        try {

            String carJson = objectMapper.writeValueAsString(car);

            String truckJson = objectMapper.writeValueAsString(truck);

            System.out.println("Car JSON: " + carJson);
            System.out.println("Truck JSON: " + truckJson);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
