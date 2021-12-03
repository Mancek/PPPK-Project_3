/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.model.Hotel;
import hr.algebra.model.Person;
import hr.algebra.utilities.ValidationUtils;
import hr.algebra.viewmodel.ReservationViewModel;
import java.net.URL;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author Manuel
 */
public class ReservationsController implements Initializable {

    private Map<TextField, Label> validationMap;

    private final ObservableList<ReservationViewModel> reservations = FXCollections.observableArrayList();

    private ReservationViewModel selectedReservationViewModel;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabEdit;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<ReservationViewModel> tvReservations;
    @FXML
    private TableColumn<ReservationViewModel, String> tcFromDate, tcToDate;
    @FXML
    private TableColumn<ReservationViewModel, Integer> tcHotelID, tcPersonID;
    @FXML
    private TextField tfHotelID, tfPersonID, tfFromDate, tfToDate;
    @FXML
    private Label lbHotelIDError, lbPersonIDError, lbFromDateError, lbToDateError;
    
    private List<Hotel> hotels;
    private List<Person> people;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValidation();
        initReservations();
        initTable();
        addIntegerMask(tfHotelID);
        addIntegerMask(tfPersonID);
        setupListeners();
        initForeignKeys();
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfHotelID, lbHotelIDError),
                new AbstractMap.SimpleImmutableEntry<>(tfPersonID, lbPersonIDError),
                new AbstractMap.SimpleImmutableEntry<>(tfFromDate, lbFromDateError),
                new AbstractMap.SimpleImmutableEntry<>(tfToDate, lbToDateError))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initReservations() {
        try {
            RepositoryFactory.getRepository().getReservations().forEach(reservation -> reservations.add(new ReservationViewModel(reservation)));
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Exiting...").show();
        }
    }

    private void initTable() {
        tcHotelID.setCellValueFactory(reservation -> reservation.getValue().getHotelIdProperty().asObject());
        tcPersonID.setCellValueFactory(reservation -> reservation.getValue().getPersonIdProperty().asObject());
        tcFromDate.setCellValueFactory(reservation -> reservation.getValue().getFromDateProperty());
        tcToDate.setCellValueFactory(reservation -> reservation.getValue().getToDateProperty());
        tvReservations.setItems(reservations);
    }

    private void addIntegerMask(TextField tf) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("\\d*")) {
                return change;
            }
            return null;
        };
        // first we must convert integer to string, and then we apply filter
        tf.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    }

    private void setupListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabEdit)) {
                bindReservation(null);
            }
        });
        reservations.addListener((ListChangeListener.Change<? extends ReservationViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository().deleteReservation(pvm.getReservation());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idReservation = RepositoryFactory.getRepository().addReservation(pvm.getReservation());
                            pvm.getReservation().setIdReservation(idReservation);
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    @FXML
    private void delete(ActionEvent event) {
        if (tvReservations.getSelectionModel().getSelectedItem() != null) {
            reservations.remove(tvReservations.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvReservations.getSelectionModel().getSelectedItem() != null) {
            bindReservation(tvReservations.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEdit);            
        }
    }

    private void bindReservation(ReservationViewModel viewModel) {
        resetForm();
        
        selectedReservationViewModel = viewModel != null ? viewModel : new ReservationViewModel(null);
        tfHotelID.setText(String.valueOf(selectedReservationViewModel.getHotelIdProperty().get()));
        tfPersonID.setText(String.valueOf(selectedReservationViewModel.getPersonIdProperty().get()));
        tfFromDate.setText(selectedReservationViewModel.getFromDateProperty().get());
        tfToDate.setText(selectedReservationViewModel.getToDateProperty().get());
    }

    private void resetForm() {
        validationMap.values().forEach(lb -> lb.setVisible(false));
    }

    @FXML
    private void commit(ActionEvent event) {
        if (formValid()) {
            selectedReservationViewModel.getReservation().setHotelID(Integer.valueOf(tfHotelID.getText().trim()));
            selectedReservationViewModel.getReservation().setPersonID(Integer.valueOf(tfPersonID.getText().trim()));
            selectedReservationViewModel.getReservation().setFromDate(tfFromDate.getText().trim());
            selectedReservationViewModel.getReservation().setToDate(tfToDate.getText().trim());
            
            if (selectedReservationViewModel.getReservation().getIdReservation() == 0) {
                reservations.add(selectedReservationViewModel);
            } else {
                try {
                    // we cannot listen to the upates
                    RepositoryFactory.getRepository().updateReservation(selectedReservationViewModel.getReservation());
                    tvReservations.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedReservationViewModel = null;
            tpContent.getSelectionModel().select(tabList);
            resetForm();
        }
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty() || tf.getId().contains("Date") && !ValidationUtils.isValidDate(tf.getText().trim(), "dd.MM.yyyy")
                || tf.getId().contains("HotelID") && hotels.stream().filter(h -> h.getIdHotel().equals(Integer.parseInt(tf.getText().trim()))).findFirst().equals(Optional.empty())
                || tf.getId().contains("PersonID") && people.stream().filter(p -> p.getIdPerson().equals(Integer.parseInt(tf.getText().trim()))).findFirst().equals(Optional.empty())) {
                validationMap.get(tf).setVisible(true);
                ok.set(false);
            } else {
                validationMap.get(tf).setVisible(false);
            }
        });
        return ok.get();
    }

    private void initForeignKeys() {
        try {
            hotels = RepositoryFactory.getRepository().getHotels();
            people = RepositoryFactory.getRepository().getPeople();
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Exiting...").show();
        }
    }
    
}
