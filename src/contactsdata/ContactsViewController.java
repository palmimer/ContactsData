
package contactsdata;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author szilmai
 */
public class ContactsViewController implements Initializable {
    
    @FXML
    StackPane menuPane;
    @FXML
    SplitPane basicWindow;
    @FXML
    Pane contactPane;
    @FXML
    Pane exportPane;
    @FXML
    Pane alertPane;
    @FXML
    TableView table;
    @FXML
    TextField inputLastName;
    @FXML
    TextField inputFirstName;
    @FXML
    TextField inputEmail;
    @FXML
    TextField inputPhoneNumber;
    @FXML
    TextField inputExport;
    @FXML
    Button addNewContactButton;
    @FXML
    Button exportButton;
    
    DB db = new DB();
    
    //osztályváltozók a menüpontokra
    private final String MENU_CONTACTS = "Kontaktok";
    private final String MENU_LIST = "Lista";
    private final String MENU_EXPORT = "Exportálás";
    private final String MENU_EXIT = "Kilépés";
    
    //ez fogja az adatbázisból tartalmazni az adatokat
    //az FX Collections observableArr.List-je egy statikus függvény, amibe átadjuk a Contact objektumokat
    private final ObservableList<Contact> contactsData = FXCollections.observableArrayList();
    
    //ez állítja elő a táblázatot, és teszi bele az adatokat
    public void setTableData(){
        //létrehozzuk az oszlopokat, meghatározzuk a bennük lévő cellák típusát és az azokban lévő adatokat
        // Első oszlop: 
        //az új oszlop létrehozásakor megadjuk a nevét
        TableColumn lastNameCol = new TableColumn("Vezetéknév");
        lastNameCol.setMinWidth(100);
        //ebben az oszlopban minden cellának textfield legyen a tartalma (abba bele lehet írni és elmenteni)
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        // a cellák értékei legyenek a contact objektumok lastName változójában lévő adat
        // feltételezzük, hogy a Table-nek lesznek adatai, abból veszi majd ki
        //na de mi van, ha nincsenek adatok????
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        
        //ha szerkeszthetővé akarom tenni a cellákat
        lastNameCol.setOnEditCommit(
                //itt átadunk egy eseménykezelőt (az egy interface), típusa egy Event osztályból származó osztály (benne az <esemény>)
            new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
                @Override
                //megírjuk az interface metódusát, aminek paramétere egy Event (esemény)
                //ez a metódus arra, hogy mit csináljon, ha módosítanak egy adatot a táblában
                public void handle(TableColumn.CellEditEvent<Contact, String> event) {
                    //a táblából vegye ki az összes elemet, és abból vegye ki az aktuális cellához tartozó egész sort (ezt átalakítjuk egy Contact objektummá)
                    Contact actualContact = (Contact) event.getTableView().getItems().get(event.getTablePosition().getRow());
                    //ebben a Contact objektumban nevezze át a lastName változót az új értékre
                    actualContact.setLastName(event.getNewValue());
                    //és frissítse az adatbázisban
                    db.updateContact(actualContact);
                }
        });
        
        TableColumn firstNameCol = new TableColumn("Keresztnév");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        // a Propertyvaluefact-nek átadunk egy objektumot, és megmondjuk mit vegyen ki belőle
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        
        firstNameCol.setOnEditCommit(
                //itt átadunk egy eseménykezelőt, benne az <esemény>
            new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Contact, String> event) {
                    Contact actualContact = (Contact) event.getTableView().getItems().get(
                            event.getTablePosition().getRow());
                    actualContact.setFirstName(event.getNewValue());
                    db.updateContact(actualContact);
                }
        });
        
        TableColumn emailCol = new TableColumn("E-mailcím");
        emailCol.setMaxWidth(180);
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));
        emailCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Contact, String> event) {
                    Contact actualContact = (Contact) event.getTableView().getItems().get(
                            event.getTablePosition().getRow());
                    String newEmail = event.getNewValue();
                    if (newEmail.length() > 4 && newEmail.contains("@") && newEmail.contains(".") && !newEmail.equals("")) {
                        actualContact.setEmail(newEmail);
                        db.updateContact(actualContact);
                    } else {
                        actualContact.setEmail(event.getOldValue());
                    }
                }
        });
        
        TableColumn phoneNumCol = new TableColumn("Telefonszám");
        phoneNumCol.setMinWidth(140);
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("phoneNum"));
        phoneNumCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Contact, String> event) {
                    Contact actualContact = (Contact) event.getTableView().getItems().get(
                            event.getTablePosition().getRow());
                    actualContact.setPhoneNumber(event.getNewValue());
                    db.updateContact(actualContact);
                }
        });
         //itt helyezzük el az oszlopokat a táblázatban
        table.getColumns().addAll(lastNameCol, firstNameCol, emailCol, phoneNumCol);
        //beletesszük az adatbázisból az adatokat a contactsData listába
        ArrayList<Contact> allContacts = db.getAllContacts();
        if (allContacts != null) {
            contactsData.addAll(allContacts);
        }
        
        // és beletesszük a listánkból az adatokat a táblázatba
        table.setItems(contactsData);
    }
    
    public void setMenuData(){
        //*létre kell hozni egy TreeItem-et
        TreeItem<String> treeItemRoot1 = new TreeItem<>("Menü");
        
        //rá kell tenni a TreeView-ra
        TreeView<String> treeView = new TreeView<>(treeItemRoot1);
        
        //beállítom, hogy ez a legelső gyökér menüpont ne látszódjon, mert ebből jön ki majd az első menüpont
        treeView.setShowRoot(false);
        
        //a kilépés ikon elhelyezése
        Node exitNode = new ImageView(new Image(getClass().getResourceAsStream("/exit-png-image.png")));
        
        //létrehozom a menüpontokat
        TreeItem<String> nodeItemA = new TreeItem<>(MENU_CONTACTS);
        TreeItem<String> nodeItemB = new TreeItem<>(MENU_EXIT, exitNode);
        
        //rá kell tenni ezeket a gyökérmenüre
        treeItemRoot1.getChildren().addAll(nodeItemA, nodeItemB);
        
        //beteszem a képeket egy-egy node objektumba
        //először Image objektum, utána azt egy ImageView obj-ba teszem, ami egy kiterjesztése a Node-nak
        Node contactsNode = new ImageView(new Image(getClass().getResourceAsStream("/contact-png-image.png")));
        Node exportNode = new ImageView(new Image(getClass().getResourceAsStream("/download-arrow-png-image.png")));
        
        //még két almenü az A menüpont alá
        TreeItem<String> nodeItemA1 = new TreeItem<>(MENU_LIST, contactsNode);
        TreeItem<String> nodeItemA2 = new TreeItem<>(MENU_EXPORT, exportNode);
        
        //ezeket meg a hozzájuk tartozó szülőmenüre kell tenni
        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
        
        //ha azt akarjuk, hogy a menü kinyitva jelenjen meg
        nodeItemA.setExpanded(true);
        
        //a menüfát rátesszük a menuPane-re
        menuPane.getChildren().add(treeView);
        
        // a menüpontok működése:
        
        //ha azt szeretném hogy egy kattintásra kinyíljon a menüpont,
        //egy listenert adunk a menüfához, ami figyeli, mire kattint a felhasználó
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener(){
            public void changed(ObservableValue observable, Object oldValue, Object newValue){
                //az item, amire a felhasználó rákattint
                TreeItem selectedItem = (TreeItem<String>) newValue;
                //elmentjük az objektum nevét egy Stringben
                String selectedMenuItem = (String) selectedItem.getValue();
                //switch-csel irányítjuk, mit csináljon, ha adott menüpontra kattintanak 
                if (selectedMenuItem != null) {
                    switch(selectedMenuItem){
                        case MENU_CONTACTS:
                            //hogy ne szálljon el, ha nincs a menüpont alatt másik menüpont:try-catch
                            try {
                                //a menüt nyissa meg
                                selectedItem.setExpanded(true);
                            } catch (Exception ex) {
                            }
                            break;
                        case MENU_LIST:
                            contactPane.setVisible(true);
                            exportPane.setVisible(false);
                            break;
                        case MENU_EXPORT:
                            contactPane.setVisible(false);
                            exportPane.setVisible(true);
                            exportPane.setDisable(false);
                            break;
                        case MENU_EXIT:
                            System.exit(0);
                            break;
                    }
                }
            }});
    }
    
    @FXML
    private void addContact(ActionEvent event) {
        String email = inputEmail.getText();
        String lName = inputLastName.getText();
        String fName = inputFirstName.getText();
        String phoneNum = inputPhoneNumber.getText();
        if(email.length() > 4 && email.contains("@") && email.contains(".") && !lName.equals("") && !fName.equals("") ){
            //itt adjuk meg a pontos időt a létrehozandó kontaktnak
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            //létrehozzuk az új kontakt objektumot
            Contact newContact = new Contact(lName, fName, email, phoneNum, currentTime);
            //a listához hozzáadjuk az új elemet
            contactsData.add(newContact);
            //az adatbázisba beírjuk az új elemet
            db.addNewContactToDataBase(newContact);
            //kitöröljük a mezőket
            inputLastName.clear();
            inputFirstName.clear();
            inputEmail.clear();
            inputPhoneNumber.clear();
        } else {
            alert("Valós e-mail-t adjon meg!");
        }
    }
    
    @FXML
    private void generatePdf(ActionEvent event){
        //a beviteli mezőből kivesszük a szöveget
        String fileName = inputExport.getText();
        //átalakítom a beírt szöveget üres mezők nélkülire (minden whitespace-t átír üres karakterre)
        fileName = fileName.replaceAll("\\s+", "");
        if (!fileName.equals("") || fileName != null) {
            //példányosítjuk a PDFGeneration osztályt
            PDFGeneration pdfCreator = new PDFGeneration();
            //meghívjuk a metódusát, átadva neki a file-nevet és az adatokat
            pdfCreator.generatePDF(fileName, contactsData);
            inputExport.clear();
        }
        //TODO sikeres lefutás esetén jó lenne egy visszaigazoló üzenet, hogy a pdf file létrejött
    }
    
    private void alert(String text){
        basicWindow.setOpacity(0.4);
        basicWindow.setDisable(true);
        alertPane.setDisable(false);
        
        Label label = new Label(text);
        Button alertButton = new Button("OK");
        
        alertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                basicWindow.setOpacity(1);
                basicWindow.setDisable(false);
                alertPane.setDisable(true);
                alertPane.setVisible(false);
            }
        });
        
        VBox vbox = new VBox(label, alertButton);
        alertPane.getChildren().add(vbox);
        
    }
    
    //amikor a program először lefut, ennek a törzsében szereplő minden utasítás fusson le
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //szeretnénk, ha induláskor megjelennének az adatok a táblázatban
        setTableData();
        setMenuData();
        
        
    }    
    
}
