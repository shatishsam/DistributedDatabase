package com.csci5408.distributeddatabase.query;

public class Criteria {
    String leftOperand;
    String rightOperand;
    String operator;

    public Criteria(String leftOperand, String rightOperand, String operator) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(String rightOperand) {
        this.rightOperand = rightOperand;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "   leftOperand='" + leftOperand + '\'' +
                ", rightOperand='" + rightOperand + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }
}
