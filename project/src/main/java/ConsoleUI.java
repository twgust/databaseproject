import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Hashtable;

class ConsoleUI {

    enum AccessLevel { NONE, PATIENT, DOCTOR, ADMIN }

    private BufferedReader in;
    private String loginID;
    private IDatabase db;

    ConsoleUI(IDatabase database) {
        in = new BufferedReader(new InputStreamReader(System.in));
        loginID = "";
        db = database;
    }

    private void print(String str) {
        System.out.println(str);
    }

    private void print(ResultSet resultSet) {
        try {
            if (!resultSet.isBeforeFirst()) {
                System.out.println("No results.");
                return;
            }

            ResultSetMetaData metaData = resultSet.getMetaData();
            String columnLabel;
            int columnRowSize = 0; // nbr of chars in the column row

            // Print column labels
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                columnLabel = metaData.getColumnLabel(i);
                columnRowSize += columnLabel.length() + 4;
                System.out.print(columnLabel + "    ");
            }

            // Print dashed line
            System.out.print("\n");
            for (int i = 0; i < columnRowSize; i++) {
                System.out.print('-');
            }
            System.out.print("\n");

            while(resultSet.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    Object value = resultSet.getObject(i);
                    System.out.print(value.toString() + "    ");
                }
                System.out.println("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException");
        }
    }

    private boolean loginMenu() throws IOException, SQLException {
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

    private void login(AccessLevel accessLevel) throws IOException, SQLException {
        if (accessLevel == AccessLevel.PATIENT) {
            print("Are you an already registered patient? (y/n)");
            String answer = in.readLine();
            if (answer.equals("n")) {
                if (!registerNewPatient()) {
                    loginMenu();
                    return;
                }
            }
            else {
                print("Please enter you medical number:");
                loginID = in.readLine();

                // Checks if medical nbr exists
                ResultSet result = db.getMedicalNumber(loginID);
                if (!result.isBeforeFirst()) {
                    print("No such patient exists");
                    login(AccessLevel.PATIENT);
                    return;
                }
            }

            print("Logged in as a patient");
            patientMenu();
        }
        else if (accessLevel == AccessLevel.DOCTOR) {
            print("Please enter you employee number:");
            loginID = in.readLine();

            // Checks if employee nbr exists
            ResultSet result = db.getEmployeeNumber(loginID);
            if (!result.isBeforeFirst()) {
                print("No such doctor exists");
                loginMenu();
                return;
            }

            print("Logged in as a doctor");
            doctorMenu();
        }
        else if (accessLevel == AccessLevel.ADMIN) {
            loginID = "admin";
            print("Logged in as an admin");
            adminMenu();
        }
    }

    private void adminMenu() throws IOException, SQLException {
        String answer;

        print("\n--- ADMIN MENU ---");
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
        print(db.getAllPatientsAndSum());
    }

    private void viewAllAppointments() {
        print(db.getAllUpcomingAppointments());
    }

    private void viewMedicalRecordByPatient() throws IOException {
        print("Which patient? Please enter a 9-digit medical number:");
        String answer = in.readLine();

        print(db.getMedicalRecordsByPatient(answer));
    }

    private void viewAllDoctors() {
        print(db.getAllDoctors());
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

        if (db.addDoctor(employeeNbr, firstName, lastName,
                specialization, phoneNbr)) {
            print("Added successfully.");
        }
        else {
            print("Attempt to add doctor was unsuccessful.");
        }
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

        if (db.addSpecialization(specID, specName, visitCost)) {
            print("Added successfully.");
        }
        else {
            print("Attempt to add specialization was unsuccessful.");
        }
    }

    private void deleteDoctor() throws IOException {
        print("Which doctor? Please enter an employee number:");

        String employeeNbr = in.readLine();

        if (db.deleteDoctor(employeeNbr)) {
            print("Deleted successfully.");
        }
        else {
            print("Deletion was unsuccessful.");
        }
    }

    private void doctorMenu() throws IOException, SQLException {
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
        print(db.getUpcomingAppointmentsByDoctor(loginID));
    }

    private void viewPatientsByDoctor() {
        print(db.getPatientsByDoctor(loginID));
    }

    private void viewPrescribedDrugs() throws IOException {
        print("For which patient? Please enter a 9-digit medical number:");
        String medicalNbr = in.readLine();

        print(db.getDrugsPrescribedToPatient(medicalNbr));
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

        if (!db.updateTodaysMedicalRecord(loginID,
                medicalNbr, diagnosis, description)) {
            print("Attempt to insert a medical record was unsuccessful.");
            return;
        }

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

            if (!db.prescribeNewDrug(medicalNbr, drugID, drugName))
                print("Attempt to add a drug was unsuccessful.");

        }
    }

    private void registerUnavailability() throws IOException, SQLException {
        ResultSet resultTimes = db.getDoctorAvailabilityNextWeek(loginID);
        Hashtable<Integer, LocalDateTime> timesMap = new Hashtable<>();

        Integer row = 1;
        LocalDateTime dateTime;
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
                db.registerUnavailability(loginID, timesMap.get(answerRow));
            }
            else {
                print("Invalid input.");
            }
        }
    }

    private void patientMenu() throws IOException, SQLException {
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

    private boolean registerNewPatient() throws IOException {
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

        if (!db.addPatient(medicalNbr, fName, lName, gender,
                address, phoneNbr, birthYear, birthMonth, birthDay)) {
            System.err.println("Registration was unsuccessful.");
            return false;
        }

        loginID = medicalNbr;
        return true;
    }

    private void chooseDoctorToBook() throws IOException {
        ResultSet resultDoctors = db.getAllDoctorsSpecAndCost();
        Hashtable<Integer, String> doctorMap = new Hashtable<>();

        Integer row = 1;
        try {
            while (resultDoctors.next()) {
                doctorMap.put(row, resultDoctors.getString("employee_number"));
                print("\t" + row + ". "+resultDoctors.getNString("name") +
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

    private void chooseAppointment(String employeeNbr) throws IOException {
        ResultSet resultTimes = db.getDoctorAvailabilityNextWeek(employeeNbr);
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
                    bookAppointment(timesMap.get(answerRow), employeeNbr);
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

    private void bookAppointment(LocalDateTime dateTime, String employeeNbr) {
        if (db.bookAppointment(loginID, employeeNbr, dateTime))
            print("Booking was successful.");
        else
            print("Booking was unsuccessful.");

    }

    private void viewPatientDetails(String medicalNbr) {
        print(db.getPatient(medicalNbr));
    }

    private void editPatientDetails(String medicalNbr) throws IOException {
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

        // Update patient only with fields that aren't blank
        if (db.updatePatient(medicalNbr, Fname, Lname, gender,
                phone_nbr, birthYear, birthMonth, birthDay))
            print("Your information was successfully updated.");
        else
            print("Update was unsuccessful.");

    }

    void start() {
        try {
            if (!loginMenu()) {
                close();
                return;
            }

            close();
        } catch (IOException | SQLException e) {
            close();
        }
    }

    void close() {
        print("Closing application...");
    }

}
