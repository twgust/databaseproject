import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ConsoleUI {

    enum AccessLevel { NONE, PATIENT, DOCTOR, ADMIN }

    private BufferedReader in;
    private String loginID;

    ConsoleUI() {
        in = new BufferedReader(new InputStreamReader(System.in));
        loginID = "";
    }

    private void print(String str) {
        System.out.println(str);
    }

    private boolean loginMenu() throws IOException {
        String answer;

        print("--- HEALTH CENTER ---");
        print("Login as:");
        print("\t1. Patient");
        print("\t2. Doctor");
        print("\t3. Administrator");
        print("\t4. Exit");

        answer = in.readLine();
        if (answer.equals("1"))
            login(AccessLevel.PATIENT);
        else if (answer.equals("2"))
            login(AccessLevel.DOCTOR);
        else if (answer.equals("3"))
            login(AccessLevel.ADMIN);
        else if (answer.equals("4") ||
                answer.equalsIgnoreCase("exit"))
            return false;
        else {
            print("Invalid input.");
            return loginMenu();
        }

        return true;
    }

    private void login(AccessLevel accessLevel) throws IOException {
        String login;

        if (accessLevel == AccessLevel.PATIENT) {
            print("Please enter you medical number:");
            loginID = in.readLine();

            // TODO db stuff, check if medical nbr exists

            print("Logged in as a patient");
        }
        else if (accessLevel == AccessLevel.DOCTOR) {
            print("Please enter you employee number:");
            loginID = in.readLine();

            // TODO db stuff, check if employee nbr exists

            print("Logged in as a doctor");
            doctorMenu();
        }
        else if (accessLevel == AccessLevel.ADMIN) {
            loginID = "admin";
            print("Logged in as an admin");
            adminMenu();
        }
    }

    private void adminMenu() throws IOException {
        String answer;

        print("--- ADMIN MENU ---");
        print("\t1. View patients"); // Include all fields plus the sum of all visits
        print("\t2. View appointments");
        print("\t3. View medical records");
        print("\t4. View doctors");
        print("\t5. Add doctor");
        print("\t6. Add specialization");
        print("\t7. Delete doctor");
        print("\t8. Logout");

        answer = in.readLine();
        if (answer.equals("1")) {
            viewAllPatients();
            adminMenu();
        }
        else if (answer.equals("2")) {
            viewAllAppointments();
            adminMenu();
        }
        else if (answer.equals("3")) {
            viewMedicalRecordByPatient();
            adminMenu();
        }
        else if (answer.equals("4")) {
            viewAllDoctors();
            adminMenu();
        }
        else if (answer.equals("5")) {
            addDoctor();
            adminMenu();
        }
        else if (answer.equals("6")) {
            addSpecialization();
            adminMenu();
        }
        else if (answer.equals("7")) {
            deleteDoctor();
            adminMenu();
        }
        else if (answer.equals("8")) {
            loginMenu();
        }
        else if (!answer.equalsIgnoreCase("exit")) {
            print("Invalid input.");
            adminMenu();
        }
    }

    private void viewAllPatients() {
        // TODO db stuff. Also show the sum of all visit costs for each patient.
    }

    private void viewAllAppointments() {
        // TODO db stuff
    }

    private void viewMedicalRecordByPatient() throws IOException {
        print("Which patient? Please enter a 9-digit medical number:");

        String answer = in.readLine();

        // TODO db stuff
    }

    private void viewAllDoctors() {
        // TODO db stuff
    }

    private void addDoctor() throws IOException {
        String employeeNbr;
        String firstName;
        String lastName;
        String specialization;
        String phoneNbr;

        print("Please enter a new employee number:");
        employeeNbr = in.readLine();

        print("Please enter a first name:");
        firstName = in.readLine();

        print("Please enter a surname:");
        lastName = in.readLine();

        print("Please enter a specialization id:");
        specialization = in.readLine();

        print("Please enter a phone number:");
        phoneNbr = in.readLine();

        // TODO db stuff
    }

    private void addSpecialization() throws IOException {
        String specID;
        String specName;
        String visitCost;

        print("Please enter a new specialization id:");
        specID = in.readLine();

        print("Please enter the title/name of the specialization:");
        specName = in.readLine();

        print("Please enter the visiting cost:");
        visitCost = in.readLine();

        // TODO db stuff
    }

    private void deleteDoctor() throws IOException {
        print("Which doctor? Please enter an employee number:");

        String employeeNbr = in.readLine();

        // TODO db stuff
    }

    private void doctorMenu() throws IOException {
        String answer;

        print("--- DOCTOR MENU ---");
        print("\t1. View upcoming appointments");
        print("\t2. View my patients");
        print("\t3. View prescribed drugs");
        print("\t4. View medical records");
        print("\t5. Add medical record");
        print("\t6. Register unavailability");
        print("\t7. Logout");

        answer = in.readLine();
        if (answer.equals("1")) {
            viewUpcomingAppointmentsByDoctor();
            doctorMenu();
        }
        else if (answer.equals("2")) {
            viewPatientsByDoctor();
            doctorMenu();
        }
        else if (answer.equals("3")) {
            viewPrescribedDrugs();
            doctorMenu();
        }
        else if (answer.equals("4")) {
            viewMedicalRecordByPatient();
            doctorMenu();
        }
        else if (answer.equals("5")) {
            addMedicalRecord();
            doctorMenu();
        }
        else if (answer.equals("6")) {
            registerUnavailability();
            doctorMenu();
        }
        else if (answer.equals("7")) {
            loginMenu();
        }
        else if (!answer.equalsIgnoreCase("exit")) {
            print("Invalid input.");
            doctorMenu();
        }
    }


    private void viewUpcomingAppointmentsByDoctor() {

        // TODO db stuff using loginID as employee nbr

    }

    private void viewPatientsByDoctor() {

        // TODO db stuff using loginID as employee nbr

    }

    private void viewPrescribedDrugs() throws IOException {
        print("For which patient? Please enter a 9-digit medical number:");

        String medicalNbr = in.readLine();

        // TODO db stuff
    }

    private void addMedicalRecord() throws IOException {
        String medicalNbr;
        String diagnosis;
        String description;

        print("To which patient? Please enter a 9-digit medical number:");
        medicalNbr = in.readLine();

        print("Please specify the diagnosis:");
        diagnosis = in.readLine();

        print("Please write a description:");
        description = in.readLine();

        // TODO add a new medical record using the loginID as employee nbr

        String drugID = "tmp";
        String drugName;
        print("Did you prescribe any drugs? Press 'enter' if you didn't. Otherwise, ");

        while(!drugID.isBlank()) {
            print("Please enter a new drug id:");
            drugID = in.readLine();
            if (drugID.isBlank())
                break;

            print("Please enter the name of the drug:");
            drugName = in.readLine();

            // TODO add drug into db
        }
    }

    private void registerUnavailability() {
        // TODO no idea how to approach this right now
    }

    void start() {
        try {
            if (!loginMenu()) {
                close();
                return;
            }

            close();
        } catch (IOException e) {
            close();
        }
    }

    void close() {
        print("Closing application");
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }

}
