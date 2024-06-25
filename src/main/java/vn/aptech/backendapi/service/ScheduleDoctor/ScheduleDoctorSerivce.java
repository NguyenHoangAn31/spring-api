package vn.aptech.backendapi.service.ScheduleDoctor;

import java.time.LocalDate;

public interface ScheduleDoctorSerivce {
    boolean create(LocalDate day , int departmentId , int doctorId , int slotId);
    boolean delete(LocalDate day , int departmentId , int doctorId , int slotId);
    
}
