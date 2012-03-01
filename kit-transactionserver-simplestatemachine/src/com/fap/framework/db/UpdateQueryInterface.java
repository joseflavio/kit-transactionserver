package com.fap.framework.db;

import java.util.List;


public interface UpdateQueryInterface {

    String getPreparedUpdateQueryString();

    List<QueryParameter> getUpdateQueryParameters();

}// class
