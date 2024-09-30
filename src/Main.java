//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\s{1}(\\d{4}-\\d{2}-\\d{2})\\s{1}(\\d{4}-\\d{2}-\\d{2})");

    public Main() throws IOException, InterruptedException {
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        Text text = new Text();
        text.setLayoutY(80.0);
        text.setLayoutX(80.0);
        ObservableList<Order> orders = FXCollections.observableArrayList();
        TableView<Order> table = new TableView(orders);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080")).GET().build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            String rowOrders = (String)response.body();
            System.out.println(rowOrders);
            Matcher matcher = PATTERN.matcher(rowOrders);
            List<Order> resultList = new ArrayList();
            int index = 0;
            while(matcher.find()) {
                System.out.println("Match found!");
                System.out.println(matcher.group());
                String[] result = matcher.group().split(" ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                int id = Integer.parseInt(result[0]);
                System.out.println(id);
                LocalDate orderDate = LocalDate.parse(result[1], formatter);
                System.out.println(orderDate);
                LocalDate lastUpdateDate = LocalDate.parse(result[2], formatter);
                System.out.println(lastUpdateDate);
                Order addedOrder = new Order(id, orderDate, lastUpdateDate);
                resultList.add(addedOrder);
            }

            System.out.println("Result list size: " + resultList.size());
            if (resultList.isEmpty()) {
                System.err.println("No data received from server.");
            } else {
                table.setItems(FXCollections.observableList(resultList));
            }
        } catch (IOException | InterruptedException var19) {
            Exception e = var19;
            System.out.println("Error occurred during HTTP call: " + ((Exception)e).getMessage());
        }

        table.setPrefWidth(600.0);
        table.setPrefHeight(400.0);
        TableColumn<Order, Integer> IDColumn = new TableColumn("ID");
        IDColumn.setCellValueFactory(new PropertyValueFactory("id"));
        table.getColumns().add(IDColumn);

        TableColumn<Order, LocalDate> OrderDateColumn = new TableColumn("Order Date");
        OrderDateColumn.setCellValueFactory(new PropertyValueFactory("orderDate"));
        table.getColumns().add(OrderDateColumn);

        TableColumn<Order, LocalDate> OrderLastUpdateDateColumn = new TableColumn("Order Last Update Date");
        OrderLastUpdateDateColumn.setCellValueFactory(new PropertyValueFactory("orderLastUpdateDate"));
        table.getColumns().add(OrderLastUpdateDateColumn);

        TableColumn<Order, Boolean> checkboxColumn = new TableColumn<>("Active");
        checkboxColumn.setCellValueFactory(param -> {
            LocalDate lastUpdate = param.getValue().getOrderLastUpdateDate();
            return new SimpleBooleanProperty(lastUpdate.isAfter(LocalDate.now().minusMonths(6)));
        });
        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkboxColumn));
        table.getColumns().add(checkboxColumn);

        FlowPane root = new FlowPane(10.0, 10.0, new Node[]{table});
        Group group = new Group(root);
        Scene scene = new Scene(group, 300.0, 250.0);
        stage.setScene(scene);
        stage.setTitle("TableView in JavaFX");
        stage.show();
    }
}
