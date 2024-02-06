import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Expense implements Serializable {
    private Date date;
    private String category;
    private double amount;

    public Expense(Date date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}

class User implements Serializable {
    private String username;
    private String password;
    private List<Expense> expenses;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.expenses = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
}

class ExpenseTracker {
    private List<User> users;
    private User currentUser;
    private static final String DATA_FILE_PATH = "expense_data.txt";

    public ExpenseTracker() {
        this.users = new ArrayList<>();
        this.currentUser = null;
        loadExpenseData();
    }

    public void registerUser(String username, String password) {
        User newUser = new User(username, password);
        users.add(newUser);
        saveExpenseData();
        System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
        System.out.println("User registered successfully!");
    }

    public void loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
                System.out.println("Login successful!");
                return;
            }
        }
        System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
        System.out.println("Invalid username or password.");
    }

    public void logoutUser() {
        currentUser = null;
        System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
        System.out.println("Logout successful!");
    }

    public void addExpense(Date date, String category, double amount) {
        if (currentUser != null) {
            Expense newExpense = new Expense(date, category, amount);
            currentUser.addExpense(newExpense);
            saveExpenseData();
            System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            System.out.println("Expense added successfully!");
        } else {
            System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            System.out.println("Please log in first.");
        }
    }

    public void listExpenses(int listOption) {
        if (currentUser != null) {
            List<Expense> expenses = new ArrayList<>(currentUser.getExpenses());

            switch (listOption) {
                case 1:
                    break;
                case 2:
                    expenses.sort(Comparator.comparing(Expense::getDate));
                    break;
                case 3:
                    expenses.sort(Comparator.comparing(Expense::getCategory));
                    break;
                default:
                    System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
                    System.out.println("Invalid choice. Please try again.");
                    return;
            }
            System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            System.out.println("Expense Listing:");
            System.out.println("                ");
            for (Expense expense : expenses) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
                String formattedDate = dateFormat.format(expense.getDate());

                System.out.println(formattedDate + " | " + expense.getCategory() + " | " + expense.getAmount());
            }
        } else {
            System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            System.out.println("Please log in first.");
        }
    }

    public void displayCategoryWiseSummation() {
        if (currentUser != null) {
            Map<String, Double> categorySumMap = new HashMap<>();

            for (Expense expense : currentUser.getExpenses()) {
                String category = expense.getCategory();
                double amount = expense.getAmount();

                categorySumMap.put(category, categorySumMap.getOrDefault(category, 0.0) + amount);
            }

            System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            System.out.println("Category-wise Summation:");
            System.out.println("                        ");
            for (Map.Entry<String, Double> entry : categorySumMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } else {
            System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
            System.out.println("Please log in first.");
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void saveExpenseData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadExpenseData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE_PATH))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                users = (List<User>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
        }
    }
}

public class ExpenseTrackerApp {
    public static void main(String[] args) {
        ExpenseTracker expenseTracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("|-----------------------------|");
            System.out.println("|1. Register                  |");
            System.out.println("|2. Login                     |");
            System.out.println("|3. Logout                    |");
            System.out.println("|4. Add Expense               |");
            System.out.println("|5. List Expenses             |");
            System.out.println("|6. Category-wise Summation   |");
            System.out.println("|7. Exit                      |");
            System.out.println("|-----------------------------|");
            System.out.print("Enter your Choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String registerUsername = scanner.next();
                    System.out.print("Enter password: ");
                    String registerPassword = scanner.next();
                    expenseTracker.registerUser(registerUsername, registerPassword);
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.next();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.next();
                    expenseTracker.loginUser(loginUsername, loginPassword);
                    break;
                case 3:
                    expenseTracker.logoutUser();
                    break;
                case 4:
                    if (expenseTracker.getCurrentUser() != null) {
                        System.out.print("Enter expense date (DD-MM-YYYY): ");
                        String expenseDateStr = scanner.next();
                        Date expenseDate = parseDate(expenseDateStr);
                        System.out.print("Enter expense category: ");
                        String expenseCategory = scanner.next();
                        System.out.print("Enter expense amount: ");
                        double expenseAmount = scanner.nextDouble();
                        expenseTracker.addExpense(expenseDate, expenseCategory, expenseAmount);
                    } else {
                        System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
                        System.out.println("Please log in first.");
                    }
                    break;
                case 5:
                    System.out.println("|---------------------------------------|");
                    System.out.println("|1. List Expenses                       |");
                    System.out.println("|2. List Expenses (Sorted by Date)      |");
                    System.out.println("|3. List Expenses (Sorted by Category)  |");
                    System.out.println("|4. Back                                |");
                    System.out.println("|---------------------------------------|");
                    System.out.print("Enter your Choice: ");
                    int listChoice = scanner.nextInt();
                    expenseTracker.listExpenses(listChoice);
                    break;
                case 6:
                    expenseTracker.displayCategoryWiseSummation();
                    break;
                case 7:
                    System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
                    System.out.println("Exiting the Expense Tracker. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x");
        }
    }

    private static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("DD-MM-YYYY").parse(dateStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
            return null;
        }
    }
}
