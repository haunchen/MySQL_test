package com.haunchen.mysql_test;

public interface Url {
    //String url = "http://192.168.31.182";
    String url = "http://10.1.1.10";

    String login = url + "/login.php";
    String getDatas = url + "/ArrayToJson.php";
    String getTables = url + "/GetTables.php";
}

