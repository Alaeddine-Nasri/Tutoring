package tutoring;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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

    public WelcomePage(IStudent student) {
        initializeUI(student);
    }

    private void initializeUI(IStudent student) {

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

        VBox sessions = new VBox(10);
        sessions.setAlignment(Pos.TOP_CENTER);
        Label studentNameLabel = new Label("Student: ");
        try {
            String welcomeMessage = "Welcome, " + student.getName() + "!\n";
            welcomeMessage += "ID: " + student.getUUID() + "\n";

            studentNameLabel = new Label("Student "+student.getName());
            studentNameLabel.setStyle("-fx-font-size: 14px;");

            welcomeLabel = new Label(welcomeMessage);
            welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            
            IProfessorList professorList = (IProfessorList) Naming.lookup("rmi://127.0.0.1:1101/professor_list");
            List<IProfessor> professors = professorList.findProf("");
            for (IProfessor professor : professors) {
            	int sessionsPerRow = 3;
                int sessionCount = 0;

                HBox currentRow = null;
                List<ISession> professorSessions = professor.getSessions();
                for (ISession session : professorSessions) {
                    // Display session information as needed
                	if (sessionCount % sessionsPerRow == 0) {
                        // Start a new row
                        currentRow = new HBox(10); // Adjust spacing as needed
                        sessions.getChildren().add(currentRow);
                    }
                    VBox sessionBox = new VBox();

                    VBox redBox = new VBox();
                    redBox.getStyleClass().add("sessionCardStudent");
                    
                    
                    sessionBox.setVgrow(redBox, Priority.ALWAYS);
                    Region moduleColor= new Region();
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
                    HBox sessionJoin = new HBox();
                    sessionJoin.setAlignment(Pos.CENTER);
                    sessionJoin.getStyleClass().add("sessionJoin");
                    Label sessionJoinLabel = new Label(professor.getName());
                    Region spacer3 = new Region();
                    HBox.setHgrow(spacer3, Priority.ALWAYS);
                    
                    
                    Button joinSessionButton = new Button();
                    joinSessionButton.getStyleClass().add("button-needs");
                    try {
                        List<IStudent> sessionStudents = session.getStudents();
                        boolean isStudentInSession = sessionStudents.contains(student);

                        updateButtonAppearance(joinSessionButton, isStudentInSession);
                        joinSessionButton.getStyleClass().add("button-needs");
                        joinSessionButton.setOnAction(e -> {
                            try {
                                List<IStudent> updatedSessionStudents = session.getStudents();
                                boolean updatedIsStudentInSession = updatedSessionStudents.contains(student);

                                if (updatedIsStudentInSession) {
                                    String result = session.deleteRegistredStudent(student);
                                    System.out.println("Unjoining session: " + session.toString() + "\nResult: " + result);
                                } else {
                                    String result = session.addStudent(student);
                                    System.out.println("Joining session: " + session.toString() + "\nResult: " + result);
                                }

                                // Update button appearance after the action
                                updateButtonAppearance(joinSessionButton, !updatedIsStudentInSession);
                                joinSessionButton.getStyleClass().add("button-needs");

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    
                    sessionJoin.getChildren().addAll(sessionJoinLabel,spacer,joinSessionButton);
                    redBox.getChildren().addAll(moduleColor,sessionInfo,sessionInfo2,sessionJoin);
                    sessionBox.getChildren().addAll(redBox);
                    

                    currentRow.getChildren().add(sessionBox);

                    sessionCount++;
                	
                    /*Label sessionLabel = new Label(session.toString());
                    sessions.getChildren().add(sessionLabel);*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        professorInfo.getChildren().addAll(studentNameLabel, imageVBox);

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
    
 // Method to update the button appearance based on the session status
    private void updateButtonAppearance(Button button, boolean isStudentInSession) {
        if (isStudentInSession) {
            button.setText("Unjoin");
            button.getStyleClass().clear();
            button.getStyleClass().addAll("button-needs","button-unjoin");
        } else {
            button.setText("Join");
            button.getStyleClass().clear();
            button.getStyleClass().addAll("button-needs","button-join");
        }
    }


    private void selectMenuItem(Label menuItem) {
        // Apply selected style to the specified menu item
        menuItem.getStyleClass().add("selected");
    }


}
