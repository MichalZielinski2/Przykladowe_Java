package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.System.exit;

public class Main extends Application {
public ArrayList<Score> scores;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Wonsz zeczny");


        //https://stackoverflow.com/questions/16111496/java-how-can-i-write-my-arraylist-to-a-file-and-read-load-that-file-to-the


        try {
            FileInputStream fis = new FileInputStream("data\\scores");
            ObjectInputStream ois = new ObjectInputStream(fis);
            scores = (ArrayList<Score>) ois.readObject();
            ois.close();
        }catch (Exception e)
        {
            scores = new ArrayList<Score>();
            scores.add(new Score(1,"ja"));
            scores.add(new Score(2,"ja"));
        }


        //https://docs.oracle.com/javafx/2/get_started/form.htm
        GridPane pane = new GridPane();

        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(20,20,20,20));
        //pane.setGridLinesVisible(true);
        Scene scene = new Scene(pane,250,225);
        Text helo = new Text("HEJ!");
        helo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        pane.add(helo,0,0,2,1);


        Button newGame = new Button("Nowa Gra");
        newGame.setPrefSize(150,newGame.getHeight());
        //newGame.setShape(new Circle(100));  //XD
        newGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameStart(primaryStage,pane,scene);
            }
        });
        pane.add(newGame,1,1);

        Button loadGame = new Button("Wczytaj Grę");
        loadGame.setPrefSize(150,loadGame.getHeight());

        EventHandler<WindowEvent> handler = new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                exitGame();
            }
        };
        primaryStage.setOnCloseRequest(handler);

        loadGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame(primaryStage,pane,scene);
            }
        });
        pane.add(loadGame,1,2);

        Button scores = new Button("Zobacz Najlepsze Wyniki");
        scores.setPrefSize(150,scores.getHeight());
        scores.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST,handler);
                scoreboard(primaryStage,scene,handler);
                //primaryStage.setOnCloseRequest(handler);
            }
        });
        pane.add(scores,1,3);

        Button exit = new Button("wyjdź");
        exit.setPrefSize(150,exit.getHeight());
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitGame();
            }
        });
        pane.add(exit,1,4);



        //scene.getStylesheets().add(Login.class.getResource("stylll.css").toExternalForm())
        primaryStage.setScene(scene);



        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    void gameStart(Stage primaryStage, GridPane pane, Scene scene)
    {
        Game gra = new Game(primaryStage,pane,scene,scores);
        gra.start();
    }

    void loadGame(Stage primaryStage, GridPane pane, Scene scene)
    {
        Game gra = new Game(primaryStage,pane,scene,scores,"data\\save");
        gra.start();
    }

    void scoreboard(Stage primaryStage, Scene oldScene, EventHandler<WindowEvent> handler)
    {


//        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                primaryStage.setScene(oldScene);
//                primaryStage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST,this);
//                primaryStage.setOnCloseRequest(handler);
//
//            }
//        });

        //Z mojego poprzedniego projektu
        scores.sort(new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                return o2.getScore()-o1.getScore();
            }
        });

        while(scores.size()>10)
        {
            scores.remove(10);
        }



        //https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
        Pane pane = new Pane();
        Scene scene = new Scene(pane,250,225);


        TableView<Score> table = new TableView<>();
        table.setEditable(false);

        TableColumn firstColumnName = new TableColumn("Gracz");
        TableColumn secondColumnName = new TableColumn("Wynik");

        table.getColumns().add(firstColumnName);
        table.getColumns().add(secondColumnName);

        final ObservableList<Score> data = FXCollections.observableArrayList();

        for(Score s : scores)
        {
            data.add(s);
        }
        firstColumnName.setCellValueFactory(
                new PropertyValueFactory<Score,String>("person")
        );
        secondColumnName.setCellValueFactory(
                new PropertyValueFactory<Score,Integer>("score")
        );
        table.setItems(data);
        pane.getChildren().add(table);

        primaryStage.setScene(scene);
    }

    void exitGame()
    {
        System.out.println("sco");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("data\\scores");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scores);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("BŁĄD");
            alert.setHeaderText("BŁĄD ZAPISU!");
            alert.setContentText("Nie można znaleźć pliku do zapisu");

            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        exit (0);
    }

}
