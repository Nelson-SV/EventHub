package bll;

import be.Customer;


import dal.CustomerDAO;
import exceptions.EventException;

public class CustomerManager {
    private CustomerDAO customerDAO = null;

    public CustomerManager() throws EventException {
        this.customerDAO= new CustomerDAO();
    }

    /**
     * Exception handling
     */

    public void addCustomer(Customer customer) throws EventException {
        customerDAO.addCustomer(customer);
    }
}
