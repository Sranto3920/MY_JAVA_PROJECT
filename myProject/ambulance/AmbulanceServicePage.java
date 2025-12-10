package myProject.ambulance;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import myProject.ClientFX;

public class AmbulanceServicePage {
    private final List<AmbulanceInfo> providers = seed();

    public BorderPane getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label title = new Label("Ambulance & Emergency");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Button help = new Button("Help");
        help.setOnAction(e -> ClientFX.openHelpChat());
        HBox top = new HBox(10, title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        top.getChildren().addAll(spacer, help);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(8));
        root.setTop(top);

        ListView<AmbulanceInfo> list = new ListView<>();
        list.getItems().addAll(providers);
        list.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(AmbulanceInfo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.name + " • ETA: " + item.etaMinutes + " mins • " + item.city);
                }
            }
        });
        list.getSelectionModel().selectFirst();

        GridPane form = new GridPane();
        form.setHgap(10); form.setVgap(10); form.setPadding(new Insets(10));
        TextField name = new TextField(); name.setPromptText("Your name");
        TextField phone = new TextField(); phone.setPromptText("Contact number");
        TextField pickup = new TextField(); pickup.setPromptText("Pickup location");
        ComboBox<String> priority = new ComboBox<>(); priority.getItems().addAll("Critical", "High", "Normal"); priority.getSelectionModel().selectFirst();
        TextArea notes = new TextArea(); notes.setPromptText("Notes (landmarks, patient condition)"); notes.setPrefRowCount(3);

        form.addRow(0, new Label("Name"), name);
        form.addRow(1, new Label("Phone"), phone);
        form.addRow(2, new Label("Pickup"), pickup);
        form.addRow(3, new Label("Urgency"), priority);
        form.addRow(4, new Label("Notes"), notes);

        Button request = new Button("Request Ambulance");
        request.setOnAction(e -> {
            AmbulanceInfo sel = list.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Request placed");
            alert.setHeaderText("Dispatching " + sel.name);
            alert.setContentText("ETA: " + sel.etaMinutes + " mins\nDriver: " + sel.driverName + "\nPhone: " + sel.phone);
            alert.showAndWait();
        });

        VBox right = new VBox(12, new Label("Dispatch details"), form, request);
        right.setPadding(new Insets(10));
        right.setAlignment(Pos.TOP_LEFT);

        root.setCenter(list);
        BorderPane.setMargin(list, new Insets(0,10,0,0));
        root.setRight(right);

        return root;
    }

    private List<AmbulanceInfo> seed() {
        List<AmbulanceInfo> list = new ArrayList<>();
        list.add(new AmbulanceInfo("LifeLine Express", "Dhaka", 12, "Hasan", "01800000001"));
        list.add(new AmbulanceInfo("RapidAid", "Chittagong", 18, "Rahman", "01800000002"));
        list.add(new AmbulanceInfo("CareMove", "Rajshahi", 25, "Nabila", "01800000003"));
        return list;
    }

    private record AmbulanceInfo(String name, String city, int etaMinutes, String driverName, String phone) { }
}
