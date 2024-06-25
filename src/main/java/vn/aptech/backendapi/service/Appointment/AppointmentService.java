package vn.aptech.backendapi.service.Appointment;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

import vn.aptech.backendapi.dto.AppointmentDto;
import vn.aptech.backendapi.dto.Appointment.AppointmentDetail;
import vn.aptech.backendapi.dto.Appointment.CustomAppointmentDto;

public interface AppointmentService {
        AppointmentDto save(AppointmentDto dto);
        List<CustomAppointmentDto> findAll();

        AppointmentDetail appointmentDetail(int appointmentId);

        void changestatus(int id, String status);
        List<AppointmentDto> findAppointmentsByScheduleDoctorIdAndStartTime(int scheduledoctorid, LocalTime starttime);


        //boolean changestatus(int id, String status);




    List<AppointmentDto> findAppointmentsPatientByPatientIdAndStatus(int patientId,
                                                                     String status);

    List<CustomAppointmentDto> findPatientsByDoctorIdAndAppointmentUpcoming(int doctorId , LocalDate startDate);
        List<CustomAppointmentDto> findPatientsByDoctorIdAndMedicalExaminationToday(int doctorId , LocalDate startDate , LocalDate endDate);
}

