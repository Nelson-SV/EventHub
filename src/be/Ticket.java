package be;
import java.math.BigDecimal;

public class Ticket {
    private int id;
    private String eventName;
    private String ticketType;
    private int quantity;
    private int purchaseQuantity;
    private BigDecimal ticketPrice;
    private String startDate;
    private String customerName;
    private String customerEmail;
    private String UUID;
    private String color;

    private Boolean special;


    public Ticket(int id, String eventName, String ticketType, BigDecimal ticketPrice,  String startDate, String customerName, String customerEmail, String UUID, int quantity) {
        this.id = id;
        this.eventName = eventName;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
        this.startDate = startDate;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.UUID = UUID;
        this.quantity = quantity;
    }

    public Ticket (String nameEvent, String ticketType,  String startDate, BigDecimal price){
        this.eventName = nameEvent;
        this.ticketType = ticketType;
        this.startDate = startDate;
        this.ticketPrice = price;
    }

    public Ticket (int id, String ticketType, int quantity, BigDecimal ticketPrice, String color) {
        this.id = id;
        this.ticketType = ticketType;
        this.quantity = quantity;
        this.ticketPrice = ticketPrice;
        this.color = color;
    }

    public Ticket (int id, String ticketType, int quantity, BigDecimal ticketPrice) {
        this.id = id;
        this.ticketType = ticketType;
        this.quantity = quantity;
        this.ticketPrice = ticketPrice;
    }

    public Ticket (String ticketType, int quantity, BigDecimal ticketPrice, String color) {
        this.ticketType = ticketType;
        this.quantity = quantity;
        this.ticketPrice = ticketPrice;
        this.color = color;
    }

    // Copy constructor
    public Ticket(Ticket other) {
        this.id = other.id;
        this.ticketType = other.ticketType;
        this.purchaseQuantity = other.quantity;
        this.ticketPrice = other.ticketPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getStartDate() {
        return startDate;
    }

    public Boolean getSpecial(){return special;}

    public void setSpecial(Boolean type){special = type;}

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return ticketType +
                ", q: " + quantity +
                ", price: " + ticketPrice + "DKK"
                ;
    }
}

