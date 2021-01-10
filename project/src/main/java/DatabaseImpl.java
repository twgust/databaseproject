import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return null;
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
        return false;
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
        return false;
    }

    @Override
    public ResultSet getPatient(String medicalNbr) {
        return null;
    }

    @Override
    public boolean updatePatient(String medicalNbr, String fName, String lName, String gender, String phoneNbr, String birthYear, String birthMonth, String birthDay) {
        return false;
    }
}
