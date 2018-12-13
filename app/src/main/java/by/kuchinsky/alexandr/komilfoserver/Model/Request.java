package by.kuchinsky.alexandr.komilfoserver.Model;

import java.util.List;

public class Request {

    private String phone;
    private String name;
    private String address;
    private String status;
    private String total;
    private List<Order> services;

    public Request() {
    }

    public Request(String phone, String name, String address, String total, List<Order> services) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.services = services;
        this.status = "0"; //defoltniy status broni - 0, 0 - dobavlena zayavka. 1 - podtverjdeno menedjerom, 2 - otkloneno
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getServices() {
        return services;
    }

    public void setServices(List<Order> services) {
        this.services = services;
    }
}