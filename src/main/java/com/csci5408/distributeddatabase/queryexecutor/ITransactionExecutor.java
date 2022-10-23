package com.csci5408.distributeddatabase.queryexecutor;

public interface ITransactionExecutor
{
    boolean executeTransaction(Transaction transaction);
}
