package com.shopperStore.controller;

import com.shopperStore.model.user.Customer;
import com.shopperStore.model.user.User;
import com.shopperStore.model.user.UserDAO;


public class UserController extends UserDAO{

    public boolean signup(Customer customer) {
        if(customer.getEmail() != null && customer.getPassword() != null) // Validate fields that should not be empty
            return create(new Customer(customer.getEmail(), customer.getName(), customer.getPassword(), customer.getAddress())) != null;
        else {
            this.setErrMessage("Please fill in required parameters!!!");
//            System.out.println("required field is null. Dev message: sign up controller");
        }
        this.setErrMessage("Unable to sign up, server error");
        return false;
    }

    /**
     * TODO: might want to pass data to display on home screen
     * @param email - identifier
     * @param password - to validate user
     * @return - user object
     */
    public User login(String email, String password) {
        if (email.equals("") || password.equals("")) { // check if fields are null
            this.setErrMessage("Fill in required fields");
//            System.out.println("fields are null for login");
            return null;
        }
        else if (super.validateUser(email, password)) { // return user from DB
//            System.out.println("controller message: user validated successfully");
            this.setSuccessMessage("logged in successfully, welcome back!");
            return super.get(email);
        }
//        System.out.println("Validation returned false");
        this.setErrMessage("User does not exist");
        return null;
    }

}
