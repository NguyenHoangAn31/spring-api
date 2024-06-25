package vn.aptech.backendapi.service.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.aptech.backendapi.dto.AppointmentDto;
import vn.aptech.backendapi.dto.PatientDto;
import vn.aptech.backendapi.dto.Appointment.AppointmentDetail;
import vn.aptech.backendapi.dto.Appointment.CustomAppointmentDto;
import vn.aptech.backendapi.entities.Appointment;

import vn.aptech.backendapi.entities.Partient;
import vn.aptech.backendapi.entities.ScheduleDoctor;
import vn.aptech.backendapi.repository.AppointmentRepository;
import vn.aptech.backendapi.repository.PartientRepository;
import vn.aptech.backendapi.repository.ScheduleDoctorRepository;
import vn.aptech.backendapi.service.Doctor.DoctorService;
import vn.aptech.backendapi.service.Patient.PatientService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PartientRepository partientRepository;
    @Autowired
    private ScheduleDoctorRepository scheduleDoctorRepository;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;

    private AppointmentDto toDto(Appointment appointment) {
        AppointmentDto a = mapper.map(appointment, AppointmentDto.class);
        a.setPartientId(appointment.getPartient().getId());
        a.setScheduledoctorId(appointment.getScheduledoctor().getId());
        return a;
    }

    private CustomAppointmentDto toCustomDto(Appointment appointment) {
        CustomAppointmentDto a = mapper.map(appointment, CustomAppointmentDto.class);
        // a.setImage(partientRepository.findById(appointment.getPartient().getId()).get().getImage());
        // a.setFullName(partientRepository.findById(appointment.getPartient().getId()).get().getFullName());
        a.setPatientDto(mapper.map(appointment.getPartient(), PatientDto.class));
        return a;
    }

    private AppointmentDetail toAppointmentDetail(Appointment appointment) {
        AppointmentDetail a = mapper.map(appointment, AppointmentDetail.class);
        a.setPatient(patientService.getPatientByPatientId(appointment.getPartient().getId()).get());
        a.setDoctor(doctorService.findById(appointment.getScheduledoctor().getDoctor().getId()).get());
        return a;
    }

    @Override
    public List<CustomAppointmentDto> findAll() {
        List<Appointment> a = appointmentRepository.findAll();
        return a.stream().map(this::toCustomDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changestatus(int id, String status) {
        try {
            Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
            if (appointmentOptional.isPresent()) {
                Appointment a = appointmentOptional.get();
                a.setStatus(status);
                appointmentRepository.save(a);
            } else {
                // Xử lý khi không tìm thấy Appointment với id tương ứng
                throw new IllegalArgumentException("Appointment not found for id: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý các ngoại lệ tại đây nếu cần
        }
    }

    @Override
    public AppointmentDetail appointmentDetail(int appointmentId) {
        Appointment a = appointmentRepository.findById(appointmentId).get();
        return toAppointmentDetail(a);
    }

    @Override
    public AppointmentDto save(AppointmentDto dto) {
        Appointment a = mapper.map(dto, Appointment.class);
        a.setAppointmentDate(LocalDate.parse(dto.getAppointmentDate()));
        a.setMedicalExaminationDay(LocalDate.parse(dto.getMedicalExaminationDay()));
        a.setClinicHours(LocalTime.parse(dto.getClinicHours()));
        if (dto.getPartientId() != 0) {
            Partient p = partientRepository.getPatientByUserId(dto.getPartientId());
            a.setPartient(mapper.map(p, Partient.class));
        }
        if (dto.getScheduledoctorId() != 0) {
            Optional<ScheduleDoctor> s = scheduleDoctorRepository.findById(dto.getScheduledoctorId());
            s.ifPresent(scheduledoctor -> a.setScheduledoctor(mapper.map(s, ScheduleDoctor.class)));
        }
        Appointment result = appointmentRepository.save(a);
        return toDto(result);
    }

    @Override
    public List<AppointmentDto> findAppointmentsByScheduleDoctorIdAndStartTime(int scheduledoctorid,
            LocalTime starttime) {
        return appointmentRepository.findAppointmentsByScheduleDoctorIdAndStartTime(scheduledoctorid, starttime)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AppointmentDto mapAppointmentDto(Appointment appointment) {
        AppointmentDto app = new AppointmentDto();
        app.setId(appointment.getId());
        app.setPartientId(appointment.getPartient().getId());
        app.setScheduledoctorId(appointment.getScheduledoctor().getId());
        app.setImage(appointment.getScheduledoctor().getDoctor().getImage());
        app.setFullName(appointment.getScheduledoctor().getDoctor().getFullName());
        app.setDepartmentName(appointment.getScheduledoctor().getDoctor().getDepartment().getName());
        app.setAppointmentDate(String.valueOf(appointment.getAppointmentDate()));
        app.setNote(appointment.getNote());
        app.setPayment(appointment.getPayment());
        app.setTitle(appointment.getScheduledoctor().getDoctor().getTitle());
        app.setClinicHours(String.valueOf(appointment.getClinicHours()));
        app.setStatus(appointment.getStatus());
        app.setMedicalExaminationDay(String.valueOf(appointment.getMedicalExaminationDay()));
        app.setPrice(appointment.getScheduledoctor().getDoctor().getPrice());
        return app;
    }

    @Override
    public List<AppointmentDto> findAppointmentsPatientByPatientIdAndStatus(int patientId,
                                                                            String status) {
        return appointmentRepository.findAppointmentsPatientByPatientIdAndStatus(patientId, status)
                .stream()
                .map(this::mapAppointmentDto)
                .sorted(Comparator.comparing(AppointmentDto::getMedicalExaminationDay))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomAppointmentDto> findPatientsByDoctorIdAndAppointmentUpcoming(int doctorId, LocalDate startDate) {
        return appointmentRepository.findPatientsByDoctorIdAndAppointmentUpcoming(doctorId, startDate).stream()
                .map(this::toCustomDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomAppointmentDto> findPatientsByDoctorIdAndMedicalExaminationToday(int doctorId,
            LocalDate startDate, LocalDate endDate) {
        return appointmentRepository.findPatientsByDoctorIdAndMedicalExaminationToday(doctorId, startDate, endDate)
                .stream().map(this::toCustomDto)
                .collect(Collectors.toList());
    }

}
