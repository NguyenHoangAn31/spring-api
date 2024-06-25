package vn.aptech.backendapi.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.backendapi.dto.AppointmentDto;
import vn.aptech.backendapi.dto.Appointment.AppointmentDetail;
import vn.aptech.backendapi.dto.Appointment.CustomAppointmentDto;
import vn.aptech.backendapi.service.Appointment.AppointmentService;

@RestController
@RequestMapping(value = "/api/appointment", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomAppointmentDto>> findAll() {
        List<CustomAppointmentDto> result = appointmentService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDetail> appointmentDetail(@PathVariable("id") int id) {
        AppointmentDetail result = appointmentService.appointmentDetail(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/changestatus/{id}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changestatus(@PathVariable("id") int id, @PathVariable("status") String status) {
        try {
            appointmentService.changestatus(id, status);
            return ResponseEntity.ok("Appointment status updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update appointment status");
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<AppointmentDto> test(@RequestBody AppointmentDto dto) {
        System.out.println(dto);
        AppointmentDto result = appointmentService.save(dto);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/appointmentbyscheduledoctoridandstarttime/{scheduledoctorid}/{starttime}")
    public ResponseEntity<List<AppointmentDto>> getAppointments(
            @PathVariable("starttime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime starttime,
            @PathVariable("scheduledoctorid") int scheduledoctorid) {
        List<AppointmentDto> result = appointmentService
                .findAppointmentsByScheduleDoctorIdAndStartTime(scheduledoctorid, starttime);
        return ResponseEntity.ok(result);
    }

    // Tìm những lịch khám của patient ngày hôm nay dựa vào patientId và medical_exam_date
    @GetMapping("/appointment-schedule-patientId-and-status/{patientId}/{status}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsPatient(
            @PathVariable("patientId") int patientId, @PathVariable("status") String status) {
        List<AppointmentDto> result = appointmentService
                .findAppointmentsPatientByPatientIdAndStatus(patientId, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/patientsbydoctoridandmedicalexaminationupcoming/{doctorid}/{startdate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomAppointmentDto>> findPatientsByDoctorIdAndAppointmentUpcoming(
            @PathVariable("doctorid") int doctorId,
            @PathVariable("startdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        List<CustomAppointmentDto> result = appointmentService.findPatientsByDoctorIdAndAppointmentUpcoming(doctorId,
                startDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/patientsbydoctoridandmedicalexaminationtoday/{doctorid}/{startdate}/{enddate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomAppointmentDto>> findPatientsByDoctorIdAndMedicalExaminationToday(
            @PathVariable("doctorid") int doctorId,
            @PathVariable("startdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable("enddate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CustomAppointmentDto> result = appointmentService
                .findPatientsByDoctorIdAndMedicalExaminationToday(doctorId, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
