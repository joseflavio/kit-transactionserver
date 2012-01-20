package com.kit.lightserver.services.db;

import java.util.List;

public interface UpdateQueryInterface {

    String getPreparedUpdateQueryString();

    List<QueryParameter> getUpdateQueryParameters();

}// class
