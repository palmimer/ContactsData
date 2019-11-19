/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactsdata;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author szilmai
 */
public class Contact {
    //az ilyen típusú változók tudnak majd kommunikálni az asatbázissal
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty email;
    private final SimpleStringProperty phoneNum;
    
    //egy üres konstruktor
    public Contact() {
        this.lastName = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.phoneNum = new SimpleStringProperty("");
    }

    public Contact(String lastName, String firstName, String email, String phoneNum) {
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.email = new SimpleStringProperty(email);
        this.phoneNum = new SimpleStringProperty(phoneNum);
    }

    public Contact(String lastName, String firstName, String email) {
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.email = new SimpleStringProperty(email);
        this.phoneNum = new SimpleStringProperty("");
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPhoneNum() {
        return phoneNum.get();
    }
    
    public void setLastName(String lastName){
        this.lastName.set(lastName);
    }
    
    public void setFirstName(String firstName){
        this.firstName.set(firstName);
    }
    
    public void setEmail(String email){
        this.email.set(email);
    }
    
    public void setPhoneNumber(String phoneNum){
        this.phoneNum.set(phoneNum);
    }
}
