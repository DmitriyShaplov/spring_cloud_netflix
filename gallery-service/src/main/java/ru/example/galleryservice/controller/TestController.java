package ru.example.galleryservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping
    public String home() {
        return "<a href='test/showAllServices'>Show All Services</a>";
    }

    @RequestMapping(value = "/showAllServices", method = RequestMethod.GET)
    public String showAllServiceIds() {

        List<String> serviceIds = this.discoveryClient.getServices();

        if (serviceIds == null || serviceIds.isEmpty()) {
            return "No services found!";
        }

        StringBuilder result = new StringBuilder("Service Ids:");

        for (String serviceId : serviceIds) {
            result.append("<br><a href='showService?serviceId=")
                    .append(serviceId)
                    .append("'>")
                    .append(serviceId).append("</a>");
        }
        return result.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/showService", method = RequestMethod.GET)
    public String showFirstService(@RequestParam(defaultValue = "") String serviceId) {

        List<ServiceInstance> instances = this.discoveryClient.getInstances(serviceId);

        if (instances == null || instances.isEmpty()) {
            return "No instances for service: " + serviceId;
        }

        StringBuilder result = new StringBuilder("Instances for Service: " + serviceId + "<br>");

        for (ServiceInstance serviceInstance : instances) {
            result.append("<br>Instance: ").append(serviceInstance.getUri());
            result.append("<br>" + "Hostname: ").append(serviceInstance.getHost());
            result.append("<br>" + "Port: ").append(serviceInstance.getPort()).append("<br>");
        }

        return result.toString();
    }
}
