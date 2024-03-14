package com.example.btnavbar;

public class Locations {
    public Locations(String location, String address) {
        this.location = location;
        this.address = address;
    }

    String location;
    String address;
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
