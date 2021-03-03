package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.exit;



public class Game extends Thread {
    Stage window;

    GridPane oldPane;
    Pane newPane;
    Scene oldScene;
    Scene newScene;
    Head head;

    boolean isPaused=false;
    boolean needSpawn=false;
    boolean speeded = false;

    Color color = Color.DARKGREEN;
    Food apple;

    double mouseLocationX;
    double mouseLocationY;
    static double speed =1.5;
    ArrayList<Part> parts;
    ArrayList<Score> scores;

    public Game(Stage primaryStage, GridPane pane, Scene scene, ArrayList<Score> scores) {
        this.scores=scores;
        this.window = primaryStage;
        this.oldPane = pane;
        this.window.hide();
        parts = new ArrayList<>();
        oldScene=scene;
        newPane = new Pane();


        //dodanie tla
        Rectangle backgrand = new Rectangle();
        backgrand.setFill(Color.rgb(51, 51, 51));
        newPane.getChildren().add(backgrand);
        backgrand.setX(0);
        backgrand.setY(0);
        backgrand.setHeight(750);
        backgrand.setWidth(1200);


        //pierwsze dodanie jedzenia
        apple=new Food();
        newPane.getChildren().add(apple);
        apple.relocate(100,100);

        //dodawanie głowy
        head = new Head(color,Color.WHITE);
        newPane.getChildren().add(head);
        head.relocate(600,250);

        //dodawanie pierwszego elementu ogona
        Part part1 = new Part(20, color, head);
        newPane.getChildren().add(part1);
        part1.relocate(580, 250);
        head.toFront();
        parts.add(part1);

        newScene = new Scene(newPane,1200,750);
        window.setScene(newScene);
        window.sizeToScene();
        window.show();

        newPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseLocationX=event.getX();
                mouseLocationY=event.getY();
            }
        });

        newScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode()== KeyCode.ESCAPE&&(!isPaused))
                {
                    isPaused=true;

                    FlowPane gridMenu = new FlowPane(Orientation.VERTICAL);
                    gridMenu.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: black; -fx-border-width: 3" );
                    //gridMenu.setStyle("-fx-border-color: black");

                    Button resume = new Button("Wznów");
                    resume.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            isPaused=false;
                            gridMenu.setStyle(null);
                            newPane.getChildren().remove(gridMenu);
                        }
                    });


                    gridMenu.getChildren().add(resume);

                    Button saveAndExit = new Button("Zapisz i wyjdz");

                    saveAndExit.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {


                            interrupt();
                            SaveData save = new SaveData(head.getX(),head.getY(),head.getRotation(),apple.getX(),apple.getY(),parts);
                            try {
                                FileOutputStream fos = new FileOutputStream("data\\save");
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(save);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            primaryStage.setScene(oldScene);

                        }
                    });

                    //gridMenu.add(saveAndExit,0,1);
                    gridMenu.getChildren().add(saveAndExit);

                    Button leave = new Button("wycowaj sie");

                    leave.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            endGame();
                        }
                    });

                    //gridMenu.add(leave,0,2);
                    gridMenu.getChildren().add(leave);
                    gridMenu.setMaxHeight(150);
                    //gridMenu.setAlignment(Pos.CENTER);
                    gridMenu.setHgap(0);
                    gridMenu.setVgap(5);
                    gridMenu.setPadding(new Insets(20,20,20,20));
                    newPane.getChildren().add(gridMenu);
                    gridMenu.relocate(newPane.getWidth()/2-50,newPane.getHeight()/2-75);

                }
            }
        });


        //start();




    }

    public Game(Stage primaryStage, GridPane pane, Scene scene, ArrayList<Score> scores, String s) {
        this.scores=scores;
        this.window = primaryStage;
        this.oldPane = pane;
        this.window.hide();
        Pane newPane = new Pane();
        this.newPane=newPane;
        //newPane.setMinSize();

        FileInputStream fis = null;
        SaveData saveLoad=null;
        try {
            fis = new FileInputStream("data\\save");
            ObjectInputStream ois = new ObjectInputStream(fis);
            saveLoad = (SaveData) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            exit(2);
        } catch (ClassNotFoundException e) {
            exit(3);
            e.printStackTrace();
        }

        Rectangle backgrand = new Rectangle();
        backgrand.setFill(Color.rgb(51, 51, 51));
        newPane.getChildren().add(backgrand);
        backgrand.setX(0);
        backgrand.setY(0);
        backgrand.setHeight(750);
        backgrand.setWidth(1200);

        //pierwsze dodanie jedzenia
        apple=new Food();
        newPane.getChildren().add(apple);
        apple.relocate(saveLoad.foodLocationX,saveLoad.foodLocationY);


        //dodawanie głowy
        head = new Head(color,Color.WHITE);
        newPane.getChildren().add(head);
        head.relocate(saveLoad.headLocationX,saveLoad.headLocationY);
        head.setRotaation(new Rotate(saveLoad.headRotation));

        //wczytywanie ogona
        parts = new ArrayList<>();
            //wczytywanie pierwszego elementu ogona.
        parts.add(new Part(20,color,head));
        newPane.getChildren().add(parts.get(0));
        parts.get(0).relocate(saveLoad.tailDataa.get(0),saveLoad.tailDataa.get(1));

        for(int i = 2; i<saveLoad.tailDataa.size()-1;i+=2)
        {
            parts.add(new Part(20,color,parts.get(parts.size()-1)));
            newPane.getChildren().add(parts.get(i/2));
            parts.get(i/2).relocate(saveLoad.tailDataa.get(i),saveLoad.tailDataa.get(i+1));
        }


        oldScene=scene;



        //dodanie tla






        newScene = new Scene(newPane,1200,750);
        window.setScene(newScene);
        window.sizeToScene();
        window.show();

        newPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseLocationX=event.getX();
                mouseLocationY=event.getY();
            }
        });

        newScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode()== KeyCode.ESCAPE&&(!isPaused))
                {
                    isPaused=true;

                    FlowPane gridMenu = new FlowPane(Orientation.VERTICAL);
                    gridMenu.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: black; -fx-border-width: 3" );
                    //gridMenu.setStyle("-fx-border-color: black");

                    Button resume = new Button("Wznów");
                    resume.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            isPaused=false;
                            gridMenu.setStyle(null);
                            newPane.getChildren().remove(gridMenu);
                        }
                    });


                    gridMenu.getChildren().add(resume);

                    Button saveAndExit = new Button("Zapisz i wyjdz");

                    saveAndExit.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {


                            interrupt();
                            SaveData save = new SaveData(head.getX(),head.getY(),head.getRotation(),apple.getX(),apple.getY(),parts);
                            try {
                                FileOutputStream fos = new FileOutputStream("data\\save");
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(save);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            primaryStage.setScene(oldScene);

                        }
                    });

                    //gridMenu.add(saveAndExit,0,1);
                    gridMenu.getChildren().add(saveAndExit);

                    Button leave = new Button("wycowaj sie");

                    leave.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            endGame();
                        }
                    });

                    //gridMenu.add(leave,0,2);
                    gridMenu.getChildren().add(leave);
                    gridMenu.setMaxHeight(150);
                    //gridMenu.setAlignment(Pos.CENTER);
                    gridMenu.setHgap(0);
                    gridMenu.setVgap(5);
                    gridMenu.setPadding(new Insets(20,20,20,20));
                    newPane.getChildren().add(gridMenu);
                    gridMenu.relocate(newPane.getWidth()/2-50,newPane.getHeight()/2-75);

                }
            }
        });


        //start();




    }


    @Override
    public void run() {
        synchronized (window) {

                    long lastFrame = System.currentTimeMillis();

                    while (!this.isInterrupted()) {
                        while ((!isPaused) && (!this.isInterrupted())) {

                            try {
                                long l = (System.currentTimeMillis() - lastFrame);
                                Thread.sleep(l > 16 ? 0 : 16 - l);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                //this.interrupt();
                            }
                            lastFrame = currentTimeMillis();
                            synchronized (head) {
                                //rotacja głowy
                                head.rotateToVector(mouseLocationX - head.getX(), mouseLocationY - head.getY());

                                //ruch głowy
                                double baseMoveY = Math.sin(Math.toRadians(head.getRotation()));
                                double baseMoveX = Math.sqrt(1 - (baseMoveY * baseMoveY));

                                if (head.getRotation() > -90 && head.getRotation() < 90) { // dobra strona
                                    head.relocate(head.getX() + baseMoveX * speed, head.getY() + baseMoveY * speed);
                                } else { // gdzie? nie w tą stronę!
                                    head.relocate(head.getX() - baseMoveX * speed, head.getY() + baseMoveY * speed);
                                }
                            }
                            synchronized (parts) {
                                //ruch ogona
                                for (Part p : parts) {
                                    double x;
                                    double y;
                                    if (p.next == head) {
                                        x = p.vectorToNext()[0] - 20;
                                        y = p.vectorToNext()[1] - 20;
                                    } else {
                                        x = p.vectorToNext()[0] - 0;
                                        y = p.vectorToNext()[1] - 0;
                                    }
                                    if (Math.sqrt(x * x + y * y) >= 30) {
                                        double scalar = speed / Math.sqrt(x * x + y * y);
                                        p.relocate(p.getX() + x * scalar, p.getY() + y * scalar);
                                    }

                                }
                            }
                            //obsługa jedzenia
                            synchronized (apple) {
                                if (((head.getX() - apple.getX()) * (head.getX() - apple.getX()) + (head.getY() - apple.getY()) * (head.getY() - apple.getY())) <= 725) {
                                    //needSpawn=true;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            spawnPart();
                                        }
                                    });
                                    apple.relocate(Math.random() * newPane.getWidth(), Math.random() * newPane.getHeight());

                                }
                            }
                            //obsługa zderzen
                            synchronized (head) {
                                synchronized (parts) {
                                    for (int i = 1; i < parts.size(); i++) {

                                        if ((parts.get(i).getX() - head.getX()) * (parts.get(i).getX() - head.getX()) + (parts.get(i).getY() - head.getY()) * (parts.get(i).getY() - head.getY()) < 225) {
                                            endGame();
                                        }
                                    }
                                }
                                if (head.getX() < 25 ||
                                        head.getX() >
                                                newPane.getWidth() - 25 ||
                                        head.getY() < 25 ||
                                        head.getY() >
                                                newPane.getHeight() - 25) {
                                    endGame();
                                }
                            }

                        }
                    }

        }
    }

    private void endGame() {
        Game to = this;
        to.interrupt();
        Platform.runLater(new Runnable() {
            @Override public void run() {
                System.out.println("END GAME");

                //window.hide();
                GridPane ScorePane = new GridPane();

                ScorePane.setAlignment(Pos.CENTER);
                ScorePane.setHgap(20);
                ScorePane.setVgap(10);
                ScorePane.setPadding(new Insets(20,20,20,20));
                //pane.setGridLinesVisible(true);
                Scene scene = new Scene(ScorePane,250,100);

                Text text = new Text("podaj nick by zapisać wynik");
                ScorePane.add(text,0,1,2,4);

                TextField label = new TextField();
                ScorePane.add(label,0,2);

                Button button = new Button("zapisz");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        scores.add(new Score(parts.size(),label.getText()));
                        window.setScene(oldScene);
                    }
                });
                ScorePane.add(button,1,3);

                window.setScene(scene);





            }
        });


        //window.show();
    }

    public void spawnPart()
    {
        Part oldOne = parts.get(parts.size()-1);

        Part newOne = new Part(20,color,oldOne);

        newOne.relocate(oldOne.getX(),oldOne.getY());
        newPane.getChildren().add(newOne);
        parts.add(newOne);
    }


}
