package com.epam.jwd.hrmanager.model;

import java.util.Optional;

public class Address implements Entity {

    private final Long id;
    private final City city;
    private final Street street;
    private final int houseNumber;
    private final Integer flatNumber;

    public Address(Long id, City city, Street street, int hoseNumber, Integer flatNumber) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.houseNumber = hoseNumber;
        this.flatNumber = flatNumber;
    }

    public Address(City city, Street street, int hoseNumber, Integer flatNumber) {
        this(null, city, street, hoseNumber, flatNumber);
    }

    public int getHouseNumber() {
        return houseNumber;
    }


    @Override
    public Long getId() {
        return id;
    }

    public City getCity() {
        return city;
    }

    public Street getStreet() {
        return street;
    }

    public Optional<Integer> getFlatNumber() {
        return Optional.of(flatNumber);
    }

    public Address withCity(City city) {
        return new Address(id, city, street, houseNumber, flatNumber);
    }

    public Address withStreet(Street street) {
        return new Address(id, city, street, houseNumber, flatNumber);
    }

    public Address withHouseNumber(int houseNumber){
        return new Address(id, city, street, houseNumber, flatNumber);
    }

    public Address withFlatNumber(Integer flatNumber){
        return new Address(id, city, street, houseNumber, flatNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (houseNumber != address.houseNumber) return false;
        if (!id.equals(address.id)) return false;
        if (!city.equals(address.city)) return false;
        if (!street.equals(address.street)) return false;
        return flatNumber.equals(address.flatNumber);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + street.hashCode();
        result = 31 * result + houseNumber;
        result = 31 * result + flatNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", city=" + city +
                ", street=" + street +
                ", hoseNumber=" + houseNumber +
                ", flatNumber=" + flatNumber +
                '}';
    }
}
