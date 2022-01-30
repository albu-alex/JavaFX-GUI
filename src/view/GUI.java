package view;

import controller.GUIController;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Example;
import model.ProgramState;

import java.util.Objects;

public class GUI extends Application {
	private GUIController controller;

	private ProgramState selectedThread;

	private ListView<Integer> threadListView; 
	private ListView<String> outputListView;
	private ListView<String> fileTableListView;
	private ListView<String> stackListView;
	private TableView<Integer> heapTableView; // here I need it to store Integers because that's the key of the Heap structure
	private TableView<String> symbolTableTableView;
	private TextField programStateCountTextField;
	
	private Button advanceOneStepButton;
	private Button fullProgramExecutionButton;
	private Button selectExampleButton;
	private ListView<Example> exampleListView;
	
	private final int MINIMUM_MAIN_WINDOW_WIDTH = 600; // in pixels
	private final int MINIMUM_MAIN_WINDOW_HEIGHT = 200; // in pixels
	private final int UPPER_LAYOUT_GAP = 10;
	private final int MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH = 120;
	private final int MAXIMUM_THREAD_LIST_VIEW_WIDTH = MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH;
	private final int MINIMUM_SELECT_EXAMPlE_BUTTON_WIDTH = 150;
	private final int MAXIMUM_MAIN_STRUCTURES_LAYOUT_HEIGHT = 650;
	private final double COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH = 0.5;

	
	private void displayErrorMessage(String message) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle("Error");
		errorAlert.setContentText(message);
		errorAlert.setResizable(true);
		errorAlert.showAndWait();
	}
	
	private void updateStackListView() {
		this.stackListView.getItems().clear();
		this.selectedThread.getExecutionStack().forEach(statement -> this.stackListView.getItems().add(statement.toString()));
	}
	
	private void updateSymbolTableTableView() {
		this.symbolTableTableView.getItems().clear();
		this.selectedThread.getSymbolTable().forEachKey(variableName -> this.symbolTableTableView.getItems().add(variableName));
	}
	
	private void updateThreadDependantStructures() {
		if (this.selectedThread == null) {
			return;
		}
		
		this.updateStackListView();
		this.updateSymbolTableTableView();
	}
	
	// the thread list view will change when a new thread is introduced / a thread is completed => taken out of the repo
	private void updateThreadListView() {
		this.threadListView.getItems().clear();
		this.controller.getThreadList().forEach(thread -> this.threadListView.getItems().add(thread.getThreadID()));
	}
	
	private void updateHeapTableView() {
		this.heapTableView.getItems().clear();
		ProgramState firstAvailableThread = this.controller.getFirstAvailableThread();
		if (firstAvailableThread == null) {
			return ;
		}
		
		firstAvailableThread.getHeap().forEachKey(variableAddress -> this.heapTableView.getItems().add(variableAddress));
	}
	
	private void updateOutputListView() {
		this.outputListView.getItems().clear();
		ProgramState firstAvailableThread = this.controller.getFirstAvailableThread();
		if (firstAvailableThread == null) {
			return ;
		}
		
		firstAvailableThread.getOutput().forEach(message -> this.outputListView.getItems().add(message.toString()));
	}
	
	private void updateFileTableListView() {
		this.fileTableListView.getItems().clear();
		ProgramState firstAvailableThread = this.controller.getFirstAvailableThread();
		if (firstAvailableThread == null) {
			return ;
		}
		
		firstAvailableThread.getFileTable().forEachKey(fileName -> this.fileTableListView.getItems().add(fileName.toString()));
	}
	
	// threadList, heap, output, filetable - they don't depend on the current thread
	private void updateGlobalStructures() {
		this.updateThreadListView();
		this.updateHeapTableView();
		this.updateOutputListView();
		this.updateFileTableListView();
	}
	
	public void updateAllStructures() {
		// normally I wouldn't need to call this from the controller, I could just call it here after each button press for next step
		// however, what if there are some internal modifications that are done even when I don't press the button - then I need this
		this.updateGlobalStructures();
		this.updateThreadDependantStructures();
		
		// update the textfield for the thread count; only after the threadListView is updated in updateGlobalStructures()
		this.programStateCountTextField.setText("Threads: " + this.threadListView.getItems().size());
	}
	
	public void beforeProgramExecution() {
		this.updateAllStructures();
		this.selectExampleButton.setDisable(true);
		this.exampleListView.setDisable(true);
		this.advanceOneStepButton.setDisable(false);
		this.fullProgramExecutionButton.setDisable(false);
	}
	
	public void afterProgramExecution() {
		this.updateThreadListView();
		this.programStateCountTextField.setText("Threads: 0");
		this.selectExampleButton.setDisable(false);
		this.exampleListView.setDisable(false);
		this.exampleListView.getSelectionModel().clearSelection();
		this.advanceOneStepButton.setDisable(true);
		this.fullProgramExecutionButton.setDisable(true);
	}
	
	private void initialiseThreadListView() {
		// should the threadList display all the threads (including those which are completed) ?
		this.threadListView = new ListView<>();
		this.threadListView.setMaxWidth(this.MAXIMUM_THREAD_LIST_VIEW_WIDTH);
		this.threadListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// It's no use to pass the currently selected thread to the controller (and back again to the GUI)
			// because the currentThread doesn't affect the program execution, just the content displayed in the GUI

			// also, if I click on the ID that's already clicked, there's no use in updating anything
			if (newValue == oldValue) {
				return;
			}

			if (newValue == null || newValue < 0) {
				// by doing this we reduce the possibility of not having a thread selected at a time, which did occur frequently for some reason
				ProgramState firstAvailableThread = controller.getFirstAvailableThread();
				newValue = firstAvailableThread.getThreadID();
			}

			selectedThread = controller.getThreadByID(newValue);
//				System.out.println(newValue);
			updateThreadDependantStructures();
		});
	}
	
	private void initialiseSymbolTableTableView() {
		this.symbolTableTableView = new TableView<>();
		this.symbolTableTableView.setEditable(false);
		
		this.symbolTableTableView.setMaxWidth(Double.MAX_VALUE);
		
		TableColumn<String, String> variableNameColumn = new TableColumn<>("Variable name");
		variableNameColumn.prefWidthProperty().bind(this.symbolTableTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH));
		variableNameColumn.setCellValueFactory(currentValue -> new ReadOnlyStringWrapper(currentValue.getValue()));
		
		TableColumn<String, String> variableValueColumn = new TableColumn<>("Value");
		variableValueColumn.prefWidthProperty().bind(this.symbolTableTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH));
		variableValueColumn.setCellValueFactory(currentValue -> {
			if (this.selectedThread == null) {
				return null;
			} 
			return new ReadOnlyStringWrapper(this.selectedThread.getSymbolTable().getValue(currentValue.getValue()).toString());
		});
		
		this.symbolTableTableView.getColumns().add(variableNameColumn);
		this.symbolTableTableView.getColumns().add(variableValueColumn);
	}
	
	private void initialiseOutputListView() {
		this.outputListView = new ListView<>();
		this.outputListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseHeapTableTableView() {
		this.heapTableView = new TableView<>();
		this.heapTableView.setEditable(false);
		
		TableColumn<Integer, String> variableAddressColumn = new TableColumn<>("Variable address");
		variableAddressColumn.prefWidthProperty().bind(this.heapTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH));
		// this approach should only be used as long as the table is non-editable (which it is in this app)
		variableAddressColumn.setCellValueFactory(currentReference -> new ReadOnlyStringWrapper("0x" + Integer.toHexString(currentReference.getValue())));
		
		TableColumn<Integer, String> variableValueColumn = new TableColumn<Integer, String>("Value");
		variableValueColumn.prefWidthProperty().bind(this.heapTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH));
		variableValueColumn.setCellValueFactory(currentReference -> {
			if (this.selectedThread == null) {
				return null;
			} 
			return new ReadOnlyStringWrapper(this.selectedThread.getHeap().getValue(currentReference.getValue()).toString());
		});
		
		this.heapTableView.getColumns().add(variableAddressColumn);
		this.heapTableView.getColumns().add(variableValueColumn);
		this.heapTableView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseFileTableListView() {
		this.fileTableListView = new ListView<>();
		this.fileTableListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseStackListView() {
		this.stackListView = new ListView<>();
		this.stackListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseAllStructures() {
		this.initialiseThreadListView();
		this.initialiseSymbolTableTableView();
		this.initialiseOutputListView();
		this.initialiseHeapTableTableView();
		this.initialiseFileTableListView();
		this.initialiseStackListView();
	}
	
	private HBox createStructuresLayout() {
		HBox mainStructuresLayout = new HBox(5);
		VBox rightLayout = new VBox(5);
		HBox upperRightLayout = new HBox(5);
		HBox lowerRightLayout = new HBox(5);
		
		this.initialiseAllStructures();
		
		// for now I don't know whether I should move these setHgrows to their corresponding item's initialise method, 
		// in case I might want to change from a HBox to sth else
		HBox.setHgrow(this.symbolTableTableView, Priority.ALWAYS);
		HBox.setHgrow(this.heapTableView, Priority.ALWAYS);
		HBox.setHgrow(this.outputListView, Priority.ALWAYS);
		HBox.setHgrow(this.stackListView, Priority.ALWAYS);
		HBox.setHgrow(this.fileTableListView, Priority.ALWAYS);
		HBox.setHgrow(upperRightLayout, Priority.ALWAYS);
		HBox.setHgrow(lowerRightLayout, Priority.ALWAYS);
		HBox.setHgrow(rightLayout, Priority.ALWAYS);
		
		upperRightLayout.getChildren().addAll(this.symbolTableTableView, this.heapTableView, this.outputListView);
		lowerRightLayout.getChildren().addAll(this.stackListView, this.fileTableListView);
		rightLayout.getChildren().addAll(upperRightLayout, lowerRightLayout);
		mainStructuresLayout.getChildren().addAll(this.threadListView, rightLayout);
		mainStructuresLayout.setMaxHeight(this.MAXIMUM_MAIN_STRUCTURES_LAYOUT_HEIGHT);
		mainStructuresLayout.setId("mainStructuresLayout");
		return mainStructuresLayout;
	}
	
	private void initialiseAdvanceOneStepButton() {
		this.advanceOneStepButton = new Button("One step");
		this.advanceOneStepButton.setOnAction(event -> {
			try {
				controller.advanceOneStepAllThreads();
			} catch (Exception e) {
				displayErrorMessage(e.getMessage());
			}
		});
		this.advanceOneStepButton.setDisable(true); // these buttons will be disabled until an example is selected
		this.advanceOneStepButton.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseFullProgramExecutionButton() {
		this.fullProgramExecutionButton = new Button("Full execution");
		this.fullProgramExecutionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent event) {
            	try {
					controller.fullProgramExecution();
				} 
            	catch (Exception e) {
					displayErrorMessage(e.getMessage());
				}
            }
		});
		this.fullProgramExecutionButton.setDisable(true);
		this.fullProgramExecutionButton.setMaxWidth(Double.MAX_VALUE);
	}
	
	private HBox createExecuteAreaLayout() {
		HBox executeAreaLayout = new HBox(5);
		
		this.programStateCountTextField = new TextField("Threads: 0");
		this.programStateCountTextField.setEditable(false);
		this.programStateCountTextField.setMaxWidth(this.MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH);
		this.programStateCountTextField.setId("threadCountTextField");
		
		this.initialiseAdvanceOneStepButton();
		this.initialiseFullProgramExecutionButton();
		
		HBox.setHgrow(this.advanceOneStepButton, Priority.ALWAYS);
		HBox.setHgrow(this.fullProgramExecutionButton, Priority.ALWAYS);
		HBox.setHgrow(executeAreaLayout, Priority.ALWAYS);
		
		executeAreaLayout.getChildren().addAll(this.programStateCountTextField, this.advanceOneStepButton, this.fullProgramExecutionButton);
		executeAreaLayout.setId("executeAreaLayout");
		
		return executeAreaLayout;
	}
	
	private void initialiseExampleComboBox() {
		this.exampleListView = new ListView<>();
		this.exampleListView.setMaxWidth(Double.MAX_VALUE);
		this.controller.getAllExamples().forEach(example -> exampleListView.getItems().add(example));
	}
	
	private void initialiseSelectExampleButton() {
		this.selectExampleButton = new Button("Execute program");
		this.selectExampleButton.setOnAction(event -> {
			try {
				createSecondaryPage();
				controller.loadProgramStateIntoRepository(exampleListView.getSelectionModel().getSelectedItem());
			}
			catch (Exception e) {
				displayErrorMessage(e.getMessage());
			}

		});
		this.selectExampleButton.setTooltip(new Tooltip("Only one program can be run at a time"));
		this.selectExampleButton.setMaxWidth(Double.MAX_VALUE);
		this.selectExampleButton.setMinWidth(this.MINIMUM_SELECT_EXAMPlE_BUTTON_WIDTH);
	}
	
	private HBox createUpperLayout() {
		HBox upperLayout = new HBox(this.UPPER_LAYOUT_GAP);
		
		this.initialiseExampleComboBox();
		this.initialiseSelectExampleButton();
		
		HBox.setHgrow(this.exampleListView, Priority.ALWAYS);
		HBox.setHgrow(this.selectExampleButton, Priority.ALWAYS);
		HBox.setHgrow(upperLayout, Priority.ALWAYS);
		
		upperLayout.getChildren().addAll(this.exampleListView, this.selectExampleButton);
		upperLayout.setId("upperLayout");
		
		return upperLayout;
	}
	
	private Scene createMainScene(){
		Scene mainScene;
		VBox mainLayout = new VBox(10);
		mainScene = new Scene(mainLayout);
		mainLayout.setStyle("-fx-background-color: #999999");
		mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("applicationStyle.css")).toExternalForm());
		mainLayout.getChildren().addAll(this.createUpperLayout());
		
		return mainScene;
	}

	private Scene createSecondaryScene(){
		Scene mainScene;
		VBox mainLayout = new VBox(10);

		mainScene = new Scene(mainLayout);
		mainLayout.setStyle("-fx-background-color: #999999");
		mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("applicationStyle.css")).toExternalForm());
		mainLayout.getChildren().addAll(createExecuteAreaLayout(), createStructuresLayout());

		return mainScene;
	}

	private void createSecondaryPage(){
		class SecondStage extends Stage{
			SecondStage(){
				this.setTitle("Interpreter");
				this.setMaxHeight(600);
				this.setMaxWidth(400);
				this.setMinHeight(MINIMUM_MAIN_WINDOW_HEIGHT);
				this.setMinWidth(MINIMUM_MAIN_WINDOW_WIDTH);
				this.setScene(createSecondaryScene());
				this.show();
			}
		}
		new SecondStage();
	}
	
	@Override
	public void start(Stage primaryStage){
		this.controller = new GUIController(this);
		
		primaryStage.setMinWidth(this.MINIMUM_MAIN_WINDOW_WIDTH);
		primaryStage.setMinHeight(this.MINIMUM_MAIN_WINDOW_HEIGHT);
		primaryStage.setMaxHeight(600);
		primaryStage.setMaxWidth(400);
		primaryStage.setTitle("Interpreter");
        primaryStage.setScene(this.createMainScene());
        primaryStage.show();
	}

}
