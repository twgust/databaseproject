import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.BooleanSupplier;

public class DatabaseImpl implements IDatabase {

    private Connection conn;

    public DatabaseImpl(){
        try {
            String password = new String(Files.readAllBytes(Paths.get("sqlpassword.txt")));
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Medical_Center;", "user", password);
            System.out.println("Connected to database.");
        } catch (SQLException | IOException throwables) {
            System.out.println("Failed");
            throwables.printStackTrace();
        }
    }

    @Override
    public ResultSet getMedicalNumber(String medicalNbr) {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT medicalnumber FROM patient WHERE patient.medicalnumber='"+medicalNbr+"'";
        try{
            st = conn.createStatement();
            rs= st.executeQuery(query);
            return rs;

        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        // else just return result
        return rs;
    }

    @Override
    public ResultSet getEmployeeNumber(String employeeNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_getEmployeeNumber @employeeNbr = ?");
            /* STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("SELECT employee_number FROM doctor WHERE employee_number = ?");

             */
            ps.setInt(1,Integer.parseInt(employeeNbr));
            return ps.executeQuery();
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getAllPatientsAndSum() {
        try {

            PreparedStatement ps = conn.prepareStatement("EXEC sp_getAllPatientsAndSum");
            /* STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("SELECT patient.medicalnumber, patient.first_name, patient.last_name, patient.gender, patient.phone, patient.birthdate, patient.reg_date, \n" +
                    "(SELECT SUM(specialization.cost) as sum_cost FROM appointment \n" +
                    "INNER JOIN doctor ON doctor.employee_number = appointment.employee_number \n" +
                    "INNER JOIN specialization ON specialization.spec_id = doctor.spec_id \n" +
                    ") as sum_cost FROM patient ORDER BY patient.first_name, patient.last_name");

             */
            return ps.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getAllUpcomingAppointments() {
        try {
        PreparedStatement ps = conn.prepareStatement("EXEC sp_getAllUpcomingAppointments");

        /* STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("SELECT appointment.app_id, appointment.employee_number, appointment.app_date, appointment.medicalnumber FROM appointment WHERE appointment.app_date > GETDATE();");
         */
        return ps.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getMedicalRecordsByPatient(String medicalNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_getMedicalRecordsByPatient @medicalNbr = ?");
            /* STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointment WHERE appointment.app_date < GETDATE() AND medicalnumber = ?");

             */
            ps.setString(1, medicalNbr);
            return ps.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getAllDoctors() {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_getAllDoctors");
            /*STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("SELECT employee_number, first_name, last_name, phone, specialization  FROM doctor INNER JOIN specialization ON specialization.spec_id = doctor.spec_id");

             */
            return ps.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addDoctor(String employeeNbr, String firstName, String lastName, String specialization, String phoneNbr) {
        try {

            PreparedStatement ps = conn.prepareStatement("EXEC sp_addDoctor @employeeNbr = ?, @firstName = ?, @lastName = ?, @spec_id = ?, @phone = ?");
            /* STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("INSERT INTO doctor(employee_number,first_name,last_name,spec_id, phone) VALUES(?,?,?,?,?)");

             */
            ps.setInt(1,Integer.parseInt(employeeNbr));
            ps.setNString(2,firstName);
            ps.setNString(3,lastName);
            ps.setInt(4,Integer.parseInt(specialization));
            ps.setString(5,phoneNbr);
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException throwables) {
            //throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addSpecialization(String specName, String visitCost) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_addSpecialization @specName = ?, @cost = ?");
            /*STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("INSERT INTO specialization(specialization,cost) VALUES(?,?)");

             */
            //ps.setInt(1,Integer.parseInt(specId));
            ps.setString(1,specName);
            ps.setInt(2, Integer.parseInt(visitCost));
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean deleteDoctor(String employeeNbr) {
        try {

            PreparedStatement ps = conn.prepareStatement("EXEC sp_deleteDoctor @employeeNbr = ?");
            /*STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("DELETE FROM doctor WHERE employee_number = ?");

             */
            ps.setInt(1, Integer.parseInt(employeeNbr));
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public ResultSet getUpcomingAppointmentsByDoctor(String employeeNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_getUpcomingAppointmentsByDoctor @employeeNbr = ?");
            /*STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("SELECT appointment.app_id, appointment.employee_number, appointment.app_date, appointment.medicalnumber FROM appointment WHERE appointment.app_date > GETDATE() AND employee_number = ?;");
            */
            ps.setInt(1, Integer.parseInt(employeeNbr));
            return ps.executeQuery();
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getPatientsByDoctor(String employeeNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_getPatientsByDoctor @employeeNbr = ?");
            /*
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT patient.medicalnumber, patient.first_name, patient.last_name, patient.gender, patient.phone, patient.birthdate, patient.reg_date FROM patient INNER JOIN appointment ON appointment.medicalnumber = patient.medicalnumber AND appointment.employee_number = ?");
             */
            ps.setInt(1, Integer.parseInt(employeeNbr));
            return ps.executeQuery();
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getAllDrugs() {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_getAllDrugs");
            /*
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM drug");
             */
            return ps.executeQuery();
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getDrugsPrescribedToPatient(String medicalNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_getDrugsPrescribedToPatient @medicalNbr = ?");
            /*
            STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("SELECT patient.medicalnumber,first_name,last_name,gender,phone,birthdate,name as drug FROM patient INNER JOIN prescribed_drugs ON prescribed_drugs.medicalnumber = patient.medicalnumber INNER JOIN drug ON drug.drug_id = prescribed_drugs.drug_id WHERE patient.medicalnumber = ?");
             */
            ps.setInt(1, Integer.parseInt(medicalNbr));
            return ps.executeQuery();
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateTodaysMedicalRecord(String employeeNbr, String medicalNbr, String diagnosis, String description) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_updateTodaysMedicalRecord @employeeNbr = ?, @medicalNbr = ?, @diagnosis = ?, @description = ?");
            /*STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET diagnosis = ?, description = ? WHERE year(app_date) = year(GETDATE()) AND month(app_date) = month(GETDATE()) AND day(app_date) = day(GETDATE()) AND medicalnumber = ? AND employee_number = ?");
             */
            ps.setString(1, diagnosis);
            ps.setString(2,description);
            ps.setInt(3, Integer.parseInt(medicalNbr));
            ps.setInt(4, Integer.parseInt(employeeNbr));
            int returnCode  = ps.executeUpdate();
            if(returnCode < 1){
                return false;
            }
            else return true;
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean prescribeNewDrug(String medicalNbr, String drugId) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_prescribeNewDrug @medicalNbr = ?, @drug_id = ?");
            /*
            STATEMENT
            PreparedStatement ps = conn.prepareStatement("INSERT INTO prescribed_drugs(medicalnumber, drug_id) VALUES(?,?)");
             */
            ps.setInt(1,Integer.parseInt(medicalNbr));
            ps.setInt(2,Integer.parseInt(drugId));
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addNewDrug(String drugName) {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_addNewDrug @name = ?");
            /*
            STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("INSERT INTO drug(name) VALUES(?)");
             */
            ps.setString(1,drugName);
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean registerUnavailability(String employeeNbr, LocalDateTime dateTime) {
        try {
            PreparedStatement ps =conn.prepareStatement("EXEC sp_registerUnavailability @employeeNbr = ?, @dateTime = ?");
            /*STATEMENT CODE
            PreparedStatement ps = conn.prepareStatement("INSERT INTO unavailability(employee_number, app_date) VALUES(?,?)");

             */
            ps.setInt(1, Integer.parseInt(employeeNbr));
            ps.setTimestamp(2, Timestamp.valueOf(dateTime));
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addPatient(String medicalNbr, String fName, String lName, String gender, String address, String phoneNbr, String birthYear, String birthMonth, String birthDay) {
        try{
            PreparedStatement ps = conn.prepareStatement("INSERT INTO patient(medicalnumber, first_name, last_name, gender, address, phone, birthdate, reg_date) VALUES ('" + medicalNbr + "','" + fName + "','"
                    + lName + "','" + gender + "','" + address + "','" + phoneNbr + "','" + birthYear + "-" + birthMonth + "-"
                    + birthDay+ "', CURRENT_TIMESTAMP);");
            ps.executeUpdate();
        }catch (SQLException throwables){
            System.out.println("FAIL");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ResultSet getAllDoctorsSpecAndCost() {
        try {
            PreparedStatement ps = conn.prepareStatement("EXEC sp_getAllDoctorsSpecAndCost");
            /*
           PreparedStatement ps = conn.prepareStatement("SELECT employee_number, first_name  + ' ' + last_name AS name, specialization.specialization, cost FROM doctor INNER JOIN specialization ON specialization.spec_id = doctor.spec_id");
            */
            return ps.executeQuery();

        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getDoctorAvailabilityNextWeek(String employeeNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM SF_getDoctorAvailabilityNextWeek(?)");
            ps.setInt(1,Integer.parseInt(employeeNbr));
            return ps.executeQuery();
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean bookAppointment(String medicalNbr, String employeeNbr, LocalDateTime dateTime) {

        try{
            // query [unavailability] table for doctor selected in UI
            PreparedStatement ps = conn.prepareStatement("INSERT INTO appointment(employee_number, app_date,medicalnumber, time_of_booking) VALUES(?,?,?, CURRENT_TIMESTAMP)");
            ps.setInt(1, Integer.parseInt(employeeNbr));
            ps.setTimestamp(2, Timestamp.valueOf(dateTime));
            ps.setInt(3,Integer.parseInt(medicalNbr));
            ps.executeUpdate();
            return true;
        }catch (SQLException throwables ){
            //throwables.printStackTrace();
            if(throwables.getErrorCode() == 3609){
                System.out.println("Today is not friday");
            }
            else System.out.println("Unknown error occurred");
            return false;
        }catch (NumberFormatException e){
            return false;
        }
    }

    @Override
    public ResultSet getPatient(String medicalNbr) {
        Statement st = null;
        ResultSet rs = null;
        String query = "SELECT * FROM [patient] WHERE patient.medicalnumber='"+medicalNbr+"'";
        try{
            st = conn.createStatement();
            rs= st.executeQuery(query);
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean updatePatient(String medicalNbr, String fName, String lName, String gender, String phoneNbr, String birthYear, String birthMonth, String birthDay) {
        Statement st = null;
        String baseQuery = "UPDATE patient SET ";
        String query = "";


        if(fName != null && !fName.isBlank()){
            // query to update fName
            query += "first_name ='" + fName + "'";
        }
        if(lName != null && !lName.isBlank()){
            // query to update lName
            query += ", last_name ='" + lName + "'";
        }
        if(gender.equalsIgnoreCase("m") || gender.equalsIgnoreCase("f")){
            // query to update
            query += ", gender ='" + gender.toUpperCase() + "'";
        }
        // no lower limit
        if(phoneNbr.matches("[0-9]+") && phoneNbr.length() <= 15){
            // query to update
            query += ", phone ='" + phoneNbr + "'";
        }
        if(birthYear.matches("[0-9]+") && birthYear.length() <= 4){
            // query to update
            query += ", birthdate ='" + birthYear + "-";
        }
        if(birthMonth.matches("[0-9]+") && birthMonth.length() <= 2){
            // query to update
            query += birthMonth;
        }
        if(birthDay.matches("[0-9]+") && birthDay.length() <=2){
            // query
            query += "-" + birthDay +"'";
        }
        if(query.isBlank()){
            return false;
        }

        query+= " WHERE medicalnumber = '"+medicalNbr+"';";
        System.out.println(query);

        try{
            st = conn.createStatement();
            st.executeUpdate(baseQuery+query);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

}
