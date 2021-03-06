package com.voxlearning.poseidon.api.annotation.dao.hbase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HbaseDocument {
    String table() ;
    String family() default "#DEFAULT_FAMILY#";
}
