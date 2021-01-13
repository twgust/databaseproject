import java.sql.ResultSet;
import java.time.LocalDateTime;

/**
 * @author Kasper S. Skott
 */
interface IDatabase {

    /**
     * Returns a single row with the medical nbr as specified, but only
     * if it exists. If it doesn't exist nothing should be selected.
     * @param medicalNbr the medical number to check
     */
    ResultSet getMedicalNumber(String medicalNbr);

    /**
     * Returns a single row with the employee nbr as specified, but only
     * if it exists. If it doesn't exist nothing should be selected.
     * @param employeeNbr the employee number to check
     */
    ResultSet getEmployeeNumber(String employeeNbr);

    /**
     * Returns all patients, each with their respective sum of all
     * visit costs of every appointment they've been to.
     * Columns returned are medical number, full name, and visit cost sum
     */
    ResultSet getAllPatientsAndSum();

    /**
     * Returns every appointment that hasn't yet occurred.
     */
    ResultSet getAllUpcomingAppointments();

    /**
     * Returns all medical records for the specified patient.
     * @param medicalNbr the patient
     */
    ResultSet getMedicalRecordsByPatient(String medicalNbr);

    /**
     * Returns all doctors.
     */
    ResultSet getAllDoctors();

    /**
     * Attemps to insert a new doctor.
     * @param employeeNbr
     * @param firstName
     * @param lastName
     * @param specialization the id of the spec
     * @param phoneNbr
     * @return true if successful, otherwise false
     */
    boolean addDoctor(String employeeNbr, String firstName, String lastName,
                      String specialization, String phoneNbr);

    /**
     * Attempts to insert a new specialization.
     * @param specId
     * @param specName
     * @param visitCost
     * @return true if successful, otherwise false
     */
    boolean addSpecialization(String specName, String visitCost);

    /**
     * Attempts to delete a doctor.
     * @param employeeNbr
     * @return true if successful, otherwise false
     */
    boolean deleteDoctor(String employeeNbr);

    /**
     * Returns every upcoming appointment for the given doctor.
     * @param employeeNbr doctor
     */
    ResultSet getUpcomingAppointmentsByDoctor(String employeeNbr);

    /**
     * Returns every patient that has had an appointment with
     * the doctor specified.
     * @param employeeNbr the doctor
     */
    ResultSet getPatientsByDoctor(String employeeNbr);

    /**
     * Returns all drugs in the database
     */
    ResultSet getAllDrugs();

    /**
     * Get every drug prescribed to the patient
     * @param medicalNbr the patient
     */
    ResultSet getDrugsPrescribedToPatient(String medicalNbr);

    /**
     * Update the appointment table with diagnosis and description,
     * on the appointment that is today between the doctor and patient.
     * @param employeeNbr the doctor
     * @param medicalNbr the patient
     * @param diagnosis
     * @param description
     * @return true if successful, otherwise false
     */
    boolean updateTodaysMedicalRecord(String employeeNbr, String medicalNbr,
                                      String diagnosis, String description);

    /**
     * Prescribes the given drug to the patient.
     * @param medicalNbr
     * @param drugId
     * @return true if successful, otherwise false
     */
    boolean prescribeNewDrug(String medicalNbr, String drugId);

    /**
     * Attempts to add a new drug with the given name.
     * @param drugName
     * @return true if successful, otherwise false
     */
    boolean addNewDrug(String drugName);

    /**
     * Attempts to register a date and time as unavailable for the doctor.
     * @param employeeNbr
     * @param dateTime
     * @return true if successful, otherwise false
     */
    boolean registerUnavailability(String employeeNbr, LocalDateTime dateTime);

    /**
     * Attempts to add a new patient.
     * @param medicalNbr
     * @param fName
     * @param lName
     * @param gender
     * @param address
     * @param phoneNbr
     * @param birthYear
     * @param birthMonth
     * @param birthDay
     * @return true if successful, otherwise false
     */
    boolean addPatient(String medicalNbr, String fName, String lName,
                       String gender, String address, String phoneNbr,
                       String birthYear, String birthMonth, String birthDay);

    /**
     * Returns all doctors and the specialization name and visit cost.
     * Columns returned must contain the following, with these exact names:
     * - "employee_number"
     * - "name" (full name)
     * - "specialization" (name of spec)
     * - "cost"
     */
    ResultSet getAllDoctorsSpecAndCost();

    /**
     * Returns every time available for the doctor for the coming week.
     * Results must contain a column called "datetime" of type DATETIME.
     * Nothing else is actually needed.
     * @param employeeNbr the doctor
     */
    ResultSet getDoctorAvailabilityNextWeek(String employeeNbr);

    /**
     * Attempts to book an appointment for the patient and doctor
     * at the specified date time.
     * @param medicalNbr
     * @param employeeNbr
     * @param dateTime
     * @return true if successul, otherwise false
     */
    boolean bookAppointment(String medicalNbr, String employeeNbr,
                            LocalDateTime dateTime);

    /**
     * Returns all information on a patient.
     * @param medicalNbr
     */
    ResultSet getPatient(String medicalNbr);

    /**
     * Update the patient with the given values.
     * If a value is null, then don't update that column.
     * @param medicalNbr
     * @param fName
     * @param lName
     * @param gender
     * @param phoneNbr
     * @param birthYear
     * @param birthMonth
     * @param birthDay
     * @return true if successful, otherwise false
     */
    boolean updatePatient(String medicalNbr, String fName, String lName,
                          String gender, String phoneNbr, String birthYear,
                          String birthMonth, String birthDay);

}
