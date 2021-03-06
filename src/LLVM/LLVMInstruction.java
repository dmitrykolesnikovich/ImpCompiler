package LLVM;


import java.util.List;

import Utils.StringUtils;

class LLVMInstruction implements LLVMCodeGenerator {
 	
 	public class ArithmeticOperation {
	    public final static String ADD = "add";
	    public final static String SUB = "sub";
	    public final static String DIV = "sdiv";
	    public final static String MUL = "mul";
	    public final static String REM = "srem";
	}
	public class ComparisonOperation {
	    public final static String EQ = "eq";
	    public final static String NEQ = "ne";
	    public final static String SGT = "sgt";
	    public final static String SGE = "sge";
	    public final static String SLT = "slt";
	    public final static String SLE = "sle";
	}
	public class BitwiseOperation {
	    public final static String OR = "or";
	    public final static String AND = "and";
	}


 	private String[] codeLines;

 	public LLVMInstruction(String code)  {
 		this.codeLines = code.split("\n");
 	}
 	/* (non-Javadoc)
 	 * @see LLVM.LLVMCodeGenerator#generate()
 	 */
 	public String generate() {
 		return this.generate(0);
 	}

 	/* (non-Javadoc)
 	 * @see LLVM.LLVMCodeGenerator#generate(int)
 	 */
 	public String generate(int indentation) {
 		String result = "";
 		for (String line: this.codeLines) {
 			result += StringUtils.repeatString("  ", indentation) + line + "\n";
 		}
 		return result;
 	}

 	/**
 	 * Build an instruction containing "br label %lbl"
 	 * @param lbl the name of the basicGroup to jump to
 	 * @return the built LLVMInstruction
 	 */
 	static public LLVMInstruction createUnConditionalJump(String lbl) {
 		String code = "br label %" + lbl;
 		return new LLVMInstruction(code);
 	}
 	/**
 	 * Build an instruction containing "br i1 condVarName , label %lbl1 , label %lbl2"
 	 * @param condVarName The name of the variable to store the bool result in
 	 * @param lbl1 the name of the group to jump to if condVarName is true
 	 * @param lbl2 the name of the group to jump to if condVarName is false
 	 * @return the created LLVMInstruction instance
 	 */
 	static public LLVMInstruction createConditionalJump(String condVarName, String lbl1, String lbl2) {
 		String code = "br i1 " + condVarName + " , label %" + lbl1 + " , label %" + lbl2;
		return new LLVMInstruction(code);
 	}

 	/**
 	 * Build an instruction containing "rtnVarname = icmp op i32 var1 , var2"
 	 * @param op
 	 * @param rtnVarname
 	 * @param var1
 	 * @param var2
 	 * @return the created LLVMInstruction instance
 	 */
 	static public LLVMInstruction createComparison(String op, String rtnVarname, String var1, String var2) {
 		String code = rtnVarname + " = icmp " + op.toString() + " i32 " + var1 + "," + var2;
 		return new LLVMInstruction(code);
 	}

 	static public LLVMInstruction createBitwiseOperation(String op, String rtnVarname, String var1, String var2) {
 		String code = rtnVarname + " = " + op + " i1 " + var1 + "," + var2;
 		return new LLVMInstruction(code);
 	}

 	static public LLVMInstruction createIntPrint(String varname) {
 		String code = "call void @printint(i32 " + varname + ")";
 		return new LLVMInstruction(code);
 	}
 	static public LLVMInstruction createRead(String varname) {
 		String code = varname + " = call i32 @readInt()";
 		return new LLVMInstruction(code);
 	}
 	static public LLVMInstruction createIntFuncCall(String rtnVarName, String funcName, List<String> params) {
 		String strParams = "";
 		for (String strparam : params) {
 			strParams += "i32 " + strparam + ", ";
		}
 		if (strParams.length() > 2) {
 			strParams = strParams.substring(0, strParams.length()-2);
 		}
 		String code = rtnVarName + " = call i32 @" + funcName + "(" + strParams + ")";
 		return new LLVMInstruction(code);
 	}


 	static public LLVMInstruction createIntAllocation(String varname) {
 		String code = varname + " = alloca i32";
 		return new LLVMInstruction(code);
 	}

 	static public LLVMInstruction createReturnInt(String varname) {
 		return new LLVMInstruction("ret i32 " + varname);
 	}
 	/**
 	 *
 	 * varname = %a, {@literal @a}, ...
 	 * value = 1, %a, {@literal @a}, ...
 	 */
 	static public LLVMInstruction createIntAssignation(String varname, String value) {
 		String code = "store i32 " + value + ", i32* " + varname;
 		return new LLVMInstruction(code);
 	}

 	static public LLVMInstruction createIntLoad(String rtnVarname, String varname) {
 		String code = rtnVarname + " = load i32, i32* " + varname;
 		return new LLVMInstruction(code);

 	}
 	
 	static public LLVMInstruction createStringLitteral(String varname, String str, int len) {
 		String code = varname + " = internal constant [" + Integer.toString(len) + " x i8] c\"" + str + "\"";
 		return new LLVMInstruction(code);
 	}
 	static public LLVMInstruction createStrPrint(String varname, int len) {
 		String strLen = Integer.toString(len);
 		String code = "call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([";
 		code += strLen + " x i8], [" + strLen + " x i8]* " + varname + ", i32 0, i32 0))";
 		return new LLVMInstruction(code);
 	}
 	static public LLVMInstruction createPrintNewLine() {
 		String code = "call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([2 x i8], [2 x i8]* @.strNL, i32 0, i32 0))";
 		return new LLVMInstruction(code);
 	}

 	static public LLVMInstruction createNot(String rtnVarname, String varname) {
 		String code = rtnVarname + " = xor i1 " + varname + ", 1";
 		return new LLVMInstruction(code);
 	}

 	static public LLVMInstruction createArthmeticOperation(String op, String rtnVarname, String var1, String var2) {
 		String code = rtnVarname + " = " + op.toString() + " i32 " + var1 + ", " + var2;
 		return new LLVMInstruction(code);
 	}

 	static public LLVMInstruction createMultiplication(String rtnVarname, String var1, String var2) {
 		return LLVMInstruction.createArthmeticOperation(ArithmeticOperation.MUL, rtnVarname, var1, var2);
 	}
 	static public LLVMInstruction createAddtion(String rtnVarname, String var1, String var2) {
 		return LLVMInstruction.createArthmeticOperation(ArithmeticOperation.ADD, rtnVarname, var1, var2);
 	}
 	static public LLVMInstruction createDivision(String rtnVarname, String var1, String var2) {
 		return LLVMInstruction.createArthmeticOperation(ArithmeticOperation.DIV, rtnVarname, var1, var2);
 	}
 	static public LLVMInstruction createSubstraction(String rtnVarname, String var1, String var2) {
 		return LLVMInstruction.createArthmeticOperation(ArithmeticOperation.SUB, rtnVarname, var1, var2);
 	}
 	static public LLVMInstruction createRemainer(String rtnVarname, String var1, String var2) {
 		return LLVMInstruction.createArthmeticOperation(ArithmeticOperation.REM, rtnVarname, var1, var2);
 	}
 }