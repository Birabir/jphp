package ru.regenix.jphp.compiler.common.util;

import org.objectweb.asm.Opcodes;
import ru.regenix.jphp.compiler.common.misc.StackItem;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.*;
import ru.regenix.jphp.tokenizer.token.expr.value.*;
import ru.regenix.jphp.tokenizer.token.stmt.*;
import ru.regenix.jphp.runtime.memory.DoubleMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.Memory;
import ru.regenix.jphp.runtime.memory.StringMemory;

final public class CompilerUtils {

    private CompilerUtils(){ }

    protected static Memory toCallMemory(CallExprToken call, Memory... arguments){
        return null; // TODO
    }

    public static Memory toMemory(ValueExprToken value, Memory... arguments){
        if (value instanceof IntegerExprToken){
            return LongMemory.valueOf(((IntegerExprToken) value).getValue());
        } else if (value instanceof HexExprValue){
            return LongMemory.valueOf(((HexExprValue) value).getValue());
        } else if (value instanceof DoubleExprToken){
            return DoubleMemory.valueOf(((DoubleExprToken) value).getValue());
        } else if (value instanceof StringExprToken){
            return StringMemory.valueOf(((StringExprToken) value).getValue());
        } else if (value instanceof BooleanExprToken){
            return ((BooleanExprToken) value).getValue() ? Memory.TRUE : Memory.FALSE;
        } else if (value instanceof NullExprToken){
            return Memory.NULL;
        } else if (value instanceof CallExprToken){
            return toCallMemory((CallExprToken)value, arguments);
        }

        return null;
    }

    public static Memory calcUnary(Memory o1, OperatorExprToken operator){
        if (operator.isBinary())
            throw new IllegalArgumentException("Operator is not unary");

        if (operator instanceof UnarMinusExprToken)
            return o1.negative();

        if (operator instanceof BooleanNotExprToken)
            return o1.not() ? Memory.TRUE : Memory.FALSE;

        return null;
    }

    public static boolean isControlFlow(StmtToken token){
        if (token instanceof IfStmtToken)
            return true;
        if (token instanceof WhileStmtToken)
            return true;
        if (token instanceof DoStmtToken)
            return true;
        if (token instanceof ForStmtToken)
            return true;
        if (token instanceof ForeachStmtToken)
            return true;
        if (token instanceof TryStmtToken)
            return true;

        return false;
    }

    public static Memory calcBinary(Memory o1, Memory o2, OperatorExprToken operator){
        if (!operator.isBinary())
            throw new IllegalArgumentException("Operator is not binary");

        if (operator instanceof PlusExprToken)
            return o1.plus(o2);

        if (operator instanceof MinusExprToken)
            return o1.minus(o2);

        if (operator instanceof MulExprToken)
            return o1.mul(o2);

        if (operator instanceof DivExprToken)
            return o1.div(o2);

        if (operator instanceof ModExprToken)
            return o1.mod(o2);

        if (operator instanceof ConcatExprToken)
            return StringMemory.valueOf(o1.concat(o2));

        if (operator instanceof SmallerExprToken)
            return o1.smaller(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof SmallerOrEqualToken)
            return o1.smallerEq(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof GreaterExprToken)
            return o1.greater(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof GreaterOrEqualExprToken)
            return o1.greaterEq(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof EqualExprToken)
            return o1.equal(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof BooleanNotEqualExprToken)
            return o1.notEqual(o2) ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof BooleanAndExprToken || operator instanceof BooleanAnd2ExprToken)
            return o1.toBoolean() && o2.toBoolean() ? Memory.TRUE : Memory.FALSE;

        if (operator instanceof BooleanOrExprToken || operator instanceof BooleanOr2ExprToken)
            return o1.toBoolean() || o2.toBoolean() ? Memory.TRUE : Memory.FALSE;

        throw new IllegalArgumentException("Unsupported operator: " + operator.getWord());
    }

    public static int getOperatorOpcode(OperatorExprToken operator, StackItem.Type type){
        if (operator instanceof PlusExprToken){
            switch (type){
                case DOUBLE: return Opcodes.DADD;
                case FLOAT: return Opcodes.FADD;
                case LONG: return Opcodes.LADD;
                case BYTE:
                case SHORT:
                case INT: return Opcodes.IADD;
            }
        }

        if (operator instanceof MinusExprToken){
            switch (type){
                case DOUBLE: return Opcodes.DSUB;
                case FLOAT: return Opcodes.FSUB;
                case LONG: return Opcodes.LSUB;
                case BYTE:
                case SHORT:
                case INT: return Opcodes.ISUB;
            }
        }

        if (operator instanceof MulExprToken){
            switch (type){
                case DOUBLE: return Opcodes.DMUL;
                case FLOAT: return Opcodes.FMUL;
                case LONG: return Opcodes.LMUL;
                case BYTE:
                case SHORT:
                case INT: return Opcodes.IMUL;
            }
        }

        throw new IllegalArgumentException("Unknown operator " + operator.getWord() + " for type " + type.name());
    }

    public static boolean isSideOperator(OperatorExprToken operator){
        if (operator instanceof PlusExprToken)
            return false;
        if (operator instanceof MulExprToken)
            return false;
        if (operator instanceof EqualExprToken || operator instanceof BooleanNotEqualExprToken)
            return false;
        if (operator instanceof BooleanNotExprToken)
            return false;

        return true;
    }

    public static String getOperatorCode(OperatorExprToken operator){
        if (operator instanceof IncExprToken){
            return "inc";
        } else if (operator instanceof DecExprToken){
            return "dec";
        } else if (operator instanceof UnarMinusExprToken){
            return "negative";
        } else if (operator instanceof BooleanNotExprToken){
            return "not";
        } else if (operator instanceof PlusExprToken || operator instanceof AssignPlusExprToken){
            return "plus";
        } else if (operator instanceof MinusExprToken || operator instanceof AssignMinusExprToken){
            return "minus";
        } else if (operator instanceof MulExprToken || operator instanceof AssignMulExprToken){
            return "mul";
        } else if (operator instanceof DivExprToken || operator instanceof AssignDivExprToken){
            return "div";
        } else if (operator instanceof ModExprToken || operator instanceof AssignModExprToken){
            return "mod";
        } else if (operator instanceof AssignExprToken){
            return "assign";
        } else if (operator instanceof AssignRefExprToken){
            return  "assignRef";
        } else if (operator instanceof ConcatExprToken || operator instanceof AssignConcatExprToken){
            return  "concat";
        } else if (operator instanceof SmallerExprToken){
            return "smaller";
        } else if (operator instanceof SmallerOrEqualToken){
            return "smallerEq";
        } else if (operator instanceof GreaterExprToken){
            return "greater";
        } else if (operator instanceof GreaterOrEqualExprToken){
            return "greaterEq";
        } else if (operator instanceof EqualExprToken){
            return "equal";
        } else if (operator instanceof BooleanNotEqualExprToken){
            return "notEqual";
        }

        throw new IllegalArgumentException("Unsupported operator: " + operator.getWord());
    }

    public static Class getOperatorResult(OperatorExprToken operator) {
        if (operator instanceof ConcatExprToken || operator instanceof AssignConcatExprToken){
            return String.class;
        } else if (operator instanceof BooleanNotExprToken){
            return Boolean.TYPE;
        } else if (operator instanceof SmallerExprToken){
            return Boolean.TYPE;
        } else if (operator instanceof SmallerOrEqualToken){
            return Boolean.TYPE;
        } else if (operator instanceof GreaterExprToken){
            return Boolean.TYPE;
        } else if (operator instanceof GreaterOrEqualExprToken){
            return Boolean.TYPE;
        } else if (operator instanceof EqualExprToken){
            return Boolean.TYPE;
        } else if (operator instanceof BooleanNotEqualExprToken){
            return Boolean.TYPE;
        }

        return Memory.class;
    }
}