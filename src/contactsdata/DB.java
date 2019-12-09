/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactsdata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Pálmai Mercédesz
 */
public class DB {
    //final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    final String URL = "jdbc:derby:contacts;create=true";
    final String USERNAME = "";
    final String PASSWORD = "";
    
    Connection conn = null;
    Statement createStatement = null;
    DatabaseMetaData dbmd = null;
    
    //konstruktor (ebben létrejön az adatbázis és a tábla, ha még nincs)

    public DB() {
        // megpróbáljuk életre kelteni a kapcsolatot
        try {
            System.out.println("Kapcsolódás a contacts adatbázishoz...");
            conn = DriverManager.getConnection(URL);
            System.out.println("A kapcsolat létrejött a contacts adatbázissal.");
        } catch (SQLException ex) {
            System.out.println("Valami baj van!" + ex);
            //ide lehet még dolgokat tenni, hogy mit csináljon, ha nincs kapcsolat pl. felugrik egy figyelmeztető ablak
        }
        
        //ha életre kelt (van kapcsolat)
        if(conn != null){
            //akkor létrehozunk egy lekéréseket hordozó objektumot: createStatement
            try {
                createStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Valami baj van!" + ex);
            }
        
            //megnézzük, hogy üres-e az adatbázis
            DatabaseMetaData dbmd = null;
            try {
                // elkérjük a metaadatokat az adatbázisról
                dbmd = conn.getMetaData();
            } catch (SQLException ex) {
                System.out.println("Nem sikerült a metaadatokat elkérni az adatbázisról." + ex);
            }
            
            //létrehozzuk a táblát az adatbázisban
            try {
                // a metaadatokat ResultSetben tároljuk
                //konstruktorába (String catalog - may be null, Str schemaNamePattern - may be null, Str TableNamePattern, Str[] types pl TABLE)
                ResultSet rs = dbmd.getTables(null, "APP", "CONTACTS", null);
//                System.out.println("Idáig eljutottam: a ResultSet-ben benne van a válasz. ");
                
                //megnézzük a visszakapott adatokat, van-e első sor/adat
                //ha nincs benne adat, kérjük a teherautót, hogy vigyen el egy kérést (tábla létrehozása)
                if ( !rs.next()) {
//                    System.out.println("Idáig jutottam: a ResultSet első eleme null, tehát nincs még tábla, létrehozom.");
                    createStatement.execute("create table contacts("
                            + "id integer not null primary key generated always as identity (start with 1, increment by 1), "
                            + "lastname varchar(30), "
                            + "firstname varchar(30), "
                            + "email varchar(40), "
                            + "phonenumber varchar(20), "
                            + "regtime timestamp)");
                    System.out.println("A contacts tábla létrejött.");
                } else {
                    System.out.println("A contacts tábla már létezik, nem kell létrehozni.");
                    
                }
            } catch (SQLException ex) {
                System.out.println("Nem sikerült létrehozni a táblát.");
                System.out.println("" + ex);
            }
            
            //contacts tábla fejléceinek ellenőrzése
            
            System.out.println("A contacts tábla fejléce a következő:");
            try {
                ResultSet resultSetOfContactsTable = createStatement.executeQuery("SELECT * from contacts");
                ResultSetMetaData rsmd = resultSetOfContactsTable.getMetaData();
                int columnNumber = rsmd.getColumnCount();
                for (int i = 1; i <= columnNumber; i++) {
                    if (i > 1) {
                        System.out.print(" | ");
                    }
                    System.out.print(rsmd.getColumnName(i));
                }
                System.out.println("");
            } catch (SQLException ex) {
                System.out.println("Nem sikerült lekérdezni a tábla fejlécét.");
                System.out.println("" + ex);
            }
        }
        
    } //itt vége a konstruktornak
    
    // egy listába gyűjti az adatbázisban található adatokból létrehozott objektumokat, és azt adja vissza. 
    //null-t ad, ha nincs a táblában még semmi! 
    public ArrayList<Contact> getAllContacts(){
        ArrayList<Contact> contacts = null;
        String sql = "select * from contacts";
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            contacts = new ArrayList<>();
            while(rs.next()){
                Contact contact = new Contact();
                //kiszedjük az adatbázis aktuális sorából az értékeket
                String id = rs.getString(1);
                String lastName = rs.getString(2);
                String firstName = rs.getString(3);
                String email = rs.getString(4);
                String phoneNum = rs.getString(5);
                Timestamp regTime = rs.getTimestamp(6);
                //betesszük egy contact objektumba
                contact.setId(id);
                contact.setLastName(lastName);
                contact.setFirstName(firstName);
                contact.setEmail(email);
                contact.setPhoneNumber(phoneNum);
                contact.setRegTime(regTime);
                //beletesszük a listába a contact objektumot
                contacts.add(contact);
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van az adatok lekérdezésekor.");
            System.out.println(ex);
        }
        return contacts;
    }
    
    public void addNewContactToDataBase(Contact contact){
        String sql = "insert into contacts (lastname, firstname, email, phonenumber, regtime) values (?,?,?,?,?)";
        try {
            PreparedStatement prstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prstmt.setString(1, contact.getLastName());
            prstmt.setString(2, contact.getFirstName());
            prstmt.setString(3, contact.getEmail());
            prstmt.setString(4, contact.getPhoneNum());
            prstmt.setTimestamp(5, contact.getRegTime());
            //itt nem az executeQuery()-t kell használni!!! execute v. executeUpdate
            prstmt.executeUpdate(); // ez visszadja a módosított sorok számát, ha kell
            System.out.println("Új kontakt elmentve.");
            ResultSet generatedKeys = prstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                System.out.println("Az új kontakt bekerült az adatbázisba " + newId + ". sorszámmal.");
            }
            
        } catch (SQLException ex) {
            System.out.println("Valami baj van az új kontakt elmentésekor.");
            System.out.println(ex);
        }
    }
    
    public void updateContact(Contact contact){
        String sql = "update contacts set lastname = ?, firstname = ?, email = ?, phonenumber = ? where id = ?";
        try {
            PreparedStatement prstmt = conn.prepareStatement(sql);
            prstmt.setString(1, contact.getLastName());
            prstmt.setString(2, contact.getFirstName());
            prstmt.setString(3, contact.getEmail());
            prstmt.setString(4, contact.getPhoneNum());
            prstmt.setString(5, contact.getId());
            prstmt.executeUpdate();
            System.out.println("A " + contact.getId() + ". sorszámú kontakt frissült az adatbázisban " );
        } catch (SQLException ex) {
            System.out.println("Valami baj van a kontakt frissítésekor.");
            System.out.println(ex);
        }
    }
}
