package com.kit.lightserver.services.db;

import java.util.List;

public interface SelectQueryInterface {

    String getPreparedSelectQueryString();

    List<QueryParameter> getSelectQueryParameters();


}// interface
