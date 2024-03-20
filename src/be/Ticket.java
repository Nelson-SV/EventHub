package be;

public class Ticket {

    private String eventName;
    private String ticketType;
    private String ticketPrice;
    private String[] included;

    private String startDate;
    private String customerName;
    private String customerEmail;
    private String UUID;

    private String quantity;

    public Ticket(String eventName, String ticketType, String ticketPrice, String[] included,  String startDate, String customerName, String customerEmail, String UUID, String quantity) {
        this.eventName = eventName;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
        this.included = included;

        this.startDate = startDate;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.UUID = UUID;
        this.quantity = quantity;
    }

    public Ticket (String nameEvent, String ticketType,  String startDate, String price){
        this.eventName = nameEvent;
        this.ticketType = ticketType;

        this.startDate = startDate;
        this.ticketPrice = price;
    }

    public Ticket (String ticketType, String quantity) {
        this.ticketType = ticketType;
        this.quantity = quantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
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

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String[] getIncluded() {
        return included;
    }

    public void setIncluded(String[] included) {
        this.included = included;
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
