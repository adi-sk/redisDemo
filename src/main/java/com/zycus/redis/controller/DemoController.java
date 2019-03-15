package com.zycus.redis.controller;

import com.zycus.redis.dao.UserDao;
import com.zycus.redis.model.User;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    UserDao userDao = new UserDao();


    @RequestMapping(value="/hello", method = RequestMethod.GET, produces = "text/plain")
    public String hello() {
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server successful !");
        System.out.println("DONE : " + jedis.ping());

        testString(jedis);

        testHash(jedis);
        return " done ! ";
    }

    @RequestMapping(value="/addUser", method = RequestMethod.POST)
    public String addUser(@RequestBody User user) {
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server successful !");
        System.out.println("DONE : " + jedis.ping());

        jedis.select(5);

        try {
            userDao.addUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(user.getFollowers() > 1000){
            Map<String,String> userMap = getUserMap(user.getUserId(),user.getName(),user.getFollowers()+"",user.getEmailId());
            jedis.hmset("user:" + user.getUserId() , userMap);
        }

        return " done ! ";
    }

    @RequestMapping(value="/updateName", method = RequestMethod.GET, produces = "text/plain")
    public String updateName(@RequestParam("userId") String userId, @RequestParam("userName") String userName) {
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server successful !");
        System.out.println("DONE : " + jedis.ping());

        jedis.select(5);

        try {
            userDao.updateUserName(userId,userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jedis.hset("user:" + userId,"name",userName);

        return " done ! ";
    }

    @RequestMapping(value="/getUser", method = RequestMethod.GET, produces = "application/json")
    public User getUser(@RequestParam("userId") String userId) {
        Jedis jedis = new Jedis("localhost");
        System.out.println("Connection to server successful !");
        System.out.println("DONE : " + jedis.ping());

        jedis.select(5);
        User user = new User();
        String key = "user:" +userId;
        if(jedis.hexists(key ,"userId")){
            user.setUserId(jedis.hget(key,"userId"));
            user.setName(jedis.hget(key,"name"));
            user.setFollowers(Long.parseLong(jedis.hget(key,"followers")));
            user.setEmailId(jedis.hget(key,"emailId"));
        }
        else {
            try {
                user = userDao.getUser(userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    private Map<String, String> getUserMap(String userId,String name,String followers,String emailId) {
        Map<String,String> userMap = new HashMap<>();
        userMap.put("userId",userId);
        userMap.put("name",name);
        userMap.put("followers",followers);
        userMap.put("emailId",emailId);

        return userMap;
    }


    private void testString(Jedis jedis) {
        jedis.set("name","Aditya");
        System.out.println("done setting Setting string");

    }

    private void testHash(Jedis jedis) {

        Map<String , String> user = new HashMap<>();
        user.put("name","Adi");
        user.put("lname","kur");
        user.put("score","100");



        jedis.select(0);
        jedis.hmset("users:1001",user);
    }
}