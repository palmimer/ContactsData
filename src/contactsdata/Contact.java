/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactsdata;

import java.sql.Timestamp;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author szilmai
 */
public class Contact {
    //az ilyen típusú változók tudnak majd kommunikálni az adatbázissal
    private final SimpleStringProperty id;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty email;
    private final SimpleStringProperty phoneNum;
    private Timestamp regTime;
    
    //egy üres konstruktor
    public Contact() {
        this.id = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.phoneNum = new SimpleStringProperty("");
        this.regTime = null;
    }
    //öt string + timestamp paraméteres konstruktor
    public Contact(String id, String lastName, String firstName, String email, String phoneNum, Timestamp regTime) {
        this.id = new SimpleStringProperty(id);
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.email = new SimpleStringProperty(email);
        this.phoneNum = new SimpleStringProperty(phoneNum);
        this.regTime = regTime;
    }

    //négy string (id nélkül) + timestamp paraméteres konstruktor
    public Contact(String lastName, String firstName, String email, String phoneNum, Timestamp regTime) {
        this.id = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.email = new SimpleStringProperty(email);
        this.phoneNum = new SimpleStringProperty(phoneNum);
        this.regTime = regTime;
    }

    public String getId() {
        return id.get();
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
    
    public Timestamp getRegTime() {
        return regTime;
    }
        
    public void setId(String id){
        this.id.set(id);
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
    
    public void setRegTime(Timestamp regTime) {
        this.regTime = regTime;
    }
}
