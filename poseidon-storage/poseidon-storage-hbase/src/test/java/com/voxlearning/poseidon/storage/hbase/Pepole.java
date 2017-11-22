package com.voxlearning.poseidon.storage.hbase;

/**
 * @author:hao.su<hao.su@17zuoye.com>
 * @version:2017-06-21
 * @since:17-6-21
 */
public class Pepole {

    private Integer age;
    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pepole{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
