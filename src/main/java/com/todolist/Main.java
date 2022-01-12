package com.todolist;

import com.todolist.dataModel.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainwindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Todo List!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {//khi tắt ứng dụng sẽ ghi giá trị list có trong TodoData duy nhất vào file
        TodoData.getInstance().storeTodoItems();
    }

    @Override
    public void init() throws Exception {//khi khởi động lấy giá trị từ file ghi vào list của TodoData
        TodoData.getInstance().loadTodoItems();
    }
}