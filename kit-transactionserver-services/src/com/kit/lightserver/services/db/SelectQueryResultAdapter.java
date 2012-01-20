package com.kit.lightserver.services.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SelectQueryResultAdapter<T> {

    T adaptResultSet(ResultSet rs) throws SQLException;

}// interface
