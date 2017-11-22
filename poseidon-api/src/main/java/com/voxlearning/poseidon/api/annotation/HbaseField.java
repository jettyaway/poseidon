package com.voxlearning.poseidon.api.annotation;


import java.lang.annotation.*;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited()
public @interface HbaseField {


}
