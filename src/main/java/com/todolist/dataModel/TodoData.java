package com.todolist.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

/**
 * Created by timbuchalka on 20/04/2016.
 */
public class TodoData {
    //biến private static có kiểu của chính lớp của nó = null
    private static TodoData instance;
    private static final String FILENAME = "TodoListItems.txt";

    private ObservableList<TodoItem> todoItems;
//    private List<TodoItem> todoItems;
    private DateTimeFormatter formatter;

    //hàm tạo private
    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    //lấy hàm tạo từ method static và nó chỉ trả về 1 đối tượng duy nhất
    public static TodoData getInstance() {
        //nếu biến lấy hàm tạo của lớp null thì gán cho nó hàm tạo
        //còn không thì trả về luôn biến đã được gán hàm tạo nên gọi nhiều lần hàm này cũng chỉ trả về 1 đối tượng duy nhất
        if (instance == null) {
            synchronized (TodoData.class) {
                instance = new TodoData();
            }
        }
        return instance;
    }


    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

//    public void setTodoItems(ObservableList<TodoItem> todoItems) {
//        this.todoItems = todoItems;
//    }

    //lấy dữ liệu từ file ghi vào list
    public void loadTodoItems() throws IOException {

        todoItems = FXCollections.observableArrayList();//tạo list mới
        Path path = Paths.get(FILENAME);
        BufferedReader br = Files.newBufferedReader(path);//lấy BufferedReader

        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");//cắt mỗi hàng trong file thành các phần từ phân tách bởi tab

                //lấy từng phần tử trong mảng vào biến
                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                //định dạng date
                LocalDate date = LocalDate.parse(dateString, formatter);
                //thêm các biến trên vào tham số hàm tạo của TodoItem
                TodoItem todoItem = new TodoItem(shortDescription, details, date);
                //thêm TodoItem này vào list
                todoItems.add(todoItem);
            }

        } finally {
            if(br != null) {
                br.close();
            }
        }
    }

    //lấy dữ liệu từ list ghi vào file
    public void storeTodoItems() throws IOException {

        Path path = Paths.get(FILENAME);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            //ghi từng TodoItem trong list vào file
            //fomat dạng phân tách các thuộc tính trong TodoItem bằng 1 tab
            //ghi thành 1 hàng trong file
            Iterator<TodoItem> iter = todoItems.iterator();
            while(iter.hasNext()) {
                TodoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadline().format(formatter)));
                bw.newLine();//xuống dòng để ghi 1 TodoItem mới
            }

        } finally {
            if(bw != null) {
                bw.close();
            }
        }

    }

    public void addTodoItem(TodoItem todoItem) {
        todoItems.add(todoItem);
    }
}
