package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static sample.webComm.sendData;

public class Sales implements Initializable {

    @FXML
    public Label newLbl;

    @FXML
    private Label newLbl1;

    @FXML
    public TableView<ItemStore> itemList;

    @FXML
    private TableColumn<ItemStore, String> itemID;

    @FXML
    private TableColumn<ItemStore, String> itemCategory;

    @FXML
    private TableColumn<ItemStore, String> itemName;

    @FXML
    private TextField filterField;

    @FXML
    private Button btnNewSale;

    @FXML
    private Button btnRefresh;

    @FXML
    private TableView<salesStore> itemsData;

    @FXML
    private TableColumn<salesStore, String> category;

    @FXML
    private TableColumn<salesStore, String> id;

    @FXML
    private TableColumn<salesStore, String> name;

    @FXML
    private TableColumn<salesStore, String> quantity;

    @FXML
    private TableColumn<salesStore, String> unitPrice;

    @FXML
    private TableColumn<salesStore, String> time;

    @FXML
    private TableColumn<salesStore, String> totalPrice;

    @FXML
    private Button pay_50;

    @FXML
    private Button pay_200;

    @FXML
    private Button pay_100;

    @FXML
    private Button pay_500;

    @FXML
    private Button pay_1000;

    @FXML
    private Button amount;

    @FXML
    private TextField cash;

    @FXML
    private TextField mpesa;

    @FXML
    private Button total;

    @FXML
    private TextField total_text;

    @FXML
    private Button change;

    @FXML
    private TextField change_text;

    @FXML
    private Button item_list1;
    private HashMap<String, JSONObject> mItems;
    private Float totalPaid;
    //private ObservableList<ItemStore> data;

    JSONArray   jaItems = null;
    String      saleId = null;
    String      sURL = "https://demo.dewcis.com/hcm/pos_server";
    String      token = null;
    Group       group = new Group();
    int         myQuantity = 1;
    int price = 1;
    {
        totalPaid = new Float("0");
    }
    //int myTotal = myQuantity * price;

    /*public String login(String userName, String password) throws IOException {
        token = webComm.auth(sURL, userName, password);
        return token;
    }*/
    public void token() throws IOException {
        String userName = "root";
        String password = "baraza";
        token = webComm.auth(sURL, userName, password);
    }


    public void controlEvent(ActionEvent event) throws JSONException {
        Button aBtn = (Button) event.getSource();
        addSale(aBtn.getText());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        itemID.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        itemCategory.setCellValueFactory(new PropertyValueFactory<>("itemCategory"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        ObservableList<ItemStore> myItems = FXCollections.observableArrayList();
        ObservableList<salesStore> data = FXCollections.observableArrayList();
        mItems = new HashMap<String, JSONObject>();
        editableColumn();

        try {
            token();
            String sItems = sendData(sURL + "?view=405:1", token, "read", "{}");
            JSONObject jItems = new JSONObject(sItems);
            if (jItems.has("data")) {
                jaItems = jItems.getJSONArray("data");
                for (int j = 0; j < jaItems.length(); j++) {
                    JSONObject jItem = jaItems.getJSONObject(j);
                    myItems.add(new ItemStore(jItem.getString("item_name"), jItem.getString("item_category_name"), jItem.getString("item_id")));
                    String itemId = jItem.getString("item_id");
                    mItems.put(itemId, jItem);
                }
                itemList.setItems(myItems);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        itemList.setRowFactory( tv -> {

            TableRow<ItemStore> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ItemStore rowData = row.getItem();
                    String itemId = rowData.getItemID();
                    //System.out.println("Double click on: " + rowData.getItemNome());

                    System.out. println("Double click on: " + itemId);
                    JSONObject jItem = mItems.get(itemId);
                    System.out.println(jItem);
                    try {
                        price = Integer.parseInt(jItem.getString("sales_price").replaceAll("[^a-zA-Z0-9]", ""));
                        System.out.println(price);
                        salesStore sStore = new salesStore();
                        sStore.setUnitPrice(price);
                        //sStore.setQuantity(Integer.toString(myQuantity));
                        sStore.setQuantity("1");
                        data.add(new salesStore(jItem.getString("item_category_name"), jItem.getString("item_id"), price, jItem.getString("item_name"), Integer.toString(myQuantity),"",sStore.getTotalPrice()));
                        itemsData.setItems(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                  });
            return row ;
        });


        FilteredList<ItemStore> filteredData = new FilteredList<>(myItems, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ItemStore -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (ItemStore.getItemName().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (ItemStore.getItemCategory().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                } else if (ItemStore.getItemID().toString().indexOf(searchKeyword) > -1) {
                    return true;
                } else
                    return false;
            });
        });
        SortedList<ItemStore> sortedData = new SortedList<>(filteredData);
        //bind the data with tableview
        sortedData.comparatorProperty().bind(itemList.comparatorProperty());
        //apply filtered data
        itemList.setItems(sortedData);

        //putting the cash buttons in a group
        Button pay_50 = new Button("50");
        Button pay_100 = new Button("100");
        Button pay_200 = new Button("200");
        Button pay_500 = new Button("500");
        Button pay_1000 = new Button("1000");
        Button amount = new Button("amount");
        group.getChildren().add(pay_50);
        group.getChildren().add(pay_100);
        group.getChildren().add(pay_200);
        group.getChildren().add(pay_500);
        group.getChildren().add(pay_1000);
        group.getChildren().add(amount);

    }

    private void addItems() {
    }

    public void addSale(String actionName) throws JSONException {
        if ("New Sale".equals(actionName)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String currDate = dateFormat.format(new Date());
            JSONObject jParams = new JSONObject();
            jParams.put("transaction_date", currDate);

            String sItems = sendData(sURL + "?view=405:0", token, "read", /*"{}",*/ jParams.toString());
            JSONObject jItems = new JSONObject(sItems);
            if (jItems.has("data")) {
                JSONObject jSale = jItems.getJSONObject("data");
                saleId = jSale.getString("keyfield");
            }
            //data.clear();
        }
    }

    @FXML
    public void total(ActionEvent actiontotal) {
        TableView<salesStore> table = itemsData;
        float sum = 0 ;
        for (salesStore item: table.getItems()) {
            sum = sum + Float.valueOf(item.getTotalPrice());
            System.out.println(sum);
            total_text.setText(String.valueOf(sum));

        }
    }

    @FXML
    public void addMoney(ActionEvent event) {
        Button aBtn = (Button) event.getSource();
        if(aBtn.getText().equals("0")) {
            totalPaid = new Float("0.0");
        } else {
            Float aPaid = new Float(aBtn.getText());
            totalPaid += aPaid;
        }
        cash.setText(totalPaid.toString());
    }


    private void editableColumn(){
        //boolean blank = true;
        quantity.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity.setOnEditCommit(e->{
            salesStore sItem = e.getTableView().getItems().get(e.getTablePosition().getRow());
            sItem.setQuantity(e.getNewValue());
            myQuantity = Integer.parseInt(e.getNewValue());
            System.out.println(myQuantity);
            /*try {
                updateSaleItem(sItem);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }*/

            itemsData.refresh();


            //System.out.println(myQuantity);
        });
        itemsData.setEditable(true);

    }

    public void updateSaleItem(salesStore sItem) throws JSONException {
        System.out.println("Sale Item Id : " + sItem.getId());

        JSONObject jSale = new JSONObject();
        jSale.put("quantity", sItem.getQuantity());
        jSale.put("item_id", sItem.getId());
        jSale.put("item_price", sItem.getUnitPrice());

        String aUrl = sURL + "?fnct=update&view=200:0:0:0&keydata=" + sItem.getId();
        String sData = webComm.sendData(aUrl, token, "grid_update", jSale.toString());
        JSONObject jData = new JSONObject(sData);
    }

    /*public void create_buttons(ActionEvent event) throws JSONException {
        GridPane pnItems = new GridPane();
        pnItems.setMinSize(1000, 300);
        pnItems.setPadding(new Insets(10, 10, 10, 10));
        pnItems.setVgap(5);
        pnItems.setHgap(5);
        pnItems.setAlignment(Pos.CENTER);

        EventHandler itemEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Button abtn = (Button) event.getSource();
                addItem(abtn.getId());
            }
        };

        List<Button> btnItems = new ArrayList<Button>();
        for (int il = 0; il < jaItems.length(); il++) {
            JSONObject jItem = jaItems.getJSONObject(il);
            String itemId = jItem.getString("item_id");
            String itemName = jItem.getString("item_name");

            mItems.put(itemId, jItem);

            int nCol = il % 6;
            int nRow = il / 6;

            Button btnItem = new Button(itemName);
            btnItem.setId(itemId);
            btnItem.setMinWidth(200);
            btnItem.setOnAction(itemEvent);
            pnItems.add(btnItem, nCol, nRow);
        }
    }

        public void addItem(String itemId) throws JSONException {
            System.out.println("Add Item " + itemId);

            if(saleId != null) {
                JSONObject jItem = mItems.get(itemId);

                JSONObject jSale = new JSONObject();
                jSale.put("item_id", itemId);
                jSale.put("quantity", "1");
                jSale.put("item_price", jItem.getString("sales_price"));

                String aUrl = sURL + "?fnct=insert&view=200:0:0:0&linkdata=" + saleId;
                String sData = webComm.sendData(aUrl, token, "grid_update", jSale.toString());
                JSONObject jData = new JSONObject(sData);

                if(jData.has("data") && (jData.getInt("ResultCode") == 0)) {
                    JSONObject jSaleItem = jData.getJSONObject("data");
                    if(jSaleItem.has("keyfield")) {
                        saleItem sItem = new saleItem(jSaleItem.getString("keyfield"), itemId, jItem.getString("item_name"), jItem.getString("item_category_name"), jItem.getString("sales_price"));
                        itemsData1.add(sItem);
                    }
                }
            }
        }*/
        


}

