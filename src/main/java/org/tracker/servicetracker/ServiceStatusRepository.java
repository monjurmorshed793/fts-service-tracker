package org.tracker.servicetracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ServiceStatusRepository extends JpaRepository<ServiceStatus, Long> {

    List<ServiceStatus> findByParentServiceIdAndStatus(String parentServiceId, boolean status);

    List<ServiceStatus> findByServiceIdAndStatus(String serviceId, boolean status);

  List<ServiceStatus> findByServiceId(String serviceId);

  @Query("delete from ServiceStatus s where s.parentServiceId=?1 ")
  void removeByParentServiceId(String parentServiceId);
}
