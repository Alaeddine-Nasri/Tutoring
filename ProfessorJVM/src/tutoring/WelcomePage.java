package tutoring;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class WelcomePage extends VBox {

    private Label welcomeLabel;
    VBox sessions = new VBox(10);
    public WelcomePage(IProfessor professor) {
        initializeUI(professor);
    }

    private void initializeUI(IProfessor professor) {

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        Label projectNameLabel = new Label("Tutoring");
        projectNameLabel.getStyleClass().add("title");
        topBar.getChildren().add(projectNameLabel);

        HBox professorInfo = new HBox(10);
        professorInfo.setAlignment(Pos.CENTER_RIGHT);

        VBox imageVBox = new VBox(20);
        imageVBox.setAlignment(Pos.CENTER_RIGHT);
        imageVBox.getStyleClass().add("avatar");

        
        sessions.setAlignment(Pos.TOP_CENTER);
        Label professorNameLabel = new Label("Professor: ");
        try {
            String welcomeMessage = "Welcome, " + professor.getName() + "!\n";
            welcomeMessage += "ID: " + professor.getUUID() + "\n";

            professorNameLabel = new Label(professor.getName());
            professorNameLabel.setStyle("-fx-font-size: 14px;");

            welcomeLabel = new Label(welcomeMessage);
            welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            

            if (professor.getSessions() != null && !professor.getSessions().isEmpty()) {
                // Display sessions
                int sessionsPerRow = 3;
                int sessionCount = 0;

                HBox currentRow = null;

                for (ISession session : professor.getSessions()) {
                    if (sessionCount % sessionsPerRow == 0) {
                        // Start a new row
                        currentRow = new HBox(10); // Adjust spacing as needed
                        sessions.getChildren().add(currentRow);
                    }
                    VBox sessionBox = new VBox();

                    VBox redBox = new VBox();
                    redBox.getStyleClass().add("sessionCard");
                    
                    
                    sessionBox.setVgrow(redBox, Priority.ALWAYS);
                    Region moduleColor= new Region();
                    //moduleColor.getStyleClass().add("moduleColor");
                    try {
                        Modules module = session.getModule();
                        if (module != null) {
                            switch (module) {
                                case Mathemathics:
                                    moduleColor.getStyleClass().add("moduleColorMath");
                                    break;
                                case Algorithmic:
                                    moduleColor.getStyleClass().add("moduleColorAlgo");
                                    break;
                                case Physics:
                                    moduleColor.getStyleClass().add("moduleColorPhys");
                                    break;
                                // Add more cases for other modules if needed
                                default:
                                    // Use a default background if module is not recognized
                                    moduleColor.getStyleClass().add("moduleColorDefault");
                            }
                        } else {
                            // Use a default background if module is null
                            moduleColor.getStyleClass().add("moduleColorDefault");
                        }
                        } catch (Exception e) {
                            // Handle the RemoteException as needed
                            e.printStackTrace(); // You may want to log or handle this exception appropriately
                        }
                       // moduleColor.getStyleClass().add("moduleColor");
                    
                   // moduleColor.setStyle( "-fx-background-image: url('assets/photos/Math.png');");
                    HBox sessionInfo = new HBox();
                    sessionInfo.setAlignment(Pos.CENTER);
                    sessionInfo.getStyleClass().add("sessionInfo");
                    Label sessionLabel;
                    Label sessionParticipants;
                   

                    // Additional UI elements for session details
                    HBox sessionInfo2 = new HBox();
                    sessionInfo2.setAlignment(Pos.CENTER);
                    sessionInfo2.getStyleClass().add("sessionInfo");
                    
                    Label sessionTime;
                    Label sessionPrice;

                    try {
                    	 sessionTime = new Label(session.getDate().toString());
                        sessionPrice = new Label(session.getPrice() + "$");
                        sessionLabel = new Label(session.getModule().toString());
                        sessionParticipants = new Label(session.getCapacity()+ "");
                    } catch (Exception e) {
                        // Handle the exception as needed
                        e.printStackTrace();
                        sessionLabel = new Label("Module Unavailable");
                        sessionParticipants = new Label("Capacity Unavailable");
                        sessionPrice = new Label("Price Unavailable");
                        sessionTime = new Label("Date Unavailable");
                    }
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    sessionInfo.getChildren().addAll(sessionLabel, spacer, sessionParticipants);
                    Region spacer2 = new Region();
                    HBox.setHgrow(spacer2, Priority.ALWAYS);
                    sessionInfo2.getChildren().addAll(sessionTime, spacer2, sessionPrice);
                   
                    
                   // Label sessionLabel2 = new Label("Session2: ");
                    redBox.getChildren().addAll(moduleColor,sessionInfo,sessionInfo2);
                    sessionBox.getChildren().addAll(redBox);

                    currentRow.getChildren().add(sessionBox);

                    sessionCount++;
                }
                VBox addsessionbox = new VBox();
                addsessionbox.getStyleClass().add("addSessionCard");
                Label addSessionLabel = new Label("You want to add a new session? Click here :)");
                Button addSessionButton = new Button("Add Session");
                
                addSessionButton.setOnAction(e -> {
                    	Dialog<Boolean> addSessionDialog = new Dialog<>();
                        addSessionDialog.setTitle("Add Session");
                        
                        ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
                        addSessionDialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

                        
                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(20, 150, 10, 10));

                        
                        ObservableList<Modules> modulesList = FXCollections.observableArrayList(Modules.values()); 
                        ComboBox<Modules> moduleComboBox = new ComboBox<>(modulesList);
                        moduleComboBox.setPromptText("Select Module");
                        moduleComboBox.getStyleClass().add("combo-box");
                        
                        ObservableList<Level> levelsList = FXCollections.observableArrayList(Level.values()); 
                        ComboBox<Level> levelsListBox = new ComboBox<>(levelsList);
                        levelsListBox.setPromptText("Select Level");
                        levelsListBox.getStyleClass().add("combo-box");
                        
                        TextField numberField = new TextField();
                        numberField.setPromptText("Enter Capacity");
                        
                        TextField priceField = new TextField();
                        priceField.setPromptText("Enter rate");

                        DatePicker dateInput = new DatePicker();
                        dateInput.setPromptText("Select Date");
                        
                        ComboBox<Integer> hourComboBox = new ComboBox<>(FXCollections.observableArrayList(9, 10, 11, 14, 15,16,17));
                        hourComboBox.setPromptText("Select Hour");

                        ComboBox<Integer> minuteComboBox = new ComboBox<>(FXCollections.observableArrayList(0, 15, 30, 45));
                        minuteComboBox.setPromptText("Select Minute");
                        
                        CheckBox eurCheckBox = new CheckBox("EUR");
                        CheckBox dollarCheckBox = new CheckBox("DOLLAR");


                        grid.add(new Label("Module:"), 0, 0);
                        grid.add(moduleComboBox, 1, 0); // Replace moduleField with moduleComboBox
                        grid.add(new Label("Level:"), 0, 1);
                        grid.add(levelsListBox, 1, 1);
                        grid.add(new Label("Capacity:"), 0, 2);
                        grid.add(numberField, 1, 2);
                        grid.add(new Label("Date:"), 0, 3);
                        grid.add(dateInput, 1, 3);
                        grid.add(new Label("Time:"), 0, 4);
                        grid.add(hourComboBox, 1, 4);
                        grid.add(minuteComboBox, 2, 4);
                        grid.add(new Label("¨Price:"), 0, 5);
                        grid.add(priceField, 1, 5);
                        grid.add(new Label("Currency:"), 0, 6);
                        grid.add(eurCheckBox, 1, 6);
                        grid.add(dollarCheckBox, 2, 6);
                       

                        
                        addSessionDialog.getDialogPane().setContent(grid);

                        // Request focus on the first field by default
                        Platform.runLater(moduleComboBox::requestFocus);
                        addSessionDialog.setResultConverter(dialogButton -> {
                        	try {
                            if (dialogButton == addButtonType) {
                            	Modules selectedModule = moduleComboBox.getSelectionModel().getSelectedItem();
                            	Level selectedlevel = levelsListBox.getSelectionModel().getSelectedItem();
                            	 int enteredNumber = Integer.parseInt(numberField.getText());
                                 LocalDate selectedDate = dateInput.getValue();
                                 int selectedHour = hourComboBox.getSelectionModel().getSelectedItem();
                                 int selectedMinute = minuteComboBox.getSelectionModel().getSelectedItem();
                                 int enteredPRICE = Integer.parseInt(priceField.getText());
                                 
                                 Currency selectedCurrency = null;
                                 if (eurCheckBox.isSelected()) {
                                     selectedCurrency = Currency.EUR;
                                 } else if (dollarCheckBox.isSelected()) {
                                     selectedCurrency = Currency.DOLLAR;
                                 }
                                
                                boolean sessionCreated = professor.createSession(selectedModule,selectedlevel,enteredNumber,LocalTime.of(selectedHour, selectedMinute),
                                        LocalTime.of(selectedHour, selectedMinute),selectedDate,enteredPRICE,selectedCurrency);
                          	  if (sessionCreated) {
                          		 System.out.println("Session Created for Module: " + selectedCurrency + " Level "+selectedlevel + " DATA / " + selectedDate + " Capacity " + enteredNumber);
                                    // or notify the user that the session was added successfully
                          		updateSessionDisplay(professor);
                                } else {
                              	  System.out.println("Session Error");
                                }
                          	  

                                return sessionCreated;
                            }
                            return null;
                        } catch (Exception ex) {
                            ex.printStackTrace(); // Handle RemoteException as needed
                        }
							return null;});
                        Optional<Boolean> result = addSessionDialog.showAndWait();
                    	
                    
                    
                });
                sessions.getChildren().addAll(addSessionLabel, addSessionButton);
            } else {
                // Show message and button to add sessions
                Label noSessionLabel = new Label("No sessions available. Add sessions below:");
                Button addSessionButton = new Button("Add Session");
                addSessionButton.setOnAction(e -> {
                    try {
                    	 boolean sessionCreated = professor.createSession(Modules.Algorithmic,Level.BEGINNER,1,LocalTime.of(9, 0),LocalTime.of(9, 0),LocalDate.of(2023, 12, 1),20,Currency.EUR);
                    	  if (sessionCreated) {
                    		  System.out.println("Session Created");
                              // or notify the user that the session was added successfully
                          } else {
                        	  System.out.println("Session Error");
                          }
                    
                    } catch (Exception ex) {
                        ex.printStackTrace(); // Handle RemoteException as needed
                    }
                });
                sessions.getChildren().addAll(noSessionLabel, addSessionButton);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        professorInfo.getChildren().addAll(professorNameLabel, imageVBox);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(topBar, professorInfo);
        header.getStyleClass().add("header");

        

        SplitPane splitPane = new SplitPane(createMenu(), sessions);
        splitPane.setDividerPositions(0.2);

        HBox mainPage = new HBox(10);
        mainPage.setAlignment(Pos.CENTER);
        mainPage.getChildren().addAll(splitPane);
        mainPage.getStyleClass().add("mainpage");
        splitPane.setStyle("-fx-border-color: null;-fx-background-color: #F5F5F5;");

        HBox.setHgrow(topBar, Priority.ALWAYS);
        HBox.setHgrow(splitPane, Priority.ALWAYS);

        VBox fullPage = new VBox(10);
        fullPage.setAlignment(Pos.TOP_CENTER);
        fullPage.getChildren().addAll(header, mainPage);

        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(0));
        getChildren().addAll(fullPage);
    }

    private VBox createMenu() {
        Label menuItem = createMenuItem("All Sessions");
        Label menuItem2 = createMenuItem("Next Sessions");
        Label menuItem3 = createMenuItem("Past Sessions");

        VBox menu = new VBox(5);
        menu.setAlignment(Pos.TOP_LEFT);
        menu.getChildren().addAll(menuItem, menuItem2, menuItem3);
        menu.getStyleClass().add("menu");

        // Set the first menu item as selected by default
        selectMenuItem(menuItem);

        return menu;
    }

    private Label createMenuItem(String text) {
        Label menuItem = new Label(text);
        menuItem.getStyleClass().add("menuItem");

        // Set the label to take full width
        menuItem.setMaxWidth(Double.MAX_VALUE);
        menuItem.setAlignment(Pos.CENTER_LEFT);
        menuItem.setStyle("-fx-pref-height: 30;");

        // Event handling for mouse click
        menuItem.setOnMouseClicked(event -> {
            // Reset styles for all menu items
            for (javafx.scene.Node node : ((VBox) menuItem.getParent()).getChildren()) {
                node.getStyleClass().remove("selected");
            }

            // Apply selected style to the clicked menu item
            menuItem.getStyleClass().add("selected");
        });

        return menuItem;
    }


    private void selectMenuItem(Label menuItem) {
        // Apply selected style to the specified menu item
        menuItem.getStyleClass().add("selected");
    }
    public void updateSessionDisplay(IProfessor professor) {
        try {
            // Clear existing sessions
            sessions.getChildren().clear();

            if (professor.getSessions() != null && !professor.getSessions().isEmpty()) {
                // Display sessions
                int sessionsPerRow = 3;
                int sessionCount = 0;

                HBox currentRow = null;

                for (ISession session : professor.getSessions()) {
                    if (sessionCount % sessionsPerRow == 0) {
                        // Start a new row
                        currentRow = new HBox(10); // Adjust spacing as needed
                        sessions.getChildren().add(currentRow);
                    }
                    
                    // Create and add UI elements for the session
                    VBox sessionBox = createSessionBox(session);
                    currentRow.getChildren().add(sessionBox);

                    sessionCount++;
                }
                // ... (existing logic for displaying add session option)
            } else {
                // ... (existing logic for displaying no sessions and add session option)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private VBox createSessionBox(ISession session) {
        VBox sessionBox = new VBox();

        VBox redBox = new VBox();
        redBox.getStyleClass().add("sessionCard");

        sessionBox.setVgrow(redBox, Priority.ALWAYS);
        Region moduleColor = new Region();
        try {
        Modules module = session.getModule();
        if (module != null) {
            switch (module) {
                case Mathemathics:
                    moduleColor.getStyleClass().add("moduleColorMath");
                    break;
                case Algorithmic:
                    moduleColor.getStyleClass().add("moduleColorAlgo");
                    break;
                case Physics:
                    moduleColor.getStyleClass().add("moduleColorPhys");
                    break;

                // Add more cases for other modules if needed
                default:
                    // Use a default background if module is not recognized
                    moduleColor.getStyleClass().add("moduleColorDefault");
            }
        } else {
            // Use a default background if module is null
            moduleColor.getStyleClass().add("moduleColorDefault");
        }
        } catch (Exception e) {
            // Handle the RemoteException as needed
            e.printStackTrace(); // You may want to log or handle this exception appropriately
        }
       // moduleColor.getStyleClass().add("moduleColor");

        HBox sessionInfo = new HBox();
        sessionInfo.setAlignment(Pos.CENTER);
        sessionInfo.getStyleClass().add("sessionInfo");
        Label sessionLabel;
        Label sessionParticipants;
       

        // Additional UI elements for session details
        HBox sessionInfo2 = new HBox();
        sessionInfo2.setAlignment(Pos.CENTER);
        sessionInfo2.getStyleClass().add("sessionInfo");
        
        Label sessionTime;
        Label sessionPrice;

        try {
        	 sessionTime = new Label(session.getDate().toString());
            sessionPrice = new Label(session.getPrice() + "$");
            sessionLabel = new Label(session.getModule().toString());
            sessionParticipants = new Label(session.getCapacity()+ "");
        } catch (Exception e) {
            // Handle the exception as needed
            e.printStackTrace();
            sessionLabel = new Label("Module Unavailable");
            sessionParticipants = new Label("Capacity Unavailable");
            sessionPrice = new Label("Price Unavailable");
            sessionTime = new Label("Date Unavailable");
        }
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        sessionInfo.getChildren().addAll(sessionLabel, spacer, sessionParticipants);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        sessionInfo2.getChildren().addAll(sessionTime, spacer2, sessionPrice);

        // Add more UI elements as needed

        redBox.getChildren().addAll(moduleColor, sessionInfo, sessionInfo2);
        sessionBox.getChildren().addAll(redBox);
        
        

        return sessionBox;
    }



}