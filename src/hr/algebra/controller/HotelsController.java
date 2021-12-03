/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.viewmodel.HotelViewModel;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
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
public class HotelsController implements Initializable {

    private Map<TextField, Label> validationMap;

    private final ObservableList<HotelViewModel> hotels = FXCollections.observableArrayList();

    private HotelViewModel selectedHotelViewModel;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabEdit;
    @FXML
    private Tab tabList;
    @FXML
    private TableView<HotelViewModel> tvHotels;
    @FXML
    private TableColumn<HotelViewModel, String> tcAddress, tcCity;
    @FXML
    private TableColumn<HotelViewModel, Integer> tcStars;
    @FXML
    private TextField tfAddress, tfCity, tfStars;
    @FXML
    private Label lbAddressError, lbCityError, lbStarsError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initValidation();
        initHotels();
        initTable();
        addIntegerMask(tfStars);
        setupListeners();
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfAddress, lbAddressError),
                new AbstractMap.SimpleImmutableEntry<>(tfCity, lbCityError),
                new AbstractMap.SimpleImmutableEntry<>(tfStars, lbStarsError))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initHotels() {
        try {
            RepositoryFactory.getRepository().getHotels().forEach(hotel -> hotels.add(new HotelViewModel(hotel)));
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to load the form. Exiting...").show();
        }
    }

    private void initTable() {
        tcAddress.setCellValueFactory(hotel -> hotel.getValue().getAdressProperty());
        tcCity.setCellValueFactory(hotel -> hotel.getValue().getCityProperty());
        tcStars.setCellValueFactory(hotel -> hotel.getValue().getStarsProperty().asObject());
        tvHotels.setItems(hotels);
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
                bindHotel(null);
            }
        });
        hotels.addListener((ListChangeListener.Change<? extends HotelViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository().deleteHotel(pvm.getHotel());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                } else if (change.wasAdded()) {
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idHotel = RepositoryFactory.getRepository().addHotel(pvm.getHotel());
                            pvm.getHotel().setIdHotel(idHotel);
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
        if (tvHotels.getSelectionModel().getSelectedItem() != null) {
            hotels.remove(tvHotels.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        if (tvHotels.getSelectionModel().getSelectedItem() != null) {
            bindHotel(tvHotels.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEdit);            
        }
    }

    private void bindHotel(HotelViewModel viewModel) {
        resetForm();
        
        selectedHotelViewModel = viewModel != null ? viewModel : new HotelViewModel(null);
        tfAddress.setText(selectedHotelViewModel.getAdressProperty().get());
        tfCity.setText(selectedHotelViewModel.getCityProperty().get());
        tfStars.setText(String.valueOf(selectedHotelViewModel.getStarsProperty().get()));}

    private void resetForm() {
        validationMap.values().forEach(lb -> lb.setVisible(false));
    }

    @FXML
    private void commit(ActionEvent event) {
        if (formValid()) {
            selectedHotelViewModel.getHotel().setAddress(tfAddress.getText().trim());
            selectedHotelViewModel.getHotel().setCity(tfCity.getText().trim());
            selectedHotelViewModel.getHotel().setStars(Integer.valueOf(tfStars.getText().trim()));
            if (selectedHotelViewModel.getHotel().getIdHotel() == 0) {
                hotels.add(selectedHotelViewModel);
            } else {
                try {
                    // we cannot listen to the upates
                    RepositoryFactory.getRepository().updateHotel(selectedHotelViewModel.getHotel());
                    tvHotels.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedHotelViewModel = null;
            tpContent.getSelectionModel().select(tabList);
            resetForm();
        }
    }

    private boolean formValid() {
        // discouraged but ok!
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()) {
                validationMap.get(tf).setVisible(true);
                ok.set(false);
            } else {
                validationMap.get(tf).setVisible(false);
            }
        });
        return ok.get();
    }
    
}
