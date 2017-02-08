package io.github.encore_dms.util;

import com.physion.ebuilder.expression.*;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.*;

public class ExpressionUtilities {

    private static final Map<String, String> operatorMap = createOperatorMap();
    private static Map<String, String> createOperatorMap() {
        Map<String, String> m = new HashMap<>();
        m.put("==", "=");
        return m;
    }

    private static final List<String> operatorHoistsSingletonOperands = Arrays.asList("AND", "OR");
    private static final List<String> operatorRequiresInfix = Collections.singletonList(".");

    public static String generateSql(IExpression expression) {
        return expressionSql(expression);
    }

    private static String expressionSql(IExpression expression) {
        if (expression instanceof IOperatorExpression) {
            return expressionSql((IOperatorExpression) expression);
        } else if (expression instanceof IAttributeExpression) {
            return expressionSql((IAttributeExpression) expression);
        } else if (expression instanceof ILiteralValueExpression) {
            return expressionSql((ILiteralValueExpression) expression);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static String expressionSql(IOperatorExpression expression) {
        int numOperands = expression.getOperandList().size();

        String operatorName = operatorMap.containsKey(expression.getOperatorName())
                ? operatorMap.get(expression.getOperatorName())
                : expression.getOperatorName();

        if (operatorHoistsSingletonOperands.contains(operatorName.toUpperCase()) && numOperands == 1) {
            return expressionSql(expression.getOperandList().get(0));
        } else {
            List<String> expressions = new ArrayList<>();
            for (IExpression operand :
                    expression.getOperandList()) {
                expressions.add(expressionSql(operand));
            }
            return String.join(" " + operatorName.toUpperCase() + " ", expressions);
        }
    }

    private static String expressionSql(IAttributeExpression expression) {
        return expression.getAttributeName();
    }

    private static String expressionSql(ILiteralValueExpression expression) {
        return valueExpressionSql(expression);
    }

    private static String valueExpressionSql(ILiteralValueExpression expression) {
        if (expression instanceof IBooleanLiteralValueExpression) {
            return valueExpressionSql((IBooleanLiteralValueExpression) expression);
        } else if (expression instanceof IInt32LiteralValueExpression) {
            return valueExpressionSql((IInt32LiteralValueExpression) expression);
        } else if (expression instanceof IFloat64LiteralValueExpression) {
            return valueExpressionSql((IFloat64LiteralValueExpression) expression);
        } else if (expression instanceof IStringLiteralValueExpression) {
            return valueExpressionSql((IStringLiteralValueExpression) expression);
        } else if (expression instanceof IClassLiteralValueExpression) {
            return valueExpressionSql((IClassLiteralValueExpression) expression);
        } else if (expression instanceof ITimeLiteralValueExpression) {
            return valueExpressionSql((ITimeLiteralValueExpression) expression);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static String valueExpressionSql(IBooleanLiteralValueExpression expression) {
        return (Boolean) expression.getValue() ? "1" : "0";
    }

    private static String valueExpressionSql(IInt32LiteralValueExpression expression) {
        return Integer.toString((Integer) expression.getValue());
    }

    private static String valueExpressionSql(IFloat64LiteralValueExpression expression) {
        return Double.toString((Double) expression.getValue());
    }

    private static String valueExpressionSql(IStringLiteralValueExpression expression) {
        return "'" + expression.getValue() + "'";
    }

    private static String valueExpressionSql(IClassLiteralValueExpression expression) {
        throw new NotYetImplementedException();
    }

    private static String valueExpressionSql(ITimeLiteralValueExpression expression) {
        throw new NotYetImplementedException();
    }
}
