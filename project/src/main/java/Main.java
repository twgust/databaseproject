public class Main {
    public static void main(String[] args) {
        DatabaseImpl dbImpl = new DatabaseImpl();
        ConsoleUI ui = new ConsoleUI(dbImpl);
        ui.start();

    }
}
