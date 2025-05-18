package pl.edu.hospital.dto.record;

import pl.edu.hospital.dto.PrescriptionDto;
import pl.edu.hospital.entity.enums.Specialization;

import java.time.LocalDate;
import java.util.List;

public class RecordForPatientDto {
    private String doctorFullName;
    private Specialization specialization;
    private LocalDate appointmentDate;
    private String diagnosis;
    private String summary;
    private List<PrescriptionDto> prescriptions;

    public String getDoctorFullName() {
        return doctorFullName;
    }

    public void setDoctorFullName(String doctorFullName) {
        this.doctorFullName = doctorFullName;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<PrescriptionDto> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionDto> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
