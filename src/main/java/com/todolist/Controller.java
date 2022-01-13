package com.todolist;

import com.todolist.dataModel.TodoData;
import com.todolist.dataModel.TodoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Controller {

//    private List<TodoItem> todoItems;

    @FXML
    private ListView<TodoItem> todoListView;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainBorderPane;

    private ContextMenu listContextMenu;

    public void initialize() {
//        TodoItem item1 = new TodoItem("Mail birthday card", "Buy a 30th birthday card for John",
//                LocalDate.of(2016, Month.APRIL, 25));
//        TodoItem item2 = new TodoItem("Doctor's Appointment", "See Dr. Smith at 123 Main Street.  Bring paperwork",
//                LocalDate.of(2016, Month.MAY, 23));
//        TodoItem item3 = new TodoItem("Finish design proposal for client", "I promised Mike I'd email website mockups by Friday 22nd April",
//                LocalDate.of(2016, Month.APRIL, 22));
//        TodoItem item4 = new TodoItem("Pickup Doug at the train station", "Doug's arriving on March 23 on the 5:00 train",
//                LocalDate.of(2016, Month.MARCH, 23));
//        TodoItem item5 = new TodoItem("Pick up dry cleaning", "The clothes should be ready by Wednesday",
//                LocalDate.of(2016, Month.APRIL, 20));
//
////        todoItems = new ArrayList<>();
////        todoItems.add(item1);
////        todoItems.add(item2);
////        todoItems.add(item3);
////        todoItems.add(item4);
////        todoItems.add(item5);
//
//
//
//        todoListView.getItems().setAll(item1, item2, item3, item4, item5);
//
//        //tạo giá trị lần đầu cho list trong TodoData vì lần đầu chưa có file để ghi vào list này
//        TodoData.getInstance().setTodoItems((List<TodoItem>) todoListView.getItems());

        //lắng nghe mỗi khi có sự kiện thay đổi lựa chọn của 1 item là TodoItem trong listview todoListView
        //mà không phụ thuộc vào hành động của người dùng, có thể do hệ thống tự thay đổi theo dữ liệu
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem oldItem, TodoItem newItem) {
                if (newItem != null) {
                    itemDetailsTextArea.setText(newItem.getDetails());

                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-YYYY");
                    deadlineLabel.setText(df.format(newItem.getDeadline()));
                }
            }
        });

//        //lấy list đã có trong TodoData nhờ hàm init() trong HelloApplication thêm vào listview
//        todoListView.getItems().addAll(TodoData.getInstance().getTodoItems());
        todoListView.setItems(TodoData.getInstance().getTodoItems());

        //set chỉ cho phép chọn 1 lúc 1 item trong listview
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        /*gọi hàm setCellFactory để cài đặt lại các thuộc tính của ListView
        tham số là 1 FunctionalInterface Callback, ta sẽ tạo lớp ẩn danh của
        Interface này để Override method call của nó
        cần xác định các thuộc tính để Callback truyền vào cho method call bằng generics với
        2 thuộc tính lần này là ListView<TodoItem> và ListCell<TodoItem>*/
        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {

                /*các ListCell là các phần tử con hay các hàng của list nó extends Labeled
                nên có thể định dạng cho nó giống Labeled như màu sắc
                ListCell không phải Interface nhưng ta vẫn tạo lớp ẩn danh kế thừa lớp này và
                Override method updateItem của nó*/
                ListCell<TodoItem> cell = new ListCell<TodoItem>(){
                    @Override
                    protected void updateItem(TodoItem todoItem, boolean empty) {
                        //vẫn giữ lại các cài đặt của lớp cha, chỉ cần sửa một vài giá trị
                        super.updateItem(todoItem, empty);

                        //nếu cell(hàng) không có đối tượng thì cho text bằng null
                        //nờ hàm setText(), hàm này thuộc class Labeled mà ListCell đã extends
                        if (empty) {
                            setText(null);
                        }
                        //nếu không rỗng thì cho text là thuộc tính ShortDescription của đối tượng TodoItem cell này chứa
                        else {
                            setText(todoItem.getShortDescription());

                            //nếu ngày hết hạn là hôm nay thì cho màu chữ đỏ, là ngày mai thì màu nâu
                            //quá hạn thì màu xám
                            if (todoItem.getDeadline().equals(LocalDate.now())) {
                                setTextFill(Color.RED);
                            } else if (todoItem.getDeadline().isBefore(LocalDate.now())) {
                                setTextFill(Color.BLUE);
                            } else if (todoItem.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.BROWN);
                            }
                        }
                    }
                };

                //lấy giá trị xác định trống hay không của cell và thêm sự kiện nghe thay đổi
                //nếu giá trị mới isNowEmpty là true thì set ContextMenu là null không thì
                //thêm ContextMenu đã tạo vào
                cell.emptyProperty().addListener((observableValue, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(listContextMenu);
                    }
                });

                //trả về lớp ẩn danh kế thừa cell trên vừa Override lại các updateItem của nó
                return cell;
            }
        });

        //tạo 1 ContextMenu thứ sẽ hiện lên khi kích chuột phải vào control(control này cần được gán ContextMenu)
        listContextMenu = new ContextMenu();

        //tạo 1 MenuItem tên Delete để sau đó gán vào item của ContextMenu trên
        //tạo 1 MenuItem tên Edit để sau đó gán vào item của ContextMenu trên
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        //tạo sự kiện cho MenuItem này khi kích chuột là tìm cell/hàng của todoListView đang được chọn
        //và lấy ra item của cell bằng hàm getSelectedItem, trong lần này item là 1 TodoItem
        //sau đó gọi hàm xóa item này trong List của TodoData, List này đã ràng buộc với todoListView
        // nên xóa ở List đó todoListView cũng thay đổi theo
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        //tạo sự kiện cho MenuItem này khi kích chuột là tìm cell/hàng của todoListView đang được chọn
        //và lấy ra item của cell bằng hàm getSelectedItem, trong lần này item là 1 TodoItem
        //sau đó gọi hàm edit giá trị của item này trong List của TodoData, List này đã ràng buộc với todoListView
        //nên edit ở List đó todoListView cũng thay đổi theo
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                editItem(item);
            }
        });

        //thêm 2 MenuItem này vào ContextMenu
        listContextMenu.getItems().add(deleteMenuItem);
        listContextMenu.getItems().add(editMenuItem);

    }

    //xóa 1 TodoItem có trong List của class singerton TodoData
    private void deleteItem(TodoItem item) {
        // enum AlertType.CONFIRMATION nằm trong class Alert
        // hàm khởi tạo new Alert() sẽ nhận giá trị của enum này để xác định kiểu của Alert
        // ở đây kiểu là CONFIRMATION sẽ có 2 nút OK và CANCEL
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item: " + item.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to back out.");

        //lấy giá trị nút đã nhấn của alert
        Optional<ButtonType> result = alert.showAndWait();

        //result.get() trả về 1 đối tượng ButtonType của nó,
        //ButtonType.OK là 1 đối tượng tính cả .OK, .OK không phải thuộc tính
        if (result.isPresent() && result.get() == ButtonType.OK) {

            //nếu nhấn OK thì gọi hàm xóa trong item trong List của class TodoData
            TodoData.getInstance().deleteTodoItem(item);
        }
    }

    //xóa 1 TodoItem có trong List của class singerton TodoData khi nhấn phím
    public void handleKeyPressed(KeyEvent keyEvent) {
        //lấy giá TodoItem đang được chọn trong listview
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();

        //nếu TodoItem ko null tức có cell của listview đang được chọn và sự kiện key là nhấn DELETE
        //thì gọi hàm xóa TodoItem này trong list của TodoData
        //keyEvent.getCode() lấy ra giá trị key/nút đã nhấn và equals nó với enum KeyCode.DELETE
        if (selectedItem != null && keyEvent.getCode().equals(KeyCode.DELETE)) {
            deleteItem(selectedItem);
        }
    }

    //edit giá trị của TodoItem truyền vào trong list của TodoData
    private void editItem(TodoItem item) {
        //lấy vị trí/ìndex của tham số TodoItem truyền vào hàm này trong list của TodoData
        int index = TodoData.getInstance().getTodoItems().indexOf(item);

        //nhận TodoItem từ hàm lấy kết quả nhập vào trên dialog, truyền các tham số String để hiển thị title, header và
        //tham số TodoItem là TodoItem đang chọn trên list view để gán các giá trị của nó cho các control trong dialog
        //tham số TodoItem chính là tham số truyền vào từ hàm này
        TodoItem editItem = showResultItemDialog(item,"Edit Todo Item", "Use this dialog to edit todo item");

        //nếu kết quả ko null tức đã ấn OK và nhập tất cả các giá trị từ dialog thì lấy TodoItem này set lại giá
        //trị trong list của TodoData tại đúng vị trí/index của TodoItem đang được list view chọn
        if (editItem != null) {
            TodoData.getInstance().getTodoItems().set(index, editItem);
        }
    }

    //tạo 1 dialog nhập các thông tin của 1 TodoItem mới và trả về kết quả TodoItem này nếu nhấn OK
    //còn ấn cancel trả về null
    private TodoItem showResultItemDialog(TodoItem todoItem, String title, String headerText) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        //lấy cửa sổ cha của ứng dụng gán cho Dialog để
        //khi hiển thị Dialog các phần khác trong ứng dụng không thể thao tác
        //lấy bằng cách truy xuất của sổ cha của layout gốc(root) trong trang FXML tạo sự kiện này
        //layout gốc trường hợp này là borderpane
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        //lấy ra DialogPane sẽ chứa file FXML(file này coi là 1 node root)
        //rồi lấy ra list các button và thêm vào list này nút OK, CANCEL
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        FXMLLoader fxmlLoader = new FXMLLoader();
        //set url của file FXML
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        try {
            //truyền 1 node root cho dialogPane để hiển thị trang FXML thông qua hàm fxmlLoader.load()
            dialog.getDialogPane().setContent(fxmlLoader.load());
//            Parent root = FXMLLoader.load(getClass().getResource("todoItemDialog.fxml"));
//            dialog.getDialogPane().setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //lấy ra controller của file fxml, khi chạy ứng dụng các dữ liệu trên file fxml sẽ truyền cho controller này
        //không tạo đối tượng mới của controller vì khi tạo mới tất cả dữ liệu sẽ không có
        DialogController controller = fxmlLoader.getController();

        //gọi hàm truyền vào TodoItem lấy từ tham số của hàm này và gán các giá trị cho các control của dialog bằng
        //giá trị các thuộc tính của TodoItem này truyền vào để tạo hiển thị trên dialog
        controller.setValueDialog(todoItem);

        //showAndWait sẽ hiển thị dialog và đợi người dùng nhấn 1 nút nào đó sẽ trả về kết quả 1 Optional
        //show thì trả về luôn nên không thể sử dụng
        //kết quả trả về sẽ được truyền cho Optional kiểu thuộc tính ButtonType
        Optional<ButtonType> result = dialog.showAndWait();

        //isPresent kiểm tra xem ButtonType nhận được có null không, nếu có trả về false
        if (result.isPresent() && result.get() == ButtonType.OK) {

            //nhận TodoItem đã lấy từ dialog người dùng nhập vào bằng
            //hàm processResultsItem trong controller của dialog FXML và trả về TodoItem này
            //nếu trên dialog không nhập đầy đủ các giá trị hàm sẽ trả về null
            TodoItem newItem = controller.processResultsItem();

            return newItem;
        }

        //nếu người dùng không ấn OK trả về null
        return null;
    }

    @FXML
    //thêm 1 TodoItem người dùng nhập vào dialog vào list của TodoData và
    //cho ListView chọn TodoItem này
    public void showNewItemDialog() {

        //nhận TodoItem từ hàm lấy kết quả nhập vào trên dialog, truyền các tham số String để hiển thị title, header và
        //tham số TodoItem là TodoItem đang chọn trên list view để gán các giá trị của nó cho các control trong dialog
        //do tạo mới không phải edit 1 TodoItem nên truyền vào null
        TodoItem newItem = showResultItemDialog(null,"Add New Todo Item", "Use this dialog to create a new todo item");

        //nếu kết quả ko null tức đã ấn OK và nhập tất cả các giá trị từ dialog thì thêm nó vào list của TodoData
        //thì thêm new TodoItem người dùng nhập vào dialog vào list của TodoData và cho ListView chọn TodoItem này
        if (newItem != null) {
            TodoData.getInstance().addTodoItem(newItem);

            //cho ListView chọn TodoItem vừa được thêm vào
            todoListView.getSelectionModel().select(newItem);
        }

//        //isPresent kiểm tra xem ButtonType nhận được có null không, nếu có trả về false
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//
//            //lấy ra controller của file fxml, khi chạy ứng dụng các dữ liệu trên file fxml sẽ truyền cho controller này
//            //không tạo đối tượng mới của controller vì khi tạo mới tất cả dữ liệu sẽ không có
//            DialogController controller = fxmlLoader.getController();
//
//            //thực hiện thêm TodoItem đã được gán từ dữ liệu trong dialog vào list của TodoData và trả về TodoItem này
//            TodoItem newItem = controller.processResultsItem();
//
////            //lấy list của TodoData cập nhật lại cho todoListView của Controller để
////            //cập nhật danh sách trong ứng dụng đang chạy
////            todoListView.getItems().setAll(TodoData.getInstance().getTodoItems());
//
//            //cho ListView chọn TodoItem vừa được thêm vào
//            todoListView.getSelectionModel().select(newItem);
////            System.out.println("OK pressed");
//        }
////        else {
////            System.out.println("Cancel pressed");
////        }
    }

    @FXML
    //mỗi khi click vào ListView sẽ xem đang chọn item nào trong listview
    // và set giá trị cho các vùng hiển thị thông tin
    public void handleClickListView() {
//        //mỗi khi click vào ListView sẽ xem đang chọn item nào trong listview
//        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
//        itemDetailsTextArea.setText(item.getDetails());
//        deadlineLabel.setText(item.getDeadline().toString());
//
////        StringBuilder sb = new StringBuilder();
////        sb.append(item.getDetails());
////        sb.append("\n\n\n\n");
////        sb.append(item.getDeadline() + "");
//
////        itemDetailsTextArea.setText(sb.toString());
    }
}