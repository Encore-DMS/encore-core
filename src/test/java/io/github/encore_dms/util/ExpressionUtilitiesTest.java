package io.github.encore_dms.util;

import com.physion.ebuilder.expression.*;
import io.github.encore_dms.AbstractTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionUtilitiesTest extends AbstractTest {

    @Test
    public void generateSqlWithSinglePredicate() {
        IOperatorExpression exclude = new OperatorExpression("==", Stream.of(
                new AttributeExpression("excludeFromAnalysis"),
                new BooleanLiteralValueExpression(false))
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                exclude)
                .collect(Collectors.toList()));

        assertEquals("excludeFromAnalysis = 0", ExpressionUtilities.generateSql(root));
    }

    @Test
    public void generateSqlWithDifferentValueTypes() {
        IOperatorExpression e1 = new OperatorExpression("==", Stream.of(
                new AttributeExpression("boolean"),
                new BooleanLiteralValueExpression(true))
                .collect(Collectors.toList()));

        IOperatorExpression e2 = new OperatorExpression("==", Stream.of(
                new AttributeExpression("int32"),
                new Int32LiteralValueExpression(5))
                .collect(Collectors.toList()));

        IOperatorExpression e3 = new OperatorExpression("==", Stream.of(
                new AttributeExpression("float64"),
                new Float64LiteralValueExpression(6.78))
                .collect(Collectors.toList()));

        IOperatorExpression e4 = new OperatorExpression("==", Stream.of(
                new AttributeExpression("string"),
                new StringLiteralValueExpression("string value"))
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                e1, e2, e3, e4)
                .collect(Collectors.toList()));

        assertEquals("boolean = 1 AND int32 = 5 AND float64 = 6.78 AND string = 'string value'", ExpressionUtilities.generateSql(root));
    }

    @Test
    public void generateSqlWithDifferentOperators() {
        IOperatorExpression e1 = new OperatorExpression("==", Stream.of(
                new AttributeExpression("e1"),
                new StringLiteralValueExpression("equality"))
                .collect(Collectors.toList()));

        IOperatorExpression e2 = new OperatorExpression("!=", Stream.of(
                new AttributeExpression("e2"),
                new StringLiteralValueExpression("inequality"))
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                e1, e2)
                .collect(Collectors.toList()));

        assertEquals("e1 = 'equality' AND e2 != 'inequality'", ExpressionUtilities.generateSql(root));
    }

}
