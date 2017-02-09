package io.github.encore_dms.util;

import com.physion.ebuilder.expression.*;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.*;

public class ExpressionUtilities {

    private static final String navigationOperator = ".";

    public static String generateJpql(IExpression expression) {
        return expressionJpql(expression);
    }

    private static String expressionJpql(IExpression expression) {
        if (expression instanceof IOperatorExpression) {
            return expressionJpql((IOperatorExpression) expression);
        } else if (expression instanceof IAttributeExpression) {
            return expressionJpql((IAttributeExpression) expression);
        } else if (expression instanceof ILiteralValueExpression) {
            return expressionJpql((ILiteralValueExpression) expression);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static String expressionJpql(IOperatorExpression expression) {
        List<String> expressions = new ArrayList<>();

        for (IExpression operand :
                expression.getOperandList()) {

            boolean isCompound = operand instanceof IOperatorExpression
                    && !((IOperatorExpression) operand).getOperandList().isEmpty()
                    && ((IOperatorExpression) operand).getOperandList().get(0) instanceof IOperatorExpression
                    && !((IOperatorExpression) ((IOperatorExpression) operand).getOperandList().get(0)).getOperatorName().equals(navigationOperator);

            String openBrace = isCompound ? "(" : "";
            String closeBrace = isCompound ? ")" : "";

            expressions.add(openBrace + expressionJpql(operand) + closeBrace);
        }

        String space = expression.getOperatorName().equals(navigationOperator) ? "" : " ";

        return String.join(space + expression.getOperatorName().toUpperCase() + space, expressions);
    }

    private static String expressionJpql(IAttributeExpression expression) {
        return expression.getAttributeName();
    }

    private static String expressionJpql(ILiteralValueExpression expression) {
        return valueExpressionJpql(expression);
    }

    private static String valueExpressionJpql(ILiteralValueExpression expression) {
        if (expression instanceof IBooleanLiteralValueExpression) {
            return valueExpressionJpql((IBooleanLiteralValueExpression) expression);
        } else if (expression instanceof IInt32LiteralValueExpression) {
            return valueExpressionJpql((IInt32LiteralValueExpression) expression);
        } else if (expression instanceof IFloat64LiteralValueExpression) {
            return valueExpressionJpql((IFloat64LiteralValueExpression) expression);
        } else if (expression instanceof IStringLiteralValueExpression) {
            return valueExpressionJpql((IStringLiteralValueExpression) expression);
        } else if (expression instanceof IClassLiteralValueExpression) {
            return valueExpressionJpql((IClassLiteralValueExpression) expression);
        } else if (expression instanceof ITimeLiteralValueExpression) {
            return valueExpressionJpql((ITimeLiteralValueExpression) expression);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static String valueExpressionJpql(IBooleanLiteralValueExpression expression) {
        return (Boolean) expression.getValue() ? "1" : "0";
    }

    private static String valueExpressionJpql(IInt32LiteralValueExpression expression) {
        return Integer.toString((Integer) expression.getValue());
    }

    private static String valueExpressionJpql(IFloat64LiteralValueExpression expression) {
        return Double.toString((Double) expression.getValue());
    }

    private static String valueExpressionJpql(IStringLiteralValueExpression expression) {
        return "'" + expression.getValue() + "'";
    }

    private static String valueExpressionJpql(IClassLiteralValueExpression expression) {
        throw new NotYetImplementedException();
    }

    private static String valueExpressionJpql(ITimeLiteralValueExpression expression) {
        throw new NotYetImplementedException();
    }
}
