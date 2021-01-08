import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Date;

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
        if (accessLevel == AccessLevel.PATIENT) {
            print("Are you an already registered patient? (y/n)");
            String answer = in.readLine();
            if (answer.equals("n")) {
                registerNewPatient();
            }
            else {
                print("Please enter you medical number:");
                loginID = in.readLine();

                // TODO db stuff, check if medical nbr exists
            }

            print("Logged in as a patient");
            patientMenu();
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
        print("\t8. Log out");

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
        print("\t7. Log out");

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

    private void patientMenu() throws IOException {
        print("--- PATIENT MENU ---");
        print("\t1. Book appointment");
        print("\t2. View personal details");
        print("\t3. Edit personal details");
        print("\t4. Log out");

        String answer = in.readLine();
        if (answer.equals("1")) {
            chooseDoctorToBook();
            patientMenu();
        }
        else if (answer.equals("2")) {
            viewPatientDetails(loginID);
            patientMenu();
        }
        else if (answer.equals("3")) {
            editPatientDetails(loginID);
            patientMenu();
        }
        else if (answer.equals("4")) {
            loginMenu();
        }
        else if (!answer.equalsIgnoreCase("exit")) {
            print("Invalid input.");
            patientMenu();
        }
    }

    private void registerNewPatient() throws IOException {
        String medicalNbr;
        String fName;
        String lName;
        String gender;
        String address;
        String phoneNbr;
        String birthYear;
        String birthMonth;
        String birthDay;

        print("--- PATIENT REGISTRATION ---");
        print("Please enter your medical number:");
        medicalNbr = in.readLine();

        print("Please enter your first name:");
        fName = in.readLine();

        print("Please enter your last name:");
        lName = in.readLine();

        print("Please enter your gender: (M/F)");
        gender = in.readLine();

        print("Please enter your address:");
        address = in.readLine();

        print("Please enter your phone number:");
        phoneNbr = in.readLine();

        // Use the following to build a date. I'm not sure how
        // it should be formatted to be used in the DBMS.
        print("Please enter the year you were born:");
        birthYear = in.readLine();

        print("Please enter the month you were born:");
        birthMonth = in.readLine();

        print("Please enter the day you were born:");
        birthDay = in.readLine();

        // TODO add a new patient, also add current date as registration date

        loginID = medicalNbr;
    }

    private void chooseDoctorToBook() throws IOException {
        // TODO get all doctors and their spec and cost.
        ResultSet resultDoctors = null;
        Hashtable<Integer, String> doctorMap = new Hashtable<>();

        Integer row = 1;
        try {
            while (resultDoctors.next()) {
                doctorMap.put(row, resultDoctors.getString("employee_number"));
                print("\t" + row + ". "+resultDoctors.getNString("first_name")+
                        " " + resultDoctors.getNString("last_name") +
                        ", " + resultDoctors.getNString("specialization") +
                        ", " + resultDoctors.getDouble("cost"));
                row++;
            }

            if (doctorMap.isEmpty()) {
                print("No doctors were found.");
            }
            else {
                String answer = in.readLine();

                int answerRow;
                try {
                    answerRow = Integer.parseInt(answer);
                } catch (NumberFormatException e) {
                    answerRow = -1;
                }

                if (!answer.equals("exit") && answerRow > 0 && answerRow <= row) {
                    chooseAppointment(doctorMap.get(answerRow));
                }
                else {
                    print("Invalid input.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error");
        }
    }

    private void chooseAppointment(String employee_nbr) throws IOException {
        // TODO get available times for next week, using employee_nbr
        ResultSet resultTimes = null;
        Hashtable<Integer, LocalDateTime> timesMap = new Hashtable<>();

        Integer row = 1;
        LocalDateTime dateTime;
        try {
            while (resultTimes.next()) {
                dateTime = resultTimes.getTimestamp("datetime").toLocalDateTime();
                timesMap.put(row, dateTime);

                print("\t" + row + ". " + dateTime.getDayOfWeek().name() +
                        " " + dateTime.getDayOfMonth() +
                        "/ " + dateTime.getMonthValue() +
                        ", " + dateTime.getHour() +
                        ":" + dateTime.getMinute());
                row++;
            }

            if (timesMap.isEmpty()) {
                print("No available times were found.");
            }
            else {
                String answer = in.readLine();

                int answerRow;
                try {
                    answerRow = Integer.parseInt(answer);
                } catch (NumberFormatException e) {
                    answerRow = -1;
                }

                if (!answer.equals("exit") && answerRow > 0 && answerRow <= row) {
                    bookAppointment(timesMap.get(answerRow), employee_nbr);
                }
                else {
                    print("Invalid input.");
                }
            }
        }
        catch (SQLException e) {
            System.err.println("Database error.");
        }
    }

    private void bookAppointment(LocalDateTime dateTime, String employee_nbr) {

        // TODO book appointment with dateTime, lognID and employee_nbr

    }

    private void viewPatientDetails(String medial_nbr) {

        // TODO get patient information

    }

    private void editPatientDetails(String medial_nbr) throws IOException {
        String Fname;
        String Lname;
        String gender;
        String phone_nbr;
        String birthYear;
        String birthMonth;
        String birthDay;

        print("Press 'enter' if you wish to keep the current data.");

        print("Please enter your first name:");
        Fname = in.readLine();

        print("Please enter your last name:");
        Lname = in.readLine();

        print("Please enter your gender:");
        gender = in.readLine();

        print("Please enter your phone number:");
        phone_nbr = in.readLine();

        print("Please enter the year you were born:");
        birthYear = in.readLine();

        print("Please enter the month you were born:");
        birthMonth = in.readLine();

        print("Please enter the day you born:");
        birthDay = in.readLine();

        // TODO update patient only with fields that aren't blank

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
