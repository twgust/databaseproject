import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
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
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT employee_number FROM doctor WHERE employee_number = ?");
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
            PreparedStatement ps = conn.prepareStatement("SELECT patient.medicalnumber, patient.first_name, patient.last_name, patient.gender, patient.phone, patient.birthdate, patient.reg_date, \n" +
                    "(SELECT SUM(specialization.cost) as sum_cost FROM appointment \n" +
                    "INNER JOIN doctor ON doctor.employee_number = appointment.employee_number \n" +
                    "INNER JOIN specialization ON specialization.spec_id = doctor.spec_id \n" +
                    ") as sum_cost FROM patient ORDER BY patient.first_name, patient.last_name");
            return ps.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getAllUpcomingAppointments() {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT appointment.app_id, appointment.employee_number, appointment.app_date, appointment.medicalnumber FROM appointment WHERE appointment.app_date > GETDATE();");
            return ps.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getMedicalRecordsByPatient(String medicalNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointment WHERE appointment.app_date < GETDATE() AND medicalnumber = ?");
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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM doctor");
            return ps.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addDoctor(String employeeNbr, String firstName, String lastName, String specialization, String phoneNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO doctor(employee_number,first_name,last_name,spec_id, phone) VALUES(?,?,?,?,?)");
            ps.setInt(1,Integer.parseInt(employeeNbr));
            ps.setString(2,firstName);
            ps.setString(3,lastName);
            ps.setInt(4,Integer.parseInt(specialization));
            ps.setString(5,phoneNbr);
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addSpecialization(String specId, String specName, String visitCost) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO specialization(spec_id,specialization,cost) VALUES(?,?,?)");
            ps.setInt(1,Integer.parseInt(specId));
            ps.setString(2,specName);
            ps.setInt(3, Integer.parseInt(visitCost));
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
            PreparedStatement ps = conn.prepareStatement("DELETE FROM doctor WHERE employee_number = ?");
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
            PreparedStatement ps = conn.prepareStatement("SELECT appointment.app_id, appointment.employee_number, appointment.app_date, appointment.medicalnumber FROM appointment WHERE appointment.app_date > GETDATE() AND employee_number = ?;");
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
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT patient.medicalnumber, patient.first_name, patient.last_name, patient.gender, patient.phone, patient.birthdate, patient.reg_date FROM patient INNER JOIN appointment ON appointment.medicalnumber = patient.medicalnumber AND appointment.employee_number = ?");
            ps.setInt(1, Integer.parseInt(employeeNbr));
            return ps.executeQuery();
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet getDrugsPrescribedToPatient(String medicalNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT patient.medicalnumber,first_name,last_name,gender,phone,birthdate,name FROM patient INNER JOIN prescribed_drugs ON prescribed_drugs.medicalnumber = patient.medicalnumber INNER JOIN drug ON drug.drug_id = prescribed_drugs.drug_id WHERE patient.medicalnumber = ?");
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
            PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET diagnosis = ?, description = ? WHERE year(app_date) = year(GETDATE()) AND month(app_date) = month(GETDATE()) AND day(app_date) = day(GETDATE()) AND medicalnumber = ? AND employee_number = ?");
            ps.setString(1, diagnosis);
            ps.setString(2,description);
            ps.setInt(3, Integer.parseInt(medicalNbr));
            ps.setInt(4, Integer.parseInt(employeeNbr));
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean prescribeNewDrug(String medicalNbr, String drugId, String drugName) {
        //TVETYDIG
        try {
            PreparedStatement ps = conn.prepareStatement("");
            ps.setInt(1, Integer.parseInt(medicalNbr));
            ps.setInt(2, Integer.parseInt(drugId));
            ps.setString(3,drugName);
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
            PreparedStatement ps = conn.prepareStatement("INSERT INTO unavailability(employee_number, app_date) VALUES(?,)");
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
        return false;
    }

    @Override
    public ResultSet getAllDoctorsSpecAndCost() {
        return null;
    }

    @Override
    public ResultSet getDoctorAvailabilityNextWeek(String employeeNbr) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM unavailability WHERE employee_number = ?");
            ps.setInt(1, Integer.parseInt(employeeNbr));
            return ps.executeQuery();
        } catch (SQLException | NumberFormatException throwables) {
            throwables.printStackTrace();
            return null;
        }
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

    public static void main(String[] args) {
        DatabaseImpl test = new DatabaseImpl();
        ResultSet rs = test.getUpcomingAppointmentsByDoctor("123455555");
    }
}
