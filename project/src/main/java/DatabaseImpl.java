import com.sun.source.tree.TryTree;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;

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
            if(!rs.next()){
                // no result
                return rs;
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        // else just return result
         return rs;
    }

    @Override
    public ResultSet getEmployeeNumber(String employeeNbr) {
        return null;
    }

    @Override
    public ResultSet getAllPatientsAndSum() {
        return null;
    }

    @Override
    public ResultSet getAllUpcomingAppointments() {
        return null;
    }

    @Override
    public ResultSet getMedicalRecordsByPatient(String medicalNbr) {
        return null;
    }

    @Override
    public ResultSet getAllDoctors() {
        return null;
    }

    @Override
    public boolean addDoctor(String employeeNbr, String firstName, String lastName, String specialization, String phoneNbr) {
        return false;
    }

    @Override
    public boolean addSpecialization(String specId, String specName, String visitCost) {
        return false;
    }

    @Override
    public boolean deleteDoctor(String employeeNbr) {
        return false;
    }

    @Override
    public ResultSet getUpcomingAppointmentsByDoctor(String employeeNbr) {
        return null;
    }

    @Override
    public ResultSet getPatientsByDoctor(String employeeNbr) {
        return null;
    }

    @Override
    public ResultSet getDrugsPrescribedToPatient(String medicalNbr) {
        return null;
    }

    @Override
    public boolean updateTodaysMedicalRecord(String employeeNbr, String medicalNbr, String diagnosis, String description) {
        return false;
    }

    @Override
    public boolean prescribeNewDrug(String medicalNbr, String drugId, String drugName) {
        return false;
    }

    @Override
    public boolean registerUnavailability(String employeeNbr, LocalDateTime dateTime) {
        return false;
    }
    @Override
    public boolean addPatient(String medicalNbr, String fName, String lName, String gender, String address, String phoneNbr, String birthYear, String birthMonth, String birthDay) {
        try{
            PreparedStatement ps = conn.prepareStatement("INSERT INTO [patient] VALUES ('" + medicalNbr + "','" + fName + "','"
                    + lName + "','" + gender + "','" + address + "','" + phoneNbr + "','" + birthDay + "','" + birthMonth + "','"
                    + birthDay+ "');");
        }catch (SQLException throwables){
            System.out.println("FAIL");
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ResultSet getAllDoctorsSpecAndCost() {
        return null;
    }

    @Override
    public ResultSet getDoctorAvailabilityNextWeek(String employeeNbr) {
        return null;
    }

    @Override
    public boolean bookAppointment(String medicalNbr, String employeeNbr, LocalDateTime dateTime) {
        String getUnavailability = "SELECT [employee_number] FROM [unavailabilty] WHERE unavailabilty.employee_number='"
        + employeeNbr +"' AND" + dateTime + ";"; // add dateTime
        ResultSet rsCheckUnavailability = null;
        Statement stCheckUnavailability = null;

        String insertUnavailability = "INSERT INTO [unavailability] VALUES ('" + employeeNbr + "'," + dateTime+");";
        Statement stUnavailability;

        // need to insert app_id when it's auto increment? also, description and diagnosis?
        String insertAppointment = "INSERT INTO [appointment] VALUES ([app_id], '" + employeeNbr +"'," + dateTime +
                "'" + medicalNbr + "');";
        Statement stAppointment;

        try{
            // query [unavailability] table for doctor selected in UI
            stCheckUnavailability = conn.createStatement();
            rsCheckUnavailability = stCheckUnavailability.executeQuery(getUnavailability);

            // if resultset returns that there is no unavailability then book appointment and set unavailability
            if(rsCheckUnavailability.getTimestamp("app_date") == null){
                // statement that inserts into unavailability table
                // satement that inserts into appointment table
                stUnavailability = conn.createStatement();
                stAppointment = conn.createStatement();

                stUnavailability.executeQuery(insertUnavailability);
                stAppointment.executeQuery(insertAppointment);
            }
            // if ResultSet returns unavailability (ResultSet doesn't return null)
            else{
                return false;
            }

            }catch (SQLException throwables){
            throwables.printStackTrace();
            return false;
        }
        return true;
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
        String query = "UPDATE patient SET ";
        if(fName != null || !fName.equals("")){
            // query to update fName
            query += "first_name '" + fName + "' ";
        }
        if(lName != null || !lName.equals("")){
            // query to update lName
            query += "last_name '" + lName + "' ";
        }
        if(gender.equalsIgnoreCase("m") || gender.equalsIgnoreCase("f")){
            // query to update
            query += "gender '" + gender + "' ";
        }
            // no lower limit
        if(phoneNbr.matches("[0-9]+") && phoneNbr.length() <= 15){
            // query to update
            query += "phone '" + phoneNbr + "' ";
        }
        if(birthYear.matches("[0-9]+") && birthYear.length() <= 4){
            // query to update
            query += "birthdate '" + birthYear + "-";
        }
        if(birthMonth.matches("[0-9]+") && birthYear.length() <= 2){
            // query to update
            query += birthMonth;
        }
        if(birthDay.matches("[0-9]+") && birthDay.length() <=2){
            // query
            query += "-" + birthDay;
        }
        try{
            st = conn.createStatement();
            st.executeQuery(query);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
}
