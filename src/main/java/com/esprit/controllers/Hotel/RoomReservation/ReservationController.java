package com.esprit.controllers.Hotel.RoomReservation;

import com.esprit.models.RoomReservation;
import com.esprit.services.Hotel.RoomReservationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.SQLException;
import java.time.LocalDate;

public class ReservationController {

    @FXML
    private TableView<RoomReservation> reservationsTable;

    @FXML
    private TableColumn<RoomReservation, String> hotelColumn;

    @FXML
    private TableColumn<RoomReservation, String> roomTypeColumn;

    @FXML
    private TableColumn<RoomReservation, LocalDate> checkInColumn;

    @FXML
    private TableColumn<RoomReservation, LocalDate> checkOutColumn;

    @FXML
    private TableColumn<RoomReservation, String> guestNameColumn;

    @FXML
    private TableColumn<RoomReservation, Double> totalColumn;

    @FXML
    private TableColumn<RoomReservation, Integer> roomNumberColumn;

    @FXML
    private TableColumn<RoomReservation, Boolean> isConfirmedColumn;

    @FXML
    private TableColumn<RoomReservation, Void> actionColumn;

    private RoomReservationService reservationService;

    @FXML
    public void initialize() {
        reservationService = new RoomReservationService();

        // Set up columns
        hotelColumn.setCellValueFactory(cellData -> cellData.getValue().hotelNameProperty());
        roomTypeColumn.setCellValueFactory(cellData -> cellData.getValue().roomTypeProperty());
        checkInColumn.setCellValueFactory(cellData -> cellData.getValue().checkInDateProperty());
        checkOutColumn.setCellValueFactory(cellData -> cellData.getValue().checkOutDateProperty());
        guestNameColumn.setCellValueFactory(cellData -> cellData.getValue().guestNameProperty());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty().asObject());
        roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());
        isConfirmedColumn.setCellValueFactory(cellData -> cellData.getValue().isConfirmedProperty());

        // Set up action column
        Callback<TableColumn<RoomReservation, Void>, TableCell<RoomReservation, Void>> cellFactory =
                param -> new TableCell<>() {
                    private final Button confirmButton = new Button("Confirm");
                    private final Button cancelButton = new Button("Cancel");

                    {
                        confirmButton.getStyleClass().add("button");
                        confirmButton.getStyleClass().add("confirm");
                        confirmButton.setOnAction(event -> {
                            RoomReservation reservation = getTableView().getItems().get(getIndex());
                            reservationService.confirmReservation(reservation.getId(), true);
                            updateItem(null, false);
                        });

                        cancelButton.getStyleClass().add("button");
                        cancelButton.getStyleClass().add("cancel");
                        cancelButton.setOnAction(event -> {
                            RoomReservation reservation = getTableView().getItems().get(getIndex());
                            reservationService.confirmReservation(reservation.getId(), false);
                            updateItem(null, false);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            RoomReservation reservation = getTableView().getItems().get(getIndex());
                            if (reservation.isConfirmed()) {
                                setGraphic(cancelButton);
                            } else {
                                setGraphic(confirmButton);
                            }
                        }
                    }
                };

        actionColumn.setCellFactory(cellFactory);

        // Load reservations
        try {
            reservationsTable.setItems(FXCollections.observableList(reservationService.getAllReservations()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}