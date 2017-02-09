package io.github.encore_dms.util;

import com.physion.ebuilder.expression.*;
import io.github.encore_dms.AbstractTest;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpressionUtilitiesTest extends AbstractTest {

    @Test
    public void generateJpqlWithSinglePredicate() {
        IOperatorExpression exclude = new OperatorExpression("=", Stream.of(
                new AttributeExpression("exclude"),
                new BooleanLiteralValueExpression(false))
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                exclude)
                .collect(Collectors.toList()));

        assertEquals("exclude = 0", ExpressionUtilities.generateJpql(root));
    }

    @Test
    public void generateJpqlWithDifferentValueTypes() {
        IOperatorExpression e1 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("boolean"),
                new BooleanLiteralValueExpression(true))
                .collect(Collectors.toList()));

        IOperatorExpression e2 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("int32"),
                new Int32LiteralValueExpression(5))
                .collect(Collectors.toList()));

        IOperatorExpression e3 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("float64"),
                new Float64LiteralValueExpression(6.78))
                .collect(Collectors.toList()));

        IOperatorExpression e4 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("string"),
                new StringLiteralValueExpression("string value"))
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                e1, e2, e3, e4)
                .collect(Collectors.toList()));

        assertEquals("boolean = 1 AND int32 = 5 AND float64 = 6.78 AND string = 'string value'", ExpressionUtilities.generateJpql(root));
    }

    @Test
    public void generateJpqlWithDifferentOperators() {
        IOperatorExpression e1 = new OperatorExpression("=", Stream.of(
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

        assertEquals("e1 = 'equality' AND e2 != 'inequality'", ExpressionUtilities.generateJpql(root));
    }

    @Test
    public void generateJpqlWithGroup() {
        IOperatorExpression e1_1 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e1_1"),
                new StringLiteralValueExpression("inner1"))
                .collect(Collectors.toList()));

        IOperatorExpression e1_2 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e1_2"),
                new StringLiteralValueExpression("inner2"))
                .collect(Collectors.toList()));

        IOperatorExpression e1 = new OperatorExpression("or", Stream.of(
                e1_1, e1_2)
                .collect(Collectors.toList()));

        IOperatorExpression e2 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e2"),
                new Int32LiteralValueExpression(7))
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                e1, e2)
                .collect(Collectors.toList()));

        assertEquals("(e1_1 = 'inner1' OR e1_2 = 'inner2') AND e2 = 7", ExpressionUtilities.generateJpql(root));
    }

    @Test
    public void generateJpqlWithNestedGroups() {
        IOperatorExpression e1 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e1"),
                new Int32LiteralValueExpression(7))
                .collect(Collectors.toList()));

        IOperatorExpression e2_1 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e2_1"),
                new Int32LiteralValueExpression(5))
                .collect(Collectors.toList()));

        IOperatorExpression e2_2 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e2_2"),
                new Int32LiteralValueExpression(6))
                .collect(Collectors.toList()));

        IOperatorExpression e2_3_1 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e2_3_1"),
                new Int32LiteralValueExpression(3))
                .collect(Collectors.toList()));

        IOperatorExpression e2_3_2_1 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e2_3_2_1"),
                new Int32LiteralValueExpression(2))
                .collect(Collectors.toList()));

        IOperatorExpression e2_3_2_2 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e2_3_2_2"),
                new Int32LiteralValueExpression(1))
                .collect(Collectors.toList()));

        IOperatorExpression e2_3_2 = new OperatorExpression("or", Stream.of(
                e2_3_2_1, e2_3_2_2)
                .collect(Collectors.toList()));

        IOperatorExpression e2_3 = new OperatorExpression("and", Stream.of(
                e2_3_1, e2_3_2)
                .collect(Collectors.toList()));

        IOperatorExpression e2_4 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e2_4"),
                new Int32LiteralValueExpression(8))
                .collect(Collectors.toList()));

        IOperatorExpression e2 = new OperatorExpression("or", Stream.of(
                e2_1, e2_2, e2_3, e2_4)
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                e1, e2)
                .collect(Collectors.toList()));

        assertEquals("e1 = 7 AND (e2_1 = 5 OR e2_2 = 6 OR (e2_3_1 = 3 AND (e2_3_2_1 = 2 OR e2_3_2_2 = 1)) OR e2_4 = 8)", ExpressionUtilities.generateJpql(root));
    }

    @Test
    public void generateJpqlWithRelationship() {
        IOperatorExpression relation = new OperatorExpression(".", Stream.of(
                new AttributeExpression("object"),
                new AttributeExpression("label"))
                .collect(Collectors.toList()));

        IOperatorExpression label = new OperatorExpression("=", Stream.of(
                relation,
                new StringLiteralValueExpression("string value"))
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                label)
                .collect(Collectors.toList()));

        assertEquals("object.label = 'string value'", ExpressionUtilities.generateJpql(root));
    }

    @Test
    public void generateJpqlWithDeepRelationship() {
        IOperatorExpression relation1 = new OperatorExpression(".", Stream.of(
                new AttributeExpression("object1"),
                new AttributeExpression("object2"))
                .collect(Collectors.toList()));

        IOperatorExpression relation2 = new OperatorExpression(".", Stream.of(
                relation1,
                new AttributeExpression("object3"))
                .collect(Collectors.toList()));

        IOperatorExpression relation3 = new OperatorExpression(".", Stream.of(
                relation2,
                new AttributeExpression("label"))
                .collect(Collectors.toList()));

        IOperatorExpression label = new OperatorExpression("=", Stream.of(
                relation3,
                new StringLiteralValueExpression("string value"))
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                label)
                .collect(Collectors.toList()));

        assertEquals("object1.object2.object3.label = 'string value'", ExpressionUtilities.generateJpql(root));
    }

    @Test
    public void generateJpqlWithNestedDeepRelationship() {
        IOperatorExpression e1 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e1"),
                new Int32LiteralValueExpression(7))
                .collect(Collectors.toList()));

        IOperatorExpression e2_1 = new OperatorExpression("=", Stream.of(
                new AttributeExpression("e2_1"),
                new Int32LiteralValueExpression(5))
                .collect(Collectors.toList()));

        IOperatorExpression relation1 = new OperatorExpression(".", Stream.of(
                new AttributeExpression("e2_2-1"),
                new AttributeExpression("e2_2-2"))
                .collect(Collectors.toList()));

        IOperatorExpression relation2 = new OperatorExpression(".", Stream.of(
                relation1,
                new AttributeExpression("e2_2-3"))
                .collect(Collectors.toList()));

        IOperatorExpression relation3 = new OperatorExpression(".", Stream.of(
                relation2,
                new AttributeExpression("label"))
                .collect(Collectors.toList()));

        IOperatorExpression e2_2 = new OperatorExpression("=", Stream.of(
                relation3,
                new StringLiteralValueExpression("string value"))
                .collect(Collectors.toList()));

        IOperatorExpression e2 = new OperatorExpression("and", Stream.of(
                e2_1, e2_2)
                .collect(Collectors.toList()));

        IOperatorExpression root = new OperatorExpression("and", Stream.of(
                e1, e2)
                .collect(Collectors.toList()));

        assertEquals("e1 = 7 AND (e2_1 = 5 AND e2_2-1.e2_2-2.e2_2-3.label = 'string value')", ExpressionUtilities.generateJpql(root));
    }
}
