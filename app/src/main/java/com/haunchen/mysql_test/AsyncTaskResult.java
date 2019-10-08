package com.haunchen.mysql_test;

public interface AsyncTaskResult<T extends Object> {
    void taskFinish( T result );
}
