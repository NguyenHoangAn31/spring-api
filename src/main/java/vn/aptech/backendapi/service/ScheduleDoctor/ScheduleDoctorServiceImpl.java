package vn.aptech.backendapi.service.ScheduleDoctor;

import java.time.LocalDate;
import java.util.Optional;

import javax.print.Doc;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.aptech.backendapi.entities.Doctor;
import vn.aptech.backendapi.entities.Schedule;
import vn.aptech.backendapi.entities.ScheduleDoctor;
import vn.aptech.backendapi.repository.DoctorRepository;
import vn.aptech.backendapi.repository.ScheduleDoctorRepository;
import vn.aptech.backendapi.repository.ScheduleRepository;

@Service
public class ScheduleDoctorServiceImpl implements ScheduleDoctorSerivce {

    @Autowired
    private ScheduleDoctorRepository scheduleDoctorRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public boolean create(LocalDate day, int departmentId, int doctorId, int slotId) {
        int scheduleId = scheduleRepository.findScheduleIdByDayAndDepartmentIdAndSlotId(day, departmentId, slotId);
        try {
            ScheduleDoctor scheduleDoctor = new ScheduleDoctor();
            Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
            Optional<Doctor> doctor = doctorRepository.findById(doctorId);
            schedule.ifPresent(s -> scheduleDoctor.setSchedule(mapper.map(schedule, Schedule.class)));
            doctor.ifPresent(d -> scheduleDoctor.setDoctor(mapper.map(doctor, Doctor.class)));

            scheduleDoctorRepository.save(scheduleDoctor);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean delete(LocalDate day, int departmentId, int doctorId, int slotId) {

        Optional<Integer> id = scheduleDoctorRepository.findScheduleDoctorId(day, departmentId, doctorId, slotId);
        try {
            scheduleDoctorRepository.deleteById(id.get());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
