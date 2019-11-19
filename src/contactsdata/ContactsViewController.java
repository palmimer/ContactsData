
package contactsdata;

import java.net.URL;
import java.util.ResourceBundle;
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
    Button addNewContactButton;
    
    //ez fogja az adatbázisból tartalmazni az adatokat
    private final ObservableList<Contact> contactsData = FXCollections.observableArrayList(
        new Contact("Pityi","Palkó","pityipalko@piko.com","+36 20 4558820"),
            new Contact("Buksi","Kutya","buksi@dog.com","+36 30 3588870"),
            new Contact("Pöttyös","Katica","potyikati@potty.com","+36 70 5664221"));
    
    public void setTableData(){
        //létrehozzuk az oszlopokat, meghatározzuk a bennük lévő cellák típusát és az azokban lévő adatokat
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
        
        Node exitNode = new ImageView(new Image(getClass().getResourceAsStream("/exit-png-image.png")));
        
        //létrehozom a menüpontokat
        TreeItem<String> nodeItemA = new TreeItem<>("Kontaktok");
        nodeItemA.setExpanded(true);
        TreeItem<String> nodeItemB = new TreeItem<>("Kilépés", exitNode);
        
        //rá kell tenni ezeket a gyökérmenüre
        treeItemRoot1.getChildren().addAll(nodeItemA, nodeItemB);
        
        //beteszem a képeket egy-egy node objektumba
        //először Image objektum, utána azt egy ImageView obj-ba teszem, ami egy kiterjesztése a Node-nak
        Node contactsNode = new ImageView(new Image(getClass().getResourceAsStream("/contact-png-image.png")));
        Node exportNode = new ImageView(new Image(getClass().getResourceAsStream("/download-arrow-png-image.png")));
        
        //még két almenü az A menüpont alá
        TreeItem<String> nodeItemA1 = new TreeItem<>("Lista", contactsNode);
        TreeItem<String> nodeItemA2 = new TreeItem<>("Exportálás", exportNode);
        
        //ezeket meg a hozzájuk tartozó szülőmenüre kell tenni
        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
        
        //a menüfát rátesszük a menuPane-re
        menuPane.getChildren().add(treeView);
    }
    
//    private void handleButtonAction(ActionEvent event) {
//        
//    }
    
    //amikor a program először lefut, ennek a törzsében szereplő minden utasítás fusson le
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //szeretnénk, ha induláskor megjelennének az adatok a táblázatban
        setTableData();
        setMenuData();
        
        
    }    
    
}
