
package contactsdata;

import java.net.URL;
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

/**
 * @author szilmai
 */
public class ContactsViewController implements Initializable {
    
    @FXML
    StackPane menuPane;
    @FXML
    Pane contactPane;
    @FXML
    Pane exportPane;
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
    
    //osztályváltozók a menüpontokra
    private final String MENU_CONTACTS = "Kontaktok";
    private final String MENU_LIST = "Lista";
    private final String MENU_EXPORT = "Exportálás";
    private final String MENU_EXIT = "Kilépés";
    
    //ez fogja az adatbázisból tartalmazni az adatokat
    private final ObservableList<Contact> contactsData = FXCollections.observableArrayList(
        new Contact("Pityi","Palkó","pityipalko@piko.com","+36 20 4558820"),
            new Contact("Buksi","Kutya","buksi@dog.com","+36 30 3588870"),
            new Contact("Pöttyös","Katica","potyikati@potty.com","+36 70 5664221"));
    
    //ez állítja elő a táblázatot, és teszi bele az adatokat
    public void setTableData(){
        //létrehozzuk az oszlopokat, meghatározzuk a bennük lévő cellák típusát és az azokban lévő adatokat
        //első oszlop
        TableColumn lastNameCol = new TableColumn("Vezetéknév");
        lastNameCol.setMinWidth(100);
        //ebben az oszlopban minden cellának textfield legyen a tartalma (abba bele lehet írni és elmenteni)
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        // a cellák értékei legyenek a contact objektum lastName változójában lévő adat
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        
        //ha szerkeszthetővé akarom tenni a cellákat
        lastNameCol.setOnEditCommit(
                //itt átadunk egy eseménykezelőt, benne az <esemény>
            new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Contact, String> event) {
                    //a táblából vegye ki az összes elemet, és abból vegye ki az aktuális cellához tartozó egész sort (ezt átalakítjuk egy Contact objektummá)
                    Contact actualContact = (Contact) event.getTableView().getItems().get(
                            event.getTablePosition().getRow());
                    //ebben a Contact objektumban nevezze át a lastName változót az új értékre
                    actualContact.setLastName(event.getNewValue());
                }
        });
        
        //második oszlop
        TableColumn firstNameCol = new TableColumn("Keresztnév");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        
        firstNameCol.setOnEditCommit(
                //itt átadunk egy eseménykezelőt, benne az <esemény>
            new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Contact, String> event) {
                    Contact actualContact = (Contact) event.getTableView().getItems().get(
                            event.getTablePosition().getRow());
                    actualContact.setFirstName(event.getNewValue());
                }
        });
        
        //harmadik oszlop
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
                    actualContact.setEmail(event.getNewValue());
                }
        });
        
        TableColumn phoneNumCol = new TableColumn("Telefonszám");
        phoneNumCol.setMinWidth(140);
        phoneNumCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("phoneNum"));
        phoneNumCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Contact, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Contact, String> event) {
                    Contact actualContact = (Contact) event.getTableView().getItems().get(
                            event.getTablePosition().getRow());
                    actualContact.setPhoneNumber(event.getNewValue());
                }
        });
        
        table.getColumns().addAll(lastNameCol, firstNameCol, emailCol, phoneNumCol);
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
            //hozzáadjuk a listához az új adatot
            contactsData.add(new Contact(lName, fName, email, phoneNum));
            //kitöröljük a beviteli mezőt
            inputLastName.clear();
            inputFirstName.clear();
            inputEmail.clear();
            inputPhoneNumber.clear();
        }
    }
    
    @FXML
    private void generatePdf(ActionEvent event){
        String fileName = inputExport.getText();
        //átalakítom a beírt szöveget üres mezők nélkülire
        fileName = fileName.replaceAll("\\s+", "");
        //ha a beírt filenév nem üres, vagy null
        if (!fileName.equals("") || fileName != null) {
            //akkor a filenévvel és az adatokból hozza létre a PDF file-t
            PDFGeneration pdfCreator = new PDFGeneration();
            pdfCreator.generatePDF(fileName, contactsData);
        }
    }
    
    //amikor a program először lefut, ennek a törzsében szereplő minden utasítás fusson le
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //szeretnénk, ha induláskor megjelennének az adatok a táblázatban
        setTableData();
        setMenuData();
        
        
    }    
    
}
