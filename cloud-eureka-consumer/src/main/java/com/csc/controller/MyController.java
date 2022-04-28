package com.csc.controller;

import com.netflix.appinfo.InstanceInfo;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;

@RestController
public class MyController {
    @Autowired
    DiscoveryClient client;  //可以获取服务的信息
    @Autowired
    EurekaClient client2;  //eureka的客户端信息
    @Autowired
    LoadBalancerClient lb;

    @GetMapping("/client")
    public String client(){
        List<String> instances = client.getServices();
        for (String s : instances){
            System.out.println(s);
        }
        return "hi";
    }

    @GetMapping("client2")
    public Object client2(){
        List<ServiceInstance> instances = client.getInstances("EurekaServer");
        return instances;
    }

    @GetMapping("client3")
    public Object client3(){
        //List<InstanceInfo> instances = client2.getInstancesById("localhost:EurekaServer:7001");  //获取该服务的实例信息集合
        List<InstanceInfo> instances = client2.getInstancesByVipAddress("Eureka-provider", false);
        if(instances.size() > 0){
            InstanceInfo ins = instances.get(0);  //获取第一个实例信息
            if(ins.getStatus() == InstanceInfo.InstanceStatus.UP){  //判断他的状态是不是正常启动
                String url = "http://" + ins.getHostName() + ":" + ins.getPort() + "/getHi";  //通过主机名 + 端口号 + 方法 获取url
                System.out.println(url);
                RestTemplate restTemplate = new RestTemplate();
                String forObject = restTemplate.getForObject(url, String.class);
                System.out.println(forObject);
            }
        }
        return "XXOO";
    }

    @GetMapping("client4")
    public Object client4(){
        ServiceInstance instance = lb.choose("Eureka-provider");  //从列表返回一个服务实例  ribbon会自动剔除DOWN的服务
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/getHi";
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        return forObject;
    }
}
