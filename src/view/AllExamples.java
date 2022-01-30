package view;

import model.Example;
import model.ADT.MyList;
import model.Procedure;
import model.expression.*;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.Type;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;

import java.util.ArrayList;
import java.util.Arrays;

public class AllExamples {
	private final String SRC_FOLDER_PATH = "D:\\Cursuri\\MAP_Projects\\fourth_homework\\interpreter";
	
	private Statement composeStatement(MyList<Statement> crtList){
		if (crtList.size() == 0) {
			return new EmptyStatement();
		}
		
		if (crtList.size() == 1) {
			try {
				return crtList.pop();
			} 
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		Statement lastStatement = null;
		try {
			lastStatement = crtList.pop();
			return new CompoundStatement(composeStatement(crtList), lastStatement);
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return lastStatement;
	}
	
	public Example getExample1() {
		MyList<Statement> statementList = new MyList<>();
		
		// int a; a = 23; print(a);
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new AssignmentStatement("a", new ValueExpression(new IntValue(23))));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		
		return new Example(this.composeStatement(statementList), "int a; a = 23; print(a);", this.SRC_FOLDER_PATH + "\\log1.in");
	}
	
	public Example getExample2() {
		MyList<Statement> statementList = new MyList<>();
		
		// int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("b", new IntType()));
		statementList.addLast(new AssignmentStatement("a", new ArithmeticExpression(
								new ArithmeticExpression(
									new ValueExpression(new IntValue(3)), 
									new ValueExpression(new IntValue(5)), 
									"*"), // 3 * 5 
								new ValueExpression(new IntValue(2)), 
								"+"))); // 3 * 5 + 2 );
		statementList.addLast(new AssignmentStatement("b", new ArithmeticExpression(
								new VariableExpression("a"), 
								new ValueExpression(new IntValue(1)), 
								"+"))); // b = a + 1);
		statementList.addLast(new PrintStatement(new VariableExpression("b")));
		
		return new Example(this.composeStatement(statementList), "int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);", this.SRC_FOLDER_PATH + "\\log2.in");
	}
	
	public Example getExample3() {
		MyList<Statement> statementList = new MyList<>();
		
		//bool a; int v; a=true; (If a Then v=2 Else v=3); print(v);
		statementList.addLast(new VariableDeclarationStatement("a", new BoolType()));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))));
		statementList.addLast(new IfStatement(
								new VariableExpression("a"), 
								new AssignmentStatement("v", new ValueExpression(new IntValue(2))), 
								new AssignmentStatement("v", new ValueExpression(new IntValue(3)))
							));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return new Example(this.composeStatement(statementList), "bool a; int v; a=true; (If a Then v=2 Else v=3); print(v);", this.SRC_FOLDER_PATH + "\\log3.in");
	}
	
	public Example getExample4() {
		MyList<Statement> statementList = new MyList<>();
		
		// openReadFile(str); int a; readFile(str); print(a); readFile(str); print(a); closeReadFile();
		ValueExpression val = new ValueExpression(new StringValue(this.SRC_FOLDER_PATH + "\\example4.in"));
		statementList.addLast(new OpenReadFileStatement(val));
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new ReadFileStatement(val, "a"));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		statementList.addLast(new ReadFileStatement(val, "a"));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		statementList.addLast(new CloseReadFileStatement(val));
		
		return new Example(this.composeStatement(statementList), "openReadFile(str); int a; readFile(str); print(a); readFile(str); print(a); closeReadFile();", this.SRC_FOLDER_PATH + "\\log4.in");
	}
	
	public Example getExample5() {
		MyList<Statement> statementList = new MyList<>();
		
		// Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(v); print(a);
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))));
		statementList.addLast(new HeapAllocationStatement("a", new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		
		return new Example(this.composeStatement(statementList), "Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(v); print(a);", this.SRC_FOLDER_PATH + "\\log5.in");
	}
	
	public Example getExample6() {
		MyList<Statement> statementList = new MyList<>();
		
		// Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(rH(v)); print(rH(rH(a)) + 5);
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))));
		statementList.addLast(new HeapAllocationStatement("a", new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v"))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(
													new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a"))), 
													new ValueExpression(new IntValue(5)), 
													"+")));
		
		return new Example(this.composeStatement(statementList), "Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(rH(v)); print(rH(rH(a)) + 5);", this.SRC_FOLDER_PATH + "\\log6.in");
	}
	
	public Example getExample7() {
		MyList<Statement> statementList = new MyList<>();
		
		// Ref int v; new(v, 23); print(rH(v)); wH(v, 24); print(rH(v) + 5);
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v"))));
		statementList.addLast(new HeapWritingStatement("v", new ValueExpression(new IntValue(24))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(
													new HeapReadingExpression(new VariableExpression("v")), 
													new ValueExpression(new IntValue(5)), 
													"+")));
		
		return new Example(this.composeStatement(statementList), "Ref int v; new(v, 23); print(rH(v)); wH(v, 24); print(rH(v) + 5);", this.SRC_FOLDER_PATH + "\\log7.in");
	}
	
	public Example getExample8() {
		MyList<Statement> statementList = new MyList<>();
		
		// int v; v=4; (while (v>0) print(v); v = v - 1); print(v)
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(4))));
		statementList.addLast(new WhileStatement(
								new RelationalExpression(
										new VariableExpression("v"), 
										new ValueExpression(new IntValue(0)), 
										">"), 
								new CompoundStatement(
									new PrintStatement(new VariableExpression("v")),
									new AssignmentStatement("v", new ArithmeticExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(1)), 
											"-")))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return new Example(this.composeStatement(statementList), "int v; v=4; (while (v>0) print(v); v = v - 1); print(v)", this.SRC_FOLDER_PATH + "\\log8.in");
	}
	
	public Example getExample9() {
		MyList<Statement> statementList = new MyList<>();
		
		// Ref int v; new(v, 23); Ref Ref int a; new(a, v); new(v, 24); print(rH(rH(a)));
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))));
		statementList.addLast(new HeapAllocationStatement("a", new VariableExpression("v")));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(24))));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a")))));
		
		return new Example(this.composeStatement(statementList), "Ref int v; new(v, 23); Ref Ref int a; new(a, v); new(v, 24); print(rH(rH(a)));", this.SRC_FOLDER_PATH + "\\log9.in");
	}
	
	public Example getExample10() {
		MyList<Statement> statementList = new MyList<>();
		
		//int v; Ref int a; v=10; new(a,22); fork(wH(a,30); v=32; print(v); print(rH(a))); print(v); print(rH(a));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))));
		
		MyList<Statement> threadStatementList = new MyList<>();
		threadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))));
		threadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(32))));
		threadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		statementList.addLast(new ForkStatement(this.composeStatement(threadStatementList)));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		return new Example(this.composeStatement(statementList), "int v; Ref int a; v=10; new(a,22); fork(wH(a,30); v=32; print(v); print(rH(a))); print(v); print(rH(a));", this.SRC_FOLDER_PATH + "\\log10.in");
	}
	
	public Example getExample11() {
		MyList<Statement> statementList = new MyList<>();
		
		//int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; print(v)); v=32; print(v); print(rH(a))); print(v); print(rH(a));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))));
		
		MyList<Statement> threadStatementList = new MyList<>();
		threadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))));
		
		MyList<Statement> innerThreadStatementList = new MyList<>();
		innerThreadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(33))));
		innerThreadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		threadStatementList.addLast(new ForkStatement(this.composeStatement(innerThreadStatementList)));
		threadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(32))));
		threadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		statementList.addLast(new ForkStatement(this.composeStatement(threadStatementList)));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		return new Example(this.composeStatement(statementList), "int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; print(v)); v=32; print(v); print(rH(a))); print(v); print(rH(a));", this.SRC_FOLDER_PATH + "\\log11.in");
	}

	public Example getExample12(){
		MyList<Statement> statementList = new MyList<>();

		statementList.addLast(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("cnt", new IntType()));
		statementList.addLast(new HeapAllocationStatement("v1", new ValueExpression(new IntValue(2))));
		statementList.addLast(new SemaphoreStatement("cnt", new HeapReadingExpression(new VariableExpression("v1"))));

		MyList<Statement> firstFork = new MyList<>();

		firstFork.addLast(new AcquireStatement("cnt"));
		firstFork.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		firstFork.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		firstFork.addLast(new ReleaseStatement("cnt"));
		statementList.addLast(new ForkStatement(composeStatement(firstFork)));

		MyList<Statement> secondFork = new MyList<>();

		secondFork.addLast(new AcquireStatement("cnt"));
		secondFork.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		secondFork.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(2)), "*")));
		secondFork.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		secondFork.addLast(new ReleaseStatement("cnt"));
		statementList.addLast(new ForkStatement(composeStatement(secondFork)));

		statementList.addLast(new AcquireStatement("cnt"));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), "-")));
		statementList.addLast(new ReleaseStatement("cnt"));

		return new Example(this.composeStatement(statementList), "Ref int v1; int cnt; new(v1,2); createSemaphore(cnt,rH(v1)); " +
				"fork(acquire(cnt); wh(v1,rh(v1)*10)); print(rh(v1)); release(cnt)); " +
				"fork(acquire(cnt); wh(v1,rh(v1)*10)); wh(v1,rh(v1)*2)); print(rh(v1)); release(cnt)); " +
				"acquire(cnt); print(rh(v1)-1); release(cnt);", this.SRC_FOLDER_PATH + "\\log12.in");
	}

	public Example getExample13(){
		MyList<Statement> statementList = new MyList<>();

		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("b", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(0))));
		statementList.addLast(new HeapAllocationStatement("b", new ValueExpression(new IntValue(0))));
		statementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(1))));
		statementList.addLast(new HeapWritingStatement("b", new ValueExpression(new IntValue(2))));
		statementList.addLast(new ConditionalAssignmentStatement("v",
				new RelationalExpression(
						new HeapReadingExpression(new VariableExpression("a")),
						new HeapReadingExpression(new VariableExpression("b")),
						"<"),
				new ValueExpression(new IntValue(100)),
				new ValueExpression(new IntValue(200))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new ConditionalAssignmentStatement("v",
				new RelationalExpression(
						new ArithmeticExpression(
								new HeapReadingExpression(new VariableExpression("b")),
								new ValueExpression(new IntValue(2)),
								"-"),
						new HeapReadingExpression(new VariableExpression("a")),
						">"),
				new ValueExpression(new IntValue(100)),
				new ValueExpression(new IntValue(200))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));

		return new Example(this.composeStatement(statementList), "conditional assignment", this.SRC_FOLDER_PATH + "\\log13.in");
	}

	public Example getExample14() {
		MyList<Statement> statementList = new MyList<>();

		// for(int v = 4; v > 0; v--) {print(v);}
		statementList.addLast(new ForStatement(
				"v",
				new ValueExpression(new IntValue(4)),
				new RelationalExpression(
						new VariableExpression("v"),
						new ValueExpression(new IntValue(0)),
						">"),
				new IncrementStatement("v", "-"),
				new PrintStatement(new VariableExpression("v"))
		));
		return new Example(composeStatement(statementList), "for(int v = 4; v > 0; v--) {print(v);}", this.SRC_FOLDER_PATH + "\\log14.in");
	}

	public Example getExample15(){
		MyList<Statement> statementList = new MyList<>();

		statementList.addLast(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("x", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("q", new IntType()));
		statementList.addLast(new HeapAllocationStatement("v1", new ValueExpression(new IntValue(20))));
		statementList.addLast(new HeapAllocationStatement("v2", new ValueExpression(new IntValue(30))));
		statementList.addLast(new CreateLockStatement("x"));

		MyList<Statement> thread2StatementList = new MyList<>();
		MyList<Statement> thread3StatementList = new MyList<>();
		thread3StatementList.addLast(new LockStatement("x"));
		thread3StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), "-")));
		thread3StatementList.addLast(new UnlockStatement("x"));
		thread2StatementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));
		thread2StatementList.addLast(new LockStatement("x"));
		thread2StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		thread2StatementList.addLast(new UnlockStatement("x"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));

		statementList.addLast(new CreateLockStatement("q"));

		MyList<Statement> thread4StatementList = new MyList<>();
		MyList<Statement> thread5StatementList = new MyList<>();
		thread5StatementList.addLast(new LockStatement("q"));
		thread5StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(5)), "+")));
		thread5StatementList.addLast(new UnlockStatement("q"));
		thread4StatementList.addLast(new ForkStatement(this.composeStatement(thread5StatementList)));
		thread4StatementList.addLast(new LockStatement("q"));
		thread4StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), "*")));
		thread4StatementList.addLast(new UnlockStatement("q"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread4StatementList)));

		statementList.addLast(new EmptyStatement());
		statementList.addLast(new EmptyStatement());
		statementList.addLast(new EmptyStatement());
		statementList.addLast(new EmptyStatement());
		statementList.addLast(new LockStatement("x"));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		statementList.addLast(new UnlockStatement("x"));
		statementList.addLast(new LockStatement("q"));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v2"))));
		statementList.addLast(new UnlockStatement("q"));

		return new Example(this.composeStatement(statementList), "normal lock", this.SRC_FOLDER_PATH + "\\log15.in");
	}
	public Example getExample16(){
		MyList<Statement> statementList = new MyList<>();

		statementList.addLast(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v3", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("cnt", new IntType()));
		statementList.addLast(new HeapAllocationStatement("v1", new ValueExpression(new IntValue(2))));
		statementList.addLast(new HeapAllocationStatement("v2", new ValueExpression(new IntValue(3))));
		statementList.addLast(new HeapAllocationStatement("v3", new ValueExpression(new IntValue(4))));
		statementList.addLast(new CreateLatchStatement("cnt", new HeapReadingExpression(new VariableExpression("v2"))));

		MyList<Statement> thread2StatementList = new MyList<>();
		thread2StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		thread2StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		thread2StatementList.addLast(new CountDownLatchStatement("cnt"));

		MyList<Statement> thread3StatementList = new MyList<>();
		thread3StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), "*")));
		thread3StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v2"))));
		thread3StatementList.addLast(new CountDownLatchStatement("cnt"));

		MyList<Statement> thread4StatementList = new MyList<>();
		thread4StatementList.addLast(new HeapWritingStatement("v3", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v3")), new ValueExpression(new IntValue(10)), "*")));
		thread4StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v3"))));
		thread4StatementList.addLast(new CountDownLatchStatement("cnt"));
		thread3StatementList.addLast(new ForkStatement(this.composeStatement(thread4StatementList)));
		thread2StatementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));

		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));
		statementList.addLast(new AwaitLatchStatement("cnt"));
		statementList.addLast(new PrintStatement(new ValueExpression(new IntValue(100))));
		statementList.addLast(new CountDownLatchStatement("cnt"));
		statementList.addLast(new PrintStatement(new ValueExpression(new IntValue(100))));

		return new Example(this.composeStatement(statementList), "Ref int v1; Ref int v2; Ref int v3; int cnt; " +
				"new(v1,2); new(v2,3); new(v3,4); newLatch(cnt,rH(v2)); fork(wh(v1,rh(v1)*10); print(rh(v1)); countDown(cnt);); " +
				"fork(wh(v2,rh(v2)*10); print(rh(v2)); countDown(cnt);); fork(wh(v3,rh(v3)*10); print(rh(v3)); countDown(cnt);); " +
				"await(cnt); print(100); countDown(cnt); print(100);", this.SRC_FOLDER_PATH + "\\log16.in");
	}
	public Example getExample17(){
		MyList<Statement> statementList = new MyList<>();

		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("b", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("c", new IntType()));
		statementList.addLast(new AssignmentStatement("a", new ValueExpression(new IntValue(1))));
		statementList.addLast(new AssignmentStatement("b", new ValueExpression(new IntValue(2))));
		statementList.addLast(new AssignmentStatement("c", new ValueExpression(new IntValue(5))));

		ArrayList<Expression> caseExpressionList = new ArrayList<>(Arrays.asList(
				new ArithmeticExpression(new VariableExpression("b"), new VariableExpression("c"), "*"),
				new ValueExpression(new IntValue(10))));
		ArrayList<Statement> caseStatementList = new ArrayList<>(Arrays.asList(
				new CompoundStatement(new PrintStatement(new VariableExpression("a")), new PrintStatement(new VariableExpression("b"))),
				new CompoundStatement(new PrintStatement(new ValueExpression(new IntValue(100))), new PrintStatement(new ValueExpression(new IntValue(200)))),
				new PrintStatement(new ValueExpression(new IntValue(300)))));
		statementList.addLast(new SwitchStatement(new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntValue(10)), "*"),
				caseExpressionList,
				caseStatementList));
		statementList.addLast(new PrintStatement(new ValueExpression(new IntValue(300))));

		return new Example(this.composeStatement(statementList), "switch", this.SRC_FOLDER_PATH + "\\log17.in");
	}

	public Example getExample18(){
		MyList<Statement> statementList = new MyList<>();

		// int v; v = 0; repeat {fork(print(v); v--;); v++;} until (v == 3); int x; x = 1; print(v * 10);
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(0))));
		statementList.addLast(new RepeatUntilStatement(
				new CompoundStatement(
						new ForkStatement(new CompoundStatement(
								new PrintStatement(new VariableExpression("v")),
								new IncrementStatement("v", "-"))),
						new IncrementStatement("v", "+")),
				new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(3)), "==")));
		statementList.addLast(new VariableDeclarationStatement("x", new IntType()));
		statementList.addLast(new AssignmentStatement("x", new ValueExpression(new IntValue(1))));
		statementList.addLast(new VariableDeclarationStatement("y", new IntType()));
		statementList.addLast(new AssignmentStatement("y", new ValueExpression(new IntValue(2))));
		statementList.addLast(new VariableDeclarationStatement("z", new IntType()));
		statementList.addLast(new AssignmentStatement("z", new ValueExpression(new IntValue(3))));
		statementList.addLast(new VariableDeclarationStatement("w", new IntType()));
		statementList.addLast(new AssignmentStatement("w", new ValueExpression(new IntValue(4))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), "*")));

		return new Example(this.composeStatement(statementList), "int v; v = 0; repeat {fork(print(v); v--;); v++;} until (v == 3); int x; x = 1; print(v * 10);", this.SRC_FOLDER_PATH + "\\log18.in");
	}

	public Example getExample19(){
		MyList<Statement> statementList = new MyList<>();

		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));

		MyList<Statement> thread2StatementList = new MyList<>();
		thread2StatementList.addLast(new IncrementStatement("v", "-"));
		thread2StatementList.addLast(new IncrementStatement("v", "-"));
		thread2StatementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));

		statementList.addLast(new SleepStatement(new ValueExpression(new IntValue(10))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), "*")));

		return new Example(this.composeStatement(statementList), "sleep", this.SRC_FOLDER_PATH + "\\log19.in");
	}

	public Example getExample20(){
		MyList<Statement> statementList = new MyList<>();

		statementList.addLast(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v3", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("cnt", new IntType()));
		statementList.addLast(new HeapAllocationStatement("v1", new ValueExpression(new IntValue(2))));
		statementList.addLast(new HeapAllocationStatement("v2", new ValueExpression(new IntValue(3))));
		statementList.addLast(new HeapAllocationStatement("v3", new ValueExpression(new IntValue(4))));
		statementList.addLast(new CreateBarrierStatement("cnt", new HeapReadingExpression(new VariableExpression("v2"))));

		MyList<Statement> thread2StatementList = new MyList<>();
		thread2StatementList.addLast(new AwaitBarrierStatement("cnt"));
		thread2StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		thread2StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));

		MyList<Statement> thread3StatementList = new MyList<>();
		thread3StatementList.addLast(new AwaitBarrierStatement("cnt"));
		thread3StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), "*")));
		thread3StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), "*")));
		thread3StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v2"))));
		statementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));

		statementList.addLast(new AwaitBarrierStatement("cnt"));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v3"))));

		return new Example(this.composeStatement(statementList), "barrier", this.SRC_FOLDER_PATH + "\\log20.in");
	}

	public Example getExample21(){
		MyList<Statement> statementList = new MyList<>();

		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(20))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new WaitStatement(new ValueExpression(new IntValue(10))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), "*")));

		return new Example(this.composeStatement(statementList), "wait", this.SRC_FOLDER_PATH + "\\log21.in");
	}

	public Example getExample22(){
		MyList<Statement> statementList = new MyList<>();

		MyList<Statement> procedure1StatementList = new MyList<>();
		procedure1StatementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		procedure1StatementList.addLast(new AssignmentStatement("v", new ArithmeticExpression(new VariableExpression("a"), new VariableExpression("b"), "+")));
		procedure1StatementList.addLast(new PrintStatement(new VariableExpression("v")));
		ArrayList<Type> procedure1Types = new ArrayList<>(Arrays.asList(new IntType(), new IntType()));
		ArrayList<String> procedure1Names = new ArrayList<>(Arrays.asList("a", "b"));
		statementList.addLast(new CreateProcedureStatement("sum", new Procedure(procedure1Types, procedure1Names, this.composeStatement(procedure1StatementList))));

		MyList<Statement> procedure2StatementList = new MyList<>();
		procedure2StatementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		procedure2StatementList.addLast(new AssignmentStatement("v", new ArithmeticExpression(new VariableExpression("a"), new VariableExpression("b"), "*")));
		procedure2StatementList.addLast(new PrintStatement(new VariableExpression("v")));
		ArrayList<Type> procedure2Types = new ArrayList<>(Arrays.asList(new IntType(), new IntType()));
		ArrayList<String> procedure2Names = new ArrayList<>(Arrays.asList("a", "b"));
		statementList.addLast(new CreateProcedureStatement("product", new Procedure(procedure2Types, procedure2Names, this.composeStatement(procedure2StatementList))));

		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(2))));
		statementList.addLast(new VariableDeclarationStatement("w", new IntType()));
		statementList.addLast(new AssignmentStatement("w", new ValueExpression(new IntValue(5))));
		ArrayList<Expression> procedure1Call1Values = new ArrayList<>(Arrays.asList(
				new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), "*"),
				new VariableExpression("w")
		));
		statementList.addLast(new CallProcedureStatement("sum", procedure1Call1Values));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));

		MyList<Statement> thread2StatementList = new MyList<>();
		ArrayList<Expression> procedure2Call1Values = new ArrayList<>(Arrays.asList(
				new VariableExpression("v"), new VariableExpression("w")));
		thread2StatementList.addLast(new CallProcedureStatement("product", procedure2Call1Values));

		MyList<Statement> thread3StatementList = new MyList<>();
		ArrayList<Expression> procedure1Call2Values = new ArrayList<>(Arrays.asList(
				new VariableExpression("v"), new VariableExpression("w")));
		thread3StatementList.addLast(new CallProcedureStatement("sum", procedure1Call2Values));
		thread2StatementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));

		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));

		return new Example(this.composeStatement(statementList), "procedure", this.SRC_FOLDER_PATH + "\\log22.in");
	}
	
	public MyList<Example> getAllExamples() {
		MyList<Example> exampleList = new MyList<>();
		
		exampleList.addLast(getExample1());
		exampleList.addLast(getExample2());
		exampleList.addLast(getExample3());
		exampleList.addLast(getExample4());
		exampleList.addLast(getExample5());
		exampleList.addLast(getExample6());
		exampleList.addLast(getExample7());
		exampleList.addLast(getExample8());
		exampleList.addLast(getExample9());
		exampleList.addLast(getExample10());
		exampleList.addLast(getExample11());
		exampleList.addLast(getExample12());
		exampleList.addLast(getExample13());
		exampleList.addLast(getExample14());
		exampleList.addLast(getExample15());
		exampleList.addLast(getExample16());
		exampleList.addLast(getExample17());
		exampleList.addLast(getExample18());
		exampleList.addLast(getExample19());
		exampleList.addLast(getExample20());
		exampleList.addLast(getExample21());
		exampleList.addLast(getExample22());

		return exampleList;
	}
}
