package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Label label;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfEmail;
    @FXML
    private TableView<User> tvUsers;
    @FXML
    private TableColumn<User,Integer> colId;
    @FXML
    private TableColumn<User,String> colFirstName;
    @FXML
    private TableColumn<User,String> colLastName;
    @FXML
    private TableColumn<User,String> colEmail;
    @FXML
    private TableColumn<User,Integer> colAge;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    @FXML
    private void handleButtonAction(ActionEvent event){
        //label.setText("Sup?!");
        if (event.getSource() == btnInsert){
            insertRecord();
            System.out.println("User inserted");
        }else if(event.getSource() == btnUpdate){
            updateRecord();
            System.out.println("User updated");
        }else if(event.getSource() == btnDelete){
            deleteRecord();
            System.out.println("User deleted");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showUsers();
    }

    public Connection getConnection(){
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/DataBase","root","secret");

        }catch (Exception e){
            System.out.println("Error " + e.getMessage());
            return null;
        }
        return connection;
    }

    public ObservableList<User> getUserList(){
        ObservableList<User> userList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM User";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            User user;
            while (resultSet.next()){
                user = new User(resultSet.getInt("id"),resultSet.getString("firstName"),
                        resultSet.getString("lastName"),resultSet.getInt("age"),resultSet.getString("email"));
                userList.add(user);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return userList;
    }

    public void showUsers(){
        ObservableList<User> list = getUserList();

        colId.setCellValueFactory(new PropertyValueFactory<User,Integer>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        colAge.setCellValueFactory(new PropertyValueFactory<User,Integer>("age"));
        colEmail.setCellValueFactory(new PropertyValueFactory<User,String>("email"));

        tvUsers.setItems(list);
    }

    private void insertRecord(){
        String query = "INSERT INTO User VALUES (" + tfId.getText() + ",'" + tfFirstName.getText() + "','" + tfLastName.getText() +
                "'," + tfAge.getText() + ",'" + tfEmail.getText() + "')";
        executeQuery(query);
        showUsers();
    }

    private void updateRecord(){
        String query = "UPDATE User SET firstName = '" + tfFirstName.getText() + "', lastName = '" + tfLastName.getText() + "', age = " + tfAge.getText() +
                ", email = '" + tfEmail.getText() + "' WHERE id = " + tfId.getText() + "";
        executeQuery(query);
        showUsers();
    }

    private void deleteRecord(){
        String querry = "DELETE FROM User WHERE id =" + tfId.getText() + "";
        executeQuery(querry);
        showUsers();
    }

    private void executeQuery(String query) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
