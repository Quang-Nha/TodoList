package com.todolist;

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

    public void initialize() {
        shortDescriptionField.setText("");
        detailArea.setText("");
        deadlinePicker.setValue(LocalDate.now());
    }

    //lấy TodoItem từ dialog người dùng nhập vào
    public TodoItem processResultsItem() {
        String shortDescription = shortDescriptionField.getText().trim();
        String detail = detailArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();

        TodoItem newItem = new TodoItem(shortDescription, detail, deadlineValue);

        //nếu các giá trị nhận về là rỗng thì trả về null
        if (shortDescription.isBlank() || detail.isBlank()) {
            return null;
        }

        return newItem;
    }

    //truyền vào 1 TodoItem và gán giá trị các thuộc tính của nó cho các control trong dialog
    //nếu truyền vào null thì bỏ qua
    public void setValueDialog(TodoItem todoItem) {
        if (todoItem != null) {
            shortDescriptionField.setText(todoItem.getShortDescription());
            detailArea.setText(todoItem.getDetails());
            deadlinePicker.setValue(todoItem.getDeadline());
        }
    }
}
