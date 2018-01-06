package org.tracker.servicetracker;

import jdk.net.SocketFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ServiceStatusRepository extends JpaRepository<ServiceStatus, Long> {

    List<ServiceStatus> findByParentServiceIdAndStatus(String parentServiceId, boolean status);

    List<ServiceStatus> findByServiceIdAndStatus(String serviceId, boolean status);

    void deleteAllByParentServiceId(String parentServiceId);
}
