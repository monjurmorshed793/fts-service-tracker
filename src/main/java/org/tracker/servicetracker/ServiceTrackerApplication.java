package org.tracker.servicetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController("/")
public class ServiceTrackerApplication {


    @Autowired
    private ServiceStatusRepository serviceStatusRepository;

    Logger logger = LoggerFactory.getLogger(ServiceTrackerApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ServiceTrackerApplication.class, args);
	}


	@GetMapping("/")
	public List<ServiceStatus> getAllStatusList(){
	    return serviceStatusRepository.findAll();
    }

    @GetMapping("/service")
    public boolean isExecuted(@RequestParam("service-name") String serviceName, @RequestParam("service-number") Integer serviceNumber){
      List<ServiceStatus> testStatusList = serviceStatusRepository.findAll();
      if(serviceName.equals("serviceA2")){
        boolean found=true;
      }
	    List<ServiceStatus> serviceStatusList = serviceStatusRepository.findByParentServiceIdAndStatus(serviceName, true);
	    boolean status=serviceStatusList.size()==serviceNumber?true:false;
	    if(status){
        List<ServiceStatus> serviceStatuseListOfServiceId = serviceStatusRepository.findByServiceIdAndStatus(serviceName, false);
        serviceStatuseListOfServiceId.forEach(s -> s.setStatus(true));
        serviceStatusRepository.save(serviceStatuseListOfServiceId);
        serviceStatusRepository.delete(serviceStatusList);
      }

	    return status;
    }

    @KafkaListener(topics = "tracker")
    public void listen(ConsumerRecord<?,?> cr) throws Exception{
        logger.info(cr.key().toString());
        logger.info ( cr.value().toString());
        ObjectMapper mapper= new ObjectMapper();
        ServiceStatus serviceStatus = mapper.readValue(cr.value().toString(), ServiceStatus.class);
        serviceStatusRepository.save(serviceStatus);
    }
}
