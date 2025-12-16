package com.davidhagar.gridphysics.functions.testJson;


import com.fasterxml.jackson.databind.ObjectMapper;



public class Test {


    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Dog dog = new Dog();
        dog.name = "Buddy";
        dog.breed = "Golden Retriever";

        Cat cat = new Cat();
        cat.name = "Whiskers";
        cat.livesRemaining = 7;

        String dogJson = objectMapper.writeValueAsString(dog);
        System.out.println("Dog JSON: " + dogJson);
        // Output will include: "type": "dog"

        String catJson = objectMapper.writeValueAsString(cat);
        System.out.println("Cat JSON: " + catJson);
        // Output will include: "type": "cat"


        //String dogJson = "{\"type\":\"dog\",\"name\":\"Buddy\",\"breed\":\"Golden Retriever\"}";
        Animal deserializedDog = objectMapper.readValue(dogJson, Animal.class);
        System.out.println("Deserialized Dog Name: " + deserializedDog.name);
        System.out.println("Deserialized Dog Breed: " + ((Dog) deserializedDog).breed);

        //String catJson = "{\"type\":\"cat\",\"name\":\"Whiskers\",\"livesRemaining\":7}";
        Animal deserializedCat = objectMapper.readValue(catJson, Animal.class);
        System.out.println("Deserialized Cat Name: " + deserializedCat.name);
        System.out.println("Deserialized Cat Lives Remaining: " + ((Cat) deserializedCat).livesRemaining);


    }


}
