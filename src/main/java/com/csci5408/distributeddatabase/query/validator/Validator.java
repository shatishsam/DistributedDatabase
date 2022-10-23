package com.csci5408.distributeddatabase.query.validator;

import com.csci5408.distributeddatabase.query.Query;
import com.csci5408.distributeddatabase.queryexecutor.Transaction;

public interface Validator {

    void validate(Query query, Transaction transaction) throws Exception;
}
