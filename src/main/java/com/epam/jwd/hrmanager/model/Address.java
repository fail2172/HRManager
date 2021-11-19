package com.epam.jwd.hrmanager.model;

import java.util.Optional;

public class Address implements Entity {

    private final Long id;
    private final City city;
    private final Street street;
    private final int hoseNumber;
    private final Integer flatNumber;

    public Address(Long id, int hoseNumber, Integer flatNumber, City city, Street street) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.hoseNumber = hoseNumber;
        this.flatNumber = flatNumber;
    }

    public Address(int hoseNumber, Integer flatNumber, City city, Street street) {
        this(null, hoseNumber, flatNumber, city, street);
    }

    public int getHoseNumber() {
        return hoseNumber;
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
        return new Address(id,  hoseNumber, flatNumber, city, street);
    }

    public Address withStreet(Street street) {
        return new Address(id,  hoseNumber, flatNumber, city, street);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (hoseNumber != address.hoseNumber) return false;
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
        result = 31 * result + hoseNumber;
        result = 31 * result + flatNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", city=" + city +
                ", street=" + street +
                ", hoseNumber=" + hoseNumber +
                ", flatNumber=" + flatNumber +
                '}';
    }
}