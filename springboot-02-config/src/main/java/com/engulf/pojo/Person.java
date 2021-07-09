package com.engulf.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component

//@PropertySource(value = "classpath:MyProperties.properties")

@ConfigurationProperties(prefix = "oneperson")
@Validated  //数据校验
public class Person {
    @Email
    private String name;
    private int age;
    private Boolean isHappy;
    private Date birthday;
    private Map<String,Object> maps;
    private List<Object> list;
    private Dog dog;

    public Person() {
    }

    public Person(String name, int age, Boolean isHappy, Date birthday, Map<String, Object> maps, List<Object> list, Dog dog) {
        this.name = name;
        this.age = age;
        this.isHappy = isHappy;
        this.birthday = birthday;
        this.maps = maps;
        this.list = list;
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boolean getHappy() {
        return isHappy;
    }

    public void setHappy(Boolean happy) {
        isHappy = happy;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Map<String, Object> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, Object> maps) {
        this.maps = maps;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", isHappy=" + isHappy +
                ", birthday=" + birthday +
                ", maps=" + maps +
                ", list=" + list +
                ", dog=" + dog +
                '}';
    }
}
