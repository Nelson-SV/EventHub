package be;

public class Ticket {

    private int id;
    private String eventName;
    private String ticketType;
    private int quantity;
    private float ticketPrice;
    private String startDate;
    private String customerName;
    private String customerEmail;
    private String UUID;


    public Ticket(int id, String eventName, String ticketType, float ticketPrice,  String startDate, String customerName, String customerEmail, String UUID, int quantity) {
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

    public Ticket (String nameEvent, String ticketType,  String startDate, float price){
        this.eventName = nameEvent;
        this.ticketType = ticketType;
        this.startDate = startDate;
        this.ticketPrice = price;
    }

    public Ticket (String ticketType, int quantity, float ticketPrice) {
        this.ticketType = ticketType;
        this.quantity = quantity;
        this.ticketPrice = ticketPrice;
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

    public float getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(float ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getStartDate() {
        return startDate;
    }

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
}
