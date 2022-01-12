package com.todolist;

import com.todolist.dataModel.TodoData;
import com.todolist.dataModel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea detailArea;

    @FXML
    private DatePicker deadlinePicker;

    //thêm TodoItem lấy từ dialog vào list của TodoData
    public TodoItem processResults() {
        String shortDescription = shortDescriptionField.getText().trim();
        String detail = detailArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();

        TodoItem newItem = new TodoItem(shortDescription, detail, deadlineValue);
        TodoData.getInstance().addTodoItem(newItem);
        return newItem;

    }
}
