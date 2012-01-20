package com.kit.lightserver.services.db;

import java.util.List;

public interface InsertQueryInterface {

    String getPreparedInsertQueryString();

    List<QueryParameter> getInsertQueryParameters();

}// class
